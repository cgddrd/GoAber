/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.services;

import GoAberDatabase.ActivityData;
import GoAberDatabase.CategoryUnit;
import GoAberDatabase.User;
import SessionBean.ActivityDataFacade;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;


/**
 *
 * @author samuel
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityDataServiceTest {
    
    private ActivityDataService service;
    @Mock
    private ActivityDataFacade facadeMock;
    
    public ActivityDataServiceTest() {
    }
    
    @Before
    public void setUp() {
        facadeMock = mock(ActivityDataFacade.class);
        service = new ActivityDataService();
        service.ejbFacade = facadeMock;
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of findAll method, of class ActivityDataService.
     * 
     * We expect to find 5 items.
     */
    @Test
    public void testFindAll_SomeItems() {
        when(facadeMock.findAll()).thenReturn(createMockData(5));
        assertEquals(5, service.findAll().size());
    }
    
    /**
     * Test of findAll method, of class ActivityDataService.
     * 
     * We expect to find 0 items.
     */
    @Test
    public void testFindAll_NoItems() {
        when(facadeMock.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, service.findAll().size());
    }

    /**
     * Test of findAllForUser method, of class ActivityDataService.
     * 
     * We expect to find a single item.
     */
    @Test
    public void testFindAllForUser_FindOne() {
        //create a dummy logged in user
        User current = new User();
        current.setIdUser(0);
        
        //mock call and get result
        when(facadeMock.getAllForUser(0)).thenReturn(createMockData(1));
        List<ActivityData> result = service.findAllForUser(current);
        
        //check query size matched
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(0).getUserId());
        
        ActivityData resultData = result.get(0);
        User user = resultData.getUserId();
        
        //check payload was as expected
        assertEquals(0, user.getIdUser().longValue());
        assertEquals(0, resultData.getValue().longValue());
    }
    
    /**
     * Test of findAllForUser method, of class ActivityDataService.
     * 
     * We expect to find a no items.
     */
    @Test
    public void testFindAllForUser_FindNone() {
        //create a dummy logged in user
        User current = new User();
        current.setIdUser(0);
        
        //mock call and get result
        when(facadeMock.getAllForUser(0)).thenReturn(new ArrayList<>());
        List<ActivityData> result = service.findAllForUser(current);
        
        //check query size matched
        assertEquals(0, result.size());
    }

    /**
     * Test of findById method, of class ActivityDataService.
     * 
     * We expect to find a single item
     */
    @Test
    public void testFindById_FineOne() {
        when(facadeMock.find(10)).thenReturn( new ActivityData(10));        
        ActivityData data = service.findById(10);
        assertNotNull(data);
        assertEquals(10, data.getIdActivityData().longValue());
    }

    /**
     * Test of findById method, of class ActivityDataService.
     * 
     * We expect to find a no items
     */
    @Test
    public void testFindById_FineNone() {
        when(facadeMock.find(10)).thenReturn( new ActivityData(10));        
        ActivityData data = service.findById(10);
        assertNotNull(data);
        assertEquals(10, data.getIdActivityData().longValue());
    }
    
    /**
     * Test of findAllForUserInDateRange method, of class ActivityDataService.
     * 
     * We expect to find a single item.
     * @throws java.text.ParseException
     */
    @Test
    public void testFindAllForUserInDateRange_FindOne() throws ParseException  {
        //create a dummy logged in user
        User current = new User();
        current.setIdUser(0);
        
        //setup date range
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        Date startDate = formatter.parse("22/11/15");
        Date endDate = formatter.parse("28/11/15");
        
        //set date for result object
        List<ActivityData> expResult = createMockData(1);
        expResult.get(0).setDate(formatter.parse("21/11/15"));
        
        //mock call and get result
        when(facadeMock.getAllForUserInDateRange(0, "Steps", startDate, endDate)).thenReturn(expResult);
        List<ActivityData> result = service.findAllForUserInDateRange(current, "Steps", startDate, endDate);
        
        //check query size matched
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
        assertNotNull(result.get(0).getUserId());
        
        ActivityData resultData = result.get(0);
        User user = resultData.getUserId();
        
        //check payload was as expected
        assertEquals(0, user.getIdUser().longValue());
        assertEquals(0, resultData.getValue().longValue());
    }
    
    /**
     * Test of findAllForUserInDateRange method, of class ActivityDataService.
     * 
     * We expect to find a no items.
     * @throws java.text.ParseException
     */
    @Test
    public void testFindAllForUserInDateRange_FindNone() throws ParseException {
        //create a dummy logged in user
        User current = new User();
        current.setIdUser(0);
       
        //setup date range
        DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        Date startDate = formatter.parse("22/11/15");
        Date endDate = formatter.parse("28/11/15");
        
        //mock call and get result
        when(facadeMock.getAllForUserInDateRange(0, "Steps", startDate, endDate)).thenReturn(new ArrayList<>());
        List<ActivityData> result = service.findAllForUserInDateRange(current, "Steps", startDate, endDate);
        
        //check query size matched
        assertEquals(0, result.size());
    }
    
    /**
     * Test of createForUser method, of class ActivityDataService.
     */
    @Test
    public void testCreateForUser() {
        final ArgumentCaptor<ActivityData> captor = ArgumentCaptor.forClass(ActivityData.class);
        ActivityData data = createMockSingle();
        
        User user = new User();
        user.setIdUser(5);
        
        service.createForUser(data, user);
        verify(facadeMock, times(1)).create(captor.capture());
        final ActivityData sentObject = captor.getValue();
        
        assertActivityDataEqual(data, sentObject);
        assertEquals(data.getCategoryUnitId().getCategoryId(), sentObject.getCategoryUnitId().getCategoryId());
        assertEquals(user.getIdUser(), sentObject.getUserId().getIdUser());
    }

    /**
     * Test of create method, of class ActivityDataService.
     */
    @Test
    public void testCreate() {
        final ArgumentCaptor<ActivityData> captor = ArgumentCaptor.forClass(ActivityData.class);
        ActivityData data = createMockSingle();
        
        service.create(data);
        verify(facadeMock, times(1)).create(captor.capture());
        final ActivityData sentObject = captor.getValue();
        
        assertActivityDataEqual(data, sentObject);
        assertEquals(data.getCategoryUnitId().getCategoryId(), sentObject.getCategoryUnitId().getCategoryId());
        assertEquals(data.getUserId().getIdUser(), sentObject.getUserId().getIdUser());
    }

    /**
     * Test of updateForUser method, of class ActivityDataService.
     */
    @Test
    public void testUpdateForUser() {
        final ArgumentCaptor<ActivityData> captor = ArgumentCaptor.forClass(ActivityData.class);
        ActivityData data = createMockSingle();
        data.setValue(2);
        
        User user = new User();
        user.setIdUser(5);
        
        service.updateForUser(data, user);
        verify(facadeMock, times(1)).edit(captor.capture());
        final ActivityData sentObject = captor.getValue();
        
        assertActivityDataEqual(data, sentObject);
        assertEquals(data.getCategoryUnitId().getCategoryId(), sentObject.getCategoryUnitId().getCategoryId());
        assertEquals(user.getIdUser(), sentObject.getUserId().getIdUser());
    }

    /**
     * Test of update method, of class ActivityDataService.
     */
    @Test
    public void testUpdate() {
        final ArgumentCaptor<ActivityData> captor = ArgumentCaptor.forClass(ActivityData.class);
        ActivityData data = createMockSingle();
        data.setValue(2);
        
        service.update(data);
        verify(facadeMock, times(1)).edit(captor.capture());
        final ActivityData sentObject = captor.getValue();
        
        assertActivityDataEqual(data, sentObject);
        assertEquals(data.getCategoryUnitId().getCategoryId(), sentObject.getCategoryUnitId().getCategoryId());
        assertEquals(data.getUserId().getIdUser(), sentObject.getUserId().getIdUser());
    }

    /**
     * Test of remove method, of class ActivityDataService.
     */
    @Test
    public void testRemove() {
        final ArgumentCaptor<ActivityData> captor = ArgumentCaptor.forClass(ActivityData.class);
        ActivityData data = createMockSingle();
        
        service.remove(data);
        verify(facadeMock, times(1)).remove(captor.capture());
        
        final ActivityData sentObject = captor.getValue();
        
        assertActivityDataEqual(data, sentObject);
        assertEquals(data.getCategoryUnitId().getCategoryId(), sentObject.getCategoryUnitId().getCategoryId());
        assertEquals(data.getUserId().getIdUser(), sentObject.getUserId().getIdUser());        
    }
    
    private List<ActivityData> createMockData(int size) {
        List<ActivityData> list = new ArrayList<>();
        
        for (int i =0; i < size; i++) {
            User user = new User();
            user.setIdUser(i);
            
            ActivityData data = new ActivityData();
            data.setValue(i);
            data.setUserId(user);
            
            list.add(data);
        }

        return list;
    }
    
    private ActivityData createMockSingle() {
        Date now = new Date();
        
        User defaultUser = new User();
        defaultUser.setIdUser(3);
        
        CategoryUnit defaultCategoryUnit = new CategoryUnit();
        defaultCategoryUnit.setIdCategoryUnit(2);
        
        ActivityData data = new ActivityData();
        
        data.setValue(205);
        data.setDate(now);
        data.setLastUpdated(now);
        data.setUserId(defaultUser);
        data.setCategoryUnitId(defaultCategoryUnit);
        return data;
    }
    
    private void assertActivityDataEqual(ActivityData expected, ActivityData result) {
        assertEquals(expected.getIdActivityData(), result.getIdActivityData());
        assertEquals(expected.getValue(), result.getValue());
        assertEquals(expected.getLastUpdated(), result.getLastUpdated());
        assertEquals(expected.getDate(), result.getDate());
    }
    
}
