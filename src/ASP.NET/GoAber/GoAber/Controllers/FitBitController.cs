using System.Configuration;
using System.Web.Mvc;
using DotNetOpenAuth.OAuth2;
using System.Net;
using System;
using System.Linq;
using System.Net.Http;
using GoAber.Controllers;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Collections.Generic;
using GoAber.App_Code.OAuth;
using System.Data.Entity;
using GoAber.Extensions;

namespace GoAber.Controllers
{
    public class FitBitController : Controller, DeviceAPI
    {
        private const string DEVICENAME = "fitbit";
        private const string APIADDRESS = "https://api.fitbit.com/1/user/-";
        goaberEntities db = new goaberEntities();

        private WebServerClient getClient()
        {

            devicetype deviceType = findDeviceTypeByName(DEVICENAME);
            if (deviceType == null)
                return null;
            AuthorizationServerDescription description = new AuthorizationServerDescription
            {
                TokenEndpoint = new Uri(deviceType.tokenEndpoint),
                AuthorizationEndpoint = new Uri(deviceType.authorizationEndpoint),
            };

            WebServerClient fitbit = new WebServerClient(
                description,
                deviceType.clientId,
                deviceType.consumerSecret
            );
            return fitbit;
        }

        private devicetype findDeviceTypeByName(String name)
        {
            var query = from d in db.devicetypes
                        where d.name == name
                        select d;
            devicetype deviceType = query.SingleOrDefault();
            return deviceType;
        }

        public ActionResult Index()
        {
            device device = FindDevice();
            if(device == null)
                return RedirectToAction("StartOAuth"); // redirect to authorisation
            if (DateTime.UtcNow > device.tokenExpiration)
                refreshToken(); // Token needs refreshing
            return View();
        }

        public ActionResult StartOAuth()
        {
            WebServerClient fitbit = getClient();
            if (fitbit == null)
            {
                ViewBag.Message = "Could not find FitBit connectivity settings!";
            }
            else
            {
                fitbit.RequestUserAuthorization(
                    new[] { "activity", "heartrate", "sleep" },
                    new Uri(Url.Action("Callback", "FitBit", null, Request.Url.Scheme)));
                ViewBag.Message = "Redirecting you shortly...";
            }
            return View();
        }

        public ActionResult Callback(string code)
        {
            WebServerClient fitbit = getClient();
            if (fitbit == null)
            {
                ViewBag.Message += "Got token<br />";
                return View();
            }
            ViewBag.Message = String.Format("Started Callback with Code: {0}<br />", code);
            try
            {
                IAuthorizationState authorisation = fitbit.ProcessUserAuthorization();
                if (authorisation == null)
                {
                    ViewBag.Message = "Device not authorised!";
                    return View();
                }
                devicetype deviceType = findDeviceTypeByName(DEVICENAME);
                if(deviceType == null)
                {
                    ViewBag.Message = "Could not find FitBit connectivity settings!";
                    return View();
                }

                device device = new device();
                device.ConstructionFactory(
                    authorisation.AccessToken,
                    authorisation.RefreshToken,
                    deviceType.idDeviceType,
                    authorisation.AccessTokenExpirationUtc,
                    1);
               

                device temp = FindDevice();
                if (temp != null)
                {
                    temp.refreshToken = device.refreshToken;
                    temp.tokenExpiration = device.tokenExpiration;
                    temp.accessToken = device.accessToken;
                    temp.deviceTypeId = device.deviceTypeId;
                }
                else
                {
                    db.devices.Add(device);
                }
                db.SaveChanges();
                return RedirectToAction("Index"); // redirect to list of actions
            }
            catch (Exception e)
            {
                ViewBag.Message += "Exception accessing the code. " + e.Message;
            }
            return View();
        }

        private device FindDevice()
        {
            var query = from d in db.devices
                        where d.userId == 1 // MOCK USER ID FOR NOW
                        select d;
            return query.SingleOrDefault();
        }


        private String getCurrentUserAccessToken(int userID)
        {
            device device = FindDevice();
            if (device == null)
                return null; // No token availible for this user
            if (DateTime.UtcNow > device.tokenExpiration)
                return refreshToken(); // Token needs refreshing
            return device.accessToken;
        }


        private String refreshToken()
        {
            WebServerClient fitbit = getClient();
            device device = FindDevice();
            if (device != null) {
                try
                {
                    IAuthorizationState state = new AuthorizationState();
                    state.AccessToken = device.accessToken;
                    state.Callback = new Uri("https://api.fitbit.com/oauth2/token");
                    state.RefreshToken = device.refreshToken;
                    
                    if (fitbit.RefreshAuthorization(state))
                    {
                        device.accessToken = state.AccessToken;
                        device.refreshToken = state.RefreshToken;
                        device.tokenExpiration = state.AccessTokenExpirationUtc;
                        db.Entry(device).State = EntityState.Modified;
                        db.SaveChanges();
                        return state.AccessToken;
                    }

                } catch (Exception e)
                {
                    if ((e as WebException) != null)
                    {
                        Console.WriteLine(((WebException)e).Status);
                        Console.WriteLine(((WebException)e).Message);
                    }
                    Console.WriteLine(e.StackTrace);
                }

            }
            return "";
        }

        /*
         * ------------------------------------------
         *  START API CALLS FOR VARIOUS METHODS HERE
         * ------------------------------------------
         */
        public activitydata getDayActivities(string ls_path, int userID, int day, int month, int year)
        {
            string token = getCurrentUserAccessToken(userID);
            if (String.IsNullOrEmpty(token))
                return null;
            //-----------------------------
            string result = String.Empty;
            HttpClient client = getAuthorisedClient(token);
            ViewBag.RequestingUrl = String.Format(APIADDRESS + "{0}{1}-{2}-{3}.json", ls_path, year, month, day);
            var apiResponse = client.GetAsync(ViewBag.RequestingUrl).Result;
            if (apiResponse.IsSuccessStatusCode)
            {
                result = apiResponse.Content.ReadAsStringAsync().Result;
                JToken jToken = JObject.Parse(result);
                JToken summary = jToken.SelectToken("summary");
                int categoryUnitID = 0;
                int steps = (int)summary.SelectToken("steps");
                DateTime date = new DateTime(year, month, day);
                activitydata data = new activitydata(categoryUnitID, userID, date, DateTime.Now, steps);
                return data;
            }
            ViewBag.Result = apiResponse.StatusCode;
            return null;
        }

        public activitydata getDayHeart(string ls_path, int userID, int day, int month, int year)
        {
            string token = getCurrentUserAccessToken(userID);
            if (String.IsNullOrEmpty(token))
                return null;
            //-----------------------------
            string result = String.Empty;
            HttpClient client = getAuthorisedClient(token);
            ViewBag.RequestingUrl = String.Format(APIADDRESS + "{0}{1}-{2}-{3}/1d.json", ls_path, year, month, day);
            var apiResponse = client.GetAsync(ViewBag.RequestingUrl).Result;
            if (apiResponse.IsSuccessStatusCode)
            {
                result = apiResponse.Content.ReadAsStringAsync().Result;
                JToken jToken = JObject.Parse(result);
                JToken summary = jToken.SelectToken("activities-heart");
                JToken first = jToken.First;
                int restingHeartRate = (int)first.SelectToken("restingHeartRate");
                int categoryUnitID = 0;
                DateTime date = new DateTime(year, month, day);
                activitydata data = new activitydata(categoryUnitID, userID, date, DateTime.Now, restingHeartRate);
                return data;
            }
            ViewBag.Result = apiResponse.StatusCode;
            return null;
        }

        public ActionResult getActivityDay()
        {
            int day = 6;
            int month = 11;
            int year = 2015;
            activitydata activityDay = getDayActivities("/activities/date/", 1, day, month, year);
            if (activityDay != null)
            {
                ViewBag.Result = activityDay.value;
            }
            return View();
        }
        public ActionResult getHeartDay()
        {
            int day = 6;
            int month = 11;
            int year = 2015;
            activitydata activityHeart = getDayHeart("/activities/heart/date/", 1, day, month, year);
            if (activityHeart != null)
            {
                ViewBag.Result = activityHeart.value;
            }
            return View();
        }

        private HttpClient getAuthorisedClient(string token)
        {
            HttpClient client = new HttpClient();
            client.DefaultRequestHeaders.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);
            return client;
        }
    }
}