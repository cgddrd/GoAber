/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServices.ChallengeWS;

import GoAberDatabase.CategoryUnit;
import GoAberDatabase.Challenge;
import GoAberDatabase.Community;
import SessionBean.CategoryUnitFacade;
import SessionBean.ChallengeFacade;
import SessionBean.CommunityFacade;
import WebServices.ChallengeService;
import WebServices.HandleResult;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author Dan
 */
@WebService(serviceName = "GoAberChallengesWS", portName = "GoAberChallengesWSSoap", endpointInterface = "org.goaberchallenges.GoAberChallengesWSSoap", targetNamespace = "http://goaberchallenges.org/", wsdlLocation = "META-INF/wsdl/GoAberChallengesWS/localhost_50121/WebService/ChallengesWS/GoAberChallengesWS.asmx.wsdl")
@Stateless
public class GoAberChallengesWS {
    
    @EJB
    private HandleResult io_handleResult;
    
    @EJB
    private CategoryUnitFacade io_categoryUnitFacade;

    @EJB
    private ChallengeFacade io_challengeFacade;

    @EJB
    ChallengeService io_chalservice;

    @EJB
    private CommunityFacade io_communityFacade;
    
    private static Community io_homecom = null;
    
    public GoAberChallengesWS() 
    {
    }


    public boolean recieveChallenge(org.goaberchallenges.ChallengeData challenge, int userGroup) {
        try {
            if (io_homecom == null) {
               io_homecom = io_communityFacade.findByHome().get(0);
            }
            Challenge lo_chalmod = new Challenge();

            CategoryUnit lo_catunit = io_categoryUnitFacade.find(challenge.getCategoryUnitId());
            lo_chalmod.setCategoryUnitId(lo_catunit);
            lo_chalmod.setEndTime(challenge.getEndTime().toGregorianCalendar().getTime());
            lo_chalmod.setName(challenge.getName());
            lo_chalmod.setStartTime(challenge.getStartTime().toGregorianCalendar().getTime());
            lo_chalmod.setIdChallenge(challenge.getId());
            io_challengeFacade.create(lo_chalmod);
            io_chalservice.addCommunityChallenges(null, lo_chalmod, new Integer[]{io_homecom.getIdCommunity(), challenge.getCommunityId()}, false);
            //lo_chalmod.setIdChallenge(userGroup); = challenge.id;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public org.goaberchallenges.ResultData recieveResult(org.goaberchallenges.ResultData result) {
        try {
            if (io_homecom == null) {
               io_homecom = io_communityFacade.findByHome().get(0);
            }
            return io_handleResult.RecieveResult(result, io_homecom);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
