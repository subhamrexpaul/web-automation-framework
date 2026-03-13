package com.webautomation.tests;

import com.webautomation.base.BaseTest;
import com.webautomation.pages.CartPage;
import com.webautomation.pages.HomePage;
import com.webautomation.pages.LoginPage;
import com.webautomation.reports.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

/**
 * CartTests - Test class for SauceDemo shopping cart functionality.
 * Covers add/remove items, cart count, item details, continue shopping, and empty cart.
 */
@Listeners(TestListener.class)
public class CartTests extends BaseTest {

    private HomePage homePage;
    private static final String PRODUCT_1 = "Sauce Labs Backpack";
    private static final String PRODUCT_2 = "Sauce Labs Bike Light";
    private static final String PRODUCT_3 = "Sauce Labs Bolt T-Shirt";

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        homePage = loginPage.login("standard_user", "secret_sauce");
    }

    // ==================== Test 18: Add Single Item to Cart ====================
    @Test(priority = 1, groups = {"smoke", "cart"},
            description = "Verify adding a single item to cart updates badge count to 1")
    public void testAddSingleItemToCart() {
        homePage.addProductToCart(PRODUCT_1);

        Assert.assertEquals(homePage.getCartBadgeCount(), 1,
                "Cart badge should show 1 after adding one item");
    }

    // ==================== Test 19: Add Multiple Items to Cart ====================
    @Test(priority = 2, groups = {"regression", "cart"},
            description = "Verify adding 3 items to cart updates badge count to 3")
    public void testAddMultipleItemsToCart() {
        homePage.addProductToCart(PRODUCT_1);
        homePage.addProductToCart(PRODUCT_2);
        homePage.addProductToCart(PRODUCT_3);

        Assert.assertEquals(homePage.getCartBadgeCount(), 3,
                "Cart badge should show 3 after adding three items");
    }

    // ==================== Test 20: Remove Item from Cart ====================
    @Test(priority = 3, groups = {"regression", "cart"},
            description = "Verify removing an item from cart decreases the count")
    public void testRemoveItemFromCart() {
        homePage.addProductToCart(PRODUCT_1);
        homePage.addProductToCart(PRODUCT_2);
        Assert.assertEquals(homePage.getCartBadgeCount(), 2, "Badge should show 2");

        CartPage cartPage = homePage.goToCart();
        cartPage.removeItem(PRODUCT_1);

        Assert.assertEquals(cartPage.getCartItemCount(), 1,
                "Cart should have 1 item after removing one");
        Assert.assertFalse(cartPage.isProductInCart(PRODUCT_1),
                "Removed product should not be in cart");
        Assert.assertTrue(cartPage.isProductInCart(PRODUCT_2),
                "Other product should still be in cart");
    }

    // ==================== Test 21: Cart Item Details ====================
    @Test(priority = 4, groups = {"regression", "cart"},
            description = "Verify item name and price are correctly displayed in cart")
    public void testCartItemDetails() {
        homePage.addProductToCart(PRODUCT_1);
        CartPage cartPage = homePage.goToCart();

        List<String> itemNames = cartPage.getCartItemNames();
        Assert.assertTrue(itemNames.contains(PRODUCT_1),
                "Cart should contain the added product name");

        List<String> itemPrices = cartPage.getCartItemPrices();
        Assert.assertFalse(itemPrices.isEmpty(),
                "Cart item prices should not be empty");
        Assert.assertTrue(itemPrices.get(0).contains("$"),
                "Price should contain dollar sign");
    }

    // ==================== Test 22: Continue Shopping ====================
    @Test(priority = 5, groups = {"regression", "cart"},
            description = "Verify Continue Shopping button returns to inventory page")
    public void testContinueShopping() {
        homePage.addProductToCart(PRODUCT_1);
        CartPage cartPage = homePage.goToCart();

        Assert.assertTrue(cartPage.isPageLoaded(), "Should be on cart page");

        HomePage returnedHomePage = cartPage.continueShopping();
        Assert.assertTrue(returnedHomePage.isPageLoaded(),
                "Should return to inventory page after Continue Shopping");
        Assert.assertEquals(returnedHomePage.getProductCount(), 6,
                "All products should be visible");
    }

    // ==================== Test 23: Empty Cart ====================
    @Test(priority = 6, groups = {"regression", "cart"},
            description = "Verify cart shows empty after removing all items")
    public void testEmptyCart() {
        homePage.addProductToCart(PRODUCT_1);
        homePage.addProductToCart(PRODUCT_2);

        CartPage cartPage = homePage.goToCart();
        Assert.assertEquals(cartPage.getCartItemCount(), 2, "Cart should have 2 items");

        cartPage.removeItem(PRODUCT_1);
        cartPage.removeItem(PRODUCT_2);

        Assert.assertTrue(cartPage.isCartEmpty(),
                "Cart should be empty after removing all items");
    }
}
