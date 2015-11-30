/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.Challenge;
import GoAberDatabase.User;
import GoAberDatabase.UserChallenge;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author helen
 */
@Stateless
public class UserChallengeFacade extends AbstractFacade<UserChallenge> {
    @PersistenceContext(unitName = "GoAber-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public UserChallenge findByUserAndChallenge(User userId, Challenge challengeId){
        return (UserChallenge)em.createNamedQuery("UserChallenge.findByIdUserIdChallenge").setParameter("userId", userId).setParameter("challengeId", challengeId).getSingleResult();
    }

    public UserChallengeFacade() {
        super(UserChallenge.class);
    }
    
}
