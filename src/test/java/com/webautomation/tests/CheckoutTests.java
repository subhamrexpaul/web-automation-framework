package com.webautomation.tests;

import com.webautomation.base.BaseTest;
import com.webautomation.pages.CartPage;
import com.webautomation.pages.CheckoutPage;
import com.webautomation.pages.HomePage;
import com.webautomation.pages.LoginPage;
import com.webautomation.reports.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * CheckoutTests - Test class for SauceDemo checkout flow.
 * Covers valid checkout, missing fields, cancel, complete order, and confirmation.
 */
@Listeners(TestListener.class)
public class CheckoutTests extends BaseTest {

    private HomePage homePage;
    private static final String PRODUCT = "Sauce Labs Backpack";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String POSTAL_CODE = "10001";

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void loginAndAddToCart() {
        LoginPage loginPage = new LoginPage(getDriver());
        homePage = loginPage.login("standard_user", "secret_sauce");
        homePage.addProductToCart(PRODUCT);
    }

    // ==================== Test 24: Checkout With Valid Info ====================
    @Test(priority = 1, groups = {"smoke", "checkout"},
            description = "Verify checkout proceeds to overview with valid information")
    public void testCheckoutWithValidInfo() {
        CartPage cartPage = homePage.goToCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        checkoutPage.completeStepOne(FIRST_NAME, LAST_NAME, POSTAL_CODE);

        Assert.assertEquals(checkoutPage.getPageTitleText(), "Checkout: Overview",
                "Should navigate to Checkout Overview after filling valid info");
    }

    // ==================== Test 25: Missing First Name ====================
    @Test(priority = 2, groups = {"regression", "checkout"},
            description = "Verify error when first name is missing during checkout")
    public void testCheckoutMissingFirstName() {
        CartPage cartPage = homePage.goToCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        checkoutPage.fillCheckoutInfo("", LAST_NAME, POSTAL_CODE);
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isErrorDisplayed(),
                "Error should be displayed for missing first name");
        Assert.assertTrue(checkoutPage.getErrorMessage().contains("First Name is required"),
                "Error should mention First Name is required");
    }

    // ==================== Test 26: Missing Last Name ====================
    @Test(priority = 3, groups = {"regression", "checkout"},
            description = "Verify error when last name is missing during checkout")
    public void testCheckoutMissingLastName() {
        CartPage cartPage = homePage.goToCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        checkoutPage.fillCheckoutInfo(FIRST_NAME, "", POSTAL_CODE);
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isErrorDisplayed(),
                "Error should be displayed for missing last name");
        Assert.assertTrue(checkoutPage.getErrorMessage().contains("Last Name is required"),
                "Error should mention Last Name is required");
    }

    // ==================== Test 27: Missing Postal Code ====================
    @Test(priority = 4, groups = {"regression", "checkout"},
            description = "Verify error when postal code is missing during checkout")
    public void testCheckoutMissingZip() {
        CartPage cartPage = homePage.goToCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        checkoutPage.fillCheckoutInfo(FIRST_NAME, LAST_NAME, "");
        checkoutPage.clickContinue();

        Assert.assertTrue(checkoutPage.isErrorDisplayed(),
                "Error should be displayed for missing postal code");
        Assert.assertTrue(checkoutPage.getErrorMessage().contains("Postal Code is required"),
                "Error should mention Postal Code is required");
    }

    // ==================== Test 28: Checkout Cancel ====================
    @Test(priority = 5, groups = {"regression", "checkout"},
            description = "Verify cancel button returns to cart page")
    public void testCheckoutCancel() {
        CartPage cartPage = homePage.goToCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        CartPage returnedCartPage = checkoutPage.clickCancel();
        Assert.assertTrue(returnedCartPage.isPageLoaded(),
                "Should return to cart page after clicking cancel");
        Assert.assertTrue(returnedCartPage.isProductInCart(PRODUCT),
                "Product should still be in cart after cancel");
    }

    // ==================== Test 29: Complete Order ====================
    @Test(priority = 6, groups = {"smoke", "checkout"},
            description = "Verify complete checkout flow shows Thank You confirmation")
    public void testCompleteOrder() {
        CartPage cartPage = homePage.goToCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        checkoutPage.completeStepOne(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        checkoutPage.clickFinish();

        String header = checkoutPage.getCompleteHeaderText();
        Assert.assertTrue(header.toLowerCase().contains("thank you"),
                "Completion header should contain 'Thank you'. Found: " + header);
    }

    // ==================== Test 30: Order Confirmation Details ====================
    @Test(priority = 7, groups = {"regression", "checkout"},
            description = "Verify order confirmation page shows dispatch message")
    public void testOrderConfirmationDetails() {
        CartPage cartPage = homePage.goToCart();
        CheckoutPage checkoutPage = cartPage.clickCheckout();

        checkoutPage.completeStepOne(FIRST_NAME, LAST_NAME, POSTAL_CODE);
        checkoutPage.clickFinish();

        String completeText = checkoutPage.getCompleteText();
        Assert.assertFalse(completeText.isEmpty(),
                "Completion text should not be empty");
        Assert.assertTrue(completeText.toLowerCase().contains("order"),
                "Confirmation should mention the order. Found: " + completeText);

        // Verify we can go back home
        HomePage returnedHome = checkoutPage.clickBackHome();
        Assert.assertTrue(returnedHome.isPageLoaded(),
                "Should return to home page from confirmation");
    }
}
