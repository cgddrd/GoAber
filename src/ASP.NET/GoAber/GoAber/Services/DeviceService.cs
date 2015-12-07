using GoAber.Models;
using GoAber.Models.ViewModels;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace GoAber.Services
{
    /// <summary>
    /// ActivityDataService
    /// 
    /// Provides covinence methods used by multiple controllers who wish to access 
    /// ActivityData information in a sensible way.
    /// </summary>
    public class DeviceService
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        public Device[] FindDevices(string userID)
        {
            IQueryable<Device> devices = from d in db.Devices
                                         where d.ApplicationUserId == userID
                                         select d;
            return devices.ToArray();
        }

        public Device FindDevice(string userID, string deviceName)
        {
            string deviceType = FindDeviceTypeByName(deviceName).name;
            var query = from d in db.Devices
                        where (d.ApplicationUserId == userID // MOCK USER ID FOR NOW
                        && d.deviceType.name == deviceType)
                        select d;
            return query.FirstOrDefault();
        }

        public DeviceType FindDeviceType(int deviceTypeID)
        {
            var query = from d in db.DeviceTypes
                        where d.Id == deviceTypeID
                        select d;
            return query.FirstOrDefault();
        }

        public Device RemoveDevice(int deviceID)
        {
            var query = from d in db.Devices
                        where d.Id == deviceID
                        select d;
            Device device = query.FirstOrDefault();
            db.Devices.Remove(device);
            db.SaveChanges();
            return device;
        }

        public DeviceType FindDeviceTypeByName(String name)
        {
            var query = from d in db.DeviceTypes
                        where d.name == name
                        select d;
            DeviceType deviceType = query.FirstOrDefault();
            return deviceType;
        }


        ~DeviceService()
        {
            db.Dispose();
        }
    }
}