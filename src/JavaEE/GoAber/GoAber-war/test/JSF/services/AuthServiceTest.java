/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.services;

import GoAberDatabase.Role;
import GoAberDatabase.User;
import GoAberDatabase.UserRole;
import JSF.util.FacesContextMocker;
import SessionBean.UserFacade;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import org.apache.taglibs.standard.tag.common.fmt.BundleSupport;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test cases for the AuthService class.
 *
 * @author Connor Goddard
 */
public class AuthServiceTest {

    private AuthService service;

    @Mock
    private UserFacade facadeMock;
    
    @Mock
    ExternalContext ext;
    
    @Mock
    Flash flash;
    
    @Mock
    Map<String, Object> session;
    
    @Mock
    FacesContext context;
    
    @Mock
    Principal securityPrincipal;
    

    @Before
    public void setUp() {
        facadeMock = mock(UserFacade.class);
        service = new AuthService();
        service.setUserFacade(facadeMock);
        securityPrincipal = mock(Principal.class);
        
        ext = mock(ExternalContext.class);
        flash = mock(Flash.class);
        session = new HashMap<String, Object>();
        context = FacesContextMocker.mockFacesContext();
            
        when(ext.getSessionMap()).thenReturn(session);
        when(ext.getFlash()).thenReturn(flash);
        when(ext.getUserPrincipal()).thenReturn(securityPrincipal);
        when(context.getExternalContext()).thenReturn(ext);
    }

    /**
     * Test of getActiveUser method, of class AuthService.
     *
     * Test should find the active user.
     */
    @Test
    public void testGetActiveUser_Found() {

        User user = new User();
        user.setIdUser(0);
        user.setNickname("User1");

        service = new AuthService(user);

        when(facadeMock.find(0)).thenReturn(user);
        service.setUserFacade(facadeMock);

        User result = service.getActiveUser();

        assertNotNull(result);
        assertEquals(user.getIdUser(), result.getIdUser());
        assertEquals(user.getNickname(), result.getNickname());

    }

    /**
     * Test of getActiveUser method, of class AuthService.
     *
     * Test should not find the active user.
     */
    @Test
    public void testGetActiveUser_NotFound() {

        try {
          
            User user = new User();
            user.setIdUser(0);
            user.setNickname("User1");
                      
            when(facadeMock.find(0)).thenReturn(null);
            
            service = new AuthService(user);
            service.setUserFacade(facadeMock);

            User result = service.getActiveUser();

            assertNull(result);

        } finally {
            context.release();
        }



    }
    
    @Test
    public void testisUserIdActiveUser_True() {

        try {
          
            User user1 = new User();
            user1.setIdUser(0);
            user1.setNickname("User1");
            
            User user2 = new User();
            user2.setIdUser(1);
            user2.setNickname("User2");
                      
            when(facadeMock.find(0)).thenReturn(null);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);
            
            assertTrue(service.isUserIdActiveUser(user1));
            
        } finally {
            context.release();
        }
        
    }
    
    @Test
    public void testisUserIdActiveUser_False() {

        try {
          
            User user1 = new User();
            user1.setIdUser(0);
            user1.setNickname("User1");
            
            User user2 = new User();
            user2.setIdUser(1);
            user2.setNickname("User2");
                      
            when(facadeMock.find(0)).thenReturn(user1);
            when(facadeMock.find(1)).thenReturn(user2);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);
            
            assertFalse(service.isUserIdActiveUser(user2));
            
        } finally {
            context.release();
        }
        
    }
    
    @Test
    public void testisLoggedIn_True() {

        try {
          
            User user1 = new User();
            user1.setIdUser(0);
            user1.setNickname("User1");
    
            when(facadeMock.find(0)).thenReturn(user1);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);
            
            assertTrue(service.isLoggedIn());
            
        } finally {
            context.release();
        }
        
    }
    
    @Test
    public void testisLoggedIn_False() {

        try {
          
            User user1 = new User();
            user1.setIdUser(0);
            user1.setNickname("User1");
    
            when(facadeMock.find(0)).thenReturn(user1);
            when(ext.getUserPrincipal()).thenReturn(null);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);
            
            assertFalse(service.isLoggedIn());
            
        } finally {
            context.release();
        }
        
    }
    
    @Test
    public void testisAdmin_True() {

        try {
          
            User user1 = mock(User.class);
            
            user1.setIdUser(0);
            user1.setNickname("User1");
            user1.setEmail("user1@test.com");
            
            Role adminRole = new Role("administrator");
            UserRole adminUserRole = new UserRole(0, adminRole, user1.getEmail());

            user1.setUserRoleId(adminUserRole);
            
            when(user1.getUserRoleId()).thenReturn(adminUserRole);
            when(facadeMock.find(0)).thenReturn(user1);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);
            
            assertTrue(service.isAdmin());
            
        } finally {
            context.release();
        }
        
    }
    
    @Test
    public void testisAdmin_False() {

        try {
          
            User user1 = mock(User.class);
            
            user1.setIdUser(0);
            user1.setNickname("User1");
            user1.setEmail("user1@test.com");
            
            Role adminRole = new Role("participant");
            UserRole adminUserRole = new UserRole(0, adminRole, user1.getEmail());

            user1.setUserRoleId(adminUserRole);
            
            when(user1.getUserRoleId()).thenReturn(adminUserRole);
            when(facadeMock.find(0)).thenReturn(user1);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);
            
            assertFalse(service.isAdmin());
            
        } finally {
            context.release();
        }
        
    }
    
    @Test
    public void testisCoordinator_True() {

        try {
          
            User user1 = mock(User.class);
            
            user1.setIdUser(0);
            user1.setNickname("User1");
            user1.setEmail("user1@test.com");
            
            Role coordRole = new Role("coordinator");
            UserRole adminUserRole = new UserRole(0, coordRole, user1.getEmail());

            user1.setUserRoleId(adminUserRole);
            
            when(user1.getUserRoleId()).thenReturn(adminUserRole);
            when(facadeMock.find(0)).thenReturn(user1);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);
            
            assertTrue(service.isCoordinator());
            
        } finally {
            context.release();
        }
        
    }
    
    @Test
    public void testisCoordinator_TrueDueToAdminUser() {

        try {
          
            User user1 = mock(User.class);
            
            user1.setIdUser(0);
            user1.setNickname("User1");
            user1.setEmail("user1@test.com");
            
            // An admin user should also be granted coordinator access.
            Role coordRole = new Role("administrator");
            UserRole adminUserRole = new UserRole(0, coordRole, user1.getEmail());

            user1.setUserRoleId(adminUserRole);
            
            when(user1.getUserRoleId()).thenReturn(adminUserRole);
            when(facadeMock.find(0)).thenReturn(user1);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);
            
            assertTrue(service.isCoordinator());
            
        } finally {
            context.release();
        }
        
    }
    
    @Test
    public void testisCoordinator_False() {

        try {
          
            User user1 = mock(User.class);
            
            user1.setIdUser(0);
            user1.setNickname("User1");
            user1.setEmail("user1@test.com");
            
            Role coordRole = new Role("participant");
            UserRole adminUserRole = new UserRole(0, coordRole, user1.getEmail());

            user1.setUserRoleId(adminUserRole);
            
            when(user1.getUserRoleId()).thenReturn(adminUserRole);
            when(facadeMock.find(0)).thenReturn(user1);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);
            
            assertFalse(service.isCoordinator());
            
        } finally {
            context.release();
        }
        
    }
    
    @Test
    public void testgetLoggedInUser_Found() {

        try {
          
            //User user1 = mock(User.class);
            
            User user1 = new User();
            
            user1.setIdUser(0);
            user1.setNickname("User1");
            user1.setEmail("user1@test.com");
            
            // Make sure to "fake" log the user in.
            when(securityPrincipal.getName()).thenReturn(user1.getEmail());
            
            when(facadeMock.find(0)).thenReturn(user1);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);
            
            assertEquals(user1.getEmail(), service.getLoggedInUser());
            
        } finally {
            context.release();
        }
        
    }
    
    @Test
    public void testgetLoggedInUser_NotFound() {

        try {
          
            User user1 = new User();
            
            user1.setIdUser(0);
            user1.setNickname("User1");
            user1.setEmail("user1@test.com");
            
            when(facadeMock.find(0)).thenReturn(user1);
            
            service = new AuthService(user1);
            service.setUserFacade(facadeMock);

            assertNull(service.getLoggedInUser());
            
        } finally {
            context.release();
        }
        
    }
   

}
