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
            return db.Groups
                        .Include(t => t.community)
                        .OrderBy(t => t.name);
        }

        public Team findTeamById(int id)
        {
            return db.Groups.Find(id);
        }

        public void createTeam(Team team)
        {
            db.Groups.Add(team);
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
            db.Groups.Remove(team);
            db.SaveChanges();
        }
    }
}