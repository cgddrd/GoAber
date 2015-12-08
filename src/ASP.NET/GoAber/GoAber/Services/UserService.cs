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
            var transformedUsers = activityData
                            .Where(a => a.User.Id != null) // ignore data that does not have a user
                            .GroupBy(a => a.User.Id)
                            .ToList()
                            .Select(g => new ParticipantLeaderViewModel
                            {
                                User = db.Users.Find(g.Key),
                                Total = g.Sum(x => x.value).GetValueOrDefault(0)
                            });

            // finally order the views according to who is the best
            return transformedUsers.OrderByDescending(m => m.Total);
        }

        public IEnumerable<GroupLeaderViewModel> GetSortedGroupsForUnit(int unit)
        {
            ActivityDataService dataService = new ActivityDataService();

            // first get all acitivty data matching the unit
            var activityData = dataService.GetAllActivityData();
            activityData = activityData.Where(a => a.categoryunit.unit.Id == unit);

            // group activity data by Group
            // then for each group calculate the total and store it in a model
            var transformedGroups = activityData
                            .Where(a => a.User.TeamId != null) // ignore people who are not in a team
                            .GroupBy(a => a.User.TeamId)
                            .ToList()
                            .Select(g => new GroupLeaderViewModel
                            {
                                Name = db.Teams.Find(g.Key).name,
                                NumMembers = g.Count(),
                                Total = g.Sum(x => x.value).GetValueOrDefault(0)
                            });

            return transformedGroups.OrderByDescending(m => m.Total);
        }

        public IEnumerable<ParticipantLeaderViewModel> GetUsersForGroup(int id)
        {
            ApplicationUserService service = new ApplicationUserService();
            var users = service.GetAllApplicationUsers()
                                .Where(u => u.Team.Id == id)
                                .Select(u => new ParticipantLeaderViewModel
                                {
                                    User = u,
                                    Total = 0
                                });

            return users.OrderBy(x => x.User.Nickname);
        }
    }
}
