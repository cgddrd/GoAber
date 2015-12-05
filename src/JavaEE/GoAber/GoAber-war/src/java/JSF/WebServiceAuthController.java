package JSF;

import GoAberDatabase.User;
import GoAberDatabase.WebServiceAuth;
import JSF.util.JsfUtil;
import JSF.util.PaginationHelper;
import SessionBean.WebServiceAuthFacade;
import java.io.IOException;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("webServiceAuthController")
@SessionScoped
public class WebServiceAuthController implements Serializable {

    private WebServiceAuth current;
    private DataModel items = null;
    @EJB
    private SessionBean.WebServiceAuthFacade ejbFacade;

    @EJB
    private SessionBean.UserFacade ejbUsers;

    private PaginationHelper pagination;
    private int selectedItemIndex;

    public WebServiceAuthController() {

    }

    public WebServiceAuth getSelected() {
        if (current == null) {
            current = new WebServiceAuth();
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public void redirectLogin() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            FacesContext.getCurrentInstance().getExternalContext().redirect(externalContext.getRequestContextPath() + "/faces/login/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(WebServiceAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void redirectList() {
        try {
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            FacesContext.getCurrentInstance().getExternalContext().redirect(externalContext.getRequestContextPath() + "/faces/webServiceAuth/List.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(WebServiceAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private WebServiceAuthFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {

            User lo_user = (User) ejbUsers.findUserByEmailOrNull(FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName());
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    if (isAdmin(lo_user)) {
                        return getFacade().count();
                    } else {
                        return getFacade().countUser(lo_user.getIdUser());
                    }
                }

                @Override
                public DataModel createPageDataModel() {
                    if (isAdmin(lo_user)) {
                        return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                    } else {
                        return new ListDataModel(getFacade().findRangeUser(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}, lo_user.getIdUser()));
                    }
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (WebServiceAuth) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        if (isLoggedIn()) {
            prepareList();
            current = new WebServiceAuth();
            selectedItemIndex = -1;
            return "Create";
        } else {
            redirectLogin();
            return "List";
        }
    }

//    public void redirectLogin() {
//        try {
//            FacesContext context = FacesContext.getCurrentInstance();
//            ExternalContext externalContext = context.getExternalContext();
//            externalContext.redirect("/GoAber-war/faces/login/index.xhtml");
//        } catch (IOException ex) {
//            Logger.getLogger(WebServiceAuthController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }

    private boolean isLoggedIn() {
        return (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() != null);
    }

    public boolean isAdmin(User ao_user) {
        return this.isLoggedIn() && ao_user.getUserRoleId().getRoleId().getIdRole().equals("admin");

    }

    public String create() {
        try {
            if (!isLoggedIn()) {
                return "";
            }

            User lo_user = (User) ejbUsers.findUserByEmailOrNull(FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName());

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

    public String prepareEdit() {
        current = (WebServiceAuth) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
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

    public String destroy() {
        current = (WebServiceAuth) getItems().getRowData();
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
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("WebServiceAuthDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (!isLoggedIn()) {
            redirectLogin();
            PaginationHelper lo_pag = new PaginationHelper(0) {

                @Override
                public int getItemsCount() {
                    return 0;
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel();

                }
            };
            return lo_pag.createPageDataModel();
        }
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

}
