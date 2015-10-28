using System.Configuration;
using System.Web.Mvc;
using DotNetOpenAuth.OAuth2;
using System.Net;
using System;
using System.Net.Http;
using GoAber.Controllers;

namespace GoAber.Controllers
{
    public class OAuthController : Controller, OAuthConnectivity 
    {
        private string AccessToken
        { 
 			get { return (string)Session["FitbitAccessToken"]; } 
 			set { Session["FitbitAccessToken"] = value; } 
 		}

        private WebServerClient fitbit = new WebServerClient(
                FitbitClient.ServiceDescription,
                ConfigurationManager.AppSettings["FitbitClientId"],
                ConfigurationManager.AppSettings["FitbitConsumerSecret"]);

        public ActionResult Index()
        {
            return View(); 
        }

        public ActionResult StartOAuth()
        {
            fitbit.RequestUserAuthorization(
                new[] { FitbitClient.Scopes.Activity, FitbitClient.Scopes.Heartrate, FitbitClient.Scopes.Sleep }, 
                new Uri(Url.Action("Callback", "OAuth", null, Request.Url.Scheme))); 
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
            catch(Exception e) 
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
                if(apiResponse.IsSuccessStatusCode)
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

    }
}