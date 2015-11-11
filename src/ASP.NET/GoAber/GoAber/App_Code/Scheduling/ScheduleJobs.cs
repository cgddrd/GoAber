using GoAber.Scheduling.Interfaces;
using GoAber.Scheduling;
using Hangfire;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using GoAber.Models;
using System.Diagnostics;
using GoAber.Scheduling.Jobs;

namespace GoAber.Scheduling
{
    public class ScheduleJobs
    {
        [Obsolete("Testing purposes only")]
        public static void Execute(IScheduler ao_scheduler)
        {
            List<IJob> lo_tasks = new List<IJob>();
            lo_tasks.Add(new FitBitJob());

            for (int i = 0; i < lo_tasks.Count; i++) {
                ao_scheduler.CreateRecurringJob(lo_tasks[i].GetID(), () => lo_tasks[i].Run(), Cron.Minutely);
            }
        }

        public static bool AddJob(Job ao_job)
        {
            try
            {
                IJob lo_job;
                if (ao_job.tasktype.Equals(JobType.FitBit))
                {
                    lo_job = new FitBitJob();
                }
                else if (ao_job.tasktype.Equals(JobType.JawBone))
                {
                    lo_job = new JawBoneJob();
                }
                else
                {
                    lo_job = new EmailJob();
                }
                IScheduler lo_scheduler = SchedulerFactory.Instance().GetScheduler();
                lo_scheduler.CreateRecurringJob(ao_job.id, () => lo_job.Run(), ao_job.cronexp);
                return true;
            } catch (Exception e)
            {
                Debug.WriteLine(e.Message);
                return false;
            }
        }


        public static bool RemoveJob(string as_id)
        {
            try
            {
                IScheduler lo_scheduler = SchedulerFactory.Instance().GetScheduler();
                lo_scheduler.RemoveJob(as_id);
                return true;
            } catch (Exception e)
            {
                Debug.WriteLine(e.Message);
                return false;
            }
        }

        public static bool EditJob(Job ao_job)
        {
            return AddJob(ao_job);
        }
    }
}