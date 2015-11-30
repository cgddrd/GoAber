/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import JSF.ViewModels.StatisticsSummary;
import GoAberDatabase.ActivityData;
import GoAberDatabase.User;
import JSF.services.ActivityDataService;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/** Activity data REST API
 * 
 * This is used to provide a JSON interface to the database so that AJAX calls 
 * can be made. There is no support for authentication here, so the user must
 * have a valid session in order to access their data.
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
    public List<ActivityDataDTO> weeklySummary(
            @Context HttpServletRequest req,
            @QueryParam("unit") String unit) {
        User user = getUserFromSession(req);
        
        if(user == null) {
            return null;
        }
        
        return formatActivityData(dataService.weeklySummary(user, unit));
    }
    
    @GET
    @Path("/WeeklyStatistics")
    public StatisticsSummary weeklyStatistics(
            @Context HttpServletRequest req,
            @QueryParam("unit") String unit) {
        User user = getUserFromSession(req);
        
        if(user == null) {
            return null;
        }

        return dataService.weeklyStatistics(user, unit);
    }
    
    @GET
    @Path("/MonthlySummary")
    public List<ActivityDataDTO> monthlySummary(
                @Context HttpServletRequest req, 
                @QueryParam("unit") String unit) {
        User user = getUserFromSession(req);
        
        if(user == null) {
            return null;
        }

        return formatActivityData(dataService.monthlySummary(user, unit));
    }
    
    
    @GET
    @Path("/MonthlyStatistics")
    public StatisticsSummary monthlyStatistics(
            @Context HttpServletRequest req,
            @QueryParam("unit") String unit) {
        User user = getUserFromSession(req);
        
        if(user == null) {
            return null;
        }

        return dataService.monthlyStatistics(user, unit);
    }
    
    @GET
    @Path("/AllTimeStatistics")
    public StatisticsSummary allTimeStatistics(
            @Context HttpServletRequest req,
            @QueryParam("unit") String unit) {
        User user = getUserFromSession(req);
        
        if(user == null) {
            return null;
        }

        return dataService.allTimeStatistics(user, unit);
    }
    
        @GET
    @Path("/UserSummary")
    public StatisticsSummary allTimeStatistics(
            @Context HttpServletRequest req,
            @QueryParam("user") int id,
            @QueryParam("unit") String unit) {
        
        
        return dataService.userSummary(id, unit);
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
