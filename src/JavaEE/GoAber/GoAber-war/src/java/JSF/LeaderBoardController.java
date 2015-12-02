/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Team;
import JSF.services.ActivityDataService;
import JSF.services.TeamService;
import ViewModel.GroupLeaderViewModel;
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
    private TeamService teamService;
    @EJB
    private ActivityDataService dataService;
    private String viewUnit;
    private ListDataModel<GroupLeaderViewModel> items;
    
    /**
     * Creates a new instance of LeaderBoardController
     */
    public LeaderBoardController() {
    }
    
    public String prepareGroupLeader(String unit) {
        viewUnit = unit;
        items = getGroupLeaders();
        return "GroupLeaderBoard";
    }
    
    /** Get a list of groups ordered by by who is winning.

     * @return the items
     */
    public ListDataModel<GroupLeaderViewModel> getGroupLeaders() {
        List<GroupLeaderViewModel> viewModels = createGroupLeaderViewModels(getViewUnit());
        viewModels = sortGroupLeaderViewModels(viewModels);
        return new ListDataModel(viewModels);
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
    public ListDataModel<GroupLeaderViewModel> getItems() {
        return items;
    }

    /**
     * @return the viewUnit
     */
    public String getViewUnit() {
        return viewUnit;
    }
}
