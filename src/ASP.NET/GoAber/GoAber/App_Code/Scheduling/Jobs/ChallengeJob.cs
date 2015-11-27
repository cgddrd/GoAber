using GoAber.Models;
using GoAber.Scheduling.Interfaces;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Diagnostics;
using System.Linq;
using System.Web;

namespace GoAber.App_Code.Scheduling.Jobs
{
    public class ChallengeJob : IJob
    {
        public string GetID()
        {
            return "challengejob";
        }

        public void Run(string[] args)
        {
            ApplicationDbContext db = new ApplicationDbContext();
            Debug.WriteLine("Challenge complete");
            int li_challengeid = int.Parse(args[0]);

            IQueryable<Challenge> lo_query = from c in db.Challenges
                                  where c.Id == li_challengeid
                                  select c;
            Challenge lo_challenge = lo_query.First();

            lo_challenge.complete = true;
            db.Entry(lo_challenge).State = EntityState.Modified;
            db.SaveChanges();
        }
    }
}