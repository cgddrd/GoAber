package JSF;

import GoAberDatabase.Challenge;
import GoAberDatabase.User;
import JSF.services.AuthService;
import JSF.util.JsfUtil;
import JSF.util.PaginationHelper;
import SessionBean.ChallengeFacade;
import WebServices.ChallengeService;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;
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


@ManagedBean(name="challengeController")
@SessionScoped
public class ChallengeController implements Serializable {

    private Challenge current;
    private DataModel items = null;
    @EJB private SessionBean.ChallengeFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    private Map<String,Integer> communitiesValue = new LinkedHashMap<>();
    private Integer[] communityChallenges;
    
    private Map<String,Integer> groupValue = new LinkedHashMap<>();
    private Integer[] groupChallenges;
    
    List<Challenge> challenges;
    
    ChallengeService challengeService = new ChallengeService();
    
    @EJB 
    SessionBean.UserFacade userFacade;
    @ManagedProperty(value="#{authService}")
    AuthService auth;
    
    
    
    
    public ChallengeController() {
    }
    
    public Integer[] getCommunityChallenges() {
        return communityChallenges;
    }

    public void setCommunityChallenges(Integer[] communityChallenges) {
        this.communityChallenges = communityChallenges;
    }
	
    public Map<String,Integer> getCommunitiesValue() {
        communitiesValue = challengeService.getCommunitiesValue();
        return communitiesValue;
    }
    
    public Integer[] getGroupChallenges() {
        return groupChallenges;
    }

    public void setGroupChallenges(Integer[] groupChallenges) {
        this.groupChallenges = groupChallenges;
    }
	
    public Map<String,Integer> getGroupValue() {
        groupValue = challengeService.getGroupValue(auth.getActiveUser());
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
    /*
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
    }*/
    
    
    public PaginationHelper getPaginationChallenges() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {
                @Override
                public int getItemsCount() { return challenges.size(); }

                @Override
                public DataModel createPageDataModel() {
                    List<Challenge> list = challenges;
                    if (challenges.size() > 10){
                        list = challenges.subList(getPageFirstItem(), getPageFirstItem()+getPageSize());
                    }
                    return new ListDataModel(list);
                }
            };
        }
        return pagination;
    }
    
    public DataModel getUnEnteredGroupChallenges() {
        if (items == null) {
            User currentUser = auth.getActiveUser();
            challenges = challengeService.getUnEnteredGroupChalleneges(currentUser);
            items = getPaginationChallenges().createPageDataModel();
        }
        return items;
    }
    
    public String nextUnEnteredGroupChallenges() {
        getPaginationChallenges().nextPage();
        recreateModel();
        return "NotEnteredGroup";
    }

    public String previousUnEnteredGroupChallenges() {
        getPaginationChallenges().previousPage();
        recreateModel();
        return "NotEnteredGroup";
    }
    public String prepareUnEnteredGroup() {
        recreateModel();
        return "NotEnteredGroup";
    }
    
    public DataModel getUnEnteredCommunityChallenges() {
        if (items == null) {
            User currentUser = auth.getActiveUser();
            challenges = challengeService.getUnEnteredCommunityChalleneges(currentUser);//getFacade().getUnEnteredCommunityChalleneges(currentUser);
            items = getPaginationChallenges().createPageDataModel();
        }
        return items;
    }
    
    public String nextUnEnteredCommunityChallenges() {
        getPaginationChallenges().nextPage();
        recreateModel();
        return "NotEnteredCommunity";
    }

    public String previousUnEnteredCommunityChallenges() {
        getPaginationChallenges().previousPage();
        recreateModel();
        return "NotEnteredCommunity";
    }
    
    public String prepareUnEnteredCommunity() {
        recreateModel();
        return "NotEnteredCommunity";
    }
    
    public DataModel getEnteredGroupChallenges() {
        if (items == null) {
            User currentUser = auth.getActiveUser();
            challenges = challengeService.getEnteredGroupChalleneges(currentUser);//getFacade().getEnteredGroupChalleneges(currentUser);
            items = getPaginationChallenges().createPageDataModel();
        }
        return items;
    }
    
    public String nextEnteredGroupChallenges() {
        getPaginationChallenges().nextPage();
        recreateModel();
        return "EnteredGroup";
    }

    public String previousEnteredGroupChallenges() {
        getPaginationChallenges().previousPage();
        recreateModel();
        return "EnteredGroup";
    }
    
    public String prepareEnteredGroup() {
        recreateModel();
        return "EnteredGroup";
    }
    
    public DataModel getEnteredCommunityChallenges() {
        if (items == null) {
            User currentUser = auth.getActiveUser();
            challenges = challengeService.getEnteredCommunityChalleneges(currentUser);//getFacade().getEnteredCommunityChalleneges(currentUser);
            items = getPaginationChallenges().createPageDataModel();
        }
        return items;
    }
    
    public String nextEnteredCommunityChallenges() {
        getPaginationChallenges().nextPage();
        recreateModel();
        return "EnteredCommunity";
    }

    public String previousEnteredCommunityChallenges() {
        getPaginationChallenges().previousPage();
        recreateModel();
        return "EnteredCommunity";
    }
    
    public String prepareEnteredCommunity() {
        recreateModel();
        return "EnteredCommunity";
    }
    
    
    public void loadPage(){
        recreateModel();
    }
    
    public String enterChallege() {
        current = (Challenge)items.getRowData();
        User currentUser = auth.getActiveUser();
        challengeService.enterChallege(current, currentUser);
        return "EnteredCommunity";
    }
    
    public String leaveChallege() {
        current = (Challenge)items.getRowData();
        User currentUser = auth.getActiveUser();
        challengeService.leaveChallege(current, currentUser);
        return "NotEnteredCommunity";
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
            current.setIdChallenge(UUID.randomUUID().toString());
            getFacade().create(current);
            User currentUser = auth.getActiveUser();
            current.setGroupChallengeCollection(challengeService.addGroupChallenges(currentUser, current, groupChallenges));
            current.setCommunityChallengeCollection(challengeService.addCommunityChallenges(currentUser, current, communityChallenges));
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ChallengeCreated"));
            return prepareIndex();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
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
            challenges = getFacade().findAll();
            items = getPaginationChallenges().createPageDataModel();
        }
        return items;
       
    }

    private void recreateModel() {
        items = null;
        challenges = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPaginationChallenges().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPaginationChallenges().previousPage();
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

        String getStringKey(String value) {
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

    
    public AuthService getAuth() {    
        return auth;
    }


    public void setAuth(AuthService auth) {    
        this.auth = auth;    
    }
}
