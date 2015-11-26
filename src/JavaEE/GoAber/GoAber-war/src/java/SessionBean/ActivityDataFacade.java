/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author helen
 */
@Stateless
public class ActivityDataFacade extends AbstractFacade<ActivityData> {
    @PersistenceContext(unitName = "GoAber-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ActivityDataFacade() {
        super(ActivityData.class);
    }
    
    public List<ActivityData> getAllForUser(int id) {
        return em.createNamedQuery("ActivityData.findAllForUser").setParameter("id", id).getResultList();
    }

    public List<ActivityData> getAllForUserInDateRange(int id, Date startDate, Date endDate) {
        return em.createNamedQuery("ActivityData.getAllForUserInDateRange")
                    .setParameter("id", id)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .getResultList();
    }
}
