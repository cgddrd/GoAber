using GoAber.Controllers;
using GoAber.Models;
using GoAber.Scheduling.Interfaces;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Diagnostics;
using System.Linq;
using System.Web;

namespace GoAber.Scheduling.Jobs
{
    public class FitBitJob : IJob
    {
        protected static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        ApplicationDbContext io_db;
        private string is_id = "fitbitjob";
        public FitBitJob()
        {
            io_db = new ApplicationDbContext();
        }

        public FitBitJob(string as_id)
        {
            is_id = as_id;
        }

        public string GetID()
        {
            return is_id;
        }
        private bool ActivityContainsUserForDay(List<ActivityData> ao_list, string as_userid)
        {
            foreach (ActivityData lo_activity in ao_list)
            {
                if (lo_activity.ApplicationUserId == as_userid) return true;
            }
            return false;
        }

        /// <summary>
        /// Fitbit schedule job. 
        /// When called adds activity data for each user for the day if the data for that day is not already stored for that user.
        /// </summary>
        /// <param name="args"></param>
        public void Run(string[] args)
        {

            //Debug.WriteLine("CALLED FITBIT");
           FitBitController lo_fitbitcont = new FitBitController();
           DateTime lda_today = DateTime.Today;
            string ls_jobid = args[0];

            string[] ls_usernames = GetUserIds();
            ActivityData lo_steps;
            ActivityData lo_hearts;

            var query = from j in io_db.Jobs
                        where j.id == ls_jobid
                        select j;
            if (query.Count() <= 0) return;
            Job lo_job = query.First();
            if (lo_job.lastUpdated.Value > DateTime.Now.AddMinutes(-(lo_job.minutes - 1))) return;

            for (int i = 0; i < ls_usernames.Length; i++)
            {
                lo_steps = lo_fitbitcont.GetWalkingSteps(lo_fitbitcont.FormatWalkingString(lda_today.Day, lda_today.Month, lda_today.Year), "summary.steps", ls_usernames[i], lda_today.Day, lda_today.Month, lda_today.Year, true);
                lo_hearts = lo_fitbitcont.GetHeartRate(lo_fitbitcont.FormatHeartRateString(lda_today.Day, lda_today.Month, lda_today.Year), "average[0].heartRate", ls_usernames[i], lda_today.Day, lda_today.Month, lda_today.Year, true);

                if (lo_steps != null)
                {
                    Debug.WriteLine(lo_steps.value);
                }
                else
                {
                    Debug.WriteLine("Data is null");
                }
            }
            lo_job.lastUpdated = DateTime.Now;
            io_db.Entry(lo_job).State = EntityState.Modified;
            io_db.SaveChanges();
        }

        private string[] GetUserIds()
        {
            var query = from d in io_db.Devices
                        where d.deviceTypeId == 1
                        select d.ApplicationUserId;
            return query.ToArray();
        }
    }
}