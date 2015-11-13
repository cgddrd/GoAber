/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DeviceApi;

/**
 *
 * @author helen
 */
public class JawboneApi extends DeviceApi{

    @Override
    public String getType() {
        return "Jawbone";
    }

    @Override
    public String getScope() {
        return "basic_read move_read heartrate_read";
    }
    
    
    
    
}
