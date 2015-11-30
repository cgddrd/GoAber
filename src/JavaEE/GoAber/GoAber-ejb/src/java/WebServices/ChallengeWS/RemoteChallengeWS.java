/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServices.ChallengeWS;

import GoAberDatabase.CategoryUnit;
import GoAberDatabase.Challenge;
import SessionBean.CategoryUnitFacade;
import SessionBean.ChallengeFacade;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author Dan
 */
@WebService(serviceName = "GoAberChallengesWS", portName = "GoAberChallengesWSSoap", endpointInterface = "org.tempuri.GoAberChallengesWSSoap", targetNamespace = "http://tempuri.org/", wsdlLocation = "META-INF/wsdl/RemoteChallengeWS/localhost_50121/WebService/ChallengesWS/GoAberChallengesWS.asmx.wsdl")
@Stateless
public class RemoteChallengeWS {

    @EJB
    private CategoryUnitFacade categoryUnitFacade;

    @EJB
    private ChallengeFacade challengeFacade;
    
    
    
    
    public RemoteChallengeWS() {
        
    }

    public boolean recieveChallenge(org.tempuri.ChallengeData challenge, int userGroup) {
        Challenge lo_chalmod = new Challenge();
        
                CategoryUnit lo_catunit = categoryUnitFacade.find(challenge.getCategoryUnitId());
                lo_chalmod.setCategoryUnitId(lo_catunit);
                lo_chalmod.setEndTime(challenge.getEndTime().toGregorianCalendar().getTime());
                lo_chalmod.setName(challenge.getName());
                lo_chalmod.setStartTime(challenge.getStartTime().toGregorianCalendar().getTime());;
                //lo_chalmod.setIdChallenge(userGroup); = challenge.id;
                return true;
    }

    public org.tempuri.ResultData recieveResult(org.tempuri.ResultData result) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
    
}
