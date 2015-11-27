/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.User;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Dan
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {

    @PersistenceContext(unitName = "GoAber-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

     public UserFacade() {
        super(User.class);
    }
    
    @Override
    public User find(Object id) {
        super.flushCache();
        return super.find(id);
    }

    @Override
    public List<User> findAll() {
        super.flushCache();
        return super.findAll();
    }
    
    @Override
    public List<User> findRange(int[] range) {
        super.flushCache();
        return super.findRange(range);
    }
    
    @Override
    public int count() {
        super.flushCache();
        return super.count();
    }
    
    // CG - Convenience search function using 'User' EJB named query.
    public User findUserByEmailOrNull(String searchEmail) {
        
        super.flushCache();
        
        try {

            Query queryUserByEmail = em.createNamedQuery("User.findByEmail");
            queryUserByEmail.setParameter("email", searchEmail);

            User user = (User) queryUserByEmail.getSingleResult();

            return user;

        } catch (NoResultException nRE) {
            return null;
        }

    }
    
    public User findUserById(int id) {
        
        super.flushCache();
        
        Query queryEmployeesByFirstName = em.createNamedQuery("User.findByIdUser");
        queryEmployeesByFirstName.setParameter("idUser", id);
        
        User user = (User) queryEmployeesByFirstName.getSingleResult();
        
        return user;
    }

}
