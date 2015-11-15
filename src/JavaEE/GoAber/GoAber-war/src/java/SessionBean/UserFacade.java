/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.User;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author helen
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
    @PersistenceContext(unitName = "GoAber-warPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(User.class);
    }
    
    // CG - Convenience serach function using 'User' EJB named query.
    public User findUserByEmail(String searchEmail) {
        
        Query queryEmployeesByFirstName = em.createNamedQuery("User.findByEmail");
        queryEmployeesByFirstName.setParameter("email", searchEmail);
        
        User user = (User) queryEmployeesByFirstName.getSingleResult();
        
        return user;
    }
    
}
