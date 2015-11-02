using System.Configuration;
using System.Web.Mvc;
using DotNetOpenAuth.OAuth2;
using System.Net;
using System;
using System.Net.Http;
using GoAber.Controllers;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace GoAber.Controllers
{
    public class FitBitController : Controller, OAuthConnectivity
    {
        private string AccessToken
        {
            get { return (string)Session["FitbitAccessToken"]; }
            set { Session["FitbitAccessToken"] = value; }
        }

        private static AuthorizationServerDescription description = new AuthorizationServerDescription
        {
            TokenEndpoint = new Uri("https://api.fitbit.com/oauth2/token"),
            AuthorizationEndpoint = new Uri("https://www.fitbit.com/oauth2/authorize")
        };

        private WebServerClient fitbit = new WebServerClient(
                description,
                ConfigurationManager.AppSettings["FitbitClientId"],
                ConfigurationManager.AppSettings["FitbitConsumerSecret"]
        );

        public ActionResult Index()
        {
            return View();
        }

        public ActionResult StartOAuth()
        {
            fitbit.RequestUserAuthorization(
                new[] { "activity", "heartrate", "sleep" },
                new Uri(Url.Action("Callback", "FitBit", null, Request.Url.Scheme)));
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
            string result = String.Empty;
            HttpClient client = getAuthorisedClient();
            client.DefaultRequestHeaders.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", (String)Session["InitialAccessToken"]);
            int day = DateTime.Now.Day;
            int month = DateTime.Now.Month;
            int year = DateTime.Now.Year;
            ViewBag.RequestingUrl = String.Format("https://api.fitbit.com/1/user/-/activities/date/{0}-{1}-{2}.json", year, month, day);
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
            ViewBag.RequestingUrl = String.Format("https://api.fitbit.com/1/user/-/activities/heart/date/{0}/{1}.json", "today", "1d");
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