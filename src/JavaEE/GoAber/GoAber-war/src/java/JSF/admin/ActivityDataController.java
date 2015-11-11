package JSF.admin;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Category;
import GoAberDatabase.Unit;
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
    private SessionBean.ActivityDataFacade ejbFacade;
    @EJB
    private SessionBean.CategoryFacade categoryBean;
    @EJB
    private SessionBean.UnitFacade unitBean;
    private ActivityData current;
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

    private ActivityDataFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateItems();
        return "List";
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
            getCurrent().setLastUpdated(new Date());
            getFacade().create(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/AdminBundle").getString("ActivityDataCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/AdminBundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(ActivityData data) {
        current = data;
        return "Edit";
    }

    public String update() {
        try {
            getCurrent().setLastUpdated(new Date());
            getFacade().edit(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/AdminBundle").getString("ActivityDataUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/AdminBundle").getString("PersistenceErrorOccured"));
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
        return "List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/AdminBundle").getString("ActivityDataDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/AdminBundle").getString("PersistenceErrorOccured"));
        }
    }
    
    private void recreateItems() {
        items = getFacade().findAll();
        //filteredItems = null;
    }
    
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
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
            if (object instanceof ActivityData) {
                ActivityData o = (ActivityData) object;
                return getStringKey(o.getIdActivityData());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ActivityData.class.getName());
            }
        }
    }

}
