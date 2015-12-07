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

        public void Run(string[] args)
        {
            //Debug.WriteLine("CALLED FITBIT");
            JawboneController lo_fitbitcont = new JawboneController();
            DateTime lda_today = DateTime.Today;


            string[] ls_usernames = GetUserIds();
            ActivityData lo_steps;
            ActivityData lo_hearts;

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
        }

        private string[] GetUserIds()
        {
            var query = from d in io_db.Devices
                        select d.ApplicationUserId;
            return query.ToArray();
        }
    }
}