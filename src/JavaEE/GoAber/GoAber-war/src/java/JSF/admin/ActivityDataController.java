package JSF.admin;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Category;
import GoAberDatabase.Unit;
import JSF.AuditController;
import JSF.DataRemovalAuditController;
import JSF.services.ActivityDataService;
import JSF.util.JsfUtil;
import SessionBean.ActivityDataFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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

@ManagedBean(name="adminActivityDataController")
@SessionScoped
public class ActivityDataController implements Serializable {

    @EJB
    private ActivityDataService dataService;
    @EJB
    private SessionBean.CategoryFacade categoryBean;
    @EJB
    private SessionBean.UnitFacade unitBean;
    private ActivityData current;
    private List<ActivityData> items = null;
    private List<ActivityData> filteredItems = null;
    
    @ManagedProperty(value="#{auditController}")
    AuditController audit;
    
    @ManagedProperty(value="#{dataRemovalAuditController}")
    DataRemovalAuditController dataRemovalAudit;
    private String deletedMessage = "none";
    
    

    public ActivityDataController() {
    }
    
    public void setDeletedMessage(String deletedMessage){
        this.deletedMessage = deletedMessage;
    }
    public String getDeletedMessage(){
        return deletedMessage;
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
        audit.createAudit("activityData/List", "");
        recreateItems();
        return "List";
    }

    public String prepareView(ActivityData item) {
        audit.createAudit("activityData/View", "IdActivityData=" + item.getIdActivityData());
        current = item;
        return "View";
    }

    public String prepareCreate() {
        audit.createAudit("activityData/Create", "");
        setCurrent(new ActivityData());
        return "Create";
    }

    public String create() {
        try {
            dataService.create(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActivityDataCreated"));
            audit.createAudit("activityData/Create", "Created : IdActivityData="+getCurrent().getIdActivityData());
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(ActivityData data) {
        audit.createAudit("activityData/Edit", "IdActivityData=" + data.getIdActivityData());
        current = data;
        return "Edit";
    }

    public String update() {
        try {
            dataService.update(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActivityDataUpdated"));
            audit.createAudit("activityData/View", "Updated : IdActivityData=" + getCurrent().getIdActivityData());
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareDestroy(ActivityData data) {
        audit.createAudit("activityData/Delete", "Viewed delete : IdActivityData="+data.getIdActivityData());
        current = data;
        return "List";
    }
        
    public String destroy() {
        performDestroy();
        // must recreate as something has been removed!
        recreateItems();
        return "List";
    }

    private void performDestroy() {
        dataRemovalAudit.createDataRemovalAudit(getCurrent(), deletedMessage);
        try {
            dataService.remove(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActivityDataDeleted"));
            audit.createAudit("activityData/List", "Deleted : IdActivityData="+getCurrent().getIdActivityData());
        } catch (Exception e) {
            audit.createAudit("activityData/List", "Failed to deleted : IdActivityData="+getCurrent().getIdActivityData());
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    public String prepareBatchDestroy() {
        audit.createAudit("activityData/BatchDelete", "");
        if (filteredItems == null) {
            filteredItems = items;
        }
        return "BatchDelete";
    }
    
    public String batchDestory() {
        audit.createAudit("activityData/List", "Performed batch delete of ActivityData");
        performBatchDestroy();
        recreateItems();
        return "List";
    }
    
    private void performBatchDestroy() {
        if (filteredItems == null) {
            return;
        }
        
        for (ActivityData item : filteredItems) {
            dataRemovalAudit.createDataRemovalAudit(item, deletedMessage);
            try {
                dataService.remove(item);
                //getFacade().remove(item);
                JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ActivityDataDeleted"));
            } catch (Exception e) {
                JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
    private void recreateItems() {
        items = dataService.findAll();
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
    
    public AuditController getAudit() {    
        return audit;
    }
    public void setAudit(AuditController audit) {    
        this.audit = audit;    
    }

    public DataRemovalAuditController getDataRemovalAudit() {    
        return dataRemovalAudit;
    }
    public void setDataRemovalAudit(DataRemovalAuditController dataRemovalAudit) {    
        this.dataRemovalAudit = dataRemovalAudit;    
    }
}
