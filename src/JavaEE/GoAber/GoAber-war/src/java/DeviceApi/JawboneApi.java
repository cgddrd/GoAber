/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import GoAberDatabase.ActivityData;
import GoAberDatabase.CategoryUnit;
import GoAberDatabase.User;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.json.JsonObject;
import javax.jws.WebService;
import org.scribe.oauth.OAuthService;

/**
 * 
 *
 * @author helen
 */
@ManagedBean(name = "jawboneController")
@SessionScoped
public class JawboneApi extends DeviceApi{
    int steps;
    
    public int getSteps()
    {
        return steps;
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
    
    public ActivityData getWalkingSteps(int day, int month, int year, int userID)
    {
       String url = "/moves?date=" + year + month + day;
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
        
    /*
    public void connectToJawbone()
    {
        deviceApi = new JawboneApi();
        
        OAuthService serviceBuilder = deviceApi.getOAuthService();
        try {
        /*String apiKey = "mCZQ7V2DbgQ";
        OAuthService serviceBuilder = new ServiceBuilder()
        .provider(DeviceApi.class)
        .apiKey(apiKey).apiSecret("07e4083f111f1a44ccba1bf94d21c95f5486f8f1")
        .callback("http://localhost:8080/GoAber-war/JawboneCallbackServlet")
        .build();*
        //HttpSession session = request.getSession();
        //session.setAttribute("DeviceApi", deviceApi);
        FacesContext.getCurrentInstance().getExternalContext().redirect(serviceBuilder.getAuthorizationUrl(null));
        } catch (IOException ex) {
            Logger.getLogger(JawboneApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
    
    
}
