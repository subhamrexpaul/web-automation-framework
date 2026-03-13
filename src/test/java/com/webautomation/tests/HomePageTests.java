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
 * HomePageTests - Test class for SauceDemo inventory/home page functionality.
 * Covers product display, sorting, and burger menu verification.
 */
@Listeners(TestListener.class)
public class HomePageTests extends BaseTest {

    private HomePage homePage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void loginBeforeTest() {
        LoginPage loginPage = new LoginPage(getDriver());
        homePage = loginPage.login("standard_user", "secret_sauce");
    }

    // ==================== Test 8: Products Displayed ====================
    @Test(priority = 1, groups = {"smoke", "homepage"},
            description = "Verify that 6 products are displayed on the inventory page")
    public void testProductsDisplayed() {
        int productCount = homePage.getProductCount();
        Assert.assertEquals(productCount, 6,
                "Inventory page should display exactly 6 products");
    }

    // ==================== Test 9: Sort by Name A-Z ====================
    @Test(priority = 2, groups = {"regression", "homepage", "sort"},
            description = "Verify default sort order is Name (A to Z)")
    public void testSortByNameAZ() {
        String selectedOption = homePage.getSelectedSortOption();
        Assert.assertEquals(selectedOption, "Name (A to Z)",
                "Default sort should be Name (A to Z)");

        List<String> productNames = homePage.getProductNames();
        for (int i = 0; i < productNames.size() - 1; i++) {
            Assert.assertTrue(
                    productNames.get(i).compareToIgnoreCase(productNames.get(i + 1)) <= 0,
                    "Products should be sorted A-Z. Found: " + productNames.get(i) +
                            " before " + productNames.get(i + 1));
        }
    }

    // ==================== Test 10: Sort by Name Z-A ====================
    @Test(priority = 3, groups = {"regression", "homepage", "sort"},
            description = "Verify products can be sorted by Name (Z to A)")
    public void testSortByNameZA() {
        homePage.sortBy("za");

        List<String> productNames = homePage.getProductNames();
        for (int i = 0; i < productNames.size() - 1; i++) {
            Assert.assertTrue(
                    productNames.get(i).compareToIgnoreCase(productNames.get(i + 1)) >= 0,
                    "Products should be sorted Z-A. Found: " + productNames.get(i) +
                            " before " + productNames.get(i + 1));
        }
    }

    // ==================== Test 11: Sort by Price Low to High ====================
    @Test(priority = 4, groups = {"regression", "homepage", "sort"},
            description = "Verify products sorted by Price (low to high)")
    public void testSortByPriceLowHigh() {
        homePage.sortBy("lohi");

        List<Double> prices = homePage.getProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) <= prices.get(i + 1),
                    "Prices should be ascending. Found: $" + prices.get(i) +
                            " before $" + prices.get(i + 1));
        }
    }

    // ==================== Test 12: Sort by Price High to Low ====================
    @Test(priority = 5, groups = {"regression", "homepage", "sort"},
            description = "Verify products sorted by Price (high to low)")
    public void testSortByPriceHighLow() {
        homePage.sortBy("hilo");

        List<Double> prices = homePage.getProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i + 1),
                    "Prices should be descending. Found: $" + prices.get(i) +
                            " before $" + prices.get(i + 1));
        }
    }

    // ==================== Test 13: Burger Menu Links ====================
    @Test(priority = 6, groups = {"regression", "homepage"},
            description = "Verify burger menu contains expected navigation links")
    public void testBurgerMenuLinks() {
        homePage.openBurgerMenu();

        // Wait a moment for animation
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        List<String> menuItems = homePage.getMenuItems();
        Assert.assertTrue(menuItems.contains("All Items"),
                "Menu should contain 'All Items' link");
        Assert.assertTrue(menuItems.contains("About"),
                "Menu should contain 'About' link");
        Assert.assertTrue(menuItems.contains("Logout"),
                "Menu should contain 'Logout' link");
        Assert.assertTrue(menuItems.contains("Reset App State"),
                "Menu should contain 'Reset App State' link");
    }
}
