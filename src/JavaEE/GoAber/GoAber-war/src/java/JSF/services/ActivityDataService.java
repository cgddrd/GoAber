/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.services;

import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import SessionBean.ActivityDataFacade;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

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
    
    public ActivityData findById(int id) {
        return getFacade().find(id);
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
