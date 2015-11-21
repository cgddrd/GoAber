using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using GoAber.Models;
using System.Linq;
using System.Xml.Schema;
using WebGrease.Css.Extensions;

namespace GoAber.Services
{
    public class TeamsService
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        public IEnumerable<Team> GetAllTeams()
        {
            return db.Teams
                        .Include(t => t.community)
                        .OrderBy(t => t.name);
        }

        public IEnumerable<Team> GetTeamsByCommunity(Community currentCommunity)
        {

            return db.Teams.Where(t => t.communityId == currentCommunity.Id)
                           .Include(t => t.community)
                           .OrderBy(t => t.name);

        } 

        public Team FindTeamById(int id)
        {
            return db.Teams.Find(id);
        }

        public void CreateTeam(Team team)
        {
            db.Teams.Add(team);
            db.SaveChanges();
        }

        public void UpdateTeam(Team team)
        {
            db.Entry(team).State = EntityState.Modified;
            db.SaveChanges();
        }

        public void DeleteTeam(int id)
        {
            Team team = FindTeamById(id);

            foreach (var user in team.users.ToList())
            {
                team.users.Remove(user);
            }

            db.Teams.Remove(team);
            db.SaveChanges();
        }

        public IEnumerable CreateTeamList()
        {
            var teams = db.Teams.Select(t => new
                {
                    TeamId = t.Id,
                    CommunityName = t.community.name,
                    CommunityId = t.communityId,
                    Name = t.name,
                }
            ).ToList();

            return teams;
        }
    }
}