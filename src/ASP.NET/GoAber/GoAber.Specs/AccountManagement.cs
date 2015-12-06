using Microsoft.VisualStudio.TestTools.UnitTesting;
using OpenQA.Selenium;
using OpenQA.Selenium.Firefox;
using System;
using TechTalk.SpecFlow;

namespace GoAber.Specs
{
    [Binding]
    public class AccountManagement
    {
        IWebDriver driver;
        [Given(@"I navigate to the homepage")]
        public void GivenINavigateToTheHomepage()
        {
            driver = new FirefoxDriver();
            driver.Navigate().GoToUrl("https://http://localhost:50121");
        }

        [Then(@"I should see ""(.*)""")]
        public void ThenIShouldSee(string p0)
        {
            Assert.IsTrue(driver.PageSource.Contains(p0));
        }

        [Then(@"I should not see ""(.*)""")]
        public void ThenIShouldNotSee(string p0)
        {
            Assert.IsFalse(driver.PageSource.Contains(p0));
        }

        [Then(@"I click on ""(.*)""")]
        public void ThenIClickOn(string p0)
        {
            driver.FindElement(By.LinkText(p0)).Click();
        }
        
        [Then(@"I fill in ""(.*)"" with ""(.*)""")]
        public void ThenIFillInWith(string p0, string p1)
        {
            driver.FindElement(By.Id(p0)).SendKeys(p1);
        }
        
        [Then(@"I press ""(.*)""")]
        public void ThenIPress(string p0)
        {
            driver.FindElement(By.Id(p0)).Click();
        }
    }
}
