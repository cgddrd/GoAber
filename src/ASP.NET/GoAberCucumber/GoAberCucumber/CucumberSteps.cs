using Microsoft.VisualStudio.TestTools.UnitTesting;
using OpenQA.Selenium;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.PhantomJS;
using System;
using System.Diagnostics;
using TechTalk.SpecFlow;

namespace GoAber.Specs
{
    [Binding]
    public class CucumberSteps
    {
        String address = "http://localhost:50121";

        IWebDriver driver;
        [Given(@"I navigate to the homepage")]
        public void GivenINavigateToTheHomepage()
        {
            driver = new PhantomJSDriver();
            driver.Manage().Window.Maximize();
            driver.Navigate().GoToUrl(address);
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
            IWebElement element = driver.FindElement(By.PartialLinkText(p0));
            element.Click();
        }

        [Then(@"I go to ""(.*)""")]
        public void ThenIGoTo(string p0)
        {
            String link = "http://localhost:50121/" + p0;
            driver.Navigate().GoToUrl(link);
        }

        [Then(@"I fill in ""(.*)"" with ""(.*)""")]
        public void ThenIFillInWith(string p0, string p1)
        {
            driver.FindElement(By.Id(p0)).Clear();
            driver.FindElement(By.Id(p0)).SendKeys(p1);
        }

        [Then(@"I press enter inside ""(.*)""")]
        public void ThenIPressEnterInside(string p0)
        {
            driver.FindElement(By.Id(p0)).SendKeys(OpenQA.Selenium.Keys.Enter);
        }        

        [Then(@"I press ""(.*)""")]
        public void ThenIPress(string p0)
        {
            driver.FindElement(By.LinkText(p0)).Submit();
        }

        [Then(@"I press submit")]
        public void ThenIPressSubmit(string p0)
        {
            driver.FindElement(By.ClassName("btn btn-default")).Click();
        }

        [Then(@"I press delete")]
        public void ThenIPressDelete(string p0)
        {
            driver.FindElement(By.ClassName("btn btn-danger")).Click();
        }        

        [Given(@"I am logged in")]
        public void GivenIAmLoggedIn()
        {
            driver.FindElement(By.Id("Email")).SendKeys("admin@test.com");
            driver.FindElement(By.Id("Password")).SendKeys("Hello123!");
            driver.FindElement(By.Id("Password")).SendKeys(OpenQA.Selenium.Keys.Enter);
        }

    }
}
