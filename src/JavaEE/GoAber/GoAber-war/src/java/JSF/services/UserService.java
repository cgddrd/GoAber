/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private UserFacade ejbFacade;
    
    public User getUserById(int id) {
        return getFacade().find(id);
    }

    /**
     * @return the ejbFacade
     */
    public UserFacade getFacade() {
        return ejbFacade;
    }
}
