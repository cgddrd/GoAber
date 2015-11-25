using GoAber.Models;
using GoAber.Models.ViewModels;
using GoAber.Services;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using Newtonsoft.Json;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Script.Serialization;

namespace GoAber.Controllers
{
    public class ActivityDatasAPIController : BaseAPIController
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

        //GET: ActivityDatasAPI/WeeklySummary
        public ActionResult WeeklySummary(string unit)
        {
            string userId = User.Identity.GetUserId();
            var data = dataService.WeeklySummary(userId, unit);
            var formattedData = FormatActivityData(data);
            return ToJson(formattedData);
        }

        //GET: ActivityDatasAPI/WeeklyStatistics
        public ActionResult WeeklyStatistics(string unit)
        {
            string userId = User.Identity.GetUserId();
            var data = dataService.WeeklyStatistics(userId, unit);
            return ToJson(data);
        }

        //GET: ActivityDatasAPI/MonthlySummary
        public ActionResult MonthlySummary(string unit)
        {
            string userId = User.Identity.GetUserId();
            var data = dataService.MonthlySummary(userId, unit);
            var formattedData = FormatActivityData(data);
            return ToJson(formattedData);
        }

        //GET: ActivityDatasAPI/MonthlyStatistics
        public ActionResult MonthlyStatistics(string unit)
        {
            string userId = User.Identity.GetUserId();
            var data = dataService.MonthlyStatistics(userId, unit);
            return ToJson(data);
        }

        //GET: ActivityDatasAPI/AllTimeStatistics
        public ActionResult AllTimeStatistics(string unit)
        {
            string userId = User.Identity.GetUserId();
            var data = dataService.AllTimeStatistics(userId, unit);
            return ToJson(data);
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