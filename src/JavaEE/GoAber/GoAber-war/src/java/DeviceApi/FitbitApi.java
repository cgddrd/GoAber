/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import GoAberDatabase.ActivityData;
import GoAberDatabase.CategoryUnit;
import GoAberDatabase.Device;
import GoAberDatabase.DeviceType;
import GoAberDatabase.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;

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
    
    @Override
    public void getAndSaveTokens(String code, User user)
    {
        DeviceType deviceType = deviceTypeFacade.findByName(getType());
        String urlString = getAccessTokenEndpoint();//code);
        URL url;
        try {
            url = new URL(urlString);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            String auth = deviceType.getClientId() + ":"+ deviceType.getConsumerSecret();
            byte[] encodedBytes = Base64.getEncoder().encode(auth.getBytes());
            String s = new String(encodedBytes);
            String authBytes = "Basic "+s;
	    con.setRequestProperty("Authorization", authBytes);
            con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String param = String.format("client_id=%s&grant_type=authorization_code&redirect_uri=%s&code=%s", deviceType.getClientId(), getCallbackUrl(), code);
            con.addRequestProperty("client_id", deviceType.getClientId());
            con.addRequestProperty("grant_type", "authorization_code");
            con.addRequestProperty("redirect_uri", getCallbackUrl());
            con.addRequestProperty("code", code);
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            int responseCode = con.getResponseCode();
            String err = con.getResponseMessage();
            try (InputStream is = url.openStream(); 
                    JsonReader jsonReader = Json.createReader(is)) {
 
                JsonObject jsonObject = jsonReader.readObject();
                String accessToken = jsonObject.getString("access_token");
                String refreshToken = jsonObject.getString("refresh_token");
                int expiresIn = jsonObject.getInt("expires_in");
                Date date = new Date();
                date.setSeconds(date.getSeconds() + expiresIn);
                Device device = new Device(user, deviceType, accessToken, refreshToken, date);
                deviceFacade.create(device);
            }
           
        } catch (MalformedURLException ex) {
            Logger.getLogger(DeviceApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DeviceApi.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        User user = authService.getActiveUser();
        if(user == null)
            return "index";
        int userID = user.getIdUser();
        ActivityData activityData = getWalkingSteps(day, month, year, userID);
        this.steps = activityData.getValue();
        return "ViewActivity";
    }
}
