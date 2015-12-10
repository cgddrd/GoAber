/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServices;

import GoAberDatabase.Community;
import SessionBean.CommunityFacade;
import WebServices.ChallengeWS.Consumer.ChallengeWSConsumer;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Dan
 */
@Stateless
public class CommunitiesService {
    
    @EJB CommunityFacade io_comFacade;
    @EJB ChallengeWSConsumer io_challengesConsumer;
    /**
     * Requests a community contract with a remote community.
     * Saves the response.
     * @param ao_community
     * @return 
     */
    public Community RequestContract(Community ao_community) {
        if (ao_community.getHome()) return ao_community;
        
        Community lo_homecom = io_comFacade.findByHome().get(0);
        return io_challengesConsumer.RequestContract(ao_community, lo_homecom);
    }
}
