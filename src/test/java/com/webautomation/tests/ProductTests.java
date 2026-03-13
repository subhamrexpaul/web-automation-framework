package com.webautomation.tests;

import com.webautomation.base.BaseTest;
import com.webautomation.pages.HomePage;
import com.webautomation.pages.LoginPage;
import com.webautomation.pages.ProductPage;
import com.webautomation.reports.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * ProductTests - Test class for SauceDemo product detail page functionality.
 * Covers product navigation, detail display, add-to-cart from detail, and back navigation.
 */
@Listeners(TestListener.class)
public class ProductTests extends BaseTest {

    private HomePage homePage;
    private static final String TEST_PRODUCT = "Sauce Labs Backpack";

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        homePage = loginPage.login("standard_user", "secret_sauce");
    }

    // ==================== Test 14: Product Detail Navigation ====================
    @Test(priority = 1, groups = {"regression", "product"},
            description = "Verify clicking a product navigates to its detail page")
    public void testProductDetailNavigation() {
        ProductPage productPage = homePage.clickProduct(TEST_PRODUCT);

        String productName = productPage.getProductName();
        Assert.assertEquals(productName, TEST_PRODUCT,
                "Product detail page should display the correct product name");
    }

    // ==================== Test 15: Product Detail Info ====================
    @Test(priority = 2, groups = {"regression", "product"},
            description = "Verify product detail page shows name, description, and price")
    public void testProductDetailInfo() {
        ProductPage productPage = homePage.clickProduct(TEST_PRODUCT);

        Assert.assertNotNull(productPage.getProductName(),
                "Product name should be displayed");
        Assert.assertFalse(productPage.getProductDescription().isEmpty(),
                "Product description should not be empty");
        Assert.assertTrue(productPage.getProductPriceValue() > 0,
                "Product price should be greater than 0");
        Assert.assertTrue(productPage.isProductImageDisplayed(),
                "Product image should be displayed");
    }

    // ==================== Test 16: Add to Cart from Detail ====================
    @Test(priority = 3, groups = {"regression", "product"},
            description = "Verify adding product to cart from detail page updates cart badge")
    public void testAddToCartFromDetail() {
        ProductPage productPage = homePage.clickProduct(TEST_PRODUCT);

        Assert.assertTrue(productPage.isAddToCartDisplayed(),
                "Add to Cart button should be displayed");

        productPage.addToCart();

        Assert.assertEquals(productPage.getCartBadgeCount(), 1,
                "Cart badge should show 1 after adding product");
        Assert.assertTrue(productPage.isRemoveDisplayed(),
                "Remove button should replace Add to Cart button");
    }

    // ==================== Test 17: Back to Products ====================
    @Test(priority = 4, groups = {"regression", "product"},
            description = "Verify back button returns to inventory page from product detail")
    public void testBackToProducts() {
        ProductPage productPage = homePage.clickProduct(TEST_PRODUCT);

        Assert.assertEquals(productPage.getProductName(), TEST_PRODUCT,
                "Should be on product detail page");

        HomePage returnedHomePage = productPage.backToProducts();
        Assert.assertTrue(returnedHomePage.isPageLoaded(),
                "Should return to inventory/home page after clicking back");
        Assert.assertEquals(returnedHomePage.getProductCount(), 6,
                "All products should be displayed after returning");
    }
}
