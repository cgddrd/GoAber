/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling;

import DTO.IJobDetail;
import Scheduling.Enums.JobType;
import Scheduling.Interfaces.IExecutorServiceWrapper;
import Scheduling.Interfaces.IScheduler;
import Scheduling.Jobs.AbstractJob;
import Scheduling.Jobs.FitBitJob;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.naming.InitialContext;
import java.util.concurrent.ScheduledFuture;

/**
 *
 * @author Dan
 */

public class ManagedScheduler implements IScheduler {
    
    InitialContext io_ctx;
    IExecutorServiceWrapper io_executor;
    Hashtable<String, ScheduledFuture> io_schedules;
    
    
    public ManagedScheduler(IExecutorServiceWrapper ao_executor) {
        try {
            this.io_executor = ao_executor;
            io_schedules = new Hashtable<String, ScheduledFuture>();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
    
    @Override
    public void Init(Object[] args) {
        try {
        List<IJobDetail> lo_jobs = (List<IJobDetail>)args[0];
        AbstractJob lo_schedjob = null;
        for (IJobDetail lo_job : lo_jobs) {
                switch (JobType.values()[lo_job.getTasktype()]) {
                    case Email:
                    //lo_job = new EmailJob();
                        break;
                    //lo_job = new JawBoneJob();
                    case JawBone:
                        break;
                    default:
                        lo_schedjob = new FitBitJob(lo_job);
                        break;
                }
            CreateRecurringJobImpl(lo_schedjob, false);
        }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    @Override
    public void CreateRecurringJob(AbstractJob ao_job) {
       CreateRecurringJobImpl(ao_job, true);
    }
    
    private void CreateRecurringJobImpl(AbstractJob ao_job, boolean ao_db) {
        if (ao_job.getJobDetails().getStartnow()) {
             io_schedules.put(ao_job.getID(), this.io_executor.scheduleAtFixedRate((Runnable)ao_job, 1, ao_job.getDateInSeconds(), TimeUnit.SECONDS));
        } else {
            io_schedules.put(ao_job.getID(), this.io_executor.scheduleAtFixedRate((Runnable)ao_job, ao_job.getDateInSeconds(), ao_job.getDateInSeconds(), TimeUnit.SECONDS));
        }
    }

    @Override
    public void RemoveJob(IJobDetail ao_jobdetail) {
        io_schedules.get(ao_jobdetail.getJobid()).cancel(false);
        
    }

    @Override
    public void EditRecurringJob(AbstractJob ao_job) {
        if (io_schedules.containsKey(ao_job.getID())) {
            CreateRecurringJob(ao_job);
        }
    }
    
}
