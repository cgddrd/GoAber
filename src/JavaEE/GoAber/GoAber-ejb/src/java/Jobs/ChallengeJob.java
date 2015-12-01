/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jobs;

import DTO.IJobDetail;
import GoAberDatabase.ActivityData;
import GoAberDatabase.Challenge;
import GoAberDatabase.Community;
import GoAberDatabase.CommunityChallenge;
import GoAberDatabase.Result;
import Scheduling.Jobs.AbstractJob;
import SessionBean.ActivityDataFacade;
import SessionBean.ChallengeFacade;
import SessionBean.CommunityFacade;
import SessionBean.ResultFacade;
import WebServices.ChallengeWS.Consumer.ChallengeWSConsumer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Dan
 */
public class ChallengeJob extends AbstractJob {

    ResultFacade io_resultFacade;

    ActivityDataFacade io_activityFacade;
    
    CommunityFacade io_communityFacade;
    
    ChallengeFacade io_challengeFacade;

    ChallengeWSConsumer io_challengeWSConsumer;

//    @EJB
//    ChallengeFacade challengeFacade;
//    @EJB
//    CommunityFacade communityFacade;
//    @EJB
//    ActivityDataFacade activityFacade;
//    @EJB
//    ResultFacade resultFacade;
    

    public ChallengeJob(IJobDetail ao_jobdetails) {
        super(ao_jobdetails);
        io_resultFacade = lookupResultFacadeBean();
        io_activityFacade = lookupActivityDataFacadeBean();
        io_communityFacade = lookupCommunityFacadeBean();
        io_challengeFacade = lookupChallengeFacadeBean();
        io_challengeWSConsumer = lookupChallengeWSConsumerBean();
    }

    @Override
    public String getJobType() {
        return "Challenge";
    }

    @Override
    public void run() {
        try {
            System.out.println("Challenge Complete");

            String ls_challengeid = getJobDetails().scheduler_args()[0];

            Challenge lo_challenge = io_challengeFacade.find(ls_challengeid);
            Community lo_homecom = io_communityFacade.FindByHome().get(0);

            Result lo_res = new Result();

            lo_res.setChallengeId(lo_challenge);
            lo_res.setCommunityId(lo_homecom);
            lo_res.setCategoryUnitId(lo_challenge.getCategoryUnitId());

            List<ActivityData> lo_datalist = io_activityFacade.getAllInDateRange(
                    lo_challenge.getCategoryUnitId().getIdCategoryUnit().toString(), 
                    lo_challenge.getStartTime(), 
                    lo_challenge.getEndTime()
            );
            
            int li_value = 0;
            for (ActivityData lo_data : lo_datalist) {
                li_value += lo_data.getValue();
            }
            lo_res.setValue(li_value);

            io_resultFacade.create(lo_res);

            for (CommunityChallenge lo_comchal : lo_challenge.getCommunityChallengeCollection()) {
                if (lo_comchal.getCommunityId().getHome()) {
                    continue;
                }

                Result lo_foreignResult = io_challengeWSConsumer.sendResult(lo_res, lo_comchal.getCommunityId());
                io_resultFacade.create(lo_foreignResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ChallengeWSConsumer lookupChallengeWSConsumerBean() {
        try {
            Context c = new InitialContext();
            return (ChallengeWSConsumer) c.lookup("java:global/GoAber/GoAber-ejb/ChallengeWSConsumer!WebServices.ChallengeWS.Consumer.ChallengeWSConsumer");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ChallengeFacade lookupChallengeFacadeBean() {
        try {
            Context c = new InitialContext();
            return (ChallengeFacade) c.lookup("java:global/GoAber/GoAber-ejb/ChallengeFacade!SessionBean.ChallengeFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private CommunityFacade lookupCommunityFacadeBean() {
        try {
            Context c = new InitialContext();
            return (CommunityFacade) c.lookup("java:global/GoAber/GoAber-ejb/CommunityFacade!SessionBean.CommunityFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ActivityDataFacade lookupActivityDataFacadeBean() {
        try {
            Context c = new InitialContext();
            return (ActivityDataFacade) c.lookup("java:global/GoAber/GoAber-ejb/ActivityDataFacade!SessionBean.ActivityDataFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ResultFacade lookupResultFacadeBean() {
        try {
            Context c = new InitialContext();
            return (ResultFacade) c.lookup("java:global/GoAber/GoAber-ejb/ResultFacade!SessionBean.ResultFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
