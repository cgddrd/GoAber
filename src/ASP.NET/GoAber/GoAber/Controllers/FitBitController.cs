using System.Configuration;
using System.Web.Mvc;
using DotNetOpenAuth.OAuth2;
using System.Net;
using System;
using System.Linq;
using System.Net.Http;
using GoAber.Controllers;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System.Collections.Generic;
using System.Data.Entity;
using System.Web;
using GoAber.Models;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;

namespace GoAber.Controllers
{
    public class FitBitController : DeviceAPI
    {

        protected override string DeviceName()
        {
            return "FitBit";
        }
        protected override string[] Scope()
        {
            return new[] { "activity", "heartrate", "sleep" };
        }

        public ActionResult GetActivityDay()
        {
            int day = 6;
            int month = 11;
            int year = 2015;

            var user = UserManager.FindById(User.Identity.GetUserId());

            ActivityData activityDay = GetDayActivities(String.Format("{0}{1}-{2}-{3}.json", "/activities/date/", year, month, day), "summary.steps" , user.Id, day, month, year);
            if (activityDay != null)
            {
                ViewBag.Result = activityDay.value;
            }
            return View();
        }

        public ActionResult GetHeartDay()
        {
            int day = 6;
            int month = 11;
            int year = 2015;

            var user = UserManager.FindById(User.Identity.GetUserId());

            ActivityData activityHeart = GetDayHeart(String.Format("{0}{1}-{2}-{3}.json", "/activities/heart/date/", year, month, day), "activities-heart", user.Id, day, month, year);
            if (activityHeart != null)
            {
                ViewBag.Result = activityHeart.value;
            }
            return View();
        }
    }
}
