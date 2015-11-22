/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goaber.webserviceclientsandbox;

import goaber.webserviceclientsandbox.consumer.ActivityDataSOAP;
import goaber.webserviceclientsandbox.consumer.GoAberWS;
import goaber.webserviceclientsandbox.consumer.GoAberWS_Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author Dan
 */
public class SandBoxUI {
    
    	private static final String WS_URL = "http://localhost:8080/GoAberWS/GoAberWS?WSDL";
    
    public SandBoxUI() {
        
    }
    
    public void start() {
        
         //Service service = Service.create(url, qname);
        //HelloWorld hello = service.getPort(HelloWorld.class);
        GoAberWS_Service lo_service = new GoAberWS_Service();
        GoAberWS lo_port = lo_service.getGoAberWSPort();
        
        Map<String, Object> req_ctx = ((BindingProvider)lo_port).getRequestContext();
        req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);

        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("authtoken", Collections.singletonList("3b4d592d-4b25-43d3-b8c6-c17641adc024"));
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
        //GoAberWS lo_service = new GoAberWS();
        //ObjectFactory lo_service = new ObjectFactory();
        ActivityDataSOAP lo_data = new ActivityDataSOAP();
        lo_data.setCategoryUnitId(1);
        lo_data.setValue(4);
        
        
        List<ActivityDataSOAP> lo_datas = new ArrayList<>();
        
        lo_datas.add(lo_data);
        
         boolean lb_res = lo_port.addActivityData(lo_datas);
         
         System.out.println("Result is: " + lb_res);
         
         //while(true) {}
    }
}
