/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Startups;

import GoAberDatabase.JobDetail;
import SessionBean.JobDetailFacade;
import SessionBean.SchedulerSessionBeanRemote;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.Stateless;

/**
 *
 * @author Dan
 */
@Startup
@Singleton
@LocalBean
public class SchedulerMain {

    /***
     * Takes job from the database when application is started.
     * Adds each recurring job to the scheduler.
     */
    @Stateless
    public static class MainClass {     

        @EJB
        private JobDetailFacade jobDetailFacade;

        @EJB
        private SchedulerSessionBeanRemote schedulerSessionBean;
        
        @Asynchronous
        public void Main(Object[] args) {
           List<JobDetail> lo_jobs = jobDetailFacade.findAll();
           for (int i = 0; i< lo_jobs.size(); i++) {
               if ("R".equals(lo_jobs.get(i).getSchedtype())) {
                    schedulerSessionBean.AddJob(lo_jobs.get(i));
               }
           }
        }  
    }
    
    @EJB
    private MainClass io_mainclass;
    
    
    @PostConstruct
    private void initiateMain() {
        io_mainclass.Main(new Object[] {});
        
    }
}

