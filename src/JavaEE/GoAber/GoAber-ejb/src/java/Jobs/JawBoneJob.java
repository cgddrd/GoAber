/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jobs;

import DTO.IJobDetail;
import Devices.JawboneEJB;
import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import Scheduling.Jobs.AbstractJob;
import SessionBean.ActivityDataFacade;
import SessionBean.UserFacade;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Dan
 */
public class JawBoneJob extends AbstractJob  {

    //JawboneEJB io_jawboneEJB = lookupJawboneEJBBean();
    
    ActivityDataFacade io_activityFacade;
    UserFacade io_userFacade;
    JawboneEJB io_jawboneEJB;
    
        
    public JawBoneJob(IJobDetail ao_jobdetails) {
        super(ao_jobdetails);
        io_activityFacade = lookupBean(ActivityDataFacade.class);
        io_userFacade = lookupBean(UserFacade.class);
        io_jawboneEJB = lookupBean(JawboneEJB.class);
    }
    
        @Override
    public String getJobType() {
       return "Jawbone";
    }

    @Override
    public void run() {
        System.out.println("I HAVE BEEN CALLED! " + getJobDetails().getJobid());
        List<User> lo_users = io_userFacade.findAll();
        Date lda_date = new Date();
        int day = lda_date.getDate();
        int month = lda_date.getMonth()+1;
        int year = lda_date.getYear()+1900;

//        int day = 25;
//        int month = 11;
//        int year = 2015;
        for (User lo_user : lo_users) {
            try {
                ActivityData lo_data = io_jawboneEJB.getWalkingSteps(day, month, year, lo_user);
                if (lo_data.getValue() == 0) continue;
                io_activityFacade.create(lo_data);
            } catch (Exception e) {
               // e.printStackTrace();
            }
        }
        
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

    private JawboneEJB lookupJawboneEJBBean() {
        try {
            Context c = new InitialContext();
            return (JawboneEJB) c.lookup("java:global/GoAber/GoAber-ejb/JawboneEJB!Devices.JawboneEJB");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
}
