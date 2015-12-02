/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.services;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Team;
import GoAberDatabase.User;
import JSF.util.DateUtils;
import SessionBean.ActivityDataFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import JSF.ViewModels.StatisticsSummary;
import SessionBean.TeamFacade;

/**Team Service Class.
 *
 * Provides a common interface for CRUD operations on Teams
 * 
 * @author samuel
 */
@Stateless
@LocalBean
public class TeamService {
    @EJB
    TeamFacade ejbFacade;
    
    /** Get all teams in the database
     * 
     * @return list of activity data items
     */
    public List<Team> findAll() {
        return getFacade().findAll();
    }
      
    /** Get a single team by id
     * 
     * @param id the id of the item
     * @return the matching team item or null
     */
    public Team findById(int id) {
        return getFacade().find(id);
    }
    
    private TeamFacade getFacade() {
        return ejbFacade;
    }
}
