using GoAber.Models;
using GoAber.Services;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Script.Serialization;

namespace GoAber.Controllers
{
    public class APIController : Controller
    {
        private ActivityDataService dataService = new ActivityDataService();
        private JavaScriptSerializer serializer = new JavaScriptSerializer();
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

        // GET: API
        public ActionResult Index()
        {
            return View();
        }

        //GET: API/WeeklySummary
        public string WeeklySummary(string unit)
        {
            DateTime toDate = DateTime.Today;
            DateTime fromDate = DateTime.Today.AddDays(-7);
            string userId = User.Identity.GetUserId();

            var data = dataService.getUserActivityDataByDateRange(userId, fromDate, toDate);
            data = data.Where(a => a.categoryunit.unit.name == unit);

            var formattedData = FormatActivityData(data);
            var json = serializer.Serialize(formattedData);
            return json;
        }

        private IEnumerable FormatActivityData(IEnumerable<ActivityData> data)
        {
            return data.Select(a => new
            {
                id = a.Id,
                value = a.value,
                category = a.categoryunit.category.name,
                unit = a.categoryunit.unit.name,
                activityDate = a.date.ToString(),
                lastUpdated = a.lastUpdated.ToString(),
                user = a.User.Nickname
            }).ToList();
        }
    }
}