package JSF.auth;

import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author connorgoddard
 */
@ManagedBean(name = "authController")
@SessionScoped
// We may want to change this to @ViewScoped. See: http://stackoverflow.com/a/2207147 for more information.
public class AuthController implements Serializable {
    
    private String username;
    private String password;
    private User activeUser;

    public User getActiveUser() {
        return activeUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    private String forwardURL; 
    
    @EJB 
    private SessionBean.UserFacade userFacade;
    
    @PostConstruct
    public void init() {
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        
        forwardURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);

        if (forwardURL == null) {
            forwardURL = externalContext.getRequestContextPath() + "/faces/index.xhtml";
            
        } else {
            
            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                forwardURL += "?" + originalQuery;
            }
            
        }
    }
    
    public boolean isLoggedIn() {
        
        // CG - Check if we have a user principal (i.e. is a user logged in?) and return true if so, or otherwise false.
        // See: http://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html#getUserPrincipal() for more information.
        return (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null);
        
    }
    
    public boolean isAdmin() {     
        return this.isLoggedIn() && this.activeUser.getRoleId().getIdRole().equals("admin");
    }
   
    // CG - userController.loggedInUser
    public String getLoggedInUser() {
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
    }
    
    public void loginUser() throws IOException {
        
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        try {
            
            request.login(this.username, this.password);
            
            this.activeUser = userFacade.findUserByEmail(this.username);
            
            externalContext.getSessionMap().put("loggedInUser", this.activeUser);
            externalContext.redirect(forwardURL);
            
        } catch (ServletException e) {
            
            // Handle unknown username/password in request.login().
            // CG - We are going to display this message in the 'global' message list for the login page.
            // CG - See: http://stackoverflow.com/a/11029988 for more information.
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error: Your login credentials are incorrect. Please try again.", null));
        
        } catch (Exception ex) {
            
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error: An error has occured during authentication. Please try again later.", null));

        }
    }
    
    public void logoutUser() throws IOException {
        
        // Invalidate session of a sessionscoped managed bean
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        
        // Redirect to page you want after logout
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
    
    }
    
      
}
