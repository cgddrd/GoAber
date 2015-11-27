package JSF;

import GoAberDatabase.Role;
import GoAberDatabase.User;
import GoAberDatabase.UserRole;
import JSF.services.AuthService;
import JSF.util.JsfUtil;
import JSF.util.PaginationHelper;
import SessionBean.UserFacade;
import SessionBean.UserRoleFacade;
import ViewModel.ViewModelChangePassword;
import java.io.IOException;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.xml.bind.DatatypeConverter;


@ManagedBean(name="userController")
@SessionScoped
public class UserController implements Serializable {


    private User current;
    private DataModel items = null;
    
    @EJB 
    private SessionBean.UserFacade ejbFacade;
    
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private MessageDigest md;
    
    // CG - Here we are using dependency injection to grab a reference to the session-scoped 'authService' reference.
    // See: http://www.mkyong.com/jsf2/injecting-managed-beans-in-jsf-2-0/ for more information.
    @ManagedProperty(value="#{authService}")
    private AuthService authService;
    
    // CG - When using DI via '@ManagedProperty' we need to make sure the property setter is available.
    // See: http://www.mkyong.com/jsf2/jsf-2-0-managed-bean-x-does-not-exist-check-that-appropriate-getter-andor-setter-methods-exist/ for more information.
    public void setauthService(AuthService authService) {
        this.authService = authService;
    }
    
    private UserRole currentUR;
    
    private ViewModelChangePassword viewModelChangePassword;
    
    @EJB 
    private SessionBean.UserRoleFacade urEJBFacade;
    
    @EJB 
    private SessionBean.RoleFacade rEJBFacade;
    
    public UserController() {
    }
    
    @PostConstruct
    public void Init() {
        
        currentUR = new UserRole();
        viewModelChangePassword = new ViewModelChangePassword();
        
        try {
            
           User activeUser = authService.getActiveUser();
        
        if (activeUser != null) {
            
            current = activeUser;
            
        } 
            
        } catch (Exception ex) {
            
            current = new User();
        }
        
    }

    public User getSelected() {
        if (current == null) {
            current = new User();
            selectedItemIndex = -1;
        }
        return current;
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }
        
    private UserRoleFacade getURFacade() {
        return urEJBFacade;
    }
    
    public PaginationHelper getPagination() {
        
        //getFacade().flushCache();
        
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem()+getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    public String prepareAccountView() {
        current = authService.getActiveUser();
        selectedItemIndex = -1;
        return "/myaccount/View";
    }

    public String prepareView() {
        current = (User)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() throws IOException {
        
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        
        current = new User();
        selectedItemIndex = -1;
        
        // CG - Make sure that the 'user registration success' message is displayed to the user in the login page.
        // See: http://stackoverflow.com/a/12485381 for more information.
        externalContext.getFlash().setKeepMessages(true);

        // CG - Instead of re-directing via externalContext object, we can just add 'faces-redirect=true' as a URL param.
        // See: http://stackoverflow.com/a/3642969 for more information.
        return "/login/index?faces-redirect=true";
        
    }
    
    public String create() {
        
        try {
            
            // Check to see if this user already exists (if not, we would expect a null to be returned here).
            if(getFacade().findUserByEmailOrNull(current.getEmail()) != null) {
                JsfUtil.addErrorMessage("User already exists. Please enter a different email address.");
                return null;
            }
            
            Role participantRole = rEJBFacade.find("participant");
            
            String newPassword = current.getPassword();
            current.setPassword(encodePassword(newPassword));
            
            // CG - Create the new 'UserRole' item.
            currentUR.setRoleId(participantRole);
            currentUR.setEmail(current.getEmail());
            
            // CG - Setup our new user.
            current.setUserRoleId(currentUR);
            
            getFacade().create(current);
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserCreated"));
            
            authService.setUsername(current.getEmail());
            //authService.setPassword(current.getPassword());
            
            return prepareCreate();
            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    private String encodePassword(String password) {
        if(md == null) {
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
            return DatatypeConverter.printBase64Binary(digest).toString();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String prepareAccountEdit() {
        
        current = getFacade().find(authService.getActiveUser().getIdUser());
        //current = authService.getActiveUser();
        selectedItemIndex = -1;
        return "/account/Edit";
    }

    public String prepareEdit() {
        current = (User)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String updatePassword() {
        
        try {
            
            String encodedOldPassword = encodePassword(viewModelChangePassword.getOldPassword());
            String currentUserPassword = current.getPassword();
            
            if (!encodedOldPassword.equals(currentUserPassword)) {
                JsfUtil.addErrorMessage("The original password you entered does not match the password we have on record. Please try again.");
                return null; 
            }
            
            // CG - Encode the new password.
            current.setPassword(encodePassword(viewModelChangePassword.getNewPassword()));
            
            return update();

        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String update() {
        try {
            
            // CG - Make sure to automatically update the user role email address.
            current.getUserRoleId().setEmail(current.getEmail());
            
            getFacade().edit(current);
            
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserUpdated"));
            
            // CG - Make sure to re-initialise the change password view model ready for next time.
            viewModelChangePassword = new ViewModelChangePassword();
            
            return "View";
            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (User)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            
            // CG - Prevent active user from removing their own account whilst still logged in.
            if (authService.getActiveUser().getIdUser() == current.getIdUser()) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("ErrorMessageNoDeleteActiveUser"));
            } else {
                getFacade().remove(current);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UserDeleted"));  
            }
            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count-1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex+1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }
    
    /**
     * @return the viewModelChangePassword
     */
    public ViewModelChangePassword getViewModelChangePassword() {
        return viewModelChangePassword;
    }

    /**
     * @param viewModelChangePassword the viewModelChangePassword to set
     */
    public void setViewModelChangePassword(ViewModelChangePassword viewModelChangePassword) {
        this.viewModelChangePassword = viewModelChangePassword;
    }

    @FacesConverter(value="userConverter", forClass=User.class)
    public static class UserControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UserController controller = (UserController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getIdUser());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+User.class.getName());
            }
        }

    }

}
