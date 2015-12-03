package JSF.services;

import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import JSF.exceptions.NonexistentEntityException;
import JSF.util.JsfUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.ResourceBundle;
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
@ManagedBean(name = "authService")
@SessionScoped
// We may want to change this to @ViewScoped. See: http://stackoverflow.com/a/2207147 for more information.
public class AuthService implements Serializable {

    private String username;
    private String password;
    private User activeUser;
    private String forwardURL;

    @EJB
    private SessionBean.UserFacade userFacade;
    
    
    @PostConstruct
    public void init() {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        // CG - Removed this temporarily to prevent forwarding issues. - Need to look at this again.
        //forwardURL = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_REQUEST_URI);
        if (forwardURL == null) {
            forwardURL = externalContext.getRequestContextPath() + "/faces/index.xhtml";

        } else {

            String originalQuery = (String) externalContext.getRequestMap().get(RequestDispatcher.FORWARD_QUERY_STRING);

            if (originalQuery != null) {
                forwardURL += "?" + originalQuery;
            }

        }
    }
    
    public User getActiveUser() {
        activeUser = userFacade.find(activeUser.getIdUser());

        if (activeUser == null) {

            try {
                logoutUser(true);
                return null;
            } catch (IOException ex) {
                Logger.getLogger(AuthService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return this.activeUser;

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

    public boolean isLoggedIn() {

        // CG - Check if we have a user principal (i.e. is a user logged in?) and return true if so, or otherwise false.
        // See: http://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html#getUserPrincipal() for more information.
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null && this.activeUser != null;

    }

    public boolean isAdmin() {

        // CG - Added fix to make sure that we assert the active user is not null before attempting to query the role type.
        return this.isLoggedIn() && this.activeUser != null && this.activeUser.getUserRoleId().getRoleId().getIdRole().equals("administrator");

    }

    // CG - userController.loggedInUser
    public String getLoggedInUser() {
        return FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName();
    }

    public void checkLogoutStatus() {

        Map<String, String> urlParameterMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        // CG - Check to see if the logout was normal, or is it was triggered by an error on the active user's account.
        if (urlParameterMap.containsKey("logoutStatus")) {

            String logoutStatusParam = urlParameterMap.get("logoutStatus");
            
            if (logoutStatusParam.equals("error")) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("LogoutDueToError"));
            }

            if (logoutStatusParam.equals("success")) {
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("LogoutSuccess"));
            }
        }

    }

    public void loginUser() throws IOException {

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        try {

            request.login(this.username, this.password);

            this.activeUser = (User) userFacade.findUserByEmailOrNull(this.username);

            externalContext.getSessionMap().put("loggedInUser", this.activeUser);
            externalContext.redirect(forwardURL);

        } catch (ServletException e) {

            // Handle unknown username/password in request.login().
            // CG - We are going to display this message in the 'global' message list for the login page.
            // CG - See: thtp://stackoverflow.com/a/11029988 for more information.
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error: Your login credentials are incorrect. Please try again.", null));

        } catch (Exception ex) {

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Error: An error has occured during authentication. Please try again later.", null));

        }
    }

    public void logoutUser(boolean logoutDueToError) throws IOException {

        // CG - Invalidate the current session (i.e. log the user out.)
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        /* 
         * CG - In order to prevent getting an 'IllegalStateException' when trying to use 'faces-redirect=true' after logging out,
         * we apparantly need to make sure that we create a new session instance BEFORE trying to re-direct the page.
         * 
         * See: http://stackoverflow.com/a/9687525 for more information.
         */
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        if (logoutDueToError) {

            FacesContext.getCurrentInstance().getExternalContext().redirect("login/index.xhtml?faces-redirect=true&logoutStatus=error");

        } else {

            // CG - Redirect to homepage once the user has logged out. (Use 'faces-redirect=true' to ensure we remain in the scope of the current application root URL.)
            FacesContext.getCurrentInstance().getExternalContext().redirect("login/index.xhtml?faces-redirect=true&logoutStatus=success");

        }

    }

}
