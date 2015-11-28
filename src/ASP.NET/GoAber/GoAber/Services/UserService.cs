using GoAber.Models;
using GoAber.Models.ViewModels;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace GoAber.Services
{
    public class UserService
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        public IEnumerable<ParticipantLeaderViewModel> GetSortedUsersForUnit(int unit)
        {
            ActivityDataService dataService = new ActivityDataService();

            // first get all acitivty data matching the unit
            var activityData = dataService.GetAllActivityData();
            activityData = activityData.Where(a => a.categoryunit.unit.Id == unit);

            // group activity data by user
            // then for each group calculate the total and store it in a model
            var transformedUsers = activityData.GroupBy(a => a.User.Id)
                .ToList()
                .Select(g => new ParticipantLeaderViewModel
            {
                NickName = db.Users.Find(g.Key).Nickname,
                Total = g.Sum(x => x.value).GetValueOrDefault(0)
            });

            // finally order the views according to who is the best
            return transformedUsers.OrderByDescending(m => m.Total);
        }
    }
}