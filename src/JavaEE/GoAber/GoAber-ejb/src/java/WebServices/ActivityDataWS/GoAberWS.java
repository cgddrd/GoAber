/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServices.ActivityDataWS;

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
    UserFacade io_userFacade;
    
    @EJB
    CategoryUnitFacade io_catunitFacade;
    
    @EJB
    WebServiceAuthFacade io_wsaFacade;
    
    @EJB
    ActivityDataFacade io_activitydataFacade;
    
    @Resource
    WebServiceContext io_webservicecontext;
    
    /**
     * Receives activity data from a client.
     * If it validates then save it to the database.
     * @param data
     * @return 
     */
    @WebMethod(operationName = "addActivityData")
    public boolean addActivityData(@WebParam(name = "data") List<ActivityDataSOAP> data) 
    {
        WebServiceAuth lo_webserviceAuth = getWebServiceAuth();
        if (lo_webserviceAuth == null) {
            return false;
        }
        
        return addActivityData(lo_webserviceAuth, data);
    }
    
    public boolean isAdmin(User ao_user) {    
        
        return ao_user.getUserRoleId().getRoleId().getIdRole().equals("administrator");

    }
    
    public boolean isNotNullEmptyOrWhiteSpace(String as_string) {
        return (as_string != null && !" ".equals(as_string.replace(" ", "")));
    }
    
    private boolean addActivityData(WebServiceAuth ao_webserviceAuth, List<ActivityDataSOAP> ao_data) {
        try {
            ActivityData lo_activitydata = new ActivityData();
            ActivityDataSOAP lo_activitydataSoap;
            User lo_user = io_userFacade.find(ao_webserviceAuth.getUserid());
            
            for (int i = 0; i < ao_data.size(); i++) {
                lo_activitydataSoap = ao_data.get(i);
                lo_activitydata.setCategoryUnitId(io_catunitFacade.find(lo_activitydataSoap.categoryUnitId));
                
                if (isNotNullEmptyOrWhiteSpace(lo_activitydataSoap.useremail) && isAdmin(lo_user)) {
                    lo_activitydata.setUserId(io_userFacade.findUserByEmailOrNull(lo_activitydataSoap.useremail));

                } else if (isNotNullEmptyOrWhiteSpace(lo_activitydataSoap.useremail)) {
                    return false;
                }
                else {
                    lo_activitydata.setUserId(lo_user);
                }
                lo_activitydata.setDate(lo_activitydataSoap.date);
                lo_activitydata.setValue(lo_activitydataSoap.value);
                io_activitydataFacade.create(lo_activitydata);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private WebServiceAuth getWebServiceAuth() {
        MessageContext lo_mctx = (MessageContext) io_webservicecontext.getMessageContext();
        Map lo_http_headers = (Map) lo_mctx.get(MessageContext.HTTP_REQUEST_HEADERS);
        List lo_authtokens = (List) lo_http_headers.get("authtoken");
        
        if (lo_authtokens.size() == 1) {
            return io_wsaFacade.find(lo_authtokens.get(0));
        }
        return null;
    }
    
    
}
