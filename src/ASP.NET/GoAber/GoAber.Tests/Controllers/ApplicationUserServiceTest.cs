using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using GoAber.Models;
using GoAber.Services;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;

namespace GoAber.Tests.Controllers
{
    [TestClass]
    public class ApplicationUserServiceTest
    {

        private List<ApplicationUser> userList;
        private List<Team> teamList;
        private List<Community> communityList;
        private DbSet<ApplicationUser> mockSet;
        private DbSet<Team> teamMockSet;
        private DbSet<Community> communityMockSet;
        private ApplicationDbContext mockDBContext;
        private ApplicationUserService testApplicationUserService;


        [TestInitialize]
        public void setUp()
        {

            var mockContext = new Mock<ApplicationDbContext>();

            communityList = new List<Community>
            {
                new Community {Id = 1, name = "Community1", endpointUrl = "http://community1.com"},
                new Community {Id = 2, name = "Community2", endpointUrl = "http://community2.com"},
            };
            communityMockSet = TestUtilities.TestUtilities.GetQueryableMockDbSet(communityList, (keyValues, entity) => entity.Id == (int)keyValues.Single());
            mockContext.Setup(m => m.Communities).Returns(communityMockSet);

            teamList = new List<Team>
            {
                new Team
                {
                    Id = 1,
                    communityId = 1,
                    community = mockContext.Object.Communities.First(u => u.Id == 1),
                    name = "Community1Team1"
                },
                new Team
                {
                    Id = 2,
                    communityId = 2,
                    community = mockContext.Object.Communities.First(u => u.Id == 2),
                    name = "Community1Team2"
                }
            };

            teamMockSet = TestUtilities.TestUtilities.GetQueryableMockDbSet(teamList, (keyValues, entity) => entity.Id == (int)keyValues.Single());
            mockContext.Setup(m => m.Teams).Returns(teamMockSet);

            userList = new List<ApplicationUser>
            {
                new ApplicationUser
                {
                    Id = "1",
                    Email = "user1@test.com",
                    Nickname = "user1",
                    UserName = "user1",
                    TeamId = 1,
                    Team = mockContext.Object.Teams.First(u => u.Id == 1)
                },
                new ApplicationUser
                {
                    Id = "2",
                    Email = "user2@test.com",
                    Nickname = "user2",
                    UserName = "user2",
                    TeamId = 2,
                    Team = mockContext.Object.Teams.First(u => u.Id == 2)
                }
            };

            mockSet = TestUtilities.TestUtilities.GetQueryableMockDbSet(userList, (keyValues, entity) => entity.Id.Equals((string) keyValues.Single()));


            mockContext.Setup(m => m.ActivityDatas)
                .Returns(TestUtilities.TestUtilities.GetQueryableMockDbSet(new List<ActivityData>(),
                    (keyValues, entity) => entity.Id == (int)keyValues.Single()));

            mockContext.Setup(m => m.Categories)
                .Returns(TestUtilities.TestUtilities.GetQueryableMockDbSet(new List<Category>(),
                    (keyValues, entity) => entity.Id == (int)keyValues.Single()));

            mockContext.Setup(m => m.CategoryUnits)
               .Returns(TestUtilities.TestUtilities.GetQueryableMockDbSet(new List<CategoryUnit>(),
                   (keyValues, entity) => entity.Id == (int)keyValues.Single()));

            mockContext.Setup(m => m.Challenges)
               .Returns(TestUtilities.TestUtilities.GetQueryableMockDbSet(new List<Challenge>(),
                   (keyValues, entity) => entity.Id.Equals((string)keyValues.Single())));

            mockContext.Setup(m => m.Audit)
               .Returns(TestUtilities.TestUtilities.GetQueryableMockDbSet(new List<Audit>(),
                   (keyValues, entity) => entity.AuditId.Equals((Guid)keyValues.Single())));

            mockContext.Setup(m => m.DataRemovalAudits)
               .Returns(TestUtilities.TestUtilities.GetQueryableMockDbSet(new List<DataRemovalAudit>(),
                   (keyValues, entity) => entity.Id == (int)keyValues.Single()));

            mockContext.Setup(m => m.WebServiceAuths)
               .Returns(TestUtilities.TestUtilities.GetQueryableMockDbSet(new List<WebServiceAuth>(),
                   (keyValues, entity) => entity.appname.Equals((string) keyValues.Single())));

            mockContext.Setup(m => m.Users).Returns(mockSet);

            mockDBContext = mockContext.Object;

            mockDBContext.SaveChanges();

            testApplicationUserService = new ApplicationUserService(mockDBContext);

        }

        [TestMethod]
        public void TestGetApplicationUserByIdCorrectId()
        {
            var user = testApplicationUserService.GetApplicationUserById("1");

            Assert.IsNotNull(user);

            Assert.AreEqual(user.Email, "user1@test.com");
            Assert.AreEqual(user.UserName, "user1");
            Assert.AreEqual(user.Nickname, "user1");

        }

        [TestMethod]
        public void TestGetApplicationUserByIdIncorrectId()
        {
            var user = testApplicationUserService.GetApplicationUserById("28");

            Assert.IsNull(user);

        }

        [TestMethod]
        public void TestGetAllApplicationUsers()
        {
            var users = testApplicationUserService.GetAllApplicationUsers();

            Assert.IsNotNull(users);

            Assert.AreEqual(users.Count(), 2);

            Assert.AreEqual(users.First(u => u.Id.Equals("1")).Team.Id, 1);
            Assert.AreEqual(users.First(u => u.Id.Equals("2")).Team.Id, 2);

        }

        [TestMethod]
        public void TestDeleteApplicationUser()
        {

            Assert.AreEqual(2, testApplicationUserService.GetAllApplicationUsers().Count());

            Assert.IsTrue(testApplicationUserService.DeleteApplicationUser("1"));

            Assert.IsNotNull(testApplicationUserService.GetAllApplicationUsers());

            Assert.AreEqual(1, testApplicationUserService.GetAllApplicationUsers().Count());

            Assert.IsNotNull(testApplicationUserService.GetAllApplicationUsers().First(u => u.Id.Equals("2")));

        }
    }
}
