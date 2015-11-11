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
       // DEVICENAME = "Jawbone";
      //    APIADDRESS = "https://jawbone.com/nudge/api/v.1.1/users/@me";
        //  scope = new[] { "basic_read", "move_read" };
        
        protected override string DeviceName()
        {
            return "Jawbone";
        }
        protected override string[] Scope()
        {
            return new[] { "basic_read", "move_read" };
        }


         private HttpWebRequest GetRequest(String code)
         {
            List<String[]> formdata = new List<String[]>();
            formdata.Add(new String[] { "client_id", "2mcFGghH9so" });
            formdata.Add(new String[] { "client_secret", "f0ca3e7da09288d18bc5b4053704f1a3e43d22da" });
            formdata.Add(new String[] { "grant_type", "authorization_code" });
            formdata.Add(new String[] { "code", code });

            HttpWebRequest request = ManualHttpRequest.CreateRequest(
                new Uri("https://jawbone.com/auth/oauth2/token"),
                "POST",
                "application/x-www-form-urlencoded",
                new List<String[]>(),
                formdata
                );
            return request;
        }

          public override ActionResult Callback(string code)
          {
              var user = UserManager.FindById(User.Identity.GetUserId());

              WebServerClient jawbone = getClient();

              if (jawbone == null)
              {
                  ViewBag.Message += "Got token<br />";
                  return View();
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
                ViewBag.AccessTokenExpiration = DateTime.Now.AddSeconds(authorisation["expires_in"]); //authorisation["expires_in"];
                ViewBag.Message += "Got token<br />";
                ViewBag.Message += "finished the callback.";

                
                  if (authorisation == null)
                  {
                      ViewBag.Message = "Device not authorised!";
                      return View();
                  }

                  DeviceType deviceType = findDeviceTypeByName(DeviceName());
                  if(deviceType == null)
                  {
                      ViewBag.Message = "Could not find jawbone connectivity settings!";
                      return View();
                  }

                  Device device = new Device();

                  device.ConstructionFactory(
                      authorisation["access_token"],
                      authorisation["refresh_token"],
                      deviceType.Id,
                      DateTime.Now.AddSeconds(authorisation["expires_in"]),
                      user.Id);

                  Device temp = FindDevice(user.Id); // mock user ID for now - should be getting this from session?
                
                  db.Devices.Add(device);
                  db.SaveChanges();
                  return RedirectToAction("Index"); // redirect to list of actions
              }
              catch (Exception e)
              {
                  ViewBag.Message += "Exception accessing the code. " + e.Message;
              }
              return View();
          }


          public ActionResult GetActivityDay()
          {
              int day = 26;
              int month = 10;
              int year = 2015;

              var user = UserManager.FindById(User.Identity.GetUserId());

              ActivityData activityDay = GetDayActivities(String.Format("/moves?date={0}{1}{2}", year, month, day), "data.items[0].details.steps", user.Id, day, month, year);
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

              var user = UserManager.FindById(User.Identity.GetUserId());

              ActivityData activityHeart = GetDayHeart(String.Format("/heartrates?date={0}{1}{2}", year, month, day),"todo", user.Id, day, month, year);
              if (activityHeart != null)
              {
                  ViewBag.Result = activityHeart.value;
              }
              return View();
          }
    }
}