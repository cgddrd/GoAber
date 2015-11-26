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
using GoAber.App_Code.Scheduling.Jobs;

namespace GoAber.Scheduling
{
    public class ScheduleJobs
    {
        public static bool AddJob(Job ao_jobdetail, string[] args = null)
        {
            try
            {
                IJob lo_job;
                if (ao_jobdetail.tasktype.Equals(JobType.FitBit))
                {
                    lo_job = new FitBitJob(ao_jobdetail.id);
                }
                else if (ao_jobdetail.tasktype.Equals(JobType.JawBone))
                {
                    lo_job = new JawBoneJob();
                }
                else if (ao_jobdetail.tasktype.Equals(JobType.Challenge))
                {
                    lo_job = new ChallengeJob();
                }
                else
                {
                    lo_job = new EmailJob();
                }
                IScheduler lo_scheduler = SchedulerFactory.Instance().GetScheduler();
                if (ao_jobdetail.schedtype.Equals(ScheduleType.Repeating))
                {
                    lo_scheduler.CreateRecurringJob(ao_jobdetail.id, () => lo_job.Run(args), ao_jobdetail.minutes);
                } else
                {
                    ao_jobdetail.secretid = lo_scheduler.CreateOnceJob(() => lo_job.Run(args), ao_jobdetail.minutes);
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