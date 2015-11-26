package JSF;

import GoAberDatabase.Challenge;
import GoAberDatabase.Community;
import GoAberDatabase.CommunityChallenge;
import GoAberDatabase.GroupChallenge;
import GoAberDatabase.Team;
import JSF.util.JsfUtil;
import JSF.util.PaginationHelper;
import SessionBean.CategoryUnitFacade;
import SessionBean.ChallengeFacade;
import SessionBean.CommunityChallengeFacade;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


@ManagedBean(name="challengeController")
@SessionScoped
public class ChallengeController implements Serializable {

    private Challenge current;
    private DataModel items = null;
    @EJB private SessionBean.ChallengeFacade ejbFacade;
    @EJB private SessionBean.CommunityChallengeFacade communityChallengeFacade;
    @EJB private SessionBean.GroupChallengeFacade groupChallengeFacade;
    @EJB private SessionBean.CommunityFacade communityFacade;
    @EJB private SessionBean.TeamFacade groupFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private Map<String,Integer> communitiesValue = new LinkedHashMap<>();
    private Integer[] communityChallenges;
    
    private Map<String,Integer> groupValue = new LinkedHashMap<>();
    private Integer[] groupChallenges;
    

    

    public ChallengeController() {
    }
    
   public Integer[] getCommunityChallenges() {
        return communityChallenges;
    }

    public void setCommunityChallenges(Integer[] communityChallenges) {
        this.communityChallenges = communityChallenges;
    }
	
    public Map<String,Integer> getCommunitiesValue() {
        List<Community> communities = communityFacade.findAll();
        communities.stream().forEach((community) -> {
            communitiesValue.put(community.getName(), community.getIdCommunity()); //label, value
        });
        return communitiesValue;
    }
    
    public Integer[] getGroupChallenges() {
        return groupChallenges;
    }

    public void setGroupChallenges(Integer[] groupChallenges) {
        this.groupChallenges = groupChallenges;
    }
	
    public Map<String,Integer> getGroupValue() {
        List<Team> groups = groupFacade.findAll();
        groups.stream().forEach((group) -> {
            groupValue.put(group.getName(), group.getIdGroup()); //label, value
        });
        return groupValue;
    }


    public Challenge getSelected() {
        if (current == null) {
            current = new Challenge();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ChallengeFacade getFacade() {
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
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem()+getPageSize()}));
                }
            };
        }
        return pagination;
    }
    
    
    public PaginationHelper getPagination(List<Challenge> challenges) {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(challenges.subList(getPageFirstItem(), getPageFirstItem()+getPageSize()));
                    //return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem()+getPageSize()}));
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
        current = (Challenge)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Challenge();
        selectedItemIndex = -1;
        return "Create";
    }
    
    public String prepareIndex(){
        recreateModel();
        return "Index";
    }

    public String create() {
        try {
            getFacade().create(current);
            addGroupChallenges();
            addCommunityChallenges();
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ChallengeCreated"));
            return prepareIndex();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    
    private void addGroupChallenges()
    {
        for (Integer groupId : groupChallenges)
        {
            Team group = groupFacade.findById(groupId);
            GroupChallenge groupChallenge = new GroupChallenge(group, current);
            groupChallengeFacade.create(groupChallenge);
        }
    }
    
    private void addCommunityChallenges()
    {
        for (Integer communityId : communityChallenges)
        {
            Community group = communityFacade.findById(communityId);
            CommunityChallenge communityChallenge = new CommunityChallenge(group, current);
            communityChallengeFacade.create(communityChallenge);
        }
    }

    public String prepareEdit() {
        current = (Challenge)getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ChallengeUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Challenge)getItems().getRowData();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ChallengeDeleted"));
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


    @FacesConverter(forClass=Challenge.class)
    public static class ChallengeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ChallengeController controller = (ChallengeController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "challengeController");
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
            if (object instanceof Challenge) {
                Challenge o = (Challenge) object;
                return getStringKey(o.getIdChallenge());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+Challenge.class.getName());
            }
        }

    }

}
