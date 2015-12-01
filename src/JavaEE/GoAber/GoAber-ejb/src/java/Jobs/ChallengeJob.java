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

    @EJB
    ChallengeFacade challengeFacade;
    @EJB
    CommunityFacade communityFacade;
    @EJB
    ActivityDataFacade activityFacade;
    @EJB
    ResultFacade resultFacade;

    public ChallengeJob(IJobDetail ao_jobdetails) {
        super(ao_jobdetails);
    }

    @Override
    public String getJobType() {
        return "Challenge";
    }

    @Override
    public void run() {
        try {
            System.out.println("Challenge Complete");

            String ls_challengeid = "";

            Challenge lo_challenge = challengeFacade.find(ls_challengeid);
            Community lo_homecom = communityFacade.FindByHome().get(0);

            Result lo_res = new Result();

            lo_res.setChallengeId(lo_challenge);
            lo_res.setCommunityId(lo_homecom);
            lo_res.setCategoryUnitId(lo_challenge.getCategoryUnitId());

            List<ActivityData> lo_datalist = activityFacade.getAllInDateRange(
                    lo_challenge.getCategoryUnitId().getIdCategoryUnit().toString(), 
                    lo_challenge.getStartTime(), 
                    lo_challenge.getEndTime()
            );
            
            int li_value = 0;
            for (ActivityData lo_data : lo_datalist) {
                li_value += lo_data.getValue();
            }
            lo_res.setValue(li_value);

            resultFacade.create(lo_res);

            for (CommunityChallenge lo_comchal : lo_challenge.getCommunityChallengeCollection()) {
                if (lo_comchal.getCommunityId().getHome()) {
                    continue;
                }

                ChallengeWSConsumer lo_consumer = new ChallengeWSConsumer();
                Result lo_foreignResult = lo_consumer.sendResult(lo_res, lo_comchal.getCommunityId());
                resultFacade.create(lo_foreignResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
