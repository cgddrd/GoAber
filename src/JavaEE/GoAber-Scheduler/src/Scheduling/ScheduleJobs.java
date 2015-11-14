/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling;

import DTO.IJobDetail;
import Scheduling.Enums.JobType;
import Scheduling.Interfaces.IScheduler;
import Scheduling.Jobs.FitBitJob;
import Scheduling.Jobs.AbstractJob;

/**
 *
 * @author Dan
 */
public class ScheduleJobs {

    public ScheduleJobs(Object[] args) {
        SchedulerFactory.Instance(args);
    }
    
    public boolean AddJob(IJobDetail ao_jobdetails) {
        try {
            AbstractJob lo_job = null;
                switch (JobType.values()[ao_jobdetails.getTasktype()]) {
                    case Email:
                    //lo_job = new EmailJob();
                        break;
                    //lo_job = new JawBoneJob();
                    case JawBone:
                        break;
                    default:
                        lo_job = new FitBitJob(ao_jobdetails);
                        break;
                }
            IScheduler lo_scheduler = SchedulerFactory.Instance(null).GetScheduler();
            lo_scheduler.CreateRecurringJob(lo_job);
            return true;
        } catch (Exception e) {
            //Debug.WriteLine(e.Message);
            return false;
        }
    }

    public boolean RemoveJob(IJobDetail ao_jobdetails) {
        try {
            IScheduler lo_scheduler = SchedulerFactory.Instance(null).GetScheduler();
            lo_scheduler.RemoveJob(ao_jobdetails);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean EditJob(IJobDetail ao_job) {
        return AddJob(ao_job);
    }

}
