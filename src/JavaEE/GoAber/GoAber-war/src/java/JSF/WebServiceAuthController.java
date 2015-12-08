package JSF;

import GoAberDatabase.User;
import GoAberDatabase.WebServiceAuth;
import JSF.services.AuthService;
import JSF.util.JsfUtil;
import SessionBean.WebServiceAuthFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@ManagedBean(name="webServiceAuthController")
@SessionScoped
public class WebServiceAuthController implements Serializable {

    private WebServiceAuth current;
    private List<WebServiceAuth> items = null;
    @EJB
    private SessionBean.WebServiceAuthFacade ejbFacade;
    
    @ManagedProperty(value="#{authService}")
    private AuthService auth;

    public WebServiceAuthController() {

    }

    public WebServiceAuth getSelected() {
        if (current == null) {
            current = new WebServiceAuth();
        }
        return current;
    }

    private WebServiceAuthFacade getFacade() {
        return ejbFacade;
    }

    
    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView(WebServiceAuth data) {
        current = data;
        return "View";
    }

    public String prepareCreate() {
        prepareList();
        current = new WebServiceAuth();
        return "Create";
    }


    public String create() {
        try {
            User lo_user = auth.getActiveUser();
            
            current.setUserid(lo_user.getIdUser());

            current.setAuthtoken(UUID.randomUUID().toString());
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WebServiceAuthCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(WebServiceAuth data) {
        current = data;
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WebServiceAuthUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(WebServiceAuth data) {
        current = data;
        performDestroy();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WebServiceAuthDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public List<WebServiceAuth> getItems() {
        User currentUser = auth.getActiveUser();
        if (items == null){
            if(auth.isAdmin()){
                items = getFacade().findAll();
            }
            else {
                items = getFacade().findByUser(currentUser.getIdUser());
            }
        }     
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public WebServiceAuth getWebServiceAuth(java.lang.String id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = WebServiceAuth.class)
    public static class WebServiceAuthControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WebServiceAuthController controller = (WebServiceAuthController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "webServiceAuthController");
            return controller.getWebServiceAuth(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof WebServiceAuth) {
                WebServiceAuth o = (WebServiceAuth) object;
                return getStringKey(o.getAuthtoken());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + WebServiceAuth.class.getName());
            }
        }

    }

    public AuthService getAuth() {    
        return auth;
    }


    public void setAuth(AuthService auth) {    
        this.auth = auth;    
    }
}
