package com.webautomation.tests;

import com.webautomation.base.BaseTest;
import com.webautomation.pages.HomePage;
import com.webautomation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.webautomation.reports.TestListener;

/**
 * LoginTests - Test class for SauceDemo login functionality.
 * Covers valid login, invalid credentials, locked user, empty fields, logout, and data-driven tests.
 */
@Listeners(TestListener.class)
public class LoginTests extends BaseTest {

    private static final String VALID_USERNAME = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";

    // ==================== Test 1: Valid Login ====================
    @Test(priority = 1, groups = {"smoke", "login"},
            description = "Verify successful login with valid standard_user credentials")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(homePage.isPageLoaded(), "Home page should be loaded after valid login");
        Assert.assertEquals(homePage.getPageTitleText(), "Products",
                "Page title should be 'Products' after login");
    }

    // ==================== Test 2: Invalid Password ====================
    @Test(priority = 2, groups = {"smoke", "login"},
            description = "Verify error message on login with wrong password")
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure(VALID_USERNAME, "wrong_password");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for invalid password");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"),
                "Error should mention credentials mismatch");
    }

    // ==================== Test 3: Empty Username ====================
    @Test(priority = 3, groups = {"smoke", "login"},
            description = "Verify error message when username is empty")
    public void testEmptyUsername() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure("", VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for empty username");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"),
                "Error should mention username is required");
    }

    // ==================== Test 4: Empty Password ====================
    @Test(priority = 4, groups = {"smoke", "login"},
            description = "Verify error message when password is empty")
    public void testEmptyPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure(VALID_USERNAME, "");

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for empty password");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"),
                "Error should mention password is required");
    }

    // ==================== Test 5: Locked Out User ====================
    @Test(priority = 5, groups = {"smoke", "login"},
            description = "Verify error message for locked_out_user")
    public void testLockedOutUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginExpectingFailure("locked_out_user", VALID_PASSWORD);

        Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                "Error message should be displayed for locked out user");
        Assert.assertTrue(loginPage.getErrorMessage().contains("locked out"),
                "Error should mention user is locked out");
    }

    // ==================== Test 6: Logout ====================
    @Test(priority = 6, groups = {"smoke", "login"},
            description = "Verify user can successfully logout after login")
    public void testLogout() {
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = loginPage.login(VALID_USERNAME, VALID_PASSWORD);

        Assert.assertTrue(homePage.isPageLoaded(), "Should be on home page after login");

        LoginPage loginPageAfterLogout = homePage.logout();
        Assert.assertTrue(loginPageAfterLogout.isPageLoaded(),
                "Should be back on login page after logout");
    }

    // ==================== Test 7: Data-Driven Login ====================
    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {"standard_user", "secret_sauce", true, "Valid standard user"},
                {"locked_out_user", "secret_sauce", false, "Locked out user"},
                {"problem_user", "secret_sauce", true, "Problem user"},
                {"performance_glitch_user", "secret_sauce", true, "Performance glitch user"},
                {"invalid_user", "secret_sauce", false, "Invalid user"},
                {"standard_user", "wrong_pass", false, "Wrong password"},
                {"", "secret_sauce", false, "Empty username"},
                {"standard_user", "", false, "Empty password"},
        };
    }

    @Test(priority = 7, dataProvider = "loginData", groups = {"regression", "login"},
            description = "Data-driven login tests with multiple credential combinations")
    public void testLoginDataDriven(String username, String password,
                                     boolean expectedSuccess, String scenario) {
        LoginPage loginPage = new LoginPage(getDriver());

        if (expectedSuccess) {
            HomePage homePage = loginPage.login(username, password);
            Assert.assertTrue(homePage.isPageLoaded(),
                    "Login should succeed for scenario: " + scenario);
        } else {
            loginPage.loginExpectingFailure(username, password);
            Assert.assertTrue(loginPage.isErrorMessageDisplayed(),
                    "Error should be displayed for scenario: " + scenario);
        }
    }
}
