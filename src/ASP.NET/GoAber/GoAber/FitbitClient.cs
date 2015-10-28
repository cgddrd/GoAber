using System;
using System.Configuration;
using DotNetOpenAuth.OAuth2;

namespace GoAber
{
    public class FitbitClient
    {
       public static readonly AuthorizationServerDescription ServiceDescription = new AuthorizationServerDescription
       {
            TokenEndpoint = new Uri("https://api.fitbit.com/oauth2/token"),
            AuthorizationEndpoint = new Uri("https://www.fitbit.com/oauth2/authorize"),
       };

       /// <summary>
       /// Initializes a new instance of the <see cref="WindowsLiveClient"/> class.
       /// </summary>
       public FitbitClient()
       {

       }

        public static bool IsFitbitClientConfigured
        {
            get
            {
                return !string.IsNullOrEmpty(ConfigurationManager.AppSettings["fitbitConsumerKey"]) &&
                       !string.IsNullOrEmpty(ConfigurationManager.AppSettings["fitbitConsumerSecret"]);
            }
        }

        public static class Scopes
        {
           public const string Activity = "activity";
           public const string Heartrate = "heartrate";
           public const string Location = "location";
           public const string Nutrition = "nutrition";
           public const string Profile = "profile";
           public const string Settings = "settings";
           public const string Sleep = "sleep";
           public const string Social = "social";
           public const string Weight = "weight";
       }
    }

}
