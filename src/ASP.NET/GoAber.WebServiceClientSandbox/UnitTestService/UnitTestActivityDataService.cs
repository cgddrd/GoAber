using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using GoAber.WebServiceClientSandbox.Consumer;

/// <summary>
/// To run these test the GoAber project must be running and a user must be logged in.
/// </summary>
namespace UnitTestService
{
    [TestClass]
    public class UnitTestActivityDataService
    {
        GoAberWS.AuthHeader authentication;
        GoAberWS.ActivityData activityData;
        GoAberWS.ActivityData activityData2;

        [TestInitialize]
        public void TestSetup()
        {
            authentication = new GoAberWS.AuthHeader();
            authentication.authtoken = "admin_token";

            activityData = new GoAberWS.ActivityData();
            activityData.value = 4;
            activityData.date = DateTime.Now;
            activityData.categoryUnitId = 1;

            activityData2 = new GoAberWS.ActivityData();
            activityData2.value = 50;
            activityData2.date = DateTime.Now;
            activityData2.categoryUnitId = 2;
        }

        [TestMethod]
        public void TestAddSingleActivityData()
        {
            GoAberWS.ActivityData[] activityDataArray = { activityData };

            GoAberWS.GoAberWSSoapClient soapClient = new GoAberWS.GoAberWSSoapClient();
            bool result = soapClient.AddActivityData(authentication, activityDataArray);
           
            Assert.IsTrue(result);
        }

        [TestMethod]
        public void TestAddMultipleActivityData()
        {
            GoAberWS.ActivityData[] activityDataArray = { activityData, activityData2 };

            GoAberWS.GoAberWSSoapClient soapClient = new GoAberWS.GoAberWSSoapClient();
            bool result = soapClient.AddActivityData(authentication, activityDataArray);

            Assert.IsTrue(result);
        }

        [TestMethod]
        public void TestInvaildTokenAddActivityData()
        {
            authentication.authtoken = "invalid";

            GoAberWS.ActivityData[] activityDataArray = { activityData };

            GoAberWS.GoAberWSSoapClient soapClient = new GoAberWS.GoAberWSSoapClient();
            bool result = soapClient.AddActivityData(authentication, activityDataArray);

            Assert.IsFalse(result);
        }
    }
}
