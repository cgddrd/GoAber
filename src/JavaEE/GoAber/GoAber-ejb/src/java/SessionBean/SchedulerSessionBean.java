/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

//From GoAber-Sheduler project.
import DTO.IJobDetail;

import SchedulingUtil.ExecutorServiceWrapper;
import Scheduling.ScheduleJobs;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * SchedulerSessionBeanRemote is a remote interface from the EJBRemoteInterface project.
 * @author Dan
 */


@Stateless
public class SchedulerSessionBean implements SchedulerSessionBeanRemote {
    
    private ScheduleJobs io_scheduler;
    public SchedulerSessionBean() {
        try {
            InitialContext lo_ctx = new InitialContext();
            ManagedScheduledExecutorService lo_executor = (ManagedScheduledExecutorService)lo_ctx.lookup("java:comp/DefaultManagedScheduledExecutorService");
            ExecutorServiceWrapper lo_execwrap = new ExecutorServiceWrapper(lo_executor);
            io_scheduler = new ScheduleJobs(new Object[] {lo_execwrap});
        } catch (NamingException ex) {
            Logger.getLogger(SchedulerSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public boolean AddJob(IJobDetail ao_job) {
        return io_scheduler.AddJob(ao_job);
    }

    @Override
    public boolean RemoveJob(IJobDetail ao_job) {
        return io_scheduler.RemoveJob(ao_job);
    }

    @Override
    public boolean EditJob(IJobDetail ao_job) {
        return io_scheduler.EditJob(ao_job);
    }

    @Override
    public String TestRemoteMethod() {
        return "I hope you get this!";
    }

    
    


}
