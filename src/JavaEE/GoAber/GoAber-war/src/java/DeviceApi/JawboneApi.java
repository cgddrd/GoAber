/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import GoAberDatabase.ActivityData;
import GoAberDatabase.CategoryUnit;
import GoAberDatabase.User;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.json.JsonObject;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Allows data retrieved be read from Jawbone
 *
 * @author helen
 */
@ManagedBean(name = "jawboneController")
@SessionScoped
public class JawboneApi extends DeviceApi{
    private Date deviceDate = new Date();

    public Date getDeviceDate() {
        return deviceDate;
    }

    public void setDeviceDate(Date deviceDate) {
        this.deviceDate = deviceDate;
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
        return JawboneApi.class;
    }
    
    /**
     * 
     * @param day
     * @param month
     * @param year
     * @param userID
     * @return 
     */
    public ActivityData getWalkingSteps(int day, int month, int year, int userID)
    {
        String url = "/moves?date=" + year + month + day;
        User user = userFacade.findUserById(userID);
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
    
    
     public String getWalkingStepsForEnteredDate()
    {
        int day = deviceDate.getDate();
        int month = deviceDate.getMonth()+1;
        int year = deviceDate.getYear()+1900;
        User user = authService.getActiveUser();
        if(user == null)
        { //should not be able to reach here without being logged in, but just in case
            return "";
        }
        int userID = user.getIdUser();
        ActivityData activityData = getWalkingSteps(day, month, year, userID);
        this.steps = activityData.getValue();
        return "";
    }
     
    /**
     * Method for testing steps can be retrieved from jawbone
     * @return 
     */
    public String getWalkingSteps()
    {
        int day = 26;
        int month = 10;
        int year = 2015;
        User user = authService.getActiveUser();
        if(user == null)
        {
            return "index";
        }
        int userID = user.getIdUser();
        ActivityData activityData = getWalkingSteps(day, month, year, userID);
        this.steps = activityData.getValue();
        return "ViewActivity";
    }
}
