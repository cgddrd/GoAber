package JSF;

import GoAberDatabase.Challenge;
import GoAberDatabase.User;
import JSF.services.AuthService;
import JSF.util.JsfUtil;
import SessionBean.ChallengeFacade;
import WebServices.ChallengeService;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;


@ManagedBean(name="challengeController")
@SessionScoped
public class ChallengeController implements Serializable {

    private Challenge current;
    @EJB private SessionBean.ChallengeFacade ejbFacade;

    
    private Map<String,Integer> communitiesValue = new LinkedHashMap<>();
    private Integer[] communityChallenges;
    
    private Map<String,Integer> groupValue = new LinkedHashMap<>();
    private Integer[] groupChallenges;
    
    private List<Challenge> challenges;
    
    private ChallengeService challengeService = new ChallengeService();
    
 
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
            //items = getPaginationChallenges().createPageDataModel();
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
            challenges = challengeService.getUnEnteredCommunityChalleneges(currentUser);//getFacade().getUnEnteredCommunityChalleneges(currentUser);
            //items = getPaginationChallenges().createPageDataModel();
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
            challenges = challengeService.getEnteredCommunityChalleneges(currentUser);//getFacade().getEnteredCommunityChalleneges(currentUser);
            //items = getPaginationChallenges().createPageDataModel();
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
            getFacade().create(current);
            User currentUser = auth.getActiveUser();
            current.setCommunityChallengeCollection(challengeService.addCommunityChallenges(currentUser, current, communityChallenges));
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ChallengeCreated"));
            return prepareIndex();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }
    public String createGroup() {
        try {
            getFacade().create(current);
            User currentUser = auth.getActiveUser();
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

    private void recreateModel() {
        challenges = null;
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

    
    public AuthService getAuth() {    
        return auth;
    }


    public void setAuth(AuthService auth) {    
        this.auth = auth;    
    }
}
