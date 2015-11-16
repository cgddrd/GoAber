/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling;

import DTO.IJobDetail;
import Scheduling.Interfaces.IExecutorServiceWrapper;
import Scheduling.Interfaces.IScheduler;
import Scheduling.Jobs.AbstractJob;
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
    }

    @Override
    public void CreateRecurringJob(AbstractJob ao_job) {
       CreateRecurringJobImpl(ao_job, true);
    }
    
    private void CreateRecurringJobImpl(AbstractJob ao_job, boolean ao_db) {
        if (ao_job.getJobDetails().getStartnow()) {
             io_schedules.put(ao_job.getJobDetails().getJobid(), this.io_executor.scheduleAtFixedRate((Runnable)ao_job, 0, ao_job.getDateInSeconds(), TimeUnit.SECONDS));
        } else {
            io_schedules.put(ao_job.getJobDetails().getJobid(), this.io_executor.scheduleAtFixedRate((Runnable)ao_job, ao_job.getDateInSeconds(), ao_job.getDateInSeconds(), TimeUnit.SECONDS));
        }
    }

    @Override
    public void RemoveRecurringJob(IJobDetail ao_jobdetail) {
        io_schedules.get(ao_jobdetail.getJobid()).cancel(true);
        io_schedules.remove(ao_jobdetail.getJobid());
    }

    @Override
    public void EditRecurringJob(AbstractJob ao_job) {
        if (io_schedules.containsKey(ao_job.getJobType())) {
            CreateRecurringJob(ao_job);
        }
    }

    @Override
    public void CreateOnceJob(AbstractJob ao_job) {
        if (ao_job.getJobDetails().getStartnow()) {
             io_schedules.put(ao_job.getJobDetails().getJobid(), this.io_executor.scheduleOnce((Runnable)ao_job, 0, TimeUnit.SECONDS));
        } else {
             io_schedules.put(ao_job.getJobDetails().getJobid(), this.io_executor.scheduleOnce((Runnable)ao_job, ao_job.getDateInSeconds(), TimeUnit.SECONDS));
        }
    }


    @Override
    public void RemoveOnceJob(IJobDetail ao_jobdetail) {
        RemoveRecurringJob(ao_jobdetail);
    }

    @Override
    public void EditOnceJob(AbstractJob ao_job) {
        if (io_schedules.containsKey(ao_job.getJobType())) {
            CreateOnceJob(ao_job);
        }
    }
    
}
