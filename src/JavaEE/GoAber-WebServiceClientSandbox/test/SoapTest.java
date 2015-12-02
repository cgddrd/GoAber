/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import goaber.webserviceclientsandbox.SandBoxUI;
import goaber.webserviceclientsandbox.consumer.ActivityDataSOAP;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author helen
 */
public class SoapTest {
    
    private SandBoxUI sandBox;
    private ActivityDataSOAP activityData;
    private ActivityDataSOAP activityData2;
    
    private String userToken = "user_token";
    private String adminToken = "admin_token";
    
    
    @Before
    public void setUp() {
               
        sandBox = new SandBoxUI();
        activityData = new ActivityDataSOAP();
        activityData.setCategoryUnitId(1);
        activityData.setValue(40);
        activityData.setUseremail("admin@test.com");
        
        activityData2 = new ActivityDataSOAP();
        activityData2.setCategoryUnitId(1);
        activityData2.setValue(70);
        activityData.setUseremail("user@test.com");
    }
    
    
    @Test
    public void testNoActivityDataSent(){
        boolean result = sandBox.sendActivityData(adminToken, new ArrayList<>());
        
        assertTrue(result);
    }
    
    
    @Test
    public void testOneActivityDataSent(){
        List<ActivityDataSOAP> activityDatas = new ArrayList<>();
        activityDatas.add(activityData);
        boolean result = sandBox.sendActivityData(adminToken, activityDatas);
        
        assertTrue(result);
    }
    
    
    @Test
    public void testMultiActivityDataSent(){
        List<ActivityDataSOAP> activityDatas = new ArrayList<>();
        activityDatas.add(activityData);
        activityDatas.add(activityData2);
        boolean result = sandBox.sendActivityData(adminToken, activityDatas);
        
        assertTrue(result);
    }
    

    @Test
    public void testActivityDataSentUsingNoneAdminToken(){
        List<ActivityDataSOAP> activityDatas = new ArrayList<>();
        activityDatas.add(activityData2);
        boolean result = sandBox.sendActivityData(userToken, activityDatas);
        
        assertTrue(result);
    }
    
    @Test
    public void testAttemptToSendActivityDataToDiffUsersAccountUsingNoneAdminToken(){
        List<ActivityDataSOAP> activityDatas = new ArrayList<>();
        activityDatas.add(activityData);
        boolean result = sandBox.sendActivityData(userToken, activityDatas);
        
        assertFalse(result);
    }
    
    
    @Test
    public void testActivityDataSentUsingInvalidToken(){
        List<ActivityDataSOAP> activityDatas = new ArrayList<>();
        activityDatas.add(activityData2);
        boolean result = sandBox.sendActivityData("invalid", activityDatas);
        
        assertFalse(result);
    }
    
    
    @Test
    public void testInvalidActivityDataSent(){
        List<ActivityDataSOAP> activityDatas = new ArrayList<>();
        activityData.setCategoryUnitId(100);
        boolean result = sandBox.sendActivityData("invalid", activityDatas);
        
        assertFalse(result);
    }
    
}
