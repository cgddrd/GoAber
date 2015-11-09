/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.CategoryUnit;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author helen
 */
@Stateless
public class CategoryUnitFacade extends AbstractFacade<CategoryUnit> {
    @PersistenceContext(unitName = "GoAber-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryUnitFacade() {
        super(CategoryUnit.class);
    }
    
    public List<CategoryUnit> findByUnit(int unitId) {
        return em.createNamedQuery("CategoryUnit.findByUnitId").setParameter("unitId", unitId).getResultList();
    }
    
    public List<CategoryUnit> findByCategory(int categoryId) {
        return em.createNamedQuery("CategoryUnit.findByCategoryId").setParameter("categoryId", categoryId).getResultList();
    }
}
