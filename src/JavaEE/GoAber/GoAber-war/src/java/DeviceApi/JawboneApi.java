/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import GoAberDatabase.ActivityData;
import GoAberDatabase.CategoryUnit;
import GoAberDatabase.User;
import Devices.JawboneEJB;
import JSF.services.AuthService;
import SessionBean.ActivityDataFacade;
import SessionBean.CategoryUnitFacade;
import SessionBean.DeviceFacade;
import SessionBean.DeviceTypeFacade;
import SessionBean.TeamFacade;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.json.JsonObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Allows data retrieved be read from Jawbone
 *
 * @author helen
 */
@ManagedBean(name = "jawboneController")
//@Stateless
@SessionScoped
public class JawboneApi {

    @EJB JawboneEJB jawboneEJB;
    
    @ManagedProperty(value="#{authService}")
    public AuthService authService;
    
    public int steps = 0;
    
    public JawboneApi() {
        
    }
    
    public void setauthService(AuthService authService)
    {
       this.authService = authService;
    }
   
    public AuthService getauthService()
    {
       return this.authService;
    }
    
    
    public boolean isConnected() {
        //jawboneEJB.setauthService(authService);
        return jawboneEJB.isConnected(authService.getActiveUser());
    }
    
    public int getSteps() {
        return this.steps;
    }
    
    public void setSteps(int steps) {
        this.steps = steps;
    }
    public Date getDeviceDate() {
        return jawboneEJB.getDeviceDate();
    }

    public void setDeviceDate(Date deviceDate) {
        jawboneEJB.setDeviceDate(deviceDate);
    }
    
    public String getType() {
        return jawboneEJB.getType();
    }

    public String getScope() {
        return jawboneEJB.getScope();
    }

    public Class getProviderClass() {
        return jawboneEJB.getProviderClass();
    }
    
    /**
     * 
     * @param day
     * @param month
     * @param year
     * @param userID
     * @return 
     */
//    public ActivityData getWalkingSteps(int day, int month, int year, int userID)
//    {
//        return jawboneEJB.getWalkingSteps(day, month, year, userID);
//    }
    
    
     public String getWalkingStepsForEnteredDate()
    {
        this.steps = jawboneEJB.getWalkingStepsForEnteredDate(authService.getActiveUser());
        return "";
    }
     
    /**
     * Method for testing steps can be retrieved from jawbone
     * @return 
     */
    public String getWalkingSteps()
    {
        return jawboneEJB.getWalkingSteps(authService.getActiveUser());
    }
    
    public void deleteDevice() {
        jawboneEJB.deleteDevice(authService.getActiveUser());
    }
    
    
    
}
