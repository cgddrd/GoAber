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
using System.Data.Entity;
using System.Web;
using GoAber.Models;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using System.IO;
using System.Web.Script.Serialization;
using GoAber.OAuth;

namespace GoAber.Controllers
{
    public class JawboneController : DeviceAPI
    {
       protected override string DeviceName()
        {
            return "Jawbone";
        }
        protected override string[] Scope()
        {
            return new[] { "basic_read", "move_read", "heartrate_read" };
        }

        
         private HttpWebRequest GetRequest(String code)
         {
            DeviceType deviceType = deviceService.FindDeviceTypeByName(DeviceName());
            List<String[]> formdata = new List<String[]>();
            formdata.Add(new String[] { "client_id", deviceType.clientId });
            formdata.Add(new String[] { "client_secret", deviceType.consumerSecret });
            formdata.Add(new String[] { "grant_type", "authorization_code" });
            formdata.Add(new String[] { "code", code });

            HttpWebRequest request = ManualHttpRequest.CreateRequest(
                new Uri(deviceType.tokenEndpoint),
                "POST",
                "application/x-www-form-urlencoded",
                new List<String[]>(),
                formdata
                );
            return request;
        }

        /**
         * Due to issues with the call to WebServerClient.ProcessUserAuthorization()
         *  the Jawbone device must have its oven Callback function, rather than using DeviceAPI's.
         *
         */
        public override ActionResult Callback(string code)
        {
            var user = UserManager.FindById(User.Identity.GetUserId());

            WebServerClient jawbone = GetClient();

            if (jawbone == null)
            {
                ViewBag.Message += "Got token<br />";
                return Redirect("../Devices/Index");
            }
            ViewBag.Message = String.Format("Started Callback with Code: {0}  ", code);
            try
            {
                HttpWebRequest request = GetRequest(code);
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();

                Stream dataStream = response.GetResponseStream();
                StreamReader reader = new StreamReader(dataStream);
                JavaScriptSerializer js = new JavaScriptSerializer();
                var authorisation = js.Deserialize<dynamic>(reader.ReadToEnd());
                               
                reader.Close();
                dataStream.Close();
                response.Close();

                ViewBag.Message += "Authorisation not null<br />";
                ViewBag.InitialAccessToken = authorisation["access_token"];
                Session["InitialAccessToken"] = authorisation["access_token"];
                ViewBag.InitialRefreshToken = authorisation["refresh_token"];
                Session["InitialRefreshToken"] = authorisation["refresh_token"];

                string token = authorisation["access_token"];
                ViewBag.ExtraAccessToken = token;
                ViewBag.ExtraRefreshToken = authorisation["refresh_token"];
                ViewBag.AccessTokenExpiration = DateTime.Now.AddSeconds(authorisation["expires_in"]); 

                
                if (authorisation == null)
                {
                    ViewBag.Message = "Device not authorised!";
                    return Redirect("../Devices/Index");
                }

                DeviceType deviceType = deviceService.FindDeviceTypeByName(DeviceName());
                if(deviceType == null)
                {
                    ViewBag.Message = "Could not find jawbone connectivity settings!";
                    return Redirect("../Devices/Index");
                }

                Device device = new Device();

                device.ConstructionFactory(
                    authorisation["access_token"],
                    authorisation["refresh_token"],
                    deviceType.Id,
                    DateTime.Now.AddSeconds(authorisation["expires_in"]),
                    user.Id);

                Device temp = deviceService.FindDevice(user.Id, DeviceName()); // mock user ID for now - should be getting this from session?
                
                db.Devices.Add(device);
                db.SaveChanges();
                return Redirect("../Devices/Index"); // redirect to list of actions
            }
            catch (Exception e)
            {
                ViewBag.Message += "Exception accessing the code. " + e.Message;
            }
            return Redirect("../Devices/Index");
        }




        public override ActivityData GetWalkingSteps(int year, int month, int day)
        {
            var user = UserManager.FindById(User.Identity.GetUserId());

            ActivityData activityDay = GetWalkingSteps(String.Format("/moves?date={0}{1}{2}", year, month, day), "data.items[0].details.steps", user.Id, day, month, year);
            return activityDay;
        }

        public override ActivityData GetHeartRate(int year, int month, int day)
        {
            var user = UserManager.FindById(User.Identity.GetUserId());
            ActivityData activityHeart = GetHeartRate(String.Format("/heartrates?date={0}{1}{2}", year, month, day), "data.items[0].resting_heartrate", user.Id, day, month, year);
            
            return activityHeart;
        }

        [HttpPost, ActionName("GetActivityData")]
        public ActionResult GetActivityData(DateTime dateJawbone, int jawboneDeviceID, int fitBitDeviceID, int fitbitSteps)
        {
            int day = dateJawbone.Day;
            int month = dateJawbone.Month;
            int year = dateJawbone.Year;

            ActivityData activityDay = GetWalkingSteps(year, month, day);
            if (activityDay != null)
            {
                ViewBag.JawboneSteps = activityDay.value;
            }
            else
            {
                ViewBag.JawboneSteps = 0;
            }

            ViewBag.JawboneConnected = true;
            ViewBag.FitBitDeviceID = fitBitDeviceID;
            ViewBag.JawboneDeviceID = jawboneDeviceID;
            ViewBag.FitbitSteps = fitbitSteps;
            return View("../Devices/Index");
        }


        /////////////////
        //  Test code  //
        public ActionResult GetActivityDay()
          {
              int day = 26;
              int month = 10;
              int year = 2015;

              ActivityData activityDay = GetWalkingSteps(year, month, day);
              if (activityDay != null)
              {
                  ViewBag.Result = activityDay.value;
              }
              return View();
          }

          public ActionResult GetHeartDay()
          {
              int day = 6;
              int month = 11;
              int year = 2015;
            
              ActivityData activityHeart = GetHeartRate(year, month, day);
              if (activityHeart != null)
              {
                  ViewBag.Result = activityHeart.value;
              }
              return View();
          }
    }
}