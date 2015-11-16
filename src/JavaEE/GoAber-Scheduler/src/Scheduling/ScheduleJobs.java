/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling;

import DTO.IJobDetail;
import Scheduling.Interfaces.IScheduler;
import Scheduling.Jobs.AbstractJob;

/**
 *
 * @author Dan
 */
public class ScheduleJobs {

    public ScheduleJobs(Object[] args) {
        SchedulerFactory.Instance(args);
    }
    
    public boolean AddJob(AbstractJob ao_job) {
        try {
            IScheduler lo_scheduler = SchedulerFactory.Instance(null).GetScheduler();
            lo_scheduler.CreateRecurringJob(ao_job);
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

    public boolean EditJob(AbstractJob ao_job) {
        return AddJob(ao_job);
    }

}
