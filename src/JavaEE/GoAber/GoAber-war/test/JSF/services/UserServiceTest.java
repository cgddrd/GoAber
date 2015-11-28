/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.services;

import GoAberDatabase.User;
import SessionBean.UserFacade;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Test cases for the User Service Class
 *
 * @author samuel
 */
public class UserServiceTest {
    private UserService service;
    
    @Mock
    private UserFacade facadeMock;

    @Before
    public void setUp() {
        facadeMock = mock(UserFacade.class);
        service = new UserService();
        service.ejbFacade = facadeMock;
    }
    
    /**
     * Test of getUserById method, of class UserService.
     * 
     * Test should find a user.
     */
    @Test
    public void testGetUserById_Found() {
        User user = new User();
        user.setIdUser(0);
        user.setNickname("Hello");
        
        when(facadeMock.find(0)).thenReturn(user);
        User result = service.getUserById(0);
        
        assertNotNull(result);
        assertEquals(user.getIdUser(), result.getIdUser());
        assertEquals(user.getNickname(), result.getNickname());
        
    }
    
    /**
     * Test of getUserById method, of class UserService.
     * 
     * Test should not find a user.
     */
    @Test
    public void testGetUserById_NotFound() {
        User user = new User();
        user.setIdUser(0);
        user.setNickname("Hello");
        
        when(facadeMock.find(0)).thenReturn(null);
        User result = service.getUserById(0);
        
        assertNull(result);
    }
    
}
