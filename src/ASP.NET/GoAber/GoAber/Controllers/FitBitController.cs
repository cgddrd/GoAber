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

namespace GoAber.Controllers
{
    public class FitBitController : Controller, OAuthConnectivity
    {
        private const string deviceName = "fitbit";
        private const string apiAddress = "https://api.fitbit.com/1/user/-";

        private WebServerClient getClient()
        {
            devicetype deviceType = findDeviceTypeByName(deviceName);
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
            goaberEntities db = new goaberEntities();
            var query = from d in db.devicetypes
                        where d.name == name
                        select d;
            devicetype deviceType = query.SingleOrDefault();
            return deviceType;
        }

        public ActionResult Index()
        {
            goaberEntities db = new goaberEntities();
            var query = from d in db.devices
                        where d.userId == 1 // MOCK USER ID FOR NOW
                        select d;
            device device = query.SingleOrDefault();
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
                devicetype deviceType = findDeviceTypeByName(deviceName);
                if(deviceType == null)
                {
                    ViewBag.Message = "Could not find FitBit connectivity settings!";
                    return View();
                }
                device device = new device
                {
                    accessToken = authorisation.AccessToken,
                    refreshToken = authorisation.RefreshToken,
                    deviceTypeId = deviceType.idDeviceType,
                    tokenExpiration = authorisation.AccessTokenExpirationUtc,
                    userId = 1 // USE PROPER USER ID HERE!
                };
                goaberEntities db = new goaberEntities();
                db.devices.Add(device);
                db.SaveChanges();
                return RedirectToAction("Index"); // redirect to list of actions
            }
            catch (Exception e)
            {
                ViewBag.Message += "Exception accessing the code. " + e.Message;
            }
            return View();
        }


        private String getCurrentUserAccessToken(int userID)
        {
            goaberEntities db = new goaberEntities();
            var query = from d in db.devices
                        where d.userId == 1 // MOCK USER ID FOR NOW
                        select d;
            device device = query.SingleOrDefault();
            if (device == null)
                return null; // No token availible for this user
            if (DateTime.UtcNow > device.tokenExpiration)
                return refreshToken(); // Token needs refreshing
            return device.accessToken;
        }

        private String refreshToken()
        {
            String newToken = "";
            return newToken;
        }

        /*
         * ------------------------------------------
         *  START API CALLS FOR VARIOUS METHODS HERE
         * ------------------------------------------
         */

        public ActionResult ShowDay()
        {
            string token = getCurrentUserAccessToken(1);
            if(String.IsNullOrEmpty(token))
                RedirectToAction("StartOAuth"); // redirect to authorisation
            //-----------------------------
            string result = String.Empty;
            HttpClient client = getAuthorisedClient();
            client.DefaultRequestHeaders.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);
            int day = DateTime.Now.Day;
            int month = DateTime.Now.Month;
            int year = DateTime.Now.Year;
            ViewBag.RequestingUrl = String.Format(apiAddress+"/activities/date/{0}-{1}-{2}.json", year, month, day);
            var apiResponse = client.GetAsync(ViewBag.RequestingUrl).Result;
            if (apiResponse.IsSuccessStatusCode)
            {
                result = apiResponse.Content.ReadAsStringAsync().Result;
                ViewBag.Result = result;
            }
            else
            {
                ViewBag.Result = apiResponse.StatusCode;
            }
            return View();
        }

        public ActionResult ShowHeartDay()
        {
            string result = String.Empty;
            HttpClient client = getAuthorisedClient();
            int day = DateTime.Now.Day;
            int month = DateTime.Now.Month;
            int year = DateTime.Now.Year;
            ViewBag.RequestingUrl = String.Format(apiAddress+"/activities/heart/date/{0}/{1}.json", "today", "1d");
            var apiResponse = client.GetAsync(ViewBag.RequestingUrl).Result;
            if (apiResponse.IsSuccessStatusCode)
            {
                result = apiResponse.Content.ReadAsStringAsync().Result;
                ViewBag.Result = result;
            }
            else
            {
                ViewBag.Result = apiResponse.StatusCode;
            }
            return View();
        }

        private HttpClient getAuthorisedClient()
        {
            HttpClient client = new HttpClient();
            client.DefaultRequestHeaders.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", (String)Session["InitialAccessToken"]);
            return client;
        }
    }
}