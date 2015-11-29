/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.Community;
import GoAberDatabase.Team;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author samuel
 */
@Stateless
public class TeamFacade extends AbstractFacade<Team> {
    @PersistenceContext(unitName = "GoAber-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeamFacade() {
        super(Team.class);
    }
    
    
    public Team findById(int groupId){
        try {
          return (Team)em.createNamedQuery("Team.findByIdGroup").setParameter("idGroup", groupId).getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
    
    public List<Team> findByCommunity(Community communityId){
        try {
          return em.createNamedQuery("Team.findByIdCommunity").setParameter("communityId", communityId).getResultList();
        } catch(NoResultException e) {
            return null;
        }
    }
}
