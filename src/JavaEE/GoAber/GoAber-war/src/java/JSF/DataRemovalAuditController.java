package JSF;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Audit;
import GoAberDatabase.DataRemovalAudit;
import JSF.auth.AuthController;
import JSF.util.JsfUtil;
import JSF.util.PaginationHelper;
import SessionBean.DataRemovalAuditFacade;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "dataRemovalAuditController")
@SessionScoped
public class DataRemovalAuditController implements Serializable {

    private DataRemovalAudit current;
    private DataModel items = null;
    @EJB
    private SessionBean.DataRemovalAuditFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    @ManagedProperty(value="#{authController}")
    AuthController auth;

    public DataRemovalAuditController() {
    }

    public DataRemovalAudit getSelected() {
        if (current == null) {
            current = new DataRemovalAudit();
            selectedItemIndex = -1;
        }
        return current;
    }

    private DataRemovalAuditFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
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
        current = (DataRemovalAudit) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
    
    /**
     * Saves the activity data that is about to be removed to the DataRemovalAudit table
     * @param activityData Data being removed
     * @param message Reason for deletion
     */
    public void createDataRemovalAudit(ActivityData activityData, String message)
    {
        current = new DataRemovalAudit();
        current.setMessage(message);
        current.setUserIdWhoRemoved(auth.getActiveUser());
        current.setUserIdData(activityData.getUserId());
        
        String jsonString = "{\"activityData\": { \"Id\":" + activityData.getIdActivityData();
        jsonString += "\"categoryUnitId\":" + activityData.getCategoryUnitId().getIdCategoryUnit();
        jsonString += "\"ApplicationUserId\":" + activityData.getUserId().getIdUser();
        jsonString += "\"date\":" + activityData.getDate().toString() + "} }";
        jsonString += "\"lastUpdated\":" + activityData.getLastUpdated().toString() + "} }";
        jsonString += "\"value\":" + activityData.getValue() + "} }";
        
        current.setDataRemoved(jsonString);
        current.setDateRemoved(new Date());
        getFacade().create(current);
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

    @FacesConverter(forClass = DataRemovalAudit.class)
    public static class DataRemovalAuditControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DataRemovalAuditController controller = (DataRemovalAuditController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "dataRemovalAuditController");
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
            if (object instanceof DataRemovalAudit) {
                DataRemovalAudit o = (DataRemovalAudit) object;
                return getStringKey(o.getIdDataRemovalAudit());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + DataRemovalAudit.class.getName());
            }
        }

    }
    public AuthController getAuth() {    
        return auth;
    }


    public void setAuth(AuthController auth) {    
        this.auth = auth;    
    }

}
