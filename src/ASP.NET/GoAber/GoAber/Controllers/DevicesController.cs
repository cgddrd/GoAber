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
using GoAber.Auth;
using GoAber.Models;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.Owin;
using GoAber.Services;

namespace GoAber.Controllers
{
    [GAAuthorize]
    public class DevicesController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();
        private DeviceService deviceService = new DeviceService();
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

        public ActionResult Index()
        {
            var user = UserManager.FindById(User.Identity.GetUserId());
           // ViewBag.FitBitConnected = false;
          //  ViewBag.JawboneConnected = false;
            ViewBag.FitbitSteps = 0;
            ViewBag.JawboneSteps = 0;
            ViewBag.FitBitDeviceID = -1;
            ViewBag.JawboneDeviceID = -1;

            Device[] devices = deviceService.FindDevices(user.Id);
            foreach (Device device in devices)
            {
                if (device != null)
                {
                    DeviceType deviceType = deviceService.FindDeviceType(device.deviceTypeId);
                    if (deviceType.name.ToLower() == "fitbit")
                    {
                        //ViewBag.FitBitConnected = true;
                        ViewBag.FitBitDeviceID = device.Id;
                    } 
                    if (deviceType.name.ToLower() == "jawbone")
                    {
                       // ViewBag.JawboneConnected = true;
                        ViewBag.JawBoneDeviceID = device.Id;
                    }
                }
            }
            return View();
        }


        public ActionResult Delete(int deviceID)
        {
            Device device = deviceService.RemoveDevice(deviceID);
            DeviceType deviceType = deviceService.FindDeviceType(device.deviceTypeId);
            ViewBag.deviceName = UppercaseFirst(deviceType.name);
            return View();
        }

        public string UppercaseFirst(string s)
        {
            return char.ToUpper(s[0]) + s.Substring(1);
        }
    }
}