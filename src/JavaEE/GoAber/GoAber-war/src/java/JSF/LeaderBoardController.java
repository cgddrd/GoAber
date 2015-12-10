package JSF;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Team;
import GoAberDatabase.User;
import JSF.services.ActivityDataService;
import JSF.services.TeamService;
import JSF.services.UserService;
import ViewModel.GroupLeaderViewModel;
import ViewModel.ParticipantLeaderViewModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.ListDataModel;

/**
 *
 * @author samuel
 */
@ManagedBean(name="leaderBoardController")
@SessionScoped
public class LeaderBoardController {  
    @EJB
    private UserService userService;
    @EJB
    private TeamService teamService;
    @EJB
    private ActivityDataService dataService;
    private String viewUnit;
    private ListDataModel<GroupLeaderViewModel> groups;
    private ListDataModel<ParticipantLeaderViewModel> participants;
    
    /**
     * Creates a new instance of LeaderBoardController
     */
    public LeaderBoardController() {
    }
    
    public String prepareGroupLeaders(String unit) {
        viewUnit = unit;
        groups = getGroupLeaders();
        return "GroupLeaderBoard";
    }
    
    public String prepareParticipantLeaders(String unit) {
        viewUnit = unit;
        participants = getParticipantLeaders();
        return "ParticipantLeaderBoard";
    }
    
    /** Get a list of groups ordered by by who is winning.
     * @return the groups in order to highest activity data total
     */
    public ListDataModel<GroupLeaderViewModel> getGroupLeaders() {
        List<GroupLeaderViewModel> viewModels = createGroupLeaderViewModels(getViewUnit());
        viewModels = sortGroupLeaderViewModels(viewModels);
        return new ListDataModel(viewModels);
    }
    
    
    /** Get a list of users ordered by by who is winning.
     * @return the users in order of highest activity data total
     */
    public ListDataModel<ParticipantLeaderViewModel> getParticipantLeaders() {
        List<ParticipantLeaderViewModel> viewModels = createParticipantLeaderViewModels(getViewUnit());
        viewModels = sortParticipantLeaderViewModels(viewModels);
        return new ListDataModel(viewModels);
    }
    
    private List<ParticipantLeaderViewModel> createParticipantLeaderViewModels(String unit) {
        List<ParticipantLeaderViewModel> viewModels = new ArrayList<>();
        
        if(unit != null) {
            List<User> users = userService.findAll();
        
            for (User user : users) {
                List<ActivityData> data = dataService.findAllForUserWithUnit(user, unit);
                ParticipantLeaderViewModel viewModel = new ParticipantLeaderViewModel(user, data);
                viewModels.add(viewModel);
            }   
        }
        
        return viewModels;
    }
    
    private List<GroupLeaderViewModel> createGroupLeaderViewModels(String unit) {
        List<GroupLeaderViewModel> viewModels = new ArrayList<>();
        
        if(unit != null) {
            List<Team> teams = teamService.findAll();
        
            for (Team team : teams) {
                List<ActivityData> data = dataService.findAllForGroup(team, unit);
                GroupLeaderViewModel viewModel = new GroupLeaderViewModel(team.getName(), data);
                viewModels.add(viewModel);
            }   
        }
        
        return viewModels;
    }
    
    private List<ParticipantLeaderViewModel> sortParticipantLeaderViewModels(List<ParticipantLeaderViewModel> viewModels) {
        Collections.sort(viewModels, new Comparator<ParticipantLeaderViewModel>() {

            @Override
            public int compare(ParticipantLeaderViewModel user1, ParticipantLeaderViewModel user2) {
                // Note: negative sign so that items are sorted in descending order
                return -Double.compare(user1.getTotal(), user2.getTotal());
            }
        });
        
        return viewModels;
    }
    
    private List<GroupLeaderViewModel> sortGroupLeaderViewModels(List<GroupLeaderViewModel> viewModels) {
        Collections.sort(viewModels, new Comparator<GroupLeaderViewModel>() {

            @Override
            public int compare(GroupLeaderViewModel group1, GroupLeaderViewModel group2) {
                // Note: negative sign so that items are sorted in descending order
                return -Double.compare(group1.getTotal(), group2.getTotal());
            }
        });
        return viewModels;
    }

    /**
     * @return the items
     */
    public ListDataModel<GroupLeaderViewModel> getGroups() {
        return groups;
    }

    /**
     * @return the viewUnit
     */
    public String getViewUnit() {
        return viewUnit;
    }

    /**
     * @return the participants
     */
    public ListDataModel<ParticipantLeaderViewModel> getParticipants() {
        return participants;
    }
}