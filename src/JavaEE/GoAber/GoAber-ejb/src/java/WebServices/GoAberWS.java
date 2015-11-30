/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServices;

import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import GoAberDatabase.WebServiceAuth;
import SessionBean.ActivityDataFacade;
import SessionBean.CategoryUnitFacade;
import SessionBean.UserFacade;
import SessionBean.WebServiceAuthFacade;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Dan
 */
@WebService(serviceName = "GoAberWS")
@Stateless()
public class GoAberWS {

    @EJB
    UserFacade io_u;
    
    @EJB
    CategoryUnitFacade io_cf;
    
    @EJB
    WebServiceAuthFacade io_wsaf;
    
    @EJB
    ActivityDataFacade io_adf;
    
    @Resource
    WebServiceContext io_wsctx;
    /**
     * This is a sample web service operation
     * @param txt
     * @return 
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    
    @WebMethod(operationName = "addActivityData")
    public boolean addActivityData(@WebParam(name = "data") List<ActivityDataSOAP> data) 
    {
        WebServiceAuth lo_wsa = getWebServiceAuth();
        if (lo_wsa == null) {
            return false;
        }
        
        return addActivityData(lo_wsa, data);
    }
    
    public boolean isAdmin(User ao_user) {    
        
        return ao_user.getUserRoleId().getRoleId().getIdRole().equals("admin");

    }
    
    public boolean isNotNullEmptyOrWhiteSpace(String as_string) {
        return (as_string != null && !" ".equals(as_string.replace(" ", "")));
    }
    
    private boolean addActivityData(WebServiceAuth ao_wsa, List<ActivityDataSOAP> ao_data) {
        try {
            ActivityData lo_ad = new ActivityData();
            ActivityDataSOAP lo_ads;
            User lo_user = io_u.find(ao_wsa.getUserid());
            
            for (int i = 0; i < ao_data.size(); i++) {
                lo_ads = ao_data.get(i);
                lo_ad.setCategoryUnitId(io_cf.find(lo_ads.categoryUnitId));
                
                if (isNotNullEmptyOrWhiteSpace(lo_ads.useremail) && isAdmin(lo_user)) {
                    lo_ad.setUserId(io_u.findUserByEmailOrNull(lo_ads.useremail));

                } else if (isNotNullEmptyOrWhiteSpace(lo_ads.useremail)) {
                    return false;
                }
                else {
                    lo_ad.setUserId(lo_user);
                }
                lo_ad.setValue(lo_ads.value);
                io_adf.create(lo_ad);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private WebServiceAuth getWebServiceAuth() {
        MessageContext lo_mctx = (MessageContext) io_wsctx.getMessageContext();
        Map lo_http_headers = (Map) lo_mctx.get(MessageContext.HTTP_REQUEST_HEADERS);
        List lo_authtokens = (List) lo_http_headers.get("authtoken");
        
        if (lo_authtokens.size() == 1) {
            return io_wsaf.find(lo_authtokens.get(0));
        }
        return null;
    }
    
    
}
