package JSF.admin;

import GoAberDatabase.Community;
import GoAberDatabase.Team;
import JSF.util.JsfUtil;
import SessionBean.TeamFacade;

import java.io.Serializable;
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

@ManagedBean(name = "adminTeamController")
@SessionScoped
public class TeamController implements Serializable {

    @EJB
    private SessionBean.TeamFacade ejbFacade;
    @EJB
    private SessionBean.CommunityFacade communityBean;
    private Team current;
    private List<Team> items = null;
    private List<Team> filteredItems = null;
    private List<Community> communities;
    public TeamController() {
    }
    
    @PostConstruct
    public void init() {
        communities = communityBean.findAll();
        recreateItems();
    }

    public List<Community> getCommunities() {
        return communities;
    }
    
    public Team getSelected() {
        if (getCurrent() == null) {
            setCurrent(new Team());
        }
        return getCurrent();
    }

    private TeamFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        recreateItems();
        return "List";
    }

    public String prepareView(Team item) {
        current = item;
        return "View";
    }

    public String prepareCreate() {
        setCurrent(new Team());
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TeamCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Team data) {
        current = data;
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(getCurrent());
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TeamUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareDestroy(Team data) {
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TeamDeleted"));
        } catch (Exception e) {
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
    public List<Team> getFilteredItems() {
        return filteredItems;
    }

    /**
     * @param filteredItems the filteredItems to set
     */
    public void setFilteredItems(List<Team> filteredItems) {
        this.filteredItems = filteredItems;
    }

    /**
     * @return the items
     */
    public List<Team> getItems() {
        return items;
    }

    /**
     * @return the current
     */
    public Team getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(Team current) {
        this.current = current;
    }
    

    @FacesConverter(forClass = Team.class)
    public static class TeamControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TeamController controller = (TeamController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "teamController");
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
            if (object instanceof Team) {
                Team o = (Team) object;
                return getStringKey(o.getIdGroup());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Team.class.getName());
            }
        }

    }

}
