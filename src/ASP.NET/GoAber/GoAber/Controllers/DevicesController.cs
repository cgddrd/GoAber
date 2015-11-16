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
    public class DevicesController : Controller
    {
        private ApplicationDbContext db = new ApplicationDbContext();
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
            ViewBag.FitBitConnected = false;
            ViewBag.JawboneConnected = false;

            Device[] devices = FindDevices(user.Id);
            foreach (Device device in devices)
            {
                if (device != null)
                {
                    DeviceType deviceType = FindDeviceType(device.deviceTypeId);
                    if (deviceType.name == "fitbit")
                    {
                        ViewBag.FitBitConnected = true;
                        ViewBag.FitBitDeviceID = device.Id;
                    } 
                    if (deviceType.name == "jawbone")
                    {
                        ViewBag.JawboneConnected = true;
                        ViewBag.JawBoneDeviceID = device.Id;
                    }
                }
            }
            return View();
        }

        private Device[] FindDevices(string userID)
        {
            IQueryable<Device> devices = from d in db.Devices
                        where d.ApplicationUserId == userID
                        select d;
            return devices.ToArray();
        }

        private DeviceType FindDeviceType(int deviceTypeID)
        {
            var query = from d in db.DeviceTypes
                        where d.Id == deviceTypeID 
                        select d;
            return query.FirstOrDefault();
        }

        private Device RemoveDevice(int deviceID)
        {
            var query = from d in db.Devices
                        where d.Id == deviceID 
                        select d;
            Device device = query.FirstOrDefault();
            db.Devices.Remove(device);
            db.SaveChanges();
            return device;
        }

        public ActionResult Delete(int deviceID)
        {
            Device device = RemoveDevice(deviceID);
            DeviceType deviceType = FindDeviceType(device.deviceTypeId);
            ViewBag.deviceName = UppercaseFirst(deviceType.name);
            return View();
        }

        public string UppercaseFirst(string s)
        {
            return char.ToUpper(s[0]) + s.Substring(1);
        }
    }
}