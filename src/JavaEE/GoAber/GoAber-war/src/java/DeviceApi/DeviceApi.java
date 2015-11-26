/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import GoAberDatabase.Device;
import GoAberDatabase.DeviceType;
import GoAberDatabase.User;
import JSF.auth.AuthController;
import SessionBean.ActivityDataFacade;
import SessionBean.CategoryUnitFacade;
import SessionBean.DeviceFacade;
import SessionBean.DeviceTypeFacade;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.json.Json;
//import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
//import javax.json.JsonStructure;
//import javax.json.stream.JsonParser;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.HttpsURLConnection;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.Preconditions;

/**
 *
 * @author Craig
 */
public abstract class DeviceApi extends DefaultApi20
{
    DeviceTypeFacade deviceTypeFacade = lookupDeviceTypeFacadeBean();
    DeviceFacade deviceFacade = lookupDeviceFacadeBean();
    ActivityDataFacade activityDataFacade = lookupActivityDataFacadeBean();
    CategoryUnitFacade categoryUnitFacade = lookupCategoryUnitFacadeBean();
    @EJB 
    SessionBean.UserFacade userFacade;
    @ManagedProperty(value="#{authController}")
    AuthController authController;
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
       
    @Override
    public String getAccessTokenEndpoint() {
        
        DeviceType deviceType = deviceTypeFacade.findByName(getType());
        
        return String.format("%s?grant_type=authorization_code&client_id=%s&client_secret=%s", deviceType.getTokenEndpoint(), deviceType.getClientId(), deviceType.getConsumerSecret());
    }
    
    public String getAccessTokenEndpoint(String code) {
        return String.format(getAccessTokenEndpoint() + "&code=%s", code);
    }

    
    public String getCallbackUrl()
    {
        String CALLBACK_URL = "http://localhost:8080/GoAber-war/%sCallbackServlet";
        CALLBACK_URL =  String.format(CALLBACK_URL, getType());
        return CALLBACK_URL;
    }
    
    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        
        Preconditions.checkValidUrl(config.getCallback(), "Must provide a valid url as callback. Facebook does not support OOB");
        
        DeviceType deviceType = deviceTypeFacade.findByName(getType());
        return String.format("%s?response_type=code&client_id=%s&redirect_uri=%s&scope=%s", deviceType.getAuthorizationEndpoint(), deviceType.getClientId(), getCallbackUrl(), getScope());
    }
    
    
    public OAuthService getOAuthService()
    {
       DeviceType deviceType = deviceTypeFacade.findByName(getType());
       String apiKey = deviceType.getClientId();
       String apiSecret = deviceType.getConsumerSecret();
       
       OAuthService service =  new ServiceBuilder()
                .provider(getProviderClass())
                .apiKey(apiKey).apiSecret(apiSecret)
                .callback(getCallbackUrl())
                .build();
       return service;
    }
    
    public void getAndSaveTokensFitbit(String code, User user)
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
            System.out.println("Craig "+ responseCode);
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
    
    
    public void getAndSaveTokens(String code, User user)
    {
        DeviceType deviceType = deviceTypeFacade.findByName(getType());
        String urlString = getAccessTokenEndpoint(code);
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
                Device device = new Device(user, deviceType, accessToken, refreshToken, date);
                deviceFacade.create(device);
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
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(DeviceTypeFacade) ejbCtx.lookup("DeviceTypeFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }   
    
    /*
    public ActivityData getWalkingSteps(String requestUrl, String json, int day, int month, int year, User userId)
    {
        CategoryUnit categoryUnit = new CategoryUnit();//categoryUnitFacade.findByCategoryAndUnit("Walking", "Steps");
        return getActivityData(requestUrl, json, day, month, year, userId, categoryUnit);
    }*/
    
    public JsonObject getActivityData(String requestUrl, int day, int month, int year, User userId)//, CategoryUnit categoryUnitId
    {
        DeviceType deviceType = deviceTypeFacade.findByName(getType());
        Device device = deviceFacade.findByUserAndDeviceType(authController.getActiveUser(), deviceType);
        if(device == null)
            return null;
        String accessToken = device.getAccessToken();
        if(device.getTokenExpiration().before(new Date()))
        {
            accessToken = refreshToken();
            if(accessToken.isEmpty())
                return null;
        } 
        String fullUrl = deviceType.getApiEndpoint() + requestUrl;

         OAuthRequest request = new OAuthRequest(Verb.GET, fullUrl); 
         Token token = new Token(accessToken, deviceType.getConsumerSecret());
         getOAuthService().signRequest(token, request); 

         request.addHeader("Host", "http://localhost:8080");
         request.addHeader("Authorization", "Bearer " + accessToken);

         Response response = request.send();

        try{
            try (InputStream is = new ByteArrayInputStream(response.getBody().getBytes()); 
                    JsonReader jsonReader = Json.createReader(is)) {

                JsonObject jsonObject = jsonReader.readObject();
                jsonReader.close();
                return jsonObject;
            }
        } catch (IOException ex) {
            Logger.getLogger(DeviceApi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } 
    
    
    private DeviceFacade lookupDeviceFacadeBean() {
        try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(DeviceFacade) ejbCtx.lookup("DeviceFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    private ActivityDataFacade lookupActivityDataFacadeBean() {
        try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(ActivityDataFacade) ejbCtx.lookup("ActivityDataFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private CategoryUnitFacade lookupCategoryUnitFacadeBean() {
        try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(CategoryUnitFacade) ejbCtx.lookup("CategoryUnitFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
   
    public boolean isConnected(){
       User currentUser = authController.getActiveUser();
       DeviceType deviceType = deviceTypeFacade.findByName(getType());
       if(currentUser == null || deviceType == null)
           return false;
       Device device = deviceFacade.findByUserAndDeviceType(currentUser, deviceType);
       if(device == null)
           return false;
       return true;
   }
   
   public void deleteDevice(){
       User currentUser = authController.getActiveUser();
       DeviceType deviceType = deviceTypeFacade.findByName(getType());
       if(currentUser == null || deviceType == null)
           return;
       Device device = deviceFacade.findByUserAndDeviceType(currentUser, deviceType);
       if(device != null)
           deviceFacade.remove(device);
   }
   
   public void setAuthController(AuthController authController){
       this.authController = authController;
   }
   
   public AuthController getAuthController(){
       return this.authController;
   }

    private String refreshToken() {
        String refreshToken = "";
        return refreshToken;
    }
    
}
