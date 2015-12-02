/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServices;

import GoAberDatabase.ActivityData;
import GoAberDatabase.CategoryUnit;
import GoAberDatabase.Challenge;
import GoAberDatabase.Community;
import GoAberDatabase.Result;
import SessionBean.ActivityDataFacade;
import SessionBean.CategoryUnitFacade;
import SessionBean.ChallengeFacade;
import SessionBean.CommunityFacade;
import SessionBean.ResultFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.goaberchallenges.ResultData;

/**
 *
 * @author Dan
 */
@Stateless
public class HandleResult {

    @EJB
    CategoryUnitFacade catunitFacade;
    @EJB
    ChallengeFacade challengeFacade;
    @EJB
    CommunityFacade communityFacade;
    @EJB
    ResultFacade resultFacade;
    @EJB
    ActivityDataFacade activityFacade;

    public ResultData RecieveResult(ResultData ao_result, Community ao_homecom) {
        CategoryUnit lo_catunit = catunitFacade.find(ao_result.getCategoryUnitId());
        Challenge lo_challenge = challengeFacade.find(ao_result.getChallengeId());
        Community lo_community = communityFacade.find(ao_result.getCommunityId());

        Result lo_resmodel = new Result();
        lo_resmodel.setCategoryUnitId(lo_catunit);
        lo_resmodel.setChallengeId(lo_challenge);
        lo_resmodel.setCommunityId(lo_community);
        lo_resmodel.setValue(ao_result.getValue());

        resultFacade.create(lo_resmodel);
        Result lo_homeresult = new Result();
        lo_homeresult.setCategoryUnitId(lo_catunit);
        lo_homeresult.setChallengeId(lo_challenge);
        lo_homeresult.setCommunityId(ao_homecom);

        List<ActivityData> lo_datalist = activityFacade.getAllInDateRange(
                lo_challenge.getCategoryUnitId().getIdCategoryUnit().toString(),
                lo_challenge.getStartTime(),
                lo_challenge.getEndTime()
        );

        int li_value = 0;
        for (ActivityData lo_data : lo_datalist) {
            li_value += lo_data.getValue();
        }

        lo_homeresult.setValue(li_value);
        
        resultFacade.create(lo_homeresult);

        ResultData lo_resultdata = new ResultData();
        lo_resultdata.setCategoryUnitId(lo_homeresult.getCategoryUnitId().getIdCategoryUnit());
        lo_resultdata.setChallengeId(lo_homeresult.getChallengeId().getIdChallenge());
        lo_resultdata.setCommunityId(lo_homeresult.getCommunityId().getIdCommunity());
        lo_resultdata.setValue(lo_homeresult.getValue());
        
        return lo_resultdata;
    }
}
