/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
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
    
    @Override
    public String getType() {
        return "Jawbone";
    }

    @Override
    public String getScope() {
        return "basic_read move_read heartrate_read";
    }

    @Override
    public Class getProviderClass() {
        return JawboneApi.class;
    }
    
    public ActivityData getWalkingSteps(int day, int month, int year)
    {
       String url = "/moves?date=" + year + month + day;
       String jsonPath = "data.items[0].details.steps";
       User userId = new User(); // TODO need CG stuff to get the user
       return getWalkingSteps(url, jsonPath, day, month, year, userId);
        
    }
    
    public String getWalkingSteps()
    {
        int day = 27;
        int month = 10;
        int year = 2015;
        ActivityData activityData = getWalkingSteps(day, month, year);
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
