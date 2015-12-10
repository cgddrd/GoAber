/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goaber.webserviceclientsandbox;

import goaber.webserviceclientsandbox.consumer.ActivityDataSOAP;
import goaber.webserviceclientsandbox.consumer.GoAberWS;
import goaber.webserviceclientsandbox.consumer.GoAberWS_Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public void header(GoAberWS ao_port, String as_token) {
        Map<String, Object> req_ctx = ((BindingProvider) ao_port).getRequestContext();
        req_ctx.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);

        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        headers.put("authtoken", Collections.singletonList(as_token));
        req_ctx.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }

    public List<ActivityDataSOAP> createData() {
        ActivityDataSOAP lo_data = new ActivityDataSOAP();
        lo_data.setCategoryUnitId(1);
        lo_data.setValue(4);
        //lo_data.setUseremail("dan@dan.com");

        List<ActivityDataSOAP> lo_datas = new ArrayList<>();

        lo_datas.add(lo_data);

        return lo_datas;
    }

    public boolean sendData(String as_token) {
        GoAberWS_Service lo_service = new GoAberWS_Service();
        GoAberWS lo_port = lo_service.getGoAberWSPort();

        header(lo_port, as_token);
        return lo_port.addActivityData(createData());
    }
    
    public boolean sendActivityData(String token, List<ActivityDataSOAP> activityData){
        GoAberWS_Service service = new GoAberWS_Service();
        GoAberWS goAberPort = service.getGoAberWSPort();

        header(goAberPort, token);
        return goAberPort.addActivityData(activityData);
    }

    public void start() {
        boolean lb_stop = false;
        do {
            try {
                BufferedReader lo_br = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter Auth Token: ");
                String ls_token = lo_br.readLine();

                boolean lb_res = sendData(ls_token);

                System.out.println("Result is: " + lb_res);

                System.out.println("Quit? (q) :");

                String ls_quit = lo_br.readLine();

                if (ls_quit.equals("q")) {
                    lb_stop = true;
                }
                //while(true) {}
            } catch (IOException ex) {
                Logger.getLogger(SandBoxUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!lb_stop);
    }
}
