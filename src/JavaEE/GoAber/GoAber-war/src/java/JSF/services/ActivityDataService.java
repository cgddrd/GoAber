/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.services;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Community;
import GoAberDatabase.Team;
import GoAberDatabase.Unit;
import GoAberDatabase.User;
import JSF.util.DateUtils;
import SessionBean.ActivityDataFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import JSF.ViewModels.StatisticsSummary;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    ActivityDataFacade ejbFacade;
    
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
        List<ActivityData> data = getFacade().getAllForUserInDateRange(user.getIdUser(), unit, startDate, endDate);
        HashMap<Date, List<ActivityData>> groups = groupActivityDataByDate(data);
        return combineGroupsByDate(groups);
    }
    
    /** Group activity data by date.
     * 
     * It is possible to have multiple activity data for a single day. This
     * groups data items on the same day into a HashMap with the date as the 
     * key.
     * 
     * @param data the list of data to group
     * @return HashMap containing lists of activity data for a day.
     */
    private HashMap<Date, List<ActivityData>> groupActivityDataByDate(List<ActivityData> data) {
        HashMap<Date, List<ActivityData>> dateGroups = new HashMap<>();
        for (ActivityData item : data) {
            if (!dateGroups.containsKey(item.getDate())) {
                // add a new date to the hashmap
                List<ActivityData> sublist = new ArrayList<>();
                sublist.add(item);
                dateGroups.put(item.getDate(), sublist);
            } else {
                // we have mutliple records matching this date
                dateGroups.get(item.getDate()).add(item);
            }
        }
        return dateGroups;
    }
    
    /** Combine grouped items into a single record for the day.
     * 
     * Convert a HashMap of grouped items for a single date into a single 
     * activity data record.
     * 
     * @param groups the HashMap groups for each day
     * @return List with one activity data item for each day.
     */ 
    private List<ActivityData> combineGroupsByDate(HashMap<Date, List<ActivityData>> groups) {
        List<ActivityData> combinedData = new ArrayList<>();
        
        for (Map.Entry pair : groups.entrySet()) {
            List<ActivityData> sublist = (List<ActivityData>) pair.getValue();
            
            // sum each item with the same date
            int total = 0;
            for (ActivityData item : sublist) {
                total += item.getValue();
            }
            
            // Use one item to represent meta-data and set value to total
            ActivityData tmp = sublist.get(0);
            ActivityData squashedItem = new ActivityData(
                    total, 
                    tmp.getLastUpdated(), 
                    tmp.getDate(), 
                    tmp.getUserId(), 
                    tmp.getCategoryUnitId()
            );
            combinedData.add(squashedItem);
        }
        
        return combinedData;
    }
    
    /** Get all activity data matching a specific group.
     * 
     * @param group the team to find activity data for
     * @param unit the unit to find activity data for
     * @return list of activity data items
     */
    public List<ActivityData> findAllForGroup(Team group, String unit) {
        return getFacade().getAllForGroup(group.getIdGroup(), unit);
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

    public List<ActivityData> findAllForGroupInDateRange(Team team, User user, Unit unit, Date startDate, Date endDate) {
        return getFacade().getAllForGroupInDateRange(team.getIdGroup(), user.getIdUser(), unit.getName(), startDate, endDate);
    }

    public List<ActivityData> findAllForCommunityInDateRange(Community community, User user, Unit unit, Date startDate, Date endDate) {
        return getFacade().getAllForCommunityInDateRange(community.getIdCommunity(), user.getIdUser(), unit.getName(), startDate, endDate);
    }
}
