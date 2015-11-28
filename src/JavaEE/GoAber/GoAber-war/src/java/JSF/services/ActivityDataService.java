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

/** Provides a common interface for CRUD operations on ActivityData
 *
 * @author samuel
 */
@Stateless
@LocalBean
public class ActivityDataService {
    @EJB
    SessionBean.ActivityDataFacade ejbFacade;
    
    public List<ActivityData> findAll() {
        return getFacade().findAll();
    }
    
    public List<ActivityData> findAllForUser(User user) {
        return getFacade().getAllForUser(user.getIdUser());
    }
    
    public List<ActivityData> findAllForUserWithUnit(User user, String unit) {
        return getFacade().getAllForUserWithUnit(user.getIdUser(), unit);
    }
        
    public ActivityData findById(int id) {
        return getFacade().find(id);
    }
    
    public List<ActivityData> findAllForUserInDateRange(User user, String unit, Date startDate, Date endDate) {
        return getFacade().getAllForUserInDateRange(user.getIdUser(), unit, startDate, endDate);
    }
    
    public List<ActivityData> weeklySummary(User user, String unit) {
        Date startDate = DateUtils.getDateLastWeek();
        Date endDate = new Date();
        return findAllForUserInDateRange(user, unit, startDate, endDate);
    }
    
    public StatisticsSummary weeklyStatistics(User user, String unit) {
        Date startDate = DateUtils.getDateLastWeek();
        Date endDate = new Date();
        return statisticsSummary(user, unit, startDate, endDate);
    }
    
    public List<ActivityData> monthlySummary(User user, String unit) {
        Date startDate = DateUtils.getDateLastMonth();
        Date endDate = new Date();
        return findAllForUserInDateRange(user, unit, startDate, endDate);
    }
    
    public StatisticsSummary monthlyStatistics(User user, String unit) {
        Date startDate = DateUtils.getDateLastMonth();
        Date endDate = new Date();
        return statisticsSummary(user, unit, startDate, endDate);
    }
    
    public StatisticsSummary allTimeStatistics(User user, String unit) {
       List<ActivityData> data = findAllForUserWithUnit(user, unit);
       return new StatisticsSummary(data);
    }
    
    public StatisticsSummary userSummary(int id, String unit) {
       List<ActivityData> data = getFacade().getAllForUserWithUnit(id, unit);
       return new StatisticsSummary(data);
    }
    
    
    public StatisticsSummary statisticsSummary(User user, String unit, Date startDate, Date endDate) {
        List<ActivityData> data = findAllForUserInDateRange(user, unit, startDate, endDate);
        return new StatisticsSummary(data);
    }
    
    public void createForUser(ActivityData data, User user) {
        data.setUserId(user);
        create(data);
    }
    
    public void create(ActivityData data) {
        data.setLastUpdated(new Date());
        getFacade().create(data);
    }
    
    public void updateForUser(ActivityData data, User user) {
        data.setUserId(user);
        update(data);
    }
    
    public void update(ActivityData data) {
        data.setLastUpdated(new Date());
        getFacade().edit(data);
    }
    
    public void remove(ActivityData data) {
        getFacade().remove(data);
    }
    
    private ActivityDataFacade getFacade() {
        return ejbFacade;
    }
}
