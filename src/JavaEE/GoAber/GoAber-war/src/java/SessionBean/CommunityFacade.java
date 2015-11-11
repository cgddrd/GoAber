/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.Community;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author samuel
 */
@Stateless
public class CommunityFacade extends AbstractFacade<Community> {
    @PersistenceContext(unitName = "GoAber-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CommunityFacade() {
        super(Community.class);
    }
    
}
