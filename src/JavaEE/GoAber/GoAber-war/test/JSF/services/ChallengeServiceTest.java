/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JSF.services;

import GoAberDatabase.Challenge;
import GoAberDatabase.Community;
import GoAberDatabase.CommunityChallenge;
import GoAberDatabase.GroupChallenge;
import GoAberDatabase.Team;
import GoAberDatabase.User;
import SessionBean.ActivityDataFacade;
import SessionBean.ChallengeFacade;
import SessionBean.CommunityChallengeFacade;
import SessionBean.CommunityFacade;
import SessionBean.GroupChallengeFacade;
import SessionBean.TeamFacade;
import SessionBean.UserChallengeFacade;
import WebServices.ChallengeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.isA;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author helen
 */
@RunWith(MockitoJUnitRunner.class)
public class ChallengeServiceTest {
    
    private ChallengeService challengeService;
    @Mock
    private ChallengeFacade facadeMock;
    @Mock
    private CommunityFacade communityFacade;
    @Mock
    CommunityChallengeFacade communityChallengeFacade;
    @Mock
    GroupChallengeFacade groupChallengeFacade;
    @Mock
    TeamFacade groupFacade;
    @Mock
    UserChallengeFacade userChallengeFacade;
        
    User u;
    List<Team> groups;
    List<Community> communities;
    
    public ChallengeServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        facadeMock = mock(ChallengeFacade.class);
        communityFacade = mock(CommunityFacade.class);
        groupChallengeFacade = mock(GroupChallengeFacade.class);
        communityChallengeFacade = mock(CommunityChallengeFacade.class);
        groupFacade = mock(TeamFacade.class);
        userChallengeFacade = mock(UserChallengeFacade.class);
        
        challengeService = new ChallengeService(facadeMock, communityChallengeFacade, 
                            groupChallengeFacade, communityFacade, 
                             groupFacade, userChallengeFacade);
        
        communities = new ArrayList<>();
        Community c = new Community(1, "Community1");
        communities.add(c);
        c = new Community(2, "Community2");
        communities.add(c);
        
        groups = new ArrayList<>();
        Team team = new Team(1, "Team1");
        team.setCommunityId(c);
        groups.add(team);
        team = new Team(2, "Team2");
        team.setCommunityId(c);
        groups.add(team);
        
        
        u = new User();
        u.setGroupId(team);
        
        when(groupFacade.findByCommunity(any())).thenReturn(groups);
        when(communityFacade.findAll()).thenReturn(communities);
        when(groupFacade.findById(team.getIdGroup())).thenReturn(team);
        when(groupFacade.findById(groups.get(0).getIdGroup())).thenReturn(groups.get(0));
        when(communityFacade.findById(c.getIdCommunity())).thenReturn(c);
        when(communityFacade.findById(communities.get(0).getIdCommunity())).thenReturn(communities.get(0));
        //doNothing(groupChallengeFacade.create(isA(GroupChallenge.class)));
    }
    
    
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testGetGroupValues() {      
        Map<String,Integer> result = challengeService.getGroupValue(u);       
        
        Assert.assertEquals(1, result.get("Team1").intValue());
        Assert.assertEquals(2, result.get("Team2").intValue());
    }
    
    
    @Test
    public void testGetCommunityValues() {      
        Map<String,Integer> result = challengeService.getCommunitiesValue();       
        
        Assert.assertEquals(1, result.get("Community1").intValue());
        Assert.assertEquals(2, result.get("Community2").intValue());
    }
    
    /////Tests for addGroupChallenges()/////////
    
    @Test
    public void testAddGroupChallengesNoGroups(){//Only user's Group should be added 
        Challenge challenge = new Challenge();
        Collection<GroupChallenge> result = challengeService.addGroupChallenges(u, challenge, new Integer[0]);
        
        Assert.assertEquals(1, result.size());
        GroupChallenge groupChallenge = (GroupChallenge) result.toArray()[0];
        Assert.assertEquals(u.getGroupId(), groupChallenge.getGroupId());
    }
    
    @Test
    public void testAddGroupChallengesWithUsersGroup(){//Only user's Group should be added 
        Challenge challenge = new Challenge();
        Integer groupIds[] = {u.getGroupId().getIdGroup()};
        Collection<GroupChallenge> result = challengeService.addGroupChallenges(u, challenge, groupIds);
        
        Assert.assertEquals(1, result.size());
        GroupChallenge groupChallenge = (GroupChallenge) result.toArray()[0];
        Assert.assertEquals(u.getGroupId(), groupChallenge.getGroupId());
    }
    
    @Test
    public void testAddGroupChallengesWithMultipleGroups(){//Only user's Group should be added 
        Challenge challenge = new Challenge();
        Integer groupIds[] = {groups.get(0).getIdGroup(), groups.get(1).getIdGroup()};
        Collection<GroupChallenge> result = challengeService.addGroupChallenges(u, challenge, groupIds);
        
        Assert.assertEquals(2, result.size());
        GroupChallenge groupChallenge = (GroupChallenge) result.toArray()[0];
        Assert.assertEquals(groups.get(0), groupChallenge.getGroupId());
        groupChallenge = (GroupChallenge) result.toArray()[1];
        Assert.assertEquals(groups.get(1), groupChallenge.getGroupId());
    }
    
    /////Tests for addCommunitiesChallenges()/////////
    
    @Test
    public void testAddCommunitiesChallengesNoCommunities(){//Only user's Group should be added 
        Challenge challenge = new Challenge();
        Collection<CommunityChallenge> result = challengeService.addCommunityChallenges(u, challenge, new Integer[0]);
        
        Assert.assertEquals(1, result.size());
        CommunityChallenge communityChallenge = (CommunityChallenge) result.toArray()[0];
        Assert.assertEquals(u.getGroupId().getCommunityId(), communityChallenge.getCommunityId());
    }
    
    @Test
    public void testAddCommunityChallengesWithUsersCommunity(){//Only user's Group should be added 
        Challenge challenge = new Challenge();
        Integer communityIds[] = {u.getGroupId().getCommunityId().getIdCommunity()};
        Collection<CommunityChallenge> result = challengeService.addCommunityChallenges(u, challenge, communityIds);
        
        Assert.assertEquals(1, result.size());
        CommunityChallenge communityChallenge = (CommunityChallenge) result.toArray()[0];
        Assert.assertEquals(u.getGroupId().getCommunityId(), communityChallenge.getCommunityId());
    }
    
    @Test
    public void testAddCommunityChallengesWithMultipleCommunities(){
        Challenge challenge = new Challenge();
        Integer communityIds[] = {communities.get(0).getIdCommunity(), communities.get(1).getIdCommunity()};
        Collection<CommunityChallenge> result = challengeService.addCommunityChallenges(u, challenge, communityIds);
        
        Assert.assertEquals(2, result.size());
        CommunityChallenge communityChallenge = (CommunityChallenge) result.toArray()[0];
        Assert.assertEquals(communities.get(0), communityChallenge.getCommunityId());
        communityChallenge = (CommunityChallenge) result.toArray()[1];
        Assert.assertEquals(communities.get(1), communityChallenge.getCommunityId());
    }
}
