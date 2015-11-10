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

namespace GoAber.Controllers
{
    public class JawboneController : Controller//, DeviceAPI
    {
        private string AccessToken
        {
            get { return (string)Session["FitbitAccessToken"]; }
            set { Session["FitbitAccessToken"] = value; }
        }

        public static readonly AuthorizationServerDescription ServiceDescription = new AuthorizationServerDescription
        {
            TokenEndpoint = new Uri("https://jawbone.com/auth/oauth2/token"),
            AuthorizationEndpoint = new Uri("https://jawbone.com/auth/oauth2/auth"),
        };

        private WebServerClient fitbit = new WebServerClient(
                ServiceDescription,
                "2mcFGghH9so",
                "f0ca3e7da09288d18bc5b4053704f1a3e43d22da");

        public ActionResult Index()
        {
            fitbit.RequestUserAuthorization(
                new[] { "basic_read", "move_read" },
                new Uri(Url.Action("Callback", "Jawbone", null, Request.Url.Scheme)));
            return View();
        }

        public ActionResult Callback(string code)
        {
            ViewBag.Message = String.Format("Started Callback with Code: {0}<br />", code);

            try
            {
                IAuthorizationState authorisation = fitbit.ProcessUserAuthorization();
                if (authorisation != null)
                {
                    ViewBag.Message += "Authorisation not null<br />";
                    ViewBag.InitialAccessToken = authorisation.AccessToken;
                    Session["InitialAccessToken"] = authorisation.AccessToken;
                    ViewBag.InitialRefreshToken = authorisation.RefreshToken;
                    Session["InitialRefreshToken"] = authorisation.RefreshToken;

                    if (authorisation.AccessTokenExpirationUtc.HasValue)
                    {
                        fitbit.RefreshAuthorization(authorisation, TimeSpan.FromMinutes(30));
                        string token = authorisation.AccessToken;
                        ViewBag.ExtraAccessToken = token;
                        ViewBag.ExtraRefreshToken = authorisation.RefreshToken;
                        ViewBag.AccessTokenExpiration = authorisation.AccessTokenExpirationUtc;
                        ViewBag.Message += "Got token<br />";
                    }

                    ViewBag.Message += "finished the callback.";
                }
            }
            catch (Exception e)
            {
                ViewBag.Message += "Exception accessing the code. " + e.Message;
            }

            return View();
        }

        public ActionResult ShowDay()
        {

            ViewBag.Result = "Starting <br />";
            string result = String.Empty;
            using (HttpClient client = new HttpClient())
            {
                client.DefaultRequestHeaders.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", (String)Session["InitialAccessToken"]);
                ViewBag.RequestingUrl = String.Format("https://api.fitbit.com/1/user/-/activities/date/{0}-{1}-{2}.json", 2015, 05, 01);
                var apiResponse = client.GetAsync(ViewBag.RequestingUrl).Result;
                if (apiResponse.IsSuccessStatusCode)
                {
                    result = apiResponse.Content.ReadAsStringAsync().Result;
                    ViewBag.Result += result;
                }
                else
                {
                    ViewBag.Result = apiResponse.StatusCode;
                }
            }
            return View();
        }





        /*  private const string DEVICENAME = "jawbone";
          //private const string APIADDRESS = "https://jawbone.com/auth/oauth2/auth";
          private ApplicationDbContext db = new ApplicationDbContext();
          private ApplicationUserManager _userManager;

          // CG - We need to create our UserManager instance (copied from AccountController). 
          // This works because the OWIN context is shared application-wide. See: http://stackoverflow.com/a/27751581
          public ApplicationUserManager UserManager
          {
              get
              {
                  return _userManager ?? HttpContext.GetOwinContext().GetUserManager<ApplicationUserManager>();
              }
              private set
              {
                  _userManager = value;
              }
          }

          private WebServerClient getClient()
          {

              DeviceType deviceType = findDeviceTypeByName(DEVICENAME);
              if (deviceType == null)
                  return null;
              AuthorizationServerDescription description = new AuthorizationServerDescription
              {
                  TokenEndpoint = new Uri(deviceType.tokenEndpoint),
                  AuthorizationEndpoint = new Uri(deviceType.authorizationEndpoint),
              };

              WebServerClient jawbone = new WebServerClient(
                  description,
                  deviceType.clientId,
                  deviceType.consumerSecret
              );
              return jawbone;
          }


          private DeviceType findDeviceTypeByName(String name)
          {
              var query = from d in db.DeviceTypes
                          where d.name == name
                          select d;
              DeviceType deviceType = query.FirstOrDefault();
              return deviceType;
          }

          public ActionResult Index()
          {
              var user = UserManager.FindById(User.Identity.GetUserId());

              Device device = FindDevice(user.Id); // GET USER ID FROM SESSION!!

              if(device == null)
                  return RedirectToAction("StartOAuth"); // redirect to authorisation

              if (DateTime.UtcNow > device.tokenExpiration)
                  RefreshToken(user.Id); // Token needs refreshing
              return View();
          }

          public ActionResult StartOAuth()
          {
              WebServerClient jawbone = getClient();
              jawbone.ProcessUserAuthorization();
              if (jawbone == null)
              {
                  ViewBag.Message = "Could not find jawbone connectivity settings!";
              }
              else
              {
                  jawbone.RequestUserAuthorization(
                      new[] { "basic_read", "move_read"},
                      new Uri(Url.Action("Callback", "Jawbone", null, Request.Url.Scheme)));

                  ViewBag.Message = "Redirecting you shortly...";
              }
              return View();
          }

          public ActionResult Callback(string code)
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
                  ViewBag.Message += "Debug : before AuthorizationState  ";
                  IAuthorizationState authorisation = jawbone.ProcessUserAuthorization();
                  ViewBag.Message = "Debug : after IAuthorizationState";
                  if (authorisation == null)
                  {
                      ViewBag.Message = "Device not authorised!";
                      return View();
                  }

                  DeviceType deviceType = findDeviceTypeByName(DEVICENAME);
                  if(deviceType == null)
                  {
                      ViewBag.Message = "Could not find jawbone connectivity settings!";
                      return View();
                  }

                  Device device = new Device();

                  device.ConstructionFactory(
                      authorisation.AccessToken,
                      authorisation.RefreshToken,
                      deviceType.Id,
                      authorisation.AccessTokenExpirationUtc,
                      user.Id);

                  Device temp = FindDevice(user.Id); // mock user ID for now - should be getting this from session?

                  if (temp != null)
                  {
                      temp.refreshToken = device.refreshToken;
                      temp.tokenExpiration = device.tokenExpiration;
                      temp.accessToken = device.accessToken;
                      temp.deviceTypeId = device.deviceTypeId;
                  }
                  else
                  {
                      db.Devices.Add(device);
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

          private Device FindDevice(string userID)
          {
              var query = from d in db.Devices
                          where d.ApplicationUserId == userID // MOCK USER ID FOR NOW
                          select d;
              return query.FirstOrDefault();
          }


          private String GetCurrentUserAccessToken(string userID)
          {
              Device device = FindDevice(userID);
              if (device == null)
                  return null; // No token availible for this user
              if (DateTime.UtcNow > device.tokenExpiration)
                  return RefreshToken(userID); // Token needs refreshing
              return device.accessToken;
          }


          private String RefreshToken(string userID)
          {
              WebServerClient fitbit = getClient();
              Device device = FindDevice(userID);
              if (device != null) {
                  try
                  {
                      IAuthorizationState state = new AuthorizationState();
                      state.AccessToken = device.accessToken; 
                      state.Callback = new Uri(device.deviceType.tokenEndpoint);
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


           * ------------------------------------------
           *  START API CALLS FOR VARIOUS METHODS HERE
           * ------------------------------------------
           *
          public ActivityData GetDayActivities(string ls_path, string userID, int day, int month, int year)
          {
              string token = GetCurrentUserAccessToken(userID);
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
                  ActivityData data = new ActivityData(categoryUnitID, userID, date, DateTime.Now, steps);
                  return data;
              }
              ViewBag.Result = apiResponse.StatusCode;
              return null;
          }

          public ActivityData GetDayHeart(string ls_path, string userID, int day, int month, int year)
          {
              string token = GetCurrentUserAccessToken(userID);
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
                  int restingHeartRate = 0;
                  int categoryUnitID = 0;
                  DateTime date = new DateTime(year, month, day);
                  ActivityData data = new ActivityData(categoryUnitID, userID, date, DateTime.Now, restingHeartRate);
                  return data;
              }
              ViewBag.Result = apiResponse.StatusCode;
              return null;
          }

          public ActionResult GetActivityDay()
          {
              int day = 6;
              int month = 11;
              int year = 2015;

              var user = UserManager.FindById(User.Identity.GetUserId());

              ActivityData activityDay = GetDayActivities("/activities/date/", user.Id, day, month, year);
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

              ActivityData activityHeart = GetDayHeart("/activities/heart/date/", user.Id, day, month, year);
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
          }*/
    }
}