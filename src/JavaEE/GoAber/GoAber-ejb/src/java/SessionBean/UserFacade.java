/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import GoAberDatabase.User;
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

    // CG - Convenience serach function using 'User' EJB named query.
    public User findUserByEmailOrNull(String searchEmail) {

        try {

            Query queryUserByEmail = em.createNamedQuery("User.findByEmail");
            queryUserByEmail.setParameter("email", searchEmail);

            User user = (User) queryUserByEmail.getSingleResult();

            return user;

        } catch (NoResultException nRE) {
            return null;
        }

    }

}
