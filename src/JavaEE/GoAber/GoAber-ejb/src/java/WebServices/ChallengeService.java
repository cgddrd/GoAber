/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebServices;

import GoAberDatabase.Challenge;
import GoAberDatabase.Community;
import GoAberDatabase.CommunityChallenge;
import GoAberDatabase.GroupChallenge;
import GoAberDatabase.Team;
import GoAberDatabase.User;
import SessionBean.ChallengeFacade;
import SessionBean.CommunityChallengeFacade;
import SessionBean.CommunityFacade;
import SessionBean.GroupChallengeFacade;
import SessionBean.TeamFacade;
import SessionBean.UserChallengeFacade;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author helen
 */

public class ChallengeService {
    @EJB ChallengeFacade ejbFacade;
    @EJB CommunityChallengeFacade communityChallengeFacade = lookupCommunityChallengeFacadeBean();
    @EJB GroupChallengeFacade groupChallengeFacade = lookupGroupChallengeFacadeBean();
    @EJB CommunityFacade communityFacade=lookupCommunityFacadeBean();
    @EJB TeamFacade groupFacade = lookupTeamFacadeFacadeBean();
    @EJB UserChallengeFacade userChallengeFacade = lookupUserChallengeFacadeFacadeBean();
    
    
    
    
    public Map<String,Integer> getCommunitiesValue() {
        List<Community> communities = communityFacade.findAll();
        Map<String,Integer> communitiesValue= new LinkedHashMap<>();
        communities.stream().forEach((community) -> {
            communitiesValue.put(community.getName(), community.getIdCommunity()); //label, value
        });
        return communitiesValue;
    }
    
    public Map<String,Integer> getGroupValue() {
        List<Team> groups = groupFacade.findAll();
        Map<String,Integer> groupValue = new LinkedHashMap<>();
        groups.stream().forEach((group) -> {
            groupValue.put(group.getName(), group.getIdGroup()); //label, value
        });
        return groupValue;
    }
    
    
    public Collection<GroupChallenge> addGroupChallenges(User currentUser, Challenge challenge, Integer[] groupChallenges)
    {
        Collection<GroupChallenge> groupCollection = new ArrayList<>();
        //User currentUser = authController.getActiveUser();
        int usersGroup = -1;
        if(currentUser.getGroupId() != null)
        {
            usersGroup = currentUser.getGroupId().getIdGroup();
        }
        
        for (Integer groupId : groupChallenges)
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
        {
            if((groupChallenges.length > 0) && (!Arrays.asList(groupChallenges).contains(usersGroup)))
            {
                Team group = groupFacade.findById(usersGroup);
                GroupChallenge groupChallenge = new GroupChallenge(group, challenge, true);
                groupChallengeFacade.create(groupChallenge);
                groupCollection.add(groupChallenge);
            }
        }
        return groupCollection;
    }
    
    public Collection<CommunityChallenge> addCommunityChallenges(User currentUser, Challenge challenge, Integer[] communityChallenges)
    {
        Collection<CommunityChallenge> communityCollection = new ArrayList<>();
        //User currentUser = authController.getActiveUser();
        int usersCommunity = -1;
        if(currentUser.getGroupId() != null)
        {
            usersCommunity = currentUser.getGroupId().getCommunityId().getIdCommunity();
        }
        
        
        for (Integer communityId : communityChallenges)
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
        {
            if((communityChallenges.length > 0) && (!Arrays.asList(communityChallenges).contains(usersCommunity)))
            {
                Community community = communityFacade.findById(usersCommunity);
                CommunityChallenge communityChallenge = new CommunityChallenge(community, challenge, true);
                communityChallengeFacade.create(communityChallenge);
                communityCollection.add(communityChallenge);
            }
        }
        return communityCollection;
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
}
