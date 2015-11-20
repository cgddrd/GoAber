using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using GoAber.Models;
using System.Linq;

namespace GoAber.Services
{
    public class TeamsService
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        public IEnumerable<Team> getAllTeams()
        {
            return db.Teams
                        .Include(t => t.community)
                        .OrderBy(t => t.name);
        }

        public IEnumerable<Team> getTeamsByCommunity(Community currentCommunity)
        {

            return db.Teams.Where(t => t.communityId == currentCommunity.Id)
                           .Include(t => t.community)
                           .OrderBy(t => t.name);

        } 

        public Team findTeamById(int id)
        {
            return db.Teams.Find(id);
        }

        public void createTeam(Team team)
        {
            db.Teams.Add(team);
            db.SaveChanges();
        }

        public void updateTeam(Team team)
        {
            db.Entry(team).State = EntityState.Modified;
            db.SaveChanges();
        }

        public void deleteTeam(int id)
        {
            Team team = findTeamById(id);
            db.Teams.Remove(team);
            db.SaveChanges();
        }
    }
}