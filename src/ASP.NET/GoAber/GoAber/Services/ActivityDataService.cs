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
            var summaryStats = ComputeStatistics(weekData);
            return summaryStats;
        }

        public ActivityDataStatisticsViewModel MonthlyStatistics(string userId, string unit)
        {
            var monthData = MonthlySummary(userId, unit);
            var summaryStats = ComputeStatistics(monthData);
            return summaryStats;
        }

        public ActivityDataStatisticsViewModel AllTimeStatistics(string userId, string unit)
        {
            var data = FindActivityDataForUser(userId);
            data = data.Where(a => a.categoryunit.unit.name == unit);
            var summaryStats = ComputeStatistics(data);
            return summaryStats;
        }

        public IEnumerable<ActivityData> WeeklySummary(string userId, string unit)
        {
            DateTime toDate = DateTime.Today;
            DateTime fromDate = DateTime.Today.AddDays(-7);

            var data = GetUserActivityDataByDateRange(userId, fromDate, toDate);
            return data.Where(a => a.categoryunit.unit.name == unit);
        }

        public IEnumerable<ActivityData> MonthlySummary(string userId, string unit)
        {
            DateTime toDate = DateTime.Today;
            DateTime fromDate = DateTime.Today.AddMonths(-1);

            var data = GetUserActivityDataByDateRange(userId, fromDate, toDate);
            return data.Where(a => a.categoryunit.unit.name == unit);
        }

        public IQueryable<ActivityData> Filter(FilterViewModel filterParams)
        {
            var activityData = GetAllActivityData();
            if (!String.IsNullOrEmpty(filterParams.Email))
            {
                activityData = activityData.Where(a => a.User.Email.Contains(filterParams.Email));
            }

            if (filterParams.CategoryUnitId > 0)
            {
                activityData = activityData.Where(a => a.categoryunit.Id == filterParams.CategoryUnitId);
            }

            if (filterParams.FromDate.HasValue)
            {
                activityData = activityData.Where(a => a.date >= filterParams.FromDate.Value);
            }

            if (filterParams.ToDate.HasValue)
            {
                activityData = activityData.Where(a => a.date <= filterParams.ToDate.Value);
            }

            return activityData;
        }

        public ActivityData GetActivityDataById(int id)
        {
            return db.ActivityDatas.Find(id);
        }

        public IQueryable<ActivityData> GetAllActivityData()
        {
           return db.ActivityDatas
                      .Include(a => a.categoryunit)
                      .Include(a => a.categoryunit.category)
                      .Include(a => a.categoryunit.unit)
                      .Include(a => a.User)
                      .OrderBy(a => a.date);
        }

        public IEnumerable<ActivityData> GetUserActivityDataByDateRange(string userId, DateTime fromDate, DateTime toDate)
        {
            return GetAllActivityData()
                      .Where(a => a.date >= fromDate && a.date <= toDate)
                      .Where(a => a.User.Id == userId);
        }

        public IEnumerable<ActivityData> FindActivityDataForUser(string userId)
        {
            return GetAllActivityData()
                       .Where(a => a.User.Id == userId)
                       .OrderBy(a => a.date);
        }

        public void CreateActivityDataForUser(ActivityData activityData, string userId)
        {
            activityData.ApplicationUserId = userId;
            CreateActivityData(activityData);
        }

        public void CreateActivityData(ActivityData activityData)
        {
            activityData.lastUpdated = DateTime.Now;
            db.ActivityDatas.Add(activityData);
            db.SaveChanges();
        }


        public void EditActivityDataForUser(ActivityData activityData, string userId)
        {
            activityData.ApplicationUserId = userId;
            EditActivityData(activityData);
        }

        public void EditActivityData(ActivityData activityData)
        {
            activityData.lastUpdated = DateTime.Now;
            db.Entry(activityData).State = EntityState.Modified;
            db.SaveChanges();
        }

        public void DeleteActivityData(int id, string message, string userId)
        {
            DataRemovalAudit dataRemovalAudit = new DataRemovalAudit(message, GetActivityDataById(id), userId);
            db.DataRemovalAudits.Add(dataRemovalAudit);
            db.SaveChanges();

            ActivityData activityData = db.ActivityDatas.Find(id);
            db.ActivityDatas.Remove(activityData);
            db.SaveChanges();
        }

        public void BatchDelete(IQueryable<ActivityData> activityData, string message, string userId)
        {
            foreach (ActivityData item in activityData.ToList())
            {
                DeleteActivityData(item.Id, message, userId);
            }
        }

        public ActivityDataStatisticsViewModel ComputeStatistics(IEnumerable<ActivityData> data)
        {
            ActivityDataStatisticsViewModel summaryStats = new ActivityDataStatisticsViewModel();
            var values = data.Select(a => a.value);
            summaryStats.Average = values.Average().GetValueOrDefault(0);
            summaryStats.Total = values.Sum().GetValueOrDefault(0);

            if (data.Count() > 0)
            {
                var formattedData = data
                    .GroupBy(a => a.date)
                    .Select(a => new
                    {
                        value = a.Sum(x => x.value),
                        date = a.Select(x => x.date).First()
                    });

                var minItem = formattedData.Aggregate((c, d) => c.value < d.value ? c : d);
                summaryStats.Min = minItem.value.GetValueOrDefault(0);
                summaryStats.MinDate = minItem.date.GetValueOrDefault(new DateTime());

                var maxItem = formattedData.Aggregate((c, d) => c.value > d.value ? c : d);
                summaryStats.Max = maxItem.value.GetValueOrDefault(0);
                summaryStats.MaxDate = maxItem.date.GetValueOrDefault(new DateTime());
            }
            else
            {
                summaryStats.MinDate = DateTime.Today;
                summaryStats.MaxDate = DateTime.Today;
                summaryStats.Min = 0;
                summaryStats.Max = 0;
            }
            
            return summaryStats;
        }

        ~ActivityDataService()
        {
            db.Dispose();
        }
    }
}