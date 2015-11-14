/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.jws.WebService;

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
}
