/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import JSF.services.ActivityDataService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author samuel
 */

@Path("/API")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
@RequestScoped
public class ActivityDataAPI {
    
    @EJB
    private ActivityDataService dataService;
    
    @GET
    @Path("/WeeklySummary")
    @Produces(MediaType.APPLICATION_JSON)
    public ActivityDataDTO WeeklySummary(@Context HttpServletRequest req) {
        ActivityDataDTO dto = new ActivityDataDTO(dataService.findAll().get(0));
        return dto;
//        User user = getUserFromSession(req);
//        
//        if(user == null) {
//            return null;
//        }
//
//        Date startDate = getDateLastWeek();
//        Date endDate = new Date();
// 
//        List<ActivityData> activityData = dataService.findAllForUserInDateRange(user, startDate, endDate);
//        List<ActivityDataDTO> dto = formatActivityData(activityData);
//        return dto.get(0);
    }
    
    private User getUserFromSession(HttpServletRequest req) {
     	HttpSession session= req.getSession(true);
    	return (User) session.getAttribute("loggedInUser");
    }
    
    private List<ActivityDataDTO> formatActivityData(List<ActivityData> data) {
        List<ActivityDataDTO> formattedData = new ArrayList<>();
        
        for (ActivityData item : data) {
            ActivityDataDTO dto = new ActivityDataDTO(item);
            formattedData.add(dto);
        }
        
        return formattedData;
    }
    
    /**
     * Get the date one week prior to today
     * @return Date object for one week prior.
     */
    private Date getDateLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        // substract 7 days
        // If we give 7 there it will give 8 days back
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-6);
        return cal.getTime();
    }

}
