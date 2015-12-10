/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.util;

import javax.faces.context.FacesContext;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Utility class for generating a mock FacesContext in order to test session state via unit tests.
 * 
 * Modified from original source: http://illegalargumentexception.blogspot.co.uk/2011/12/jsf-mocking-facescontext-for-unit-tests.html#mockFacesCurrentInstance 
 * 
 * @author Connor Goddard
 */
public abstract class FacesContextMocker extends FacesContext {
    
  private FacesContextMocker() {
  }

  private static final Release RELEASE = new Release();

  private static class Release implements Answer<Void> {
    @Override
    public Void answer(InvocationOnMock invocation) throws Throwable {
      setCurrentInstance(null);
      return null;
    }
  }

  public static FacesContext mockFacesContext() {
    FacesContext context = Mockito.mock(FacesContext.class);
    setCurrentInstance(context);
    Mockito.doAnswer(RELEASE)
        .when(context)
        .release();
    return context;
  }
}
