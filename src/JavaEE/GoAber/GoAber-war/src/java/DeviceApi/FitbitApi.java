/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import GoAberDatabase.ActivityData;
import GoAberDatabase.CategoryUnit;
import GoAberDatabase.DeviceType;
import GoAberDatabase.User;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.json.JsonObject;

/**
 *
 * @author helen
 */
@ManagedBean(name = "fitbitController")
@SessionScoped
public class FitbitApi extends DeviceApi{

    @Override
    public String getType() {
        return "Fitbit";
    }

    @Override
    public String getScope() {
        return "activity";
    }

    @Override
    public Class getProviderClass() {
        return FitbitApi.class;
    }
    
    @Override
    public String getAccessTokenEndpoint() {
        DeviceType deviceType = deviceTypeFacade.findByName(getType());
        return deviceType.getTokenEndpoint();
    }
    
    public ActivityData getWalkingSteps(int day, int month, int year, int userID)
    {
       String url = "/activity?date=" + year + month + day;
       User user = userFacade.findUserById(userID);
       if(user == null)
           return null;
       JsonObject jsonObject = getActivityData(url, day, month, year, user);
       steps = jsonObject.getJsonObject("data").getJsonArray("items").getJsonObject(0).getJsonObject("details").getInt("steps");
       System.out.println("Value = " + steps);
       
       Date date = new Date(year, month, day);
       CategoryUnit categoryUnitId = new CategoryUnit();
       ActivityData activityData = new ActivityData(steps, new Date(), date, user, categoryUnitId);
       return activityData;
    }
    
    public String getWalkingSteps()
    {
        int day = 26;
        int month = 10;
        int year = 2015;
        User user = authController.getActiveUser();
        if(user == null)
            return "index";
        int userID = user.getIdUser();
        ActivityData activityData = getWalkingSteps(day, month, year, userID);
        this.steps = activityData.getValue();
        return "ViewActivity";
    }
}
