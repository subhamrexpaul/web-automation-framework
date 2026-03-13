package com.webautomation.tests;

import com.webautomation.base.BaseTest;
import com.webautomation.pages.HomePage;
import com.webautomation.pages.LoginPage;
import com.webautomation.reports.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;

/**
 * SearchTests - Test class for SauceDemo filter/sort and cross-feature regression scenarios.
 * Covers filter reset on re-login, add-all-to-cart, and remove-from-inventory toggle.
 */
@Listeners(TestListener.class)
public class SearchTests extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        homePage = loginPage.login("standard_user", "secret_sauce");
    }

    // ==================== Test 31: Filter Reset After Re-login ====================
    @Test(priority = 1, groups = {"regression", "search"},
            description = "Verify sort filter resets to default after logout and re-login")
    public void testFilterResetAfterRelogin() {
        // Change sort to Z-A
        homePage.sortBy("za");
        Assert.assertEquals(homePage.getSelectedSortOption(), "Name (Z to A)",
                "Sort should be Z-A after change");

        // Logout and re-login
        LoginPage loginPage = homePage.logout();
        Assert.assertTrue(loginPage.isPageLoaded(), "Should be on login page");

        HomePage newHomePage = loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(newHomePage.isPageLoaded(), "Should be on home page after re-login");

        // Verify sort reset to default A-Z
        String sortOption = newHomePage.getSelectedSortOption();
        Assert.assertEquals(sortOption, "Name (A to Z)",
                "Sort should reset to default 'Name (A to Z)' after re-login");
    }

    // ==================== Test 32: Add All Items to Cart ====================
    @Test(priority = 2, groups = {"regression", "search"},
            description = "Verify adding all 6 products to cart shows badge count of 6")
    public void testAddAllItemsToCart() {
        List<String> products = homePage.getProductNames();
        Assert.assertEquals(products.size(), 6, "Should have 6 products");

        // Add all products to cart
        for (String product : products) {
            homePage.addProductToCart(product);
        }

        Assert.assertEquals(homePage.getCartBadgeCount(), 6,
                "Cart badge should show 6 after adding all products");
    }

    // ==================== Test 33: Remove from Inventory Page ====================
    @Test(priority = 3, groups = {"regression", "search"},
            description = "Verify Add to Cart button toggles to Remove after adding an item")
    public void testRemoveFromInventoryPage() {
        String product = "Sauce Labs Backpack";

        // Add product - button should change to Remove
        homePage.addProductToCart(product);
        Assert.assertEquals(homePage.getCartBadgeCount(), 1,
                "Cart badge should show 1 after adding");

        // Remove product from inventory page
        homePage.removeProductFromCart(product);
        Assert.assertEquals(homePage.getCartBadgeCount(), 0,
                "Cart badge should show 0 after removing");
    }
}
