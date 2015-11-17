using DotNetOpenAuth.OAuth2;
using GoAber.Models;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace GoAber.Controllers
{
    public abstract class DeviceAPI : Controller
    {
        protected abstract string DeviceName();
        protected abstract string[] Scope();
        public abstract ActivityData GetWalkingSteps(int year, int month, int day);
        public abstract ActivityData GetHeartRate(int year, int month, int day);

        protected ApplicationDbContext db = new ApplicationDbContext();
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

        protected WebServerClient getClient()
        {
            DeviceType deviceType = findDeviceTypeByName(DeviceName());
            if (deviceType == null)
            {
                return null;
            }
            AuthorizationServerDescription description = new AuthorizationServerDescription
            {
                TokenEndpoint = new Uri(deviceType.tokenEndpoint),
                AuthorizationEndpoint = new Uri(deviceType.authorizationEndpoint),
            };

            WebServerClient client = new WebServerClient(
                description,
                deviceType.clientId,
                deviceType.consumerSecret
            );
            return client;
        }


        protected DeviceType findDeviceTypeByName(String name)
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

            if (device == null)
                return RedirectToAction("StartOAuth"); // redirect to authorisation

            if (DateTime.UtcNow > device.tokenExpiration)
                RefreshToken(user.Id); // Token needs refreshing
            return View();
        }


        protected Device FindDevice(string userID)
        {
            string deviceType = findDeviceTypeByName(DeviceName()).name;
            var query = from d in db.Devices
                        where (d.ApplicationUserId == userID // MOCK USER ID FOR NOW
                        && d.deviceType.name == deviceType)
                        select d;
            return query.FirstOrDefault();
        }


        protected String GetCurrentUserAccessToken(string userID)
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
            WebServerClient client = getClient();
            Device device = FindDevice(userID);
            if (device != null)
            {
                try
                {
                    IAuthorizationState state = new AuthorizationState();
                    state.AccessToken = device.accessToken;
                    state.Callback = new Uri(device.deviceType.tokenEndpoint);
                    state.RefreshToken = device.refreshToken;

                    if (client.RefreshAuthorization(state))
                    {
                        device.accessToken = state.AccessToken;
                        device.refreshToken = state.RefreshToken;
                        device.tokenExpiration = state.AccessTokenExpirationUtc;
                        db.Entry(device).State = EntityState.Modified;
                        db.SaveChanges();
                        return state.AccessToken;
                    }

                }
                catch (Exception e)
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


        public ActionResult StartOAuth()
        {
            WebServerClient client = getClient();
            if (client == null)
            {
                ViewBag.Message = "Could not find " + DeviceName() + " connectivity settings!";
            }
            else
            {
                client.RequestUserAuthorization(
                    Scope(),
                    new Uri(Url.Action("Callback", DeviceName(), null, Request.Url.Scheme)));

                ViewBag.Message = "Redirecting you shortly...";
            }
            return View();
        }


        public virtual ActionResult Callback(string code)
        {
            var user = UserManager.FindById(User.Identity.GetUserId());

            WebServerClient client = getClient();
            if (client == null)
            {
                ViewBag.Message += "Got token<br />";
                return View();
            }
            ViewBag.Message = String.Format("Started Callback with Code: {0}<br />", code);
            try
            {
                IAuthorizationState authorisation = client.ProcessUserAuthorization();
                if (authorisation == null)
                {
                    ViewBag.Message = "Device not authorised!";
                    return View();
                }
                DeviceType deviceType = findDeviceTypeByName(DeviceName());
                if (deviceType == null)
                {
                    ViewBag.Message = "Could not find " + DeviceName() + " connectivity settings!";
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


        public ActivityData GetWalkingSteps(string ls_path, string jsonPath, string userID, int day, int month, int year)
        {
            var user = UserManager.FindById(User.Identity.GetUserId());

            CategoryUnit categoryUnit = GetCategoryUnit("Walking", "Steps");

            ActivityData activityDay = GetDayActivity(ls_path, jsonPath, user.Id, day, month, year, categoryUnit);
            return activityDay;
        }

        public ActivityData GetHeartRate(string ls_path, string jsonPath, string userID, int day, int month, int year)
        {
            var user = UserManager.FindById(User.Identity.GetUserId());

            CategoryUnit categoryUnit = GetCategoryUnit("HeartRate", "Beats");

            ActivityData activityDay = GetDayActivity(ls_path, jsonPath, user.Id, day, month, year, categoryUnit);
            return activityDay;
        }

        //TODO this should be moved to somewhere related to the CategoryUnit
        public CategoryUnit GetCategoryUnit(string category, string unit)
        {
            var query = from d in db.CategoryUnits
                        where (d.category.name.Equals(category)
                                && d.unit.name.Equals(unit)
                                )
                        select d;
            CategoryUnit categoryUnit = query.FirstOrDefault();
            return categoryUnit;
        }

        public ActivityData GetDayActivity(string ls_path, string jsonPath, string userID, int day, int month, int year, CategoryUnit categoryUnit )
        {
            string token = GetCurrentUserAccessToken(userID);
            if (String.IsNullOrEmpty(token))
            {
                return null;
            }
            //-----------------------------
            string result = String.Empty;
            HttpClient client = getAuthorisedClient(token);

            DeviceType deviceType = findDeviceTypeByName(DeviceName());
            ViewBag.RequestingUrl = deviceType.apiEndpoint + ls_path;
            var apiResponse = client.GetAsync(ViewBag.RequestingUrl).Result;
            if (apiResponse.IsSuccessStatusCode)
            {
                result = apiResponse.Content.ReadAsStringAsync().Result;
                JToken jToken = JObject.Parse(result);
                int value = (int)jToken.SelectToken(jsonPath);
                
                DateTime date = new DateTime(year, month, day);
                ActivityData data = new ActivityData(categoryUnit.categoryId, userID, date, DateTime.Now, value);
                db.ActivityDatas.Add(data);
                db.SaveChanges();
                return data;
            }
            ViewBag.Result = apiResponse.StatusCode;
            return null;
        }

        /*
        public ActivityData GetDayActivities(string ls_path, string jsonPath, string userID, int day, int month, int year)
        {
            string token = GetCurrentUserAccessToken(userID);
            if (String.IsNullOrEmpty(token))
                return null;
            //-----------------------------
            string result = String.Empty;
            HttpClient client = getAuthorisedClient(token);

            DeviceType deviceType = findDeviceTypeByName(DeviceName());
            ViewBag.RequestingUrl = deviceType.apiEndpoint + ls_path;//"/moves?date={0}{1}{2}", year, month, day);
            var apiResponse = client.GetAsync(ViewBag.RequestingUrl).Result;
            if (apiResponse.IsSuccessStatusCode)
            {
                result = apiResponse.Content.ReadAsStringAsync().Result;
                JToken jToken = JObject.Parse(result);
                int steps = (int)jToken.SelectToken(jsonPath);
                int categoryUnitID = 0;
                DateTime date = new DateTime(year, month, day);
                ActivityData data = new ActivityData(categoryUnitID, userID, date, DateTime.Now, steps);
                return data;
            }
            ViewBag.Result = apiResponse.StatusCode;
            return null;
        }

        public ActivityData GetDayHeart(string ls_path, string jsonPath, string userID, int day, int month, int year)
        {
            string token = GetCurrentUserAccessToken(userID);
            if (String.IsNullOrEmpty(token))
                return null;
            //-----------------------------
            string result = String.Empty;
            HttpClient client = getAuthorisedClient(token);
            DeviceType deviceType = findDeviceTypeByName(DeviceName());
            ViewBag.RequestingUrl = deviceType.apiEndpoint + ls_path;//"/heartrates?date={0}{1}{2}", year, month, day);
            var apiResponse = client.GetAsync(ViewBag.RequestingUrl).Result;
            if (apiResponse.IsSuccessStatusCode)
            {
                result = apiResponse.Content.ReadAsStringAsync().Result;
                JToken jToken = JObject.Parse(result);
                JToken summary = jToken.SelectToken(jsonPath);
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
     */
        private HttpClient getAuthorisedClient(string token)
        {
            HttpClient client = new HttpClient();
            client.DefaultRequestHeaders.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);
            return client;
        }
    }
}
