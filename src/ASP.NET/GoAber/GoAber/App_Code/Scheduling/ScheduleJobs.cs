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
                if (ao_job.schedtype.Equals(ScheduleType.Repeating))
                {
                    lo_scheduler.CreateRecurringJob(ao_job.id, () => lo_job.Run(), ao_job.minutes);
                } else
                {
                    ao_job.secretid = lo_scheduler.CreateOnceJob(() => lo_job.Run(), ao_job.minutes);
                }
                return true;
            } catch (Exception e)
            {
                Debug.WriteLine(e.Message);
                return false;
            }
        }


        public static bool RemoveJob(Job ao_job)
        {
            try
            {

                IScheduler lo_scheduler = SchedulerFactory.Instance().GetScheduler();
                if (ao_job.schedtype.Equals(ScheduleType.Repeating))
                {
                    lo_scheduler.RemoveRecurringJob(ao_job.id);
                } else
                {
                    lo_scheduler.RemoveOnceJob(ao_job.secretid);
                }
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