using GoAber.Scheduling.Tasks;
using GoAber.Scheduling;
using Hangfire;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.Scheduling
{
    public class ScheduleTasks
    {
        public static void Execute(IScheduling ao_scheduler)
        {
            List<ITask> lo_tasks = new List<ITask>();
            lo_tasks.Add(new FitBitTask());

            for (int i = 0; i < lo_tasks.Count; i++) {
                ao_scheduler.CreateRecurringJob(lo_tasks[i].GetID(), () => lo_tasks[i].Run(), Cron.Minutely);
            }
        }
    }
}