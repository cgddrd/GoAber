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

/**
 * Allows data retrieved be read from Jawbone
 *
 * @author helen
 */
@ManagedBean(name = "jawboneController")
@SessionScoped
public class JawboneApi extends DeviceApi{
    
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
       steps = jsonObject.getJsonObject("data").getJsonArray("items").getJsonObject(0).getJsonObject("details").getInt("steps");
       
       Date date = new Date(year, month, day);
       CategoryUnit categoryUnitId = new CategoryUnit();
       ActivityData activityData = new ActivityData(steps, new Date(), date, user, categoryUnitId);
       return activityData;
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
