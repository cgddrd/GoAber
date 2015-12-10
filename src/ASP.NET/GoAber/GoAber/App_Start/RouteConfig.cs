using Newtonsoft.Json.Serialization;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;

namespace GoAber
{
    public class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");

            //routes.MapRoute(
            //    name: "Default",
            //    url: "{controller}/{action}/{id}",
            //    defaults: new { controller = "Home", action = "Index", id = UrlParameter.Optional },
            //    namespaces: new string[] { "GoAber.Controllers" }
            //);

            // CG - Changed the default routing to redirect to the 'WeeklySummary' page for user activity data. 
            // (Redirects to login if user is not logged in).
            routes.MapRoute(
                name: "Default",
                url: "{controller}/{action}/{id}",
                defaults: new { controller = "ActivityDatas", action = "WeeklySummary", id = UrlParameter.Optional },
                namespaces: new string[] { "GoAber.Controllers" }
            );
        }
    }
}
