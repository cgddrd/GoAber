/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.ActivityData;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Dan
 */
@Stateless
public class ActivityDataFacade extends AbstractFacade<ActivityData> {

    @PersistenceContext(unitName = "GoAber-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActivityDataFacade() {
        super(ActivityData.class);
    }
    
}
