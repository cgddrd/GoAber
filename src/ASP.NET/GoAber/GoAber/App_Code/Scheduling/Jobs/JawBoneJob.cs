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
    public class JawBoneJob : IJob
    {

        protected static readonly log4net.ILog log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);
        ApplicationDbContext io_db;
        private string is_id = "jawbonejob";

        public JawBoneJob()
        {
            io_db = new ApplicationDbContext();
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
        /// Jawbone schedule job. 
        /// When called adds activity data for each user for the day.
        /// </summary>
        /// <param name="args"></param>
        public void Run(string[] args)
        {
            //Debug.WriteLine("CALLED FITBIT");
            JawboneController lo_jawbonecont = new JawboneController();
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
                lo_steps = lo_jawbonecont.GetWalkingSteps(lo_jawbonecont.FormatWalkingString(lda_today.Day, lda_today.Month, lda_today.Year), "data.items[0].details.steps", ls_usernames[i], lda_today.Day, lda_today.Month, lda_today.Year, true);
                lo_hearts = lo_jawbonecont.GetHeartRate(lo_jawbonecont.FormatHeartRateString(lda_today.Day, lda_today.Month, lda_today.Year), "data.items[0].resting_heartrate", ls_usernames[i], lda_today.Day, lda_today.Month, lda_today.Year, true);
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
                        where d.deviceTypeId == 2
                        select d.ApplicationUserId;

            return query.ToArray();
        }
    }
}