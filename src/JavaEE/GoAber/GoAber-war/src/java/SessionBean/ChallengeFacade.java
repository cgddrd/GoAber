/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.Challenge;
import GoAberDatabase.Community;
import GoAberDatabase.Team;
import GoAberDatabase.User;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author helen
 */
@Stateless
public class ChallengeFacade extends AbstractFacade<Challenge> {
    @PersistenceContext(unitName = "GoAber-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Challenge> getUnEnteredGroupChalleneges(User userId){
        List<Challenge> challenges = em.createNamedQuery("Challenge.unEnteredGroup").setParameter("groupId", userId.getGroupId()).getResultList();
        List<Challenge> unEnteredChallenges = new ArrayList<>();
        for (Challenge challenge : challenges)
        {
            if(em.createNamedQuery("UserChallenge.findByIdUserIdChallenge").setParameter("userId", userId).setParameter("challengeId", challenge).getResultList().isEmpty())
            {
                unEnteredChallenges.add(challenge);
            }
        }
        
        return unEnteredChallenges;//em.createNamedQuery("Challenge.unEnteredGroup").setParameter("userId", userId).getResultList();;
    }
    
    public List<Challenge> getUnEnteredCommunityChalleneges(User userId){
        List<Challenge> challenges = em.createNamedQuery("Challenge.unEnteredCommunity").setParameter("communityId", userId.getGroupId().getCommunityId()).getResultList();
        
        List<Challenge> unEnteredChallenges = new ArrayList<>();
        for (Challenge challenge : challenges)
        {
            if(em.createNamedQuery("UserChallenge.findByIdUserIdChallenge").setParameter("userId", userId).setParameter("challengeId", challenge).getResultList().isEmpty())
            {
                unEnteredChallenges.add(challenge);
            }
        }
        return unEnteredChallenges;//em.createNamedQuery("Challenge.unEnteredCommunity").setParameter("userId", userId).getResultList();
    }
    
    public List<Challenge> getEnteredGroupChalleneges(User userId){
        
        return em.createNamedQuery("Challenge.enteredGroup").setParameter("userId", userId).getResultList();
    }
    
    public List<Challenge> getEnteredCommunityChalleneges(User userId){
        return em.createNamedQuery("Challenge.enteredCommunity").setParameter("userId", userId).getResultList();
    }

    public ChallengeFacade() {
        super(Challenge.class);
    }
    
}
