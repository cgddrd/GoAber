using GoAber.Models;
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
    public class ActivityDataService
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        public ActivityData getActivityDataById(int id)
        {
            return db.ActivityDatas.Find(id);
        }

        public IEnumerable<ActivityData> getAllActivityData()
        {
           return db.ActivityDatas
                      .Include(a => a.categoryunit)
                      .Include(a => a.User)
                      .OrderBy(a => a.date);
        }

        public IEnumerable<ActivityData> findActivityDataForUser(string userId)
        {
            return db.ActivityDatas
                       .Include(a => a.categoryunit)
                       .Include(a => a.User)
                       .Where(a => a.User.Id == userId)
                       .OrderBy(a => a.date);
        }

        public void createActivityDataForUser(ActivityData activityData, string userId)
        {
            activityData.ApplicationUserId = userId;
            createActivityData(activityData);
        }

        public void createActivityData(ActivityData activityData)
        {
            activityData.lastUpdated = DateTime.Now;
            db.ActivityDatas.Add(activityData);
            db.SaveChanges();
        }


        public void editActivityDataForUser(ActivityData activityData, string userId)
        {
            activityData.ApplicationUserId = userId;
            editActivityData(activityData);
        }

        public void editActivityData(ActivityData activityData)
        {
            activityData.lastUpdated = DateTime.Now;
            db.Entry(activityData).State = EntityState.Modified;
            db.SaveChanges();
        }

        public void deleteActivityData(int id, string message, string userId)
        {
            DataRemovalAudit dataRemovalAudit = new DataRemovalAudit(message, getActivityDataById(id), userId);
            db.DataRemovalAudits.Add(dataRemovalAudit);
            db.SaveChanges();

            ActivityData activityData = db.ActivityDatas.Find(id);
            db.ActivityDatas.Remove(activityData);
            db.SaveChanges();
        }

        ~ActivityDataService()
        {
            db.Dispose();
        }
    }
}