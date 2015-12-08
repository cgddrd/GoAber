/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServices;

import GoAberDatabase.Community;
import SessionBean.CommunityFacade;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.goaberchallenges.CommunityData;

/**
 *
 * @author Dan
 */
@Stateless
public class HandleContract {
    
    @EJB
    CommunityFacade communityFacade;
    
    public CommunityData RecieveContract(CommunityData ao_comdata, Community ao_homecom) {
        String ls_authtoken = UUID.randomUUID().toString();
        Community ao_com = new Community();
        ao_com.setName(ao_comdata.getName());
        ao_com.setHome(false);
        ao_com.setDomain(ao_comdata.getDomain());
        ao_com.setChallengesEndpoint(ao_comdata.getChallengesEndpoint());
        ao_com.setAuthtoken(ls_authtoken);
        
        communityFacade.create(ao_com);
        
        CommunityData ao_responsedata = new CommunityData();
        ao_responsedata.setAuthtoken(ls_authtoken);
        ao_responsedata.setChallengesEndpoint(ao_homecom.getChallengesEndpoint());
        ao_responsedata.setDomain(ao_homecom.getDomain());
        ao_responsedata.setName(ao_homecom.getName());
        
        return ao_responsedata;
    }
}
