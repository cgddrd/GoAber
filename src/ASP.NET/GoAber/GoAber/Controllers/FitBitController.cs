﻿using System.Configuration;
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
using System.Diagnostics;

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


        public override ActivityData GetWalkingSteps(int year, int month, int day)
        {
            var user = UserManager.FindById(User.Identity.GetUserId());

            ActivityData activityDay = GetWalkingSteps(String.Format("{0}{1}-{2}-{3}.json", "/activities/date/", year, month, day), "summary.steps", user.Id, day, month, year);
            return activityDay;
        }

        public override ActivityData GetHeartRate(int year, int month, int day)
        {
            var user = UserManager.FindById(User.Identity.GetUserId());
            ActivityData activityHeart = GetHeartRate(String.Format("{0}{1}-{2}-{3}.json", "/heart/date/", year, month, day), "average[0].heartRate", user.Id, day, month, year);
            return activityHeart;

        }

        [HttpPost, ActionName("GetActivityData")]
        public ActionResult GetActivityData(DateTime dateFitbit, int fitBitDeviceID, int jawboneDeviceID, int jawboneSteps)
        {
            int day = dateFitbit.Day;
            int month = dateFitbit.Month;
            int year = dateFitbit.Year;

            ActivityData activityDay = GetWalkingSteps(year, month, day);
            if (activityDay != null)
            {
                ViewBag.FitbitSteps = activityDay.value;
            }
            else
            {
                ViewBag.FitbitSteps = 0;
            }

            ViewBag.FitBitConnected = true;
            ViewBag.JawboneDeviceID = jawboneDeviceID;
            ViewBag.JawboneSteps = jawboneSteps;
            ViewBag.FitBitDeviceID = fitBitDeviceID;
            return View("../Devices/Index");
        }


        /////////////////
        //  Test code  //
        public ActionResult GetActivityDay()
        {
            int day = 26;
            int month = 10;
            int year = 2015;

            ActivityData activityDay = GetWalkingSteps(year, month, day);
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

            ActivityData activityHeart = GetHeartRate(year, month, day);
            if (activityHeart != null)
            {
                ViewBag.Result = activityHeart.value;
            }
            return View();
        }
    

    }
}
