/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling;

import GoAberDatabase.Jobdetail;
import Scheduling.Enums.JobType;
import Scheduling.Interfaces.IJob;
import Scheduling.Interfaces.IScheduler;
import Scheduling.Jobs.FitBitJob;
import javax.ejb.Stateless;
import Scheduling.Interfaces.IScheduleJobs;
import Scheduling.Jobs.AbstractJob;

/**
 *
 * @author Dan
 */
@Stateless
public class ScheduleJobs implements IScheduleJobs{

    public boolean AddJob(Jobdetail ao_jobdetails) {
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
            IScheduler lo_scheduler = SchedulerFactory.Instance().GetScheduler();
            lo_scheduler.CreateRecurringJob(lo_job);
            return true;
        } catch (Exception e) {
            //Debug.WriteLine(e.Message);
            return false;
        }
    }

    public boolean RemoveJob(String as_id) {
        try {
            IScheduler lo_scheduler = SchedulerFactory.Instance().GetScheduler();
            lo_scheduler.RemoveJob(as_id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean EditJob(Jobdetail ao_job) {
        return AddJob(ao_job);
    }

}
