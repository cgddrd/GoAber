/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.Community;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dan
 */
@Stateless
public class CommunityFacade extends AbstractFacade<Community> {

    @PersistenceContext(unitName = "GoAber-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
     public Community findById(int communityId){
        try {
            
          return (Community)em.createNamedQuery("Community.findByIdCommunity")
                  .setParameter("idCommunity", communityId)
                  .getSingleResult();
          
        } catch(NoResultException e) {
            return null;
        }
    }

    public CommunityFacade() {
        super(Community.class);
    }
    
    public List<Community> FindByHome() {
       return em.createNamedQuery("Community.findByHome", Community.class).setParameter("home", "1").getResultList();
    }
    
}
