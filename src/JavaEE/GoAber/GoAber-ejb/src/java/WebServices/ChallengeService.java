/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServices;

import DTO.IJobDetail;
import GoAberDatabase.Challenge;
import GoAberDatabase.Community;
import GoAberDatabase.CommunityChallenge;
import GoAberDatabase.GroupChallenge;
import GoAberDatabase.JobDetail;
import GoAberDatabase.Scheduletype;
import GoAberDatabase.Tasktype;
import GoAberDatabase.Team;
import GoAberDatabase.User;
import GoAberDatabase.UserChallenge;
import Jobs.ChallengeJob;
import Scheduling.Jobs.AbstractJob;
import SessionBean.ChallengeFacade;
import SessionBean.CommunityChallengeFacade;
import SessionBean.CommunityFacade;
import SessionBean.GroupChallengeFacade;
import SessionBean.SchedulerSessionBeanRemote;
import SessionBean.ScheduletypeFacade;
import SessionBean.TasktypeFacade;
import SessionBean.TeamFacade;
import SessionBean.UserChallengeFacade;
import WebServices.ChallengeWS.Consumer.ChallengeWSConsumer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Provides commonly used challenge methods
 * @author helen
 */
@Stateless
public class ChallengeService {

    @EJB ChallengeWSConsumer challengeWSConsumer;
    @EJB SchedulerSessionBeanRemote SchedSessBean;
    @EJB ScheduletypeFacade schedtypeFacade;
    @EJB TasktypeFacade tasktypeFacade;
    
    @EJB ChallengeFacade ejbFacade;// = lookupChallengeFacadeBean();
    @EJB CommunityChallengeFacade communityChallengeFacade;// = lookupCommunityChallengeFacadeBean();
    @EJB GroupChallengeFacade groupChallengeFacade;// = lookupGroupChallengeFacadeBean();
    @EJB CommunityFacade communityFacade;//=lookupCommunityFacadeBean();
    @EJB TeamFacade groupFacade;// = lookupTeamFacadeFacadeBean();
    @EJB UserChallengeFacade userChallengeFacade;// = lookupUserChallengeFacadeFacadeBean();
    

    public ChallengeService(){
//        ejbFacade = lookupChallengeFacadeBean();
//        communityChallengeFacade = lookupCommunityChallengeFacadeBean();
//        groupChallengeFacade = lookupGroupChallengeFacadeBean();
//        communityFacade=lookupCommunityFacadeBean();
//        groupFacade = lookupTeamFacadeFacadeBean();
//        userChallengeFacade = lookupUserChallengeFacadeFacadeBean();
    }
    
    // Added to allow the mocking of the facades in the test class
    public ChallengeService(ChallengeFacade ejbFacade,CommunityChallengeFacade communityChallengeFacade, 
                            GroupChallengeFacade groupChallengeFacade, CommunityFacade communityFacade, 
                             TeamFacade groupFacade, UserChallengeFacade userChallengeFacade){
        this.ejbFacade= ejbFacade;
        this.communityChallengeFacade= communityChallengeFacade;
        this.groupChallengeFacade= groupChallengeFacade;
        this.communityFacade= communityFacade;
        this.groupFacade= groupFacade;
        this.userChallengeFacade=userChallengeFacade;
    }
    
    public void AddChallengeJob(Challenge challenge) {
        JobDetail lo_job = new JobDetail();
        Scheduletype lo_stype = schedtypeFacade.find("O");
        Tasktype lo_ttype = tasktypeFacade.find("Challenge");
        lo_job.setSchedtype(lo_stype);
        lo_job.setTasktype(lo_ttype);
        lo_job.setStartnow(Boolean.TRUE);
        lo_job.setSchedulerArgs(new String[] { challenge.getIdChallenge() });
        lo_job.setShcedtimemins(1);
        
        AbstractJob lo_chaljob = new ChallengeJob(lo_job);
    }
    
    public String createChallengeID() {
       return UUID.randomUUID().toString();
    }
    
    /**
     * To view a list of the communities in a user interface a Map<String,Integer> should be produced
     * @return Map : community name, communityId
     */
    public Map<String,Integer> getCommunitiesValue() {
        List<Community> communities = communityFacade.findAll();
        Map<String,Integer> communitiesValue= new LinkedHashMap<>();
        communities.stream().forEach((community) -> {
            communitiesValue.put(community.getName(), community.getIdCommunity()); //label, value
        });
        return communitiesValue;
    }
    
     /**
     * To view a list of the groups in a user interface a Map<String,Integer> should be produced
     * @return Map : community name, groupId
     */
    public Map<String,Integer> getGroupValue(User user) {
        List<Team> groups = groupFacade.findByCommunity(user.getGroupId().getCommunityId());
        Map<String,Integer> groupValue = new LinkedHashMap<>();
        groups.stream().forEach((group) -> {
            groupValue.put(group.getName(), group.getIdGroup()); //label, value
        });
        return groupValue;
    }
    
    /**
     * Create the GroupChallenges from the list of groups that have been challenged
     * @param currentUser
     * @param challenge The challenge the groups will be added to
     * @param groups List of group IDs for the groups that have been entered into the challenge.
     * @return The collection of GroupChallenges which has been produced
     */
    public Collection<GroupChallenge> addGroupChallenges(User currentUser, Challenge challenge, Integer[] groups)
    {
        Collection<GroupChallenge> groupCollection = new ArrayList<>();
        //User currentUser = authController.getActiveUser();
        int usersGroup = -1;
        if(currentUser.getGroupId() != null)
        {
            usersGroup = currentUser.getGroupId().getIdGroup();
        }
        
        for (Integer groupId : groups)
        {
            Team group = groupFacade.findById(groupId);
            GroupChallenge groupChallenge = new GroupChallenge(group, challenge);
            
            if(groupId == usersGroup)
            {
                groupChallenge.setStartedChallenge(true);
            }
            groupChallengeFacade.create(groupChallenge);
            groupCollection.add(groupChallenge);
        }
        
        if(usersGroup != -1)
        {//(groups.length > 0) && 
            if((!Arrays.asList(groups).contains(usersGroup)))
            { // Add the current users group to the list of challenged groups
                Team group = groupFacade.findById(usersGroup);
                GroupChallenge groupChallenge = new GroupChallenge(group, challenge, true);
                groupChallengeFacade.create(groupChallenge);
                groupCollection.add(groupChallenge);
            }
        }
        return groupCollection;
    }
    
    /**
     * Create the CommunityChallenges from the list of communities that have been challenged
     * @param currentUser
     * @param challenge The challenge the communities will be added to
     * @param communities List of community IDs for the groups that have been entered into the challenge.
     * @param local Whether the call has come from a controller or a web service.
     * @return The collection of GroupChallenges which has been produced
     */
    public Collection<CommunityChallenge> addCommunityChallenges(User currentUser, Challenge challenge, Integer[] communities, boolean local)
    {
        Collection<CommunityChallenge> communityCollection = new ArrayList<>();
        //User currentUser = authController.getActiveUser();
        int usersCommunity = -1;
        if(currentUser.getGroupId() != null)
        {
            usersCommunity = currentUser.getGroupId().getCommunityId().getIdCommunity();
        }
        
        for (Integer communityId : communities)
        {
            Community group = communityFacade.findById(communityId);
            CommunityChallenge communityChallenge = new CommunityChallenge(group, challenge);
            
            if(communityId == usersCommunity)
            {
                communityChallenge.setStartedChallenge(true);
            }
            communityChallengeFacade.create(communityChallenge);
            communityCollection.add(communityChallenge);
        }
        
        if(usersCommunity != -1)
        {//(communities.length > 0) && 
            if((!Arrays.asList(communities).contains(usersCommunity)))
            {// Add the current users community to the list of challenged communities
                Community community = communityFacade.findById(usersCommunity);
                CommunityChallenge communityChallenge = new CommunityChallenge(community, challenge, true);
                communityChallengeFacade.create(communityChallenge);
                communityCollection.add(communityChallenge);
            }
        }
        
        if (local) 
        {
            boolean lb_goremote = false;
            for (Integer community : communities) {
                if (!Objects.equals(usersCommunity, community)) {
                    lb_goremote = true;
                }
            }
            if (!lb_goremote) return communityCollection;
            if (!challengeWSConsumer.addChallenge(challenge, communityCollection, usersCommunity)) {
                System.err.println("Failed to send challenge remotely");            
            }
        }
        
        return communityCollection;
    }
    
    
    public List<Challenge> getUnEnteredGroupChalleneges(User currentUser)
    {
        return ejbFacade.getUnEnteredGroupChalleneges(currentUser);
    }
    public List<Challenge> getUnEnteredCommunityChalleneges(User currentUser)
    {
        return ejbFacade.getUnEnteredCommunityChalleneges(currentUser);
    }
    public List<Challenge> getEnteredGroupChalleneges(User currentUser)
    {
        return ejbFacade.getEnteredGroupChalleneges(currentUser);
    }
    public List<Challenge> getEnteredCommunityChalleneges(User currentUser)
    {
        return ejbFacade.getEnteredCommunityChalleneges(currentUser);
    }
    
    
    public void enterChallege(Challenge challenge, User currentUser) {
        UserChallenge userChallenge = new UserChallenge(currentUser, challenge);
        userChallengeFacade.create(userChallenge);
    }
    
    public void leaveChallege(Challenge challenge, User currentUser) {
        UserChallenge userChallenge = userChallengeFacade.findByUserAndChallenge(currentUser, challenge);
        userChallengeFacade.remove(userChallenge);
    }
    
    
    
    private ChallengeFacade lookupChallengeFacadeBean() {
        try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(ChallengeFacade) ejbCtx.lookup("ChallengeFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private CommunityChallengeFacade lookupCommunityChallengeFacadeBean() {
        try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(CommunityChallengeFacade) ejbCtx.lookup("CommunityChallengeFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
     private TeamFacade lookupTeamFacadeFacadeBean() {
        try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(TeamFacade) ejbCtx.lookup("TeamFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private GroupChallengeFacade lookupGroupChallengeFacadeBean() {
        try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(GroupChallengeFacade) ejbCtx.lookup("GroupChallengeFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    private UserChallengeFacade lookupUserChallengeFacadeFacadeBean() {
        try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(UserChallengeFacade) ejbCtx.lookup("UserChallengeFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    
    private CommunityFacade lookupCommunityFacadeBean() {
        try {
            InitialContext iniCtx = new InitialContext();
            Context ejbCtx = (Context) iniCtx.lookup("java:comp/env/ejb");
            return(CommunityFacade) ejbCtx.lookup("CommunityFacade");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private ChallengeWSConsumer lookupChallengeWSConsumerBean() {
        try {
            Context c = new InitialContext();
            return (ChallengeWSConsumer) c.lookup("java:global/GoAber/GoAber-ejb/ChallengeWSConsumer!WebServices.ChallengeWS.Consumer.ChallengeWSConsumer");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
