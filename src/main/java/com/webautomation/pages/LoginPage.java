package com.webautomation.pages;

import com.webautomation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * LoginPage - Page Object for the SauceDemo login page.
 * Encapsulates all login page elements and actions.
 */
public class LoginPage {

    private static final Logger logger = LogManager.getLogger(LoginPage.class);
    private final WebDriver driver;

    // ==================== Web Elements ====================

    @FindBy(id = "user-name")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    @FindBy(className = "login_logo")
    private WebElement loginLogo;

    @FindBy(id = "login_credentials")
    private WebElement loginCredentials;

    // ==================== Constructor ====================

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("LoginPage initialized");
    }

    // ==================== Actions ====================

    /**
     * Enters username into the username field.
     */
    public LoginPage enterUsername(String username) {
        WaitUtils.waitForVisibility(driver, usernameInput);
        usernameInput.clear();
        usernameInput.sendKeys(username);
        logger.info("Entered username: {}", username);
        return this;
    }

    /**
     * Enters password into the password field.
     */
    public LoginPage enterPassword(String password) {
        WaitUtils.waitForVisibility(driver, passwordInput);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        logger.info("Entered password: ****");
        return this;
    }

    /**
     * Clicks the login button.
     */
    public LoginPage clickLogin() {
        WaitUtils.waitForClickability(driver, loginButton);
        loginButton.click();
        logger.info("Clicked login button");
        return this;
    }

    /**
     * Performs a complete login action with given credentials.
     *
     * @param username the username
     * @param password the password
     * @return HomePage if login is successful
     */
    public HomePage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        logger.info("Login performed with user: {}", username);
        return new HomePage(driver);
    }

    /**
     * Performs login that is expected to fail (does not return HomePage).
     */
    public LoginPage loginExpectingFailure(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        logger.info("Login attempted (expecting failure) with user: {}", username);
        return this;
    }

    // ==================== Getters ====================

    /**
     * Gets the error message text displayed on login failure.
     */
    public String getErrorMessage() {
        WaitUtils.waitForVisibility(driver, errorMessage);
        String text = errorMessage.getText();
        logger.info("Error message: {}", text);
        return text;
    }

    /**
     * Checks if the error message is displayed.
     */
    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the login page is loaded by verifying the logo.
     */
    public boolean isPageLoaded() {
        try {
            return loginLogo.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the page title.
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
}
