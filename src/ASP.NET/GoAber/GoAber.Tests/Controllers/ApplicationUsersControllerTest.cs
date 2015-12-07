using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web.Mvc;
using GoAber.Areas.Admin.Controllers;
using GoAber.Controllers;
using GoAber.Models;
using GoAber.Services;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;

namespace GoAber.Tests.Controllers
{
    [TestClass]
    public class ApplicationUsersControllerTest
    {
        [TestMethod]
        public void IndexPageId()
        {
            ApplicationUsersController controller = new ApplicationUsersController();

            ViewResult result = controller.Index(1) as ViewResult;

            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void IndexPageIdNull()
        {
            ApplicationUsersController controller = new ApplicationUsersController();

            ViewResult result = controller.Index(null) as ViewResult;

            Assert.IsNotNull(result);

        }

        [TestMethod]
        public void test()
        {
            //var mockSet = new Mock<DbSet<ApplicationUser>>();

            List<ApplicationUser> userList = new List<ApplicationUser>
            {
                new ApplicationUser
                {
                    Id = "1",
                    Email = "connor@test.com",
                    Nickname = "connor",
                    UserName = "connor"
                }
            };

            DbSet<ApplicationUser> mockSet = TestUtilities.TestUtilities.GetQueryableMockDbSet(userList);
            var mockContext = new Mock<ApplicationDbContext>();
            mockContext.Setup(m => m.Users).Returns(mockSet);

            //mockSet.Object.Add(new ApplicationUser
            //{
            //    Id = "1",
            //    Email = "connor@test.com",
            //    Nickname = "connor",
            //    UserName = "connor"
            //});

            //mockContext.Object.Users.Add(new ApplicationUser
            //{
            //    Id = "1",
            //    Email = "connor@test.com",
            //    Nickname = "connor",
            //    UserName = "connor"
            //});

           // mockContext.Object.SaveChanges();

            var service = new ApplicationUserService(mockContext.Object);

            var user = service.GetApplicationUserById("1");

            Assert.IsNotNull(user);

            Assert.AreEqual(user.Nickname, "connor");

        }

    }
}
