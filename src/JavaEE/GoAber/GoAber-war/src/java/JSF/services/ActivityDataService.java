/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.services;

import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import JSF.util.DateUtils;
import SessionBean.ActivityDataFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import JSF.ViewModels.StatisticsSummary;

/**Activity Data Service Class.
 *
 * Provides a common interface for CRUD operations on Activity Data
 * 
 * @author samuel
 */
@Stateless
@LocalBean
public class ActivityDataService {
    @EJB
    SessionBean.ActivityDataFacade ejbFacade;
    
    /** Get all activity data items in the database
     * 
     * @return list of activity data items
     */
    public List<ActivityData> findAll() {
        return getFacade().findAll();
    }
    
    /** Get all activity data items for the specified user
     * 
     * @param user the user to find data for
     * @return list of activity data items
     */
    public List<ActivityData> findAllForUser(User user) {
        return getFacade().getAllForUser(user.getIdUser());
    }
    
    /** Get all activity data items for a user matching the specified unit
     * 
     * @param user the user to find data for
     * @param unit the unit to match
     * @return list of activity data items
     */
    public List<ActivityData> findAllForUserWithUnit(User user, String unit) {
        return getFacade().getAllForUserWithUnit(user.getIdUser(), unit);
    }
      
    /** Get a single activity data item by id
     * 
     * @param id the id of the item
     * @return the matching activity data item or null
     */
    public ActivityData findById(int id) {
        return getFacade().find(id);
    }
    
    /** Get all activity data items for a user matching the specified unit
     * between a given date range
     * 
     * @param user the user to find data for
     * @param unit the unit to match
     * @param startDate the lower bound of the date range
     * @param endDate the upper bound of the date range
     * @return list of activity data items
     */
    public List<ActivityData> findAllForUserInDateRange(User user, String unit, Date startDate, Date endDate) {
        return getFacade().getAllForUserInDateRange(user.getIdUser(), unit, startDate, endDate);
    }
    
    /** Get all activity data items for a user matching the specified unit
     * for the last 7 days from the current date
     * 
     * @param user the user to find data for
     * @param unit the unit to match
     * @return list of activity data items
     */
    public List<ActivityData> weeklySummary(User user, String unit) {
        Date startDate = DateUtils.getDateLastWeek();
        Date endDate = new Date();
        return findAllForUserInDateRange(user, unit, startDate, endDate);
    }
    
    /** Get a statistical summary of the last 7 days
     * 
     * @param user the user to find data for
     * @param unit the unit to match
     * @return a summary object for the 7 days
     */
    public StatisticsSummary weeklyStatistics(User user, String unit) {
        Date startDate = DateUtils.getDateLastWeek();
        Date endDate = new Date();
        return statisticsSummary(user, unit, startDate, endDate);
    }
    
    /** Get all activity data items for a user matching the specified unit
     * for the month from the current date
     * 
     * @param user the user to find data for
     * @param unit the unit to match
     * @return list of activity data items
     */
    public List<ActivityData> monthlySummary(User user, String unit) {
        Date startDate = DateUtils.getDateLastMonth();
        Date endDate = new Date();
        return findAllForUserInDateRange(user, unit, startDate, endDate);
    }
    
    /** Get a statistical summary of the last month
     * 
     * @param user the user to find data for
     * @param unit the unit to match
     * @return a summary object for the last month
     */  
    public StatisticsSummary monthlyStatistics(User user, String unit) {
        Date startDate = DateUtils.getDateLastMonth();
        Date endDate = new Date();
        return statisticsSummary(user, unit, startDate, endDate);
    }
    
    /** Get a statistical summary of all of the user's data
     * 
     * @param user the user to find data for
     * @param unit the unit to match
     * @return a summary object for all data held by the system.
     */
    public StatisticsSummary allTimeStatistics(User user, String unit) {
       List<ActivityData> data = findAllForUserWithUnit(user, unit);
       return new StatisticsSummary(data);
    }
    
    /** Get a statistical summary of all of the user's data.
     * 
     * This is simular to getting an all time summary, but without needing
     * the specific user object.
     * 
     * @param id the id of the user to find data for
     * @param unit the unit to match
     * @return a summary object for all data held by the system.
     */
    public StatisticsSummary userSummary(int id, String unit) {
       List<ActivityData> data = getFacade().getAllForUserWithUnit(id, unit);
       return new StatisticsSummary(data);
    }
    
    /** Get a statistical summary of a user's data for a particular unit over
     * a given time interval.
     * 
     * @param user the id of the user to find data for
     * @param unit the unit to match
     * @param startDate the lower bound of the date range
     * @param endDate the upper bound of the date range
     * @return a summary object for all data held by the system.
     */
    public StatisticsSummary statisticsSummary(User user, String unit, Date startDate, Date endDate) {
        List<ActivityData> data = findAllForUserInDateRange(user, unit, startDate, endDate);
        return new StatisticsSummary(data);
    }
    
    /** Create a new activity data item in the database for a given user
     * 
     * @param data the data item to create
     * @param user the user to create it for
     */
    public void createForUser(ActivityData data, User user) {
        data.setUserId(user);
        create(data);
    }
    
     /** Create a new activity data item in the database
     * 
     * @param data the data item to create
     */
    public void create(ActivityData data) {
        data.setLastUpdated(new Date());
        getFacade().create(data);
    }
    
    /** Update an existing activity data item in the database for a given user.
     * 
     * @param data the data item to update
     * @param user the user to update it for
     */
    public void updateForUser(ActivityData data, User user) {
        data.setUserId(user);
        update(data);
    }
    
    /** Update an existing activity data item in the database.
     * 
     * @param data the data item to update
     */
    public void update(ActivityData data) {
        data.setLastUpdated(new Date());
        getFacade().edit(data);
    }
    
    /** Remove an activity data item from the database.
     * 
     * @param data the data item to remove
     */
    public void remove(ActivityData data) {
        getFacade().remove(data);
    }
    
    private ActivityDataFacade getFacade() {
        return ejbFacade;
    }
}
