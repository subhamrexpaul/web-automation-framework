package com.webautomation.pages;

import com.webautomation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * CheckoutPage - Page Object for the SauceDemo checkout flow.
 * Handles checkout step one (info), step two (overview), and completion.
 */
public class CheckoutPage {

    private static final Logger logger = LogManager.getLogger(CheckoutPage.class);
    private final WebDriver driver;

    // ==================== Step One: Your Information ====================

    @FindBy(id = "first-name")
    private WebElement firstNameInput;

    @FindBy(id = "last-name")
    private WebElement lastNameInput;

    @FindBy(id = "postal-code")
    private WebElement postalCodeInput;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "cancel")
    private WebElement cancelButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    // ==================== Step Two: Overview ====================

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "summary_subtotal_label")
    private WebElement subtotalLabel;

    @FindBy(className = "summary_tax_label")
    private WebElement taxLabel;

    @FindBy(className = "summary_total_label")
    private WebElement totalLabel;

    @FindBy(id = "finish")
    private WebElement finishButton;

    // ==================== Completion ====================

    @FindBy(className = "complete-header")
    private WebElement completeHeader;

    @FindBy(className = "complete-text")
    private WebElement completeText;

    @FindBy(id = "back-to-products")
    private WebElement backHomeButton;

    // ==================== Constructor ====================

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("CheckoutPage initialized");
    }

    // ==================== Step One Actions ====================

    /**
     * Fills in the checkout information form.
     *
     * @param firstName  customer first name
     * @param lastName   customer last name
     * @param postalCode customer postal/zip code
     */
    public CheckoutPage fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        WaitUtils.waitForVisibility(driver, firstNameInput);

        firstNameInput.clear();
        firstNameInput.sendKeys(firstName);

        lastNameInput.clear();
        lastNameInput.sendKeys(lastName);

        postalCodeInput.clear();
        postalCodeInput.sendKeys(postalCode);

        logger.info("Filled checkout info: {} {} {}", firstName, lastName, postalCode);
        return this;
    }

    /**
     * Clicks the Continue button on checkout step one.
     */
    public CheckoutPage clickContinue() {
        WaitUtils.waitForClickability(driver, continueButton);
        continueButton.click();
        logger.info("Clicked continue button");
        return this;
    }

    /**
     * Clicks the Cancel button.
     */
    public CartPage clickCancel() {
        WaitUtils.waitForClickability(driver, cancelButton);
        cancelButton.click();
        logger.info("Clicked cancel button");
        return new CartPage(driver);
    }

    /**
     * Completes step one by filling info and clicking Continue.
     */
    public CheckoutPage completeStepOne(String firstName, String lastName, String postalCode) {
        fillCheckoutInfo(firstName, lastName, postalCode);
        clickContinue();
        logger.info("Completed checkout step one");
        return this;
    }

    // ==================== Step Two Actions ====================

    /**
     * Clicks the Finish button on the checkout overview page.
     */
    public CheckoutPage clickFinish() {
        WaitUtils.waitForClickability(driver, finishButton);
        finishButton.click();
        logger.info("Clicked finish button");
        return this;
    }

    // ==================== Getters ====================

    /**
     * Gets the error message text.
     */
    public String getErrorMessage() {
        WaitUtils.waitForVisibility(driver, errorMessage);
        String text = errorMessage.getText();
        logger.info("Checkout error: {}", text);
        return text;
    }

    /**
     * Checks if an error message is displayed.
     */
    public boolean isErrorDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the page title text.
     */
    public String getPageTitleText() {
        WaitUtils.waitForVisibility(driver, pageTitle);
        return pageTitle.getText();
    }

    /**
     * Gets the subtotal text from the overview page.
     */
    public String getSubtotal() {
        WaitUtils.waitForVisibility(driver, subtotalLabel);
        return subtotalLabel.getText();
    }

    /**
     * Gets the tax text from the overview page.
     */
    public String getTax() {
        WaitUtils.waitForVisibility(driver, taxLabel);
        return taxLabel.getText();
    }

    /**
     * Gets the total text from the overview page.
     */
    public String getTotal() {
        WaitUtils.waitForVisibility(driver, totalLabel);
        return totalLabel.getText();
    }

    /**
     * Gets the completion header text (e.g., "Thank you for your order!").
     */
    public String getCompleteHeaderText() {
        WaitUtils.waitForVisibility(driver, completeHeader);
        String text = completeHeader.getText();
        logger.info("Order complete header: {}", text);
        return text;
    }

    /**
     * Gets the completion description text.
     */
    public String getCompleteText() {
        WaitUtils.waitForVisibility(driver, completeText);
        return completeText.getText();
    }

    /**
     * Clicks the Back Home button on the completion page.
     */
    public HomePage clickBackHome() {
        WaitUtils.waitForClickability(driver, backHomeButton);
        backHomeButton.click();
        logger.info("Clicked Back Home button");
        return new HomePage(driver);
    }
}
