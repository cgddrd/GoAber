using System.Web.Mvc;

namespace GoAber.Areas.MyAccount
{
    public class MyAccountAreaRegistration : AreaRegistration 
    {
        public override string AreaName 
        {
            get 
            {
                return "MyAccount";
            }
        }

        public override void RegisterArea(AreaRegistrationContext context) 
        {
            context.MapRoute(
                "MyAccount_default",
                "MyAccount/{controller}/{action}/{id}",
                new { action = "Index", id = UrlParameter.Optional }
            );

            //context.MapRoute(
            //    name: "Default_MyAccount_Area_Redirect",
            //    url: "{controller}/{action}/{id}",
            //    defaults: new { controller = "Home", action = "Index", id = UrlParameter.Optional },
            //    namespaces: new string[] { "GoAber.Controllers" }
            //);
        }
    }
}