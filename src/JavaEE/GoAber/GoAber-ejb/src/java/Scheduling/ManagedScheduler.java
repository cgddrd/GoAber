/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling;

import GoAberDatabase.Jobdetail;
import Scheduling.Enums.JobType;
import Scheduling.Interfaces.IScheduler;
import Scheduling.Jobs.AbstractJob;
import Scheduling.Jobs.FitBitJob;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Dan
 */
public class ManagedScheduler implements IScheduler {

    InitialContext io_ctx;
    ManagedScheduledExecutorService io_executor;
    Hashtable<String, ScheduledFuture> io_schedules;
    
    @EJB
    private SessionBean.JobdetailFacade ejbFacade;
    
    public ManagedScheduler() throws NamingException {
        this.io_ctx = new InitialContext();
        this.io_executor = (ManagedScheduledExecutorService)io_ctx.lookup("java:comp/DefaultManagedScheduledExecutorService");
        io_schedules = new Hashtable<String, ScheduledFuture>();
    }
    
    @Override
    public void Init() {
        List<Jobdetail> lo_jobs = ejbFacade.findAll();
        AbstractJob lo_schedjob = null;
        for (Jobdetail lo_job : lo_jobs) {
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
    }

    @Override
    public void CreateRecurringJob(AbstractJob ao_job) {
       CreateRecurringJobImpl(ao_job, true);
    }
    
    private void CreateRecurringJobImpl(AbstractJob ao_job, boolean ao_db) {
        io_schedules.put(ao_job.getID(), this.io_executor.scheduleAtFixedRate((Runnable)ao_job, ao_job.getDateInSeconds(), ao_job.getDateInSeconds(), TimeUnit.SECONDS));
        if (ao_db) {
            ejbFacade.create(ao_job.getJobDetails());
        }
    }

    @Override
    public void RemoveJob(String as_id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void EditRecurringJob(AbstractJob ao_job) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
