/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import JSF.util.StatisticsSummary;
import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import JSF.services.ActivityDataService;
import JSF.util.DateUtils;
import java.util.ArrayList;
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
    public List<ActivityDataDTO> weeklySummary(@Context HttpServletRequest req) {
        User user = getUserFromSession(req);
        
        if(user == null) {
            return null;
        }

        Date startDate = DateUtils.getDateLastWeek();
        Date endDate = new Date();
 
        List<ActivityData> activityData = dataService.findAllForUserInDateRange(user, startDate, endDate);
        List<ActivityDataDTO> dtos = formatActivityData(activityData);
        return dtos;
    }
    
    @GET
    @Path("/WeeklyStatistics")
    public StatisticsSummary weeklyStatistics (@Context HttpServletRequest req) {
        User user = getUserFromSession(req);
        
        if(user == null) {
            return null;
        }

        Date startDate = DateUtils.getDateLastWeek();
        Date endDate = new Date();
 
        return dataService.statisticsSummary(user, startDate, endDate);
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
}
