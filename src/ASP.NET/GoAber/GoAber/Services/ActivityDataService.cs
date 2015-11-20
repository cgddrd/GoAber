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
    public class ActivityDataService
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        public ActivityDataStatisticsViewModel WeeklyStatistics(string userId, string unit)
        {
            var weekData = WeeklySummary(userId, unit);
            var summaryStats = computeStatistics(weekData);
            return summaryStats;
        }

        public ActivityDataStatisticsViewModel MonthlyStatistics(string userId, string unit)
        {
            var monthData = MonthlySummary(userId, unit);
            var summaryStats = computeStatistics(monthData);
            return summaryStats;
        }

        public ActivityDataStatisticsViewModel AllTimeStatistics(string userId, string unit)
        {
            var data = findActivityDataForUser(userId);
            data = data.Where(a => a.categoryunit.unit.name == unit);
            var summaryStats = computeStatistics(data);
            return summaryStats;
        }

        private ActivityDataStatisticsViewModel computeStatistics(IEnumerable<ActivityData> data)
        {
            ActivityDataStatisticsViewModel summaryStats = new ActivityDataStatisticsViewModel();
            var values = data.Select(a => a.value);
            summaryStats.Average = values.Average().GetValueOrDefault(0);
            summaryStats.Total = values.Sum().GetValueOrDefault(0);

            ActivityData minItem = data.Aggregate((c, d) => c.value < d.value ? c : d);
            summaryStats.Min = minItem.value.GetValueOrDefault(0);
            summaryStats.MinDate = minItem.date.GetValueOrDefault(new DateTime());

            ActivityData maxItem = data.Aggregate((c, d) => c.value > d.value ? c : d);
            summaryStats.Max = maxItem.value.GetValueOrDefault(0);
            summaryStats.MaxDate = maxItem.date.GetValueOrDefault(new DateTime());
            return summaryStats;
        }

        public IEnumerable<ActivityData> WeeklySummary(string userId, string unit)
        {
            DateTime toDate = DateTime.Today;
            DateTime fromDate = DateTime.Today.AddDays(-7);

            var data = getUserActivityDataByDateRange(userId, fromDate, toDate);
            return data.Where(a => a.categoryunit.unit.name == unit);
        }

        public IEnumerable<ActivityData> MonthlySummary(string userId, string unit)
        {
            DateTime toDate = DateTime.Today;
            DateTime fromDate = DateTime.Today.AddMonths(-1);

            var data = getUserActivityDataByDateRange(userId, fromDate, toDate);
            return data.Where(a => a.categoryunit.unit.name == unit);
        }

        public IQueryable<ActivityData> Filter(string email, int? categoryUnitId, DateTime? fromDate, DateTime? toDate)
        {
            var activityData = getAllActivityData();
            if (!String.IsNullOrEmpty(email))
            {
                activityData = activityData.Where(a => a.User.Email.Contains(email));
            }

            if (categoryUnitId.HasValue)
            {
                activityData = activityData.Where(a => a.categoryunit.Id == categoryUnitId);
            }

            if (fromDate.HasValue)
            {
                activityData = activityData.Where(a => a.date >= fromDate);
            }

            if (toDate.HasValue)
            {
                activityData = activityData.Where(a => a.date <= toDate);
            }

            return activityData;
        }

        public ActivityData getActivityDataById(int id)
        {
            return db.ActivityDatas.Find(id);
        }

        public IQueryable<ActivityData> getAllActivityData()
        {
           return db.ActivityDatas
                      .Include(a => a.categoryunit)
                      .Include(a => a.categoryunit.category)
                      .Include(a => a.categoryunit.unit)
                      .Include(a => a.User)
                      .OrderBy(a => a.date);
        }

        public IEnumerable<ActivityData> getUserActivityDataByDateRange(string userId, DateTime fromDate, DateTime toDate)
        {
            return getAllActivityData()
                      .Where(a => a.date >= fromDate && a.date <= toDate)
                      .Where(a => a.User.Id == userId);
        }

        public IEnumerable<ActivityData> findActivityDataForUser(string userId)
        {
            return getAllActivityData()
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

        public void deleteActivityData(int id)
        {
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