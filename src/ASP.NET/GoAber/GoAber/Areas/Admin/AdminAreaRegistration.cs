using System.Web.Mvc;

namespace GoAber.Areas.Admin
{
    public class AdminAreaRegistration : AreaRegistration
    {
        public override string AreaName
        {
            get
            {
                return "Admin";
            }
        }

        public override void RegisterArea(AreaRegistrationContext context)
        {

            context.MapRoute(
                "Admin_default",
                "Admin/{controller}/{action}/{id}",
                new { action = "Index", id = UrlParameter.Optional },
                new { controller = "ControlPanel|ActivityData|Teams" },
                new[] { "GoAber.Areas.Admin.Controllers" }
            );
        }
    }
}