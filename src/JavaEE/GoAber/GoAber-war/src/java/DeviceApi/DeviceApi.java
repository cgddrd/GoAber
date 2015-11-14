/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import GoAberDatabase.ActivityData;
import GoAberDatabase.Device;
import GoAberDatabase.DeviceType;
import GoAberDatabase.User;
import SessionBean.DeviceFacade;
import SessionBean.DeviceTypeFacade;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataSource;
import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.SessionScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.jws.WebService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 *
 * @author Craig
 */
public abstract class DeviceApi extends DefaultApi20
{
    DeviceTypeFacade deviceTypeFacade = lookupDeviceTypeFacadeBean();
    DeviceFacade deviceFacade = lookupDeviceFacadeBean();
    
    int steps;
    //@EJB
    //private SessionBean.DeviceTypeFacade deviceTypeFacade;
    //@Resource(name="GoAber-warPU") 
    //private DataSource dataSource;
    
   // @PersistenceContext(unitName = "GoAber-warPU")
    //private EntityManager em ;
    
    
    public abstract String getType();
    public abstract String getScope();
    public abstract Class getProviderClass();
    
   // private static final String AUTHORIZE_URL = "https://www.fitbit.com/oauth2/authorize/oauth?client_id=%s&redirect_uri=%s&scope=activity&response_type=code";
   
    @Override
    public String getAccessTokenEndpoint() {
        
        //DeviceType deviceType = (DeviceType)em.createNamedQuery("DeviceType.findByName").setParameter("name", getType()).getSingleResult();
        DeviceType deviceType = deviceTypeFacade.findByName(getType());
        
        return String.format("%s?grant_type=authorization_code&client_id=%s&client_secret=%s", deviceType.getTokenEndpoint(), deviceType.getClientId(), deviceType.getConsumerSecret());
       // return "https://jawbone.com/auth/oauth2/token?grant_type=authorization_code&client_id=mCZQ7V2DbgQ&client_secret=07e4083f111f1a44ccba1bf94d21c95f5486f8f1";
    }
    
    public String getAccessTokenEndpoint(String code) {
        return String.format(getAccessTokenEndpoint() + "&code=%s", code);
    }

    
    private String getCallbackUrl()
    {
        String CALLBACK_URL = "http://localhost:8080/GoAber-war/%sCallbackServlet";
        return String.format(CALLBACK_URL, getType());
    }
    
    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");
        
        //private static final String AUTHORIZE_URL = "https://jawbone.com/auth/oauth2/auth?client_id=%s&redirect_uri=%s&scope=move_read&response_type=code";
        //DeviceType deviceType = (DeviceType)em.createNamedQuery("DeviceType.findByName").setParameter("name", getType()).getSingleResult();
        DeviceType deviceType = deviceTypeFacade.findByName(getType());
        return String.format("%s?response_type=code&client_id=%s&redirect_uri=%s", deviceType.getAuthorizationEndpoint(), deviceType.getClientId(), getCallbackUrl());
       
        //return String.format(AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));        
    }
    
    
    public OAuthService getOAuthService()
    {
        DeviceType deviceType = deviceTypeFacade.findByName(getType());
       /* deviceTypeFacade.
        //em.createNamedQuery("DeviceType.findByName").setParameter("name", getType()).getSingleResult();
       Query q =  em.createNamedQuery("DeviceType.findByName");
       DeviceType deviceType = (DeviceType)q.setParameter("name", getType()).getSingleResult();
        */
       String apiKey = deviceType.getClientId();//"mCZQ7V2DbgQ";
       String apiSecret = deviceType.getConsumerSecret();//"07e4083f111f1a44ccba1bf94d21c95f5486f8f1";
       
       OAuthService service =  new ServiceBuilder()
                .provider(getProviderClass())
                .apiKey(apiKey).apiSecret(apiSecret)
                .callback(getCallbackUrl())//"http://localhost:8080/GoAber-war/JawboneCallbackServlet")
                .build();
       return service;
    }
    
    
    public void getAndSaveTokens(String code)//TODO we will probably pass the user in
    {
        DeviceType deviceType = deviceTypeFacade.findByName(getType());
        String urlString = getAccessTokenEndpoint(code);//"https://jawbone.com/auth/oauth2/token?grant_type=authorization_code&client_id=mCZQ7V2DbgQ&client_secret=07e4083f111f1a44ccba1bf94d21c95f5486f8f1&code=" +code;
        URL url;
        try {
            url = new URL(urlString);
            try (InputStream is = url.openStream(); 
                    JsonReader jsonReader = Json.createReader(is)) {
 
                JsonObject jsonObject = jsonReader.readObject();
                String accessToken = jsonObject.getString("access_token");
                String refreshToken = jsonObject.getString("refresh_token");
                int expiresIn = jsonObject.getInt("expires_in");
                Date date = new Date();
                date.setSeconds(date.getSeconds() + expiresIn);
                Device device = new Device(new User(0), deviceType, accessToken, refreshToken, date);
                //deviceFacade.create(device); //TODO need connor's user stuff
                System.out.println("access_token " + accessToken);
                System.out.println("refreshToken " + refreshToken);
                System.out.println("expiresIn " + expiresIn);
            }
           
        } catch (MalformedURLException ex) {
            Logger.getLogger(DeviceApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DeviceApi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private DeviceTypeFacade lookupDeviceTypeFacadeBean() {
        try {
           // Context c = new InitialContext();
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(DeviceTypeFacade) ejbCtx.lookup("DeviceTypeFacade");
           // return (DeviceTypeFacade) c.lookup("java:global/GoAber/GoAber-war/DeviceTypeFacade!SessionBean.DeviceTypeFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private DeviceFacade lookupDeviceFacadeBean() {
        try {
           // Context c = new InitialContext();
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(DeviceFacade) ejbCtx.lookup("DeviceFacade");
           // return (DeviceTypeFacade) c.lookup("java:global/GoAber/GoAber-war/DeviceTypeFacade!SessionBean.DeviceTypeFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
   
    
    
    public ActivityData getWalkingSteps(String requestUrl, String json, int day, int month, int year)
    {
        //TODO get category unit from db
        //TODO user should be passed in as parameter
        
        return getActivityData(requestUrl, json, day, month, year);
    }
    
    public ActivityData getActivityData(String requestUrl, String jsonPath, int day, int month, int year)
    {
        //TODO category unit should be passed in as parameter
        //TODO user should be passed in as parameter
        
        // TODO get users access token from db
        
        
        //String fullUrl = deviceType.apiEndpoint + requestUrl; // TODO will be changed to this
        String fullUrl = "https://jawbone.com/nudge/api/v.1.1/users/@me" + requestUrl;
        try {
            URL url = new URL(fullUrl);
                    
             // TODO   OAuthRequest .... see scribe's facebook example     
            try (InputStream is = url.openStream(); 
                JsonReader jsonReader = Json.createReader(is)) {

                JsonObject jsonObject = jsonReader.readObject();
                int value = jsonObject.getInt(jsonPath);
                System.out.println("Value = " + value);
                // TODO fill in activityData parameters and save to db
                ActivityData activityData = new ActivityData();
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(DeviceApi.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DeviceApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
