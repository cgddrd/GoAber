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
    public class JawboneController : Controller//, DeviceAPI
    {
        /*
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
                //IAuthorizationState authorisation = fitbit.ProcessUserAuthorization();

                HttpWebRequest authorisation = (HttpWebRequest)WebRequest.Create("https://jawbone.com/auth/oauth2/token?grant_type=authorization_code&client_id=2mcFGghH9so&client_secret=f0ca3e7da09288d18bc5b4053704f1a3e43d22da&code="+code);
                if (authorisation != null)
                {
                    HttpWebResponse response = (HttpWebResponse)authorisation.GetResponse();

                    // Get the stream containing content returned by the server.
                    Stream dataStream = response.GetResponseStream();
                    StreamReader reader = new StreamReader(dataStream);
                    JavaScriptSerializer js = new JavaScriptSerializer();
                    var authorisationResponse = js.Deserialize<dynamic>(reader.ReadToEnd());
                    ViewBag.Message += authorisationResponse["access_token"];              
                    
                    // Cleanup the streams and the response.
                    reader.Close();
                    dataStream.Close();
                    response.Close();
                    
                    ViewBag.Message += "Authorisation not null<br />";
                    ViewBag.InitialAccessToken = authorisationResponse["access_token"];
                    Session["InitialAccessToken"] = authorisationResponse["access_token"];
                    ViewBag.InitialRefreshToken = authorisationResponse["refresh_token"];
                    Session["InitialRefreshToken"] = authorisationResponse["refresh_token"];
                    
                   // if (authorisationResponse["expires_in"].HasValue)
                    //{
                       // fitbit.RefreshAuthorization(authorisation, TimeSpan.FromMinutes(30));
                        string token = authorisationResponse["access_token"];
                        ViewBag.ExtraAccessToken = token;
                        ViewBag.ExtraRefreshToken = authorisationResponse["refresh_token"];
                        ViewBag.AccessTokenExpiration = authorisationResponse["expires_in"];
                        ViewBag.Message += "Got token<br />";
                  //  }

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

        */



          private const string DEVICENAME = "jawbone";
          private const string APIADDRESS =  "https://jawbone.com/nudge/api/v.1.1/users/@me";
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
             // jawbone.ProcessUserAuthorization();
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
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                // Get the stream containing content returned by the server.
                Stream dataStream = response.GetResponseStream();
                StreamReader reader = new StreamReader(dataStream);
                JavaScriptSerializer js = new JavaScriptSerializer();
                var authorisation = js.Deserialize<dynamic>(reader.ReadToEnd());
                //ViewBag.Message += authorisationResponse["access_token"];

                // Cleanup the streams and the response.
                reader.Close();
                dataStream.Close();
                response.Close();

                ViewBag.Message += "Authorisation not null<br />";
                ViewBag.InitialAccessToken = authorisation["access_token"];
                Session["InitialAccessToken"] = authorisation["access_token"];
                ViewBag.InitialRefreshToken = authorisation["refresh_token"];
                Session["InitialRefreshToken"] = authorisation["refresh_token"];

                // if (authorisationResponse["expires_in"].HasValue)
                //{
                // fitbit.RefreshAuthorization(authorisation, TimeSpan.FromMinutes(30));
                string token = authorisation["access_token"];
                ViewBag.ExtraAccessToken = token;
                ViewBag.ExtraRefreshToken = authorisation["refresh_token"];
                ViewBag.AccessTokenExpiration = DateTime.Now.AddSeconds(authorisation["expires_in"]); //authorisation["expires_in"];
                ViewBag.Message += "Got token<br />";
                //  }

                ViewBag.Message += "finished the callback.";



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
                      authorisation["access_token"],
                      authorisation["refresh_token"],
                      deviceType.Id,
                      DateTime.Now.AddSeconds(authorisation["expires_in"]),
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


          /* ------------------------------------------
           *  START API CALLS FOR VARIOUS METHODS HERE
           * ------------------------------------------
           */
          public ActivityData GetDayActivities(string ls_path, string userID, int day, int month, int year)
          {
              string token = GetCurrentUserAccessToken(userID);
              if (String.IsNullOrEmpty(token))
                  return null;
              //-----------------------------
              string result = String.Empty;
              HttpClient client = getAuthorisedClient(token);
              ViewBag.RequestingUrl = String.Format(APIADDRESS + "/moves?date={0}{1}{2}", year, month, day);
              var apiResponse = client.GetAsync(ViewBag.RequestingUrl).Result;
              if (apiResponse.IsSuccessStatusCode)
              {
                  result = apiResponse.Content.ReadAsStringAsync().Result;
                  JToken jToken = JObject.Parse(result);
                  JToken summary = jToken.SelectToken("data");
                 // summary = summary.SelectToken("items");
                int categoryUnitID = 0;
                  int steps = (int)summary.SelectToken("items[0].details.steps");
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
              ViewBag.RequestingUrl = String.Format(APIADDRESS + "/heartrates?date={0}{1}{2}", year, month, day);
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
              int day = 26;
              int month = 10;
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
          }
    }
}