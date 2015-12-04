package JSF;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Category;
import GoAberDatabase.Unit;
import GoAberDatabase.User;
import JSF.services.AuthService;
import JSF.services.ActivityDataService;
import JSF.services.UserService;
import JSF.util.JsfUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name="activityDataController")
@SessionScoped
public class ActivityDataController implements Serializable {

    @ManagedProperty(value="#{auditController}")
    private AuditController audit;
    
    @ManagedProperty(value="#{dataRemovalAuditController}")
    private DataRemovalAuditController dataRemovalAudit;
    
    @EJB
    private ActivityDataService dataService;
    @EJB
    private UserService userService;
    @EJB
    private SessionBean.CategoryFacade categoryBean;
    @EJB
    private SessionBean.UnitFacade unitBean;
    
    @ManagedProperty(value="#{authService}")
    private AuthService authService;
    
    private ActivityData current;
    private User viewUser;
    
    private String deletedMessage = "none";
    
    private List<ActivityData> items = null;
    private List<ActivityData> filteredItems = null;


    public ActivityDataController() {
    }
    
    @PostConstruct
    public void init() {
        recreateItems();
    }
    
    public List<SelectItem> getCategories() {
        List<SelectItem> names = new ArrayList<>();
        
        for (Category cat : categoryBean.findAll()) {
            names.add(new SelectItem(cat.getName()));
        }
        return names;
    }
    
    public List<SelectItem> getUnits() {
        List<SelectItem> names = new ArrayList<>();
 
        for (Unit unit : unitBean.findAll()) {
            names.add(new SelectItem(unit.getName()));
        }
        return names;
    }

    public ActivityData getSelected() {
        if (getCurrent() == null) {
            setCurrent(new ActivityData());
        }
        return getCurrent();
    }

    public String prepareList() {
        recreateItems();
        return "Manage";
    }

    public String prepareView(ActivityData item) {
        current = item;
        return "View";
    }

    public String prepareCreate() {
        setCurrent(new ActivityData());
        return "Create";
    }

    public String create() {
        try {
            User user = authService.getActiveUser();
            dataService.createForUser(getCurrent(), user);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActivityDataCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(ActivityData data) {
        current = data;
        return "Edit";
    }

    public String update() {
        try {
            User user = authService.getActiveUser();
            dataService.updateForUser(getCurrent(), user);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActivityDataUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareDestroy(ActivityData data) {
        current = data;
        return "Delete";
    }
        
    public String destroy() {
        performDestroy();
        // must recreate as something has been removed!
        recreateItems();
        return "Manage";
    }

    private void performDestroy() {
        try {
            dataService.remove(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActivityDataDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    public String prepareViewUser(User user) {
        viewUser = userService.getUserById(user.getIdUser());
        return "/activityData/ViewUser?faces-redirect=true";
    }
    
    private void recreateItems() {
        User user = authService.getActiveUser();
        items = dataService.findAllForUser(user);
        filteredItems = null;
    }

    /**
     * @return the filteredItems
     */
    public List<ActivityData> getFilteredItems() {
        return filteredItems;
    }

    /**
     * @param filteredItems the filteredItems to set
     */
    public void setFilteredItems(List<ActivityData> filteredItems) {
        this.filteredItems = filteredItems;
    }

    /**
     * @return the items
     */
    public List<ActivityData> getItems() {
        return items;
    }

    /**
     * @return the current
     */
    public ActivityData getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(ActivityData current) {
        this.current = current;
    }
    
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    /**
     * @return the viewUser
     */
    public User getViewUser() {
        return viewUser;
    }
    
    public String prepareBatchDestroy() {
        getAudit().createAudit("activityData/BatchDelete", "");
        if (filteredItems == null) {
            filteredItems = items;
        }
        return "BatchDelete";
    }
    
    public String batchDestory() {
        getAudit().createAudit("activityData/List", "Performed batch delete of ActivityData");
        performBatchDestroy();
        recreateItems();
        return "Manage";
    }
    
    private void performBatchDestroy() {
        if (filteredItems == null) {
            return;
        }
        
        for (ActivityData item : filteredItems) {
            getDataRemovalAudit().createDataRemovalAudit(item, getDeletedMessage());
            try {
                dataService.remove(item);
                //getFacade().remove(item);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActivityDataDeleted"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    /**
     * @return the audit
     */
    public AuditController getAudit() {
        return audit;
    }

    /**
     * @param audit the audit to set
     */
    public void setAudit(AuditController audit) {
        this.audit = audit;
    }

    /**
     * @return the dataRemovalAudit
     */
    public DataRemovalAuditController getDataRemovalAudit() {
        return dataRemovalAudit;
    }

    /**
     * @param dataRemovalAudit the dataRemovalAudit to set
     */
    public void setDataRemovalAudit(DataRemovalAuditController dataRemovalAudit) {
        this.dataRemovalAudit = dataRemovalAudit;
    }

    /**
     * @return the deletedMessage
     */
    public String getDeletedMessage() {
        return deletedMessage;
    }

    /**
     * @param deletedMessage the deletedMessage to set
     */
    public void setDeletedMessage(String deletedMessage) {
        this.deletedMessage = deletedMessage;
    }
    
    @FacesConverter(forClass = ActivityData.class)
    public static class ActivityDataControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ActivityDataController controller = (ActivityDataController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "activityDataController");
            return controller.dataService.findById(getKey(value));
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
            if (object instanceof ActivityData) {
                ActivityData o = (ActivityData) object;
                return getStringKey(o.getIdActivityData());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ActivityData.class.getName());
            }
        }
    }

}
