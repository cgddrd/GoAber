/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.jws.WebService;

/**
 * 
 *
 * @author helen
 */
@ManagedBean(name = "jawboneController")
@SessionScoped
public class JawboneApi extends DeviceApi{

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
    
    
    
    
}
