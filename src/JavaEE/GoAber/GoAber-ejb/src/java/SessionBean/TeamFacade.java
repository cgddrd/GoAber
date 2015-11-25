/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.Team;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dan
 */
@Stateless
public class TeamFacade extends AbstractFacade<Team> {

    @PersistenceContext(unitName = "GoAber-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TeamFacade() {
        super(Team.class);
    }
    
}
