package testCases;

import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pageObjects.AccountRegistration;
import pageObjects.HomePage;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

/*Development of hybrid framework
1.Create BasePage under 'pageObjects' which includes only constructor, this will be invoked by every page object class constructor(Re- usability).BaseClas.
2.Create pageObject classes for homePage,registrationPage under pageObject package(These will extends from basePage)
3.Create AccountRegistartionTest under  "TestCase pacakage"
4.Create BaseClass under testBase package and copy re-usabale methods
 */

public class TC001_AccountRegistration extends BaseClass {

    @Test(groups = "sanity")
    public void verify_AccRegistraion() {
        HomePage hp = new HomePage(driver);
        hp.clickMyAccount();
        hp.clickRegister();

        AccountRegistration rePage = new AccountRegistration(driver);
        rePage.setFirstName("Abcd");
        rePage.setLastname("Abcdd");
        rePage.setEmail("abcddddddd@gmail.com");
        rePage.setTelephone("123456789");
        rePage.setPwd("454545");
        rePage.setCpwd("454545");
        rePage.setPrivacyPolicy();
        rePage.setcontButton();
        String confirmationmsg = rePage.getconfirmationmsg();
        Assert.assertEquals(confirmationmsg, "Your Account Has Been Created!");
    }

}


