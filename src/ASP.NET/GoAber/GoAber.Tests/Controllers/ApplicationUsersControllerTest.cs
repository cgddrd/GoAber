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
    }
}
