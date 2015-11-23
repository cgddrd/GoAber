/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.WebServiceAuth;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Dan
 */
@Stateless
public class WebServiceAuthFacade extends AbstractFacade<WebServiceAuth> {

    @PersistenceContext(unitName = "GoAber-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WebServiceAuthFacade() {
        super(WebServiceAuth.class);
    }
    
    public int countUser(int ai_userid) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        javax.persistence.criteria.Root<WebServiceAuth> rt = cq.from(WebServiceAuth.class);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        cq.where(cb.equal(rt.get("userid"), ai_userid));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
     public List<WebServiceAuth> findRangeUser(int[] range, int ai_userid) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<WebServiceAuth> rt = cq.from(WebServiceAuth.class);
        cq.select(rt);
        cq.where(cb.equal(rt.get("userid"), ai_userid));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }
    
}
