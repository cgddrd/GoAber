using GoAber.Controllers;
using GoAber.Models;
using GoAber.Scheduling.Interfaces;
using System;
using System.Collections.Generic;
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

        public void Run()
        {
           
            Debug.WriteLine("CALLED FITBIT");
            FitBitController lo_fitbitcont = new FitBitController();
            DateTime lda_today = DateTime.Today;


            string[] ls_usernames = GetUserNames();
            ActivityData lo_days;

            for (int i = 0; i < ls_usernames.Length; i++)
            {
                lo_days = lo_fitbitcont.GetDayActivities("/activities/date/", ls_usernames[i], lda_today.Day, lda_today.Month, lda_today.Year);
                if (lo_days != null)
                {
                    Debug.WriteLine(lo_days.value);
                }
                else
                {
                    Debug.WriteLine("Data is null");
                }
            }
        }

        private string[] GetUserNames()
        {
            var query = from d in io_db.Devices
                        select d.ApplicationUserId;
            return query.ToArray();
        }
    }
}