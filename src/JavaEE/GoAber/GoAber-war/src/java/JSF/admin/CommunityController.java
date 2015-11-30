package JSF.admin;

import GoAberDatabase.Community;
import JSF.AuditController;
import JSF.util.JsfUtil;
import SessionBean.CommunityFacade;

import java.io.Serializable;
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

@ManagedBean(name = "adminCommunityController")
@SessionScoped
public class CommunityController implements Serializable {

    @EJB
    private SessionBean.CommunityFacade ejbFacade;
    private Community current;
    private List<Community> items = null;
    private List<Community> filteredItems = null;

    @ManagedProperty(value="#{auditController}")
    AuditController audit;
    
    public CommunityController() {
    }
    
    @PostConstruct
    public void init() {
        recreateItems();
    }

    public Community getSelected() {
        if (getCurrent() == null) {
            setCurrent(new Community());
        }
        return getCurrent();
    }

    private CommunityFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        audit.createAudit("community/List", "");
        recreateItems();
        return "List";
    }

    public String prepareView(Community item) {
        audit.createAudit("community/View", "IdCommunity="+item.getIdCommunity());
        current = item;
        return "View";
    }

    public String prepareCreate() {
        audit.createAudit("community/Create", "");
        setCurrent(new Community());
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CommunityCreated"));
            audit.createAudit("community/Create", "Created: IdCommunity="+getCurrent().getIdCommunity());
            return prepareCreate();
        } catch (Exception e) {
            audit.createAudit("community/Create", "Creation failed");
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Community data) {
        audit.createAudit("community/Edit", "IdCommunity="+data.getIdCommunity());
        current = data;
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CommunityUpdated"));
            audit.createAudit("community/View", "Updated : IdCommunity="+getCurrent().getIdCommunity());
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareDestroy(Community data) {
        audit.createAudit("community/Delete", "IdCommunity="+data.getIdCommunity());
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CommunityDeleted"));
            audit.createAudit("community/List", "Deleted : IdCommunity="+getCurrent().getIdCommunity());
        } catch (Exception e) {
            audit.createAudit("community/List", "Delete failed : IdCommunity="+getCurrent().getIdCommunity());
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    private void recreateItems() {
        items = getFacade().findAll();
        filteredItems = null;
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
    public List<Community> getFilteredItems() {
        return filteredItems;
    }

    /**
     * @param filteredItems the filteredItems to set
     */
    public void setFilteredItems(List<Community> filteredItems) {
        this.filteredItems = filteredItems;
    }

    /**
     * @return the items
     */
    public List<Community> getItems() {
        recreateItems();
        return items;
    }

    /**
     * @return the current
     */
    public Community getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(Community current) {
        this.current = current;
    }
        
    public AuditController getAudit() {    
        return audit;
    }
    public void setAudit(AuditController audit) {    
        this.audit = audit;    
    }
    
    @FacesConverter(value="adminCommunityConverter", forClass=Community.class)
    public static class CommunityControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CommunityController controller = (CommunityController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "adminCommunityController");
            
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
            if (object instanceof Community) {
                Community o = (Community) object;
                return getStringKey(o.getIdCommunity());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+Community.class.getName());
            }
        }

    }
}
