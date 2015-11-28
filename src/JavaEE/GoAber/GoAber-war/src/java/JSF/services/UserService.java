
package JSF.services;

import GoAberDatabase.User;
import SessionBean.UserFacade;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/** User Service class.
 * 
 * This provides access to the common functions used to get information about 
 * users. This is different from the auth controller which deals with 
 * authentication of the current user.
 *
 * @author samuel
 */
@Stateless
@LocalBean
public class UserService {
    @EJB
     UserFacade ejbFacade;
    
    /** Get a user by their id.
     * 
     * @param id the id of the user
     * @return the user object for the given id.
     */
    public User getUserById(int id) {
        return getFacade().find(id);
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }
}
