
package JSF.services;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Challenge;
import GoAberDatabase.GroupChallenge;
import GoAberDatabase.Team;
import GoAberDatabase.Unit;
import GoAberDatabase.User;
import GoAberDatabase.UserChallenge;
import SessionBean.UserFacade;
import ViewModel.LeaderItemViewModel;
import ViewModel.LeaderViewModel;
import ViewModel.ParticipantLeaderViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.model.ListDataModel;

/** User Service class.
 * 
 * This provides access to the common functions used to get information about 
 * users. This is different from the auth controller which deals with 
 * authentication of the current user.
 *
 * @author samuel
 */
@Stateless
@LocalBean
public class UserService {
    @EJB
     UserFacade ejbFacade;
    @EJB
     private ActivityDataService dataService;
    
    /** Get all users in the database
     * 
     * @return list of activity data items
     */
    public List<User> findAll() {
        return getFacade().findAll();
    }
    
    public List<ParticipantLeaderViewModel> findGroupMembers(User user) {
        List<ParticipantLeaderViewModel> viewModels = new ArrayList<>();
        List<User> users = getFacade().findUsersByTeam(user.getGroupId().getIdGroup());

        for(User teamMember : users) {
            List<ActivityData> data = getDataService().findAll();
            ParticipantLeaderViewModel  model = new ParticipantLeaderViewModel(teamMember, data);
            viewModels.add(model);
        }
       
        return viewModels;
    }
    
    /** Get a user by their id.
     * 
     * @param id the id of the user
     * @return the user object for the given id.
     */
    public User getUserById(int id) {
        return getFacade().find(id);
    }

    private UserFacade getFacade() {
        return ejbFacade;
    }

    /**
     * @return the dataService
     */
    public ActivityDataService getDataService() {
        return dataService;
    }

    /**
     * @param dataService the dataService to set
     */
    public void setDataService(ActivityDataService dataService) {
        this.dataService = dataService;
    }
}
