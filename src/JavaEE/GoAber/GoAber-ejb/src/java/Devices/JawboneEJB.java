/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Devices;

import Devices.DeviceApi;
//import DeviceApi.JawboneApi;
import Devices.DeviceApi;
import GoAberDatabase.ActivityData;
import GoAberDatabase.CategoryUnit;
import GoAberDatabase.User;
import SessionBean.ActivityDataFacade;
import SessionBean.CategoryUnitFacade;
import SessionBean.DeviceFacade;
import SessionBean.DeviceTypeFacade;
import SessionBean.UserFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.json.JsonObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Dan
 * 
 */
@Stateless
public class JawboneEJB extends DeviceApi implements Serializable {

    
        @EJB DeviceTypeFacade deviceTypeFacade; //= lookupDeviceTypeFacadeBean();
        @EJB DeviceFacade deviceFacade; //= lookupDeviceFacadeBean();
        @EJB ActivityDataFacade activityDataFacade; //= lookupActivityDataFacadeBean();
        @EJB CategoryUnitFacade categoryUnitFacade; //= lookupCategoryUnitFacadeBean();
        @EJB SessionBean.UserFacade userFacade;
        
        private Date deviceDate = new Date();
        
        public JawboneEJB() {
            
        }
    
        public ActivityData getWalkingSteps(int day, int month, int year, User user)
    {
        String url = "/moves?date=" + year + month + day;
        //User user = userFacade.findUserById(userID);
        if(user == null)
        {
           return null;
        }
        JsonObject jsonObject = getActivityData(url, day, month, year, user);
        try
        {
            steps = jsonObject.getJsonObject("data").getJsonArray("items").getJsonObject(0).getJsonObject("details").getInt("steps");
        }
        catch(IndexOutOfBoundsException ex)
        { // indexOutOfBounds will be thrown when the user has done 0 steps
            steps = 0;
        }
       Date date = new Date(year, month, day);
       CategoryUnit categoryUnitId = new CategoryUnit();
       ActivityData activityData = new ActivityData(steps, new Date(), date, user, categoryUnitId);
       return activityData;
    }
    
    
     public String getWalkingStepsForEnteredDate(User user)
    {
        int day = deviceDate.getDate();
        int month = deviceDate.getMonth()+1;
        int year = deviceDate.getYear()+1900;
        //User user = authService.getActiveUser();
        if(user == null)
        { //should not be able to reach here without being logged in, but just in case
            return "";
        }
        int userID = user.getIdUser();
        ActivityData activityData = getWalkingSteps(day, month, year, user);
        this.steps = activityData.getValue();
        return "";
    }
     
    /**
     * Method for testing steps can be retrieved from jawbone
     * @return 
     */
    public String getWalkingSteps(User user)
    {
        int day = 26;
        int month = 10;
        int year = 2015;
        if(user == null)
        {
            return "index";
        }
        int userID = user.getIdUser();
        ActivityData activityData = getWalkingSteps(day, month, year, user);
        this.steps = activityData.getValue();
        return "ViewActivity";
    }
    
    @Override
    public DeviceTypeFacade getDeviceTypeFacade() {
        if (deviceTypeFacade == null) {
            deviceTypeFacade = lookupBean(DeviceTypeFacade.class);
        }
        return deviceTypeFacade;
    }

    @Override
    public DeviceFacade getDeviceFacade() {
        if (deviceFacade == null) {
            deviceFacade = lookupBean(DeviceFacade.class);
        }
        return deviceFacade;
    }

    @Override
    public ActivityDataFacade getActivityDataFacade() {
        if (activityDataFacade == null) {
            activityDataFacade = lookupBean(ActivityDataFacade.class);
        }
        return activityDataFacade;
    }

    @Override
    public CategoryUnitFacade getCategoryUnitFacade() {
        if (categoryUnitFacade == null) {
            categoryUnitFacade = lookupBean(CategoryUnitFacade.class);
        }
        return categoryUnitFacade;
    }

    @Override
    public String getType() {
        return "Jawbone";
    }

    @Override
    public String getScope() {
        return "move_read basic_read heartrate_read";
    }

    @Override
    public Class getProviderClass() {
        return JawboneEJB.class;
    }

    public Date getDeviceDate() {
        return deviceDate;
    }

    public void setDeviceTypeFacade(DeviceTypeFacade deviceTypeFacade) {
        this.deviceTypeFacade = deviceTypeFacade;
    }

    public void setDeviceFacade(DeviceFacade deviceFacade) {
        this.deviceFacade = deviceFacade;
    }

    public void setActivityDataFacade(ActivityDataFacade activityDataFacade) {
        this.activityDataFacade = activityDataFacade;
    }

    public void setCategoryUnitFacade(CategoryUnitFacade categoryUnitFacade) {
        this.categoryUnitFacade = categoryUnitFacade;
    }

    public void setDeviceDate(Date deviceDate) {
        this.deviceDate = deviceDate;
    }
    
    
    private <T> T lookupBean(Class<T> clazz) {
          try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return (T) ejbCtx.lookup(clazz.getSimpleName());
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    @Override
    public UserFacade getUserFacade() {
        if (userFacade == null) {
            userFacade = lookupBean(UserFacade.class);
        }
        return userFacade;
    }
    


}
