package JSF.auth;

import GoAberDatabase.ActivityData;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author connorgoddard
 */
@ManagedBean(name = "authController")
@SessionScoped
public class AuthController implements Serializable {
    
    // CG - userController.loggedInUser
    public String getLoggedInUser() {
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
    }
    
    public void logoutUser() throws IOException {
        
        // Invalidate session of a sessionscoped managed bean
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        //FacesContext.getCurrentInstance().getExternalContext().
        
        // Redirect to page you want after logout
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    
    }
    
      
}
