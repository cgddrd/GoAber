/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServices.ChallengeWS.Consumer;

import GoAberDatabase.CategoryUnit;
import GoAberDatabase.Challenge;
import GoAberDatabase.Community;
import GoAberDatabase.CommunityChallenge;
import GoAberDatabase.Result;
import SessionBean.CategoryUnitFacade;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import org.goaberchallenges.ChallengeData;
import org.goaberchallenges.GoAberChallengesWS;
import org.goaberchallenges.GoAberChallengesWSSoap;
import org.goaberchallenges.ResultData;

/**
 *
 * @author Dan
 */
//@Singleton
public class ChallengeWSConsumer {

    
    public ChallengeWSConsumer() {

    }

    public boolean addChallenge(Challenge ao_challenge, Collection<CommunityChallenge> ao_comCollection, int ai_userGroup) {
        try {

            List<ChallengeData> lo_chaldatalist = new ArrayList<>();
            for (CommunityChallenge lo_comchal : ao_comCollection) {
                if (lo_comchal.getCommunityId().getHome()) {
                    continue;
                }
                ChallengeData lo_chaldata = new ChallengeData();
                lo_chaldata.setCategoryUnitId(ao_challenge.getCategoryUnitId().getIdCategoryUnit());

                if (ao_challenge.getEndTime() != null) {
                    lo_chaldata.setEndTime(getGregorianDate(ao_challenge.getEndTime()));
                }
                lo_chaldata.setStartTime(getGregorianDate(ao_challenge.getStartTime()));
                lo_chaldata.setCommunityId(lo_comchal.getCommunityId().getIdCommunity());
                lo_chaldata.setId(ao_challenge.getIdChallenge());
                lo_chaldata.setName(ao_challenge.getName());
                lo_chaldatalist.add(lo_chaldata);

                boolean lb_res = getSOAPClient(lo_comchal.getCommunityId()).recieveChallenge(lo_chaldata, ai_userGroup);
                System.out.println("Service returned: " + lb_res);
                if (!lb_res) {
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Result sendResult(Result ao_res, Community ao_sendTo) {
        ResultData lo_resdata = new ResultData();
        lo_resdata.setCategoryUnitId(ao_res.getCategoryUnitId().getIdCategoryUnit());
        lo_resdata.setChallengeId(ao_res.getChallengeId().getIdChallenge());
        lo_resdata.setCommunityId(ao_res.getCommunityId().getIdCommunity());
        lo_resdata.setValue(ao_res.getValue());
        
        ResultData lo_resultresp = getSOAPClient(ao_sendTo).recieveResult(lo_resdata);
        
        Result lo_response = new Result();
        lo_response.setCommunityId(ao_sendTo);
        lo_response.setChallengeId(ao_res.getChallengeId());
        lo_response.setCategoryUnitId(ao_res.getCategoryUnitId());
        lo_response.setValue(lo_resultresp.getValue());
        
        return lo_response;
    }

    private XMLGregorianCalendar getGregorianDate(Date ada_date) {
        try {
            GregorianCalendar lo_gregcal = new GregorianCalendar();
            lo_gregcal.setTime(ada_date);
            XMLGregorianCalendar lo_xmlgc = DatatypeFactory.newInstance().newXMLGregorianCalendar(lo_gregcal);
            return lo_xmlgc;
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(ChallengeWSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private GoAberChallengesWSSoap getSOAPClient(Community ao_com) {
        try {
            GoAberChallengesWS lo_service = new GoAberChallengesWS();
            GoAberChallengesWSSoap lo_port = lo_service.getGoAberChallengesWSSoap();

            BindingProvider lo_bp = (BindingProvider) lo_port;
            URL lo_domain = new URL(ao_com.getDomain());
            URL lo_endpoint = new URL(lo_domain, ao_com.getChallengesEndpoint());
            lo_bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, lo_endpoint.toString());
            return lo_port;
        } catch (MalformedURLException ex) {
            Logger.getLogger(ChallengeWSConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
