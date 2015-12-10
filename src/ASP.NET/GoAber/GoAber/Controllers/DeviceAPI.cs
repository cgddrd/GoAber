using DotNetOpenAuth.OAuth2;
using GoAber.Models;
using GoAber.Services;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Diagnostics;
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
        public abstract ActivityData GetWalkingSteps(int year, int month, int day, bool useDB = false);
        public abstract ActivityData GetHeartRate(int year, int month, int day, bool useDB = false);

        protected ApplicationDbContext db = new ApplicationDbContext();
        protected DeviceService deviceService = new DeviceService();
        protected CategoryUnitService categoryUnitService = new CategoryUnitService();
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

        protected WebServerClient GetClient()
        {
            DeviceType deviceType = deviceService.FindDeviceTypeByName(DeviceName());
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


        

        public ActionResult Index()
        {
            var user = UserManager.FindById(User.Identity.GetUserId());

            Device device = deviceService.FindDevice(user.Id, DeviceName()); // GET USER ID FROM SESSION!!

            if (device == null)
            {
                return RedirectToAction("StartOAuth"); // redirect to authorisation
            }
            if (DateTime.UtcNow > device.tokenExpiration)
            {
                RefreshToken(user.Id); // Token needs refreshing
            }
            return Redirect("../Devices/Index");
        }

        protected String GetCurrentUserAccessToken(string userID)
        {
            Device device = deviceService.FindDevice(userID, DeviceName());
            if (device == null)
                return null; // No token availible for this user
            if (DateTime.UtcNow > device.tokenExpiration)
                return RefreshToken(userID); // Token needs refreshing
            return device.accessToken;
        }


        private String RefreshToken(string userID)
        {
            WebServerClient client = GetClient();
            Device device = deviceService.FindDevice(userID, DeviceName());
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
            WebServerClient client = GetClient();
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
            return Redirect("../Devices/Index");
        }
        

        public virtual ActionResult Callback(string code)
        {
            var user = UserManager.FindById(User.Identity.GetUserId());

            WebServerClient client = GetClient();
            if (client == null)
            {
                return Redirect("../Devices/Index");
            }
            ViewBag.Message = String.Format("Started Callback with Code: {0}<br />", code);
            try
            {
                IAuthorizationState authorisation = client.ProcessUserAuthorization();
                if (authorisation == null)
                {
                    ViewBag.Message = "Device not authorised!";
                    return Redirect("../Devices/Index");
                }
                DeviceType deviceType = deviceService.FindDeviceTypeByName(DeviceName());
                if (deviceType == null)
                {
                    ViewBag.Message = "Could not find " + DeviceName() + " connectivity settings!";
                    return Redirect("../Devices/Index");
                }

                Device device = new Device();

                device.ConstructionFactory(
                    authorisation.AccessToken,
                    authorisation.RefreshToken,
                    deviceType.Id,
                    authorisation.AccessTokenExpirationUtc,
                    user.Id);

                // if a device of this type already exisits for this user, update it
                Device deviceToUpdate = deviceService.FindDevice(user.Id, DeviceName()); // mock user ID for now - should be getting this from session?

                if (deviceToUpdate != null)
                {
                    deviceToUpdate.refreshToken = device.refreshToken;
                    deviceToUpdate.tokenExpiration = device.tokenExpiration;
                    deviceToUpdate.accessToken = device.accessToken;
                    deviceToUpdate.deviceTypeId = device.deviceTypeId;
                }
                else
                {
                    db.Devices.Add(device);
                }
                db.SaveChanges();
                return Redirect("../Devices/Index");
                // return RedirectToAction("Index"); // redirect to list of actions
            }
            catch (Exception e)
            {
                ViewBag.Message += "Exception accessing the code. " + e.Message;
            }

            return Redirect("../Devices/Index");
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="ls_path">Relative path to the walking steps (e.g. /Moves)</param>
        /// <param name="jsonPath">JSON xPath to the avtivity data</param>
        /// <param name="userID"></param>
        /// <param name="day"></param>
        /// <param name="month"></param>
        /// <param name="year"></param>
        /// <returns></returns>
        public ActivityData GetWalkingSteps(string ls_path, string jsonPath, string userID, int day, int month, int year, bool useDB = false)
        {
            try {
                ApplicationUser user;
                if (String.IsNullOrWhiteSpace(userID))
                {
                    user = UserManager.FindById(User.Identity.GetUserId());
                    userID = user.Id;
                }
                else
                {
                    //user = UserManager.FindById(userID);
                }


                CategoryUnit categoryUnit = categoryUnitService.GetCategoryUnit("Walking", "Steps");

                ActivityData activityDay = GetDayActivity(ls_path, jsonPath, userID, day, month, year, categoryUnit, useDB);
                return activityDay;
            } catch (Exception e)
            {
                Debug.WriteLine(e.StackTrace);
                return null;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="ls_path">Relative path to the walking steps (e.g. /Moves)</param>
        /// <param name="jsonPath">JSON xPath to the avtivity data</param>
        /// <param name="userID"></param>
        /// <param name="day"></param>
        /// <param name="month"></param>
        /// <param name="year"></param>
        /// <returns></returns>
        public ActivityData GetHeartRate(string ls_path, string jsonPath, string userID, int day, int month, int year, bool useDB = false)
        {
            try
            {
                ApplicationUser user;
            if (String.IsNullOrWhiteSpace(userID))
            {
                user = UserManager.FindById(User.Identity.GetUserId());
            } else
            {
                //user = UserManager.FindById(userID);
            }

            CategoryUnit categoryUnit = categoryUnitService.GetCategoryUnit("HeartRate", "Beats");

            ActivityData activityDay = GetDayActivity(ls_path, jsonPath, userID, day, month, year, categoryUnit, useDB);
            return activityDay;
        } catch (Exception e)
            {
                Debug.WriteLine(e.StackTrace);
                return null;
            }
}

        

        public ActivityData GetDayActivity(string ls_path, string jsonPath, string userID, int day, int month, int year, CategoryUnit categoryUnit, Boolean useDB = true)
        {
            string token = GetCurrentUserAccessToken(userID);
            if (String.IsNullOrEmpty(token))
            {
                return null;
            }
            //-----------------------------
            string result = String.Empty;
            HttpClient client = getAuthorisedClient(token);

            DeviceType deviceType = deviceService.FindDeviceTypeByName(DeviceName());
            ViewBag.RequestingUrl = deviceType.apiEndpoint + ls_path;
            var apiResponse = client.GetAsync(ViewBag.RequestingUrl).Result;
            if (apiResponse.IsSuccessStatusCode)
            {
                result = apiResponse.Content.ReadAsStringAsync().Result;
                JToken jToken = JObject.Parse(result);

                int value = 0;
                try
                {
                    value = (int)jToken.SelectToken(jsonPath);
                }
                catch{/* 0 steps for given date */}
                DateTime date = new DateTime(year, month, day);
                ActivityData data = new ActivityData(categoryUnit.categoryId, userID, date, DateTime.Now, value);
                if (useDB)
                {
                    db.ActivityDatas.Add(data);
                    db.SaveChanges();
                }
                return data;
            }
            ViewBag.Result = apiResponse.StatusCode;
            return null;
        }
        
        private HttpClient getAuthorisedClient(string token)
        {
            HttpClient client = new HttpClient();
            client.DefaultRequestHeaders.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);
            return client;
        }
    }
}
