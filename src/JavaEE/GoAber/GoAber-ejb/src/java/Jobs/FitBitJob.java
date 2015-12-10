/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jobs;

import Scheduling.Jobs.AbstractJob;
import DTO.IJobDetail;
import SessionBean.ActivityDataFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 *
 * @author Dan
 */
public class FitBitJob extends AbstractJob {
    
    ActivityDataFacade io_activityFacade;

    public FitBitJob(IJobDetail ao_jobdetails) {
        super(ao_jobdetails);
        
       io_activityFacade = lookupBean(ActivityDataFacade.class);
    }

    @Override
    public String getJobType() {
       return "Fitbit";
    }

    /***
     * Runs fitbit job.
     */
    @Override
    public void run() {
        System.out.println("I HAVE BEEN CALLED! " + getJobDetails().getJobid());
    }

    private <T> T lookupBean(Class<T> clazz) {
          try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return (T) ejbCtx.lookup(clazz.getSimpleName());
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }


    
}
