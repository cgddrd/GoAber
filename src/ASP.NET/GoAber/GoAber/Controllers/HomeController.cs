using GoAber.Helpers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace GoAber.Controllers
{
    public class HomeController : BaseController
    {
        public ActionResult Index()
        {
            ViewBag.Title = Resources.Resources.HomeController_Index_Home_Page;
            return View();
        }

        public ActionResult About()
        {
            ViewBag.Title = Resources.Resources.HomeController_About_About;
            ViewBag.Message = Resources.Resources.HomeController_About_application_description;

            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Title = Resources.Resources.HomeController_Contact_title;
            ViewBag.Message = Resources.Resources.HomeController_Contact_contact_page_header;

            return View();
        }

        /// <summary>
        /// Code sourced from the following url.
        /// http://afana.me/post/aspnet-mvc-internationalization.aspx
        /// </summary>
        public ActionResult SetCulture(string culture)
        {
            // Validate input
            culture = CultureHelper.GetImplementedCulture(culture);
            // Save culture in a cookie
            HttpCookie cookie = Request.Cookies["_culture"];
            if (cookie != null)
                cookie.Value = culture;   // update cookie value
            else
            {
                cookie = new HttpCookie("_culture");
                cookie.Value = culture;
                cookie.Expires = DateTime.Now.AddYears(1);
            }
            Response.Cookies.Add(cookie);
            return RedirectToAction("Index");
        }
    }
}
