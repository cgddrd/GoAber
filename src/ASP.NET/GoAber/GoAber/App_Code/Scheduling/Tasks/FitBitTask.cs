using GoAber.Controllers;
using GoAber.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Web;

namespace GoAber.Scheduling.Tasks
{
    public class FitBitTask : ITask
    {
        public string GetID()
        {
            return "fitbittask";
        }

        public void Run()
        {
            FitBitController lo_fitbitcont = new FitBitController();
            int day = 6;
            int month = 11;
            int year = 2015;


            ActivityData lo_days = lo_fitbitcont.GetDayActivities("/activities/date/", "41b4409c-8f3e-4402-aee2-53142d3e4bb1", day, month, year);

            if (lo_days != null)
            {
                Debug.WriteLine(lo_days.value);
            } else
            {
                Debug.WriteLine("Data is null");
            }
        }
    }
}