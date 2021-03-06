package JSF;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Challenge;
import GoAberDatabase.Community;
import GoAberDatabase.CommunityChallenge;
import GoAberDatabase.GroupChallenge;
import GoAberDatabase.Team;
import GoAberDatabase.Unit;
import GoAberDatabase.User;
import GoAberDatabase.UserChallenge;
import JSF.services.ActivityDataService;
import JSF.services.AuthService;
import JSF.util.JsfUtil;
import SessionBean.ChallengeFacade;
import ViewModel.LeaderItemViewModel;
import ViewModel.LeaderViewModel;
import WebServices.ChallengeService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;


@ManagedBean(name="challengeController")
@SessionScoped
public class ChallengeController implements Serializable {
    @EJB
    private ActivityDataService dataService;

    @EJB
    private ChallengeService challengeService;

    private Challenge current;
    
    @EJB private SessionBean.ChallengeFacade ejbFacade;

    
    private Map<String,Integer> communitiesValue = new LinkedHashMap<>();
    private Integer[] communityChallenges;
    
    private Map<String,Integer> groupValue = new LinkedHashMap<>();
    private Integer[] groupChallenges;
    
    private List<Challenge> challenges;
    

    private LeaderViewModel leaderViewModel;    

    
 
    @ManagedProperty(value="#{authService}")
    private AuthService auth;
 

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
        }
        return current;
    }

    private ChallengeFacade getFacade() {
        return ejbFacade;
    }

    
    public List<Challenge> getUnEnteredGroupChallenges() {
        if (challenges == null) {
            User currentUser = auth.getActiveUser();
            challenges = challengeService.getUnEnteredGroupChalleneges(currentUser);
        }
        return challenges;
    }
 
    public String prepareUnEnteredGroup() {
        recreateModel();
        return "NotEnteredGroup";
    }
    
    public List<Challenge> getUnEnteredCommunityChallenges() {
        if (challenges == null) {
            User currentUser = auth.getActiveUser();
            challenges = challengeService.getUnEnteredCommunityChalleneges(currentUser);
        }
        return challenges;
    }

    
    public String prepareUnEnteredCommunity() {
        recreateModel();
        return "NotEnteredCommunity";
    }
    
    public List<Challenge> getEnteredGroupChallenges() {
        if (challenges == null) {
            User currentUser = auth.getActiveUser();
            challenges = challengeService.getEnteredGroupChalleneges(currentUser);
        }
        return challenges;
    }

    
    public String prepareEnteredGroup() {
        recreateModel();
        return "EnteredGroup";
    }
    
    public List<Challenge> getEnteredCommunityChallenges() {
        if (challenges == null) {
            User currentUser = auth.getActiveUser();
            challenges = challengeService.getEnteredCommunityChalleneges(currentUser);
        }
        return challenges;
    }

    
    public String prepareEnteredCommunity() {
        recreateModel();
        return "EnteredCommunity";
    }
    
    
    public void loadPage(){
        recreateModel();
    }
    
    public String enterChallege(Challenge data) {
        current = data;
        User currentUser = auth.getActiveUser();
        challengeService.enterChallege(current, currentUser);
        return "EnteredCommunity";
    }
    
    public String leaveChallege(Challenge data) {
        current = data;
        User currentUser = auth.getActiveUser();
        challengeService.leaveChallege(current, currentUser);
        return "NotEnteredCommunity";
    }
    

   
    public String prepareCommunityCreate() {
        current = new Challenge();
        return "CreateCommunity";
    }
    public String prepareGroupCreate() {
        current = new Challenge();
        return "CreateGroup";
    }
    
    public String prepareIndex(){
        recreateModel();
        return "Index";
    }

    public String createCommunity() {
        try {
            //current.setComplete(false);
            User currentUser = auth.getActiveUser();
            current.setIdChallenge(challengeService.createChallengeID());
            getFacade().create(current);
            current.setCommunityChallengeCollection(challengeService.addCommunityChallenges(currentUser, current, communityChallenges, true));
            getFacade().edit(current);
            challengeService.AddChallengeJob(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ChallengeCreated"));
            return prepareIndex();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
   
    public String createGroup() {
        try {
            //current.setComplete(false);
            User currentUser = auth.getActiveUser();
            current.setIdChallenge(challengeService.createChallengeID());
            getFacade().create(current);
            current.setGroupChallengeCollection(challengeService.addGroupChallenges(currentUser, current, groupChallenges));
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ChallengeCreated"));
            return prepareIndex();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
     

    public String prepareEdit(Challenge data) {
        current = data;
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ChallengeUpdated"));
            return "Index";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Challenge data) {
        current = data;
        performDestroy();
        recreateModel();
        return "";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        
        return "";
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ChallengeDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

	/**
     * Get all challenges. Used in coordinator view
     * @return 
     */
    public List<Challenge> getItems() {
        if (challenges == null) {
            challenges = getFacade().findAll();
        }
        return challenges;
       
    }

    public String prepareGroupLeaderBoard(Challenge challenge) {
        List<LeaderItemViewModel> viewModels = new ArrayList<>();
        for (GroupChallenge tc : challenge.getGroupChallengeCollection()) {
            Team team = tc.getGroupId();
            Unit unit = challenge.getCategoryUnitId().getUnitId();
            
            List<ActivityData> data = new ArrayList<>();
            for(UserChallenge uc : challenge.getUserChallengeCollection()) {
                User user = uc.getUserId();
                List<ActivityData> sublist = dataService.findAllForGroupInDateRange(team, user, unit, challenge.getStartTime(), challenge.getEndTime());
                data.addAll(sublist);
            }
            
            
            LeaderItemViewModel model = new LeaderItemViewModel(team.getName(), data);
            viewModels.add(model);
        }
        
        viewModels = sortLeaderViewModels(viewModels);
        leaderViewModel = new LeaderViewModel();
        leaderViewModel.setItems(new ListDataModel(viewModels));
        leaderViewModel.setUnit(challenge.getCategoryUnitId().getUnitId());
        return "LeaderBoard";
    }
    
    public String prepareCommunityLeaderBoard(Challenge challenge) {
        List<LeaderItemViewModel> viewModels = new ArrayList<>();
        for (CommunityChallenge cc : challenge.getCommunityChallengeCollection()) {
            Community community = cc.getCommunityId();
            Unit unit = challenge.getCategoryUnitId().getUnitId();
           
            List<ActivityData> data = new ArrayList<>();
            for(UserChallenge uc : challenge.getUserChallengeCollection()) {
                User user = uc.getUserId();
                List<ActivityData> sublist = dataService.findAllForCommunityInDateRange(community, user, unit, challenge.getStartTime(), challenge.getEndTime());
                data.addAll(sublist);
            }
            
            LeaderItemViewModel model = new LeaderItemViewModel(community.getName(), data);
            viewModels.add(model);
        }
        
        viewModels = sortLeaderViewModels(viewModels);
        leaderViewModel = new LeaderViewModel();
        leaderViewModel.setItems(new ListDataModel(viewModels));
        leaderViewModel.setUnit(challenge.getCategoryUnitId().getUnitId());
        return "LeaderBoard";
    }
    
    private List<LeaderItemViewModel> sortLeaderViewModels(List<LeaderItemViewModel> viewModels) {
        Collections.sort(viewModels, new Comparator<LeaderItemViewModel>() {

            @Override
            public int compare(LeaderItemViewModel model1, LeaderItemViewModel model2) {
                // Note: negative sign so that items are sorted in descending order
                return -Double.compare(model1.getTotal(), model2.getTotal());
            }
        });
        
        return viewModels;
    }
    
   

    private void recreateModel() {
        challenges = null;
    }

   

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    /**
     * @return the leaderViewModels
     */
    public LeaderViewModel getLeaderViewModel() {
        return leaderViewModel;
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
