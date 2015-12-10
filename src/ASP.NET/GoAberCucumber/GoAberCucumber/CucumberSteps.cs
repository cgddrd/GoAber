using Microsoft.VisualStudio.TestTools.UnitTesting;
using OpenQA.Selenium;
using OpenQA.Selenium.Firefox;
using OpenQA.Selenium.PhantomJS;
using System;
using System.Diagnostics;
using TechTalk.SpecFlow;
using System.Collections.ObjectModel;
using System.Collections.Generic;
using OpenQA.Selenium.Support.UI;

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
        public void ThenIPressSubmit()
        {
            driver.FindElement(By.ClassName("btn-default")).Click();
        }

        [Then(@"I press delete")]
        public void ThenIPressDelete()
        {
            driver.FindElement(By.ClassName("btn-danger")).Click();
        }        

        [Given(@"I am logged in")]
        public void GivenIAmLoggedIn()
        {
            driver.FindElement(By.Id("Email")).SendKeys("admin@test.com");
            driver.FindElement(By.Id("Password")).SendKeys("Hello123!");
            driver.FindElement(By.Id("Password")).SendKeys(OpenQA.Selenium.Keys.Enter);
        }

        [Then(@"I click on delete user for element (.*)")]
        public void ThenIClickOnDeleteUserForElement(int p0)
        {
            ReadOnlyCollection<IWebElement> listElements = driver.FindElements(By.PartialLinkText("Delete"));
            IEnumerator<IWebElement> elements = listElements.GetEnumerator();
            if (!driver.PageSource.Contains("crh13@aber.ac.uk"))
                return;
            for (var i = 1; i < p0; i++) {
                elements.MoveNext();
            }
            elements.Current.Click();
        }

        [Then(@"I click on delete team for element (.*)")]
        public void ThenIClickOnDeleteTeamForElement(int p0)
        {
            ReadOnlyCollection<IWebElement> listElements = driver.FindElements(By.PartialLinkText("Delete"));
            IEnumerator<IWebElement> elements = listElements.GetEnumerator();
            if (p0 > listElements.Count+1)
                return;
            for (var i = 1; i < p0; i++)
            {
                elements.MoveNext();
            }
            elements.Current.Click();
        }

        [Then(@"I click on edit for element (.*)")]
        public void ThenIClickOnEditForElement(int p0)
        {
            ReadOnlyCollection<IWebElement> listElements = driver.FindElements(By.PartialLinkText("Edit"));
            IEnumerator<IWebElement> elements = listElements.GetEnumerator();
            if (p0 > listElements.Count+1)
                return;
            for (var i = 0; i < p0; i++)
            {
                elements.MoveNext();
            }
            elements.Current.Click();
        }

        [Then(@"I select ""(.*)"" from ""(.*)""")]
        public void ThenISelectFrom(string p0, string p1)
        {
            SelectElement dropdown = new SelectElement(driver.FindElement(By.Id(p1)));
            dropdown.SelectByValue(p0);
        }

    }
}
