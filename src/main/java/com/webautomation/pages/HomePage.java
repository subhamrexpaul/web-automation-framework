package com.webautomation.pages;

import com.webautomation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HomePage - Page Object for the SauceDemo inventory/products page.
 * This is the main page after successful login.
 */
public class HomePage {

    private static final Logger logger = LogManager.getLogger(HomePage.class);
    private final WebDriver driver;

    // ==================== Web Elements ====================

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartIcon;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(id = "react-burger-menu-btn")
    private WebElement burgerMenuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;

    @FindBy(id = "about_sidebar_link")
    private WebElement aboutLink;

    @FindBy(id = "inventory_sidebar_link")
    private WebElement allItemsLink;

    @FindBy(id = "reset_sidebar_link")
    private WebElement resetAppStateLink;

    @FindBy(className = "bm-item-list")
    private WebElement menuItemList;

    @FindBy(id = "react-burger-cross-btn")
    private WebElement closeMenuButton;

    // ==================== Constructor ====================

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("HomePage initialized");
    }

    // ==================== Product Actions ====================

    /**
     * Gets the list of all product names displayed on the page.
     */
    public List<String> getProductNames() {
        WaitUtils.waitForAllVisible(driver, productNames);
        List<String> names = productNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        logger.info("Found {} products", names.size());
        return names;
    }

    /**
     * Gets the list of all product prices as doubles.
     */
    public List<Double> getProductPrices() {
        WaitUtils.waitForAllVisible(driver, productPrices);
        List<Double> prices = productPrices.stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
        logger.info("Product prices: {}", prices);
        return prices;
    }

    /**
     * Gets the total number of products displayed.
     */
    public int getProductCount() {
        return inventoryItems.size();
    }

    /**
     * Clicks on a product name to navigate to its detail page.
     *
     * @param productName the exact name of the product
     * @return ProductPage object
     */
    public ProductPage clickProduct(String productName) {
        for (WebElement name : productNames) {
            if (name.getText().equals(productName)) {
                WaitUtils.waitForClickability(driver, name);
                name.click();
                logger.info("Clicked on product: {}", productName);
                return new ProductPage(driver);
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    /**
     * Adds a product to the cart by its name (from the inventory page).
     *
     * @param productName the name of the product to add
     */
    public HomePage addProductToCart(String productName) {
        // SauceDemo uses button IDs like "add-to-cart-sauce-labs-backpack"
        String buttonId = "add-to-cart-" + productName.toLowerCase().replace(" ", "-");
        WebElement addButton = driver.findElement(By.id(buttonId));
        WaitUtils.waitForClickability(driver, addButton);
        addButton.click();
        logger.info("Added to cart: {}", productName);
        return this;
    }

    /**
     * Removes a product from the cart using the Remove button on the inventory page.
     *
     * @param productName the name of the product to remove
     */
    public HomePage removeProductFromCart(String productName) {
        String buttonId = "remove-" + productName.toLowerCase().replace(" ", "-");
        WebElement removeButton = driver.findElement(By.id(buttonId));
        WaitUtils.waitForClickability(driver, removeButton);
        removeButton.click();
        logger.info("Removed from cart: {}", productName);
        return this;
    }

    // ==================== Sort Actions ====================

    /**
     * Sorts products by the given option value.
     * Options: "az", "za", "lohi", "hilo"
     */
    public HomePage sortBy(String optionValue) {
        WaitUtils.waitForVisibility(driver, sortDropdown);
        Select select = new Select(sortDropdown);
        select.selectByValue(optionValue);
        logger.info("Sorted products by: {}", optionValue);
        return this;
    }

    /**
     * Gets the currently selected sort option text.
     */
    public String getSelectedSortOption() {
        Select select = new Select(sortDropdown);
        return select.getFirstSelectedOption().getText();
    }

    // ==================== Cart Actions ====================

    /**
     * Clicks the cart icon to navigate to the cart page.
     */
    public CartPage goToCart() {
        WaitUtils.waitForClickability(driver, cartIcon);
        cartIcon.click();
        logger.info("Navigated to cart");
        return new CartPage(driver);
    }

    /**
     * Gets the number displayed on the cart badge.
     * Returns 0 if no badge is displayed.
     */
    public int getCartBadgeCount() {
        try {
            String badgeText = cartBadge.getText();
            int count = Integer.parseInt(badgeText);
            logger.info("Cart badge count: {}", count);
            return count;
        } catch (Exception e) {
            logger.info("No cart badge displayed (cart is empty)");
            return 0;
        }
    }

    // ==================== Menu Actions ====================

    /**
     * Opens the burger menu.
     */
    public HomePage openBurgerMenu() {
        WaitUtils.waitForClickability(driver, burgerMenuButton);
        burgerMenuButton.click();
        logger.info("Opened burger menu");
        return this;
    }

    /**
     * Gets the list of menu link texts.
     */
    public List<String> getMenuItems() {
        WaitUtils.waitForVisibility(driver, menuItemList);
        List<WebElement> menuLinks = menuItemList.findElements(By.tagName("a"));
        List<String> items = new ArrayList<>();
        for (WebElement link : menuLinks) {
            String text = link.getText().trim();
            if (!text.isEmpty()) {
                items.add(text);
            }
        }
        logger.info("Menu items: {}", items);
        return items;
    }

    /**
     * Clicks logout from the burger menu.
     */
    public LoginPage logout() {
        openBurgerMenu();
        try { Thread.sleep(500); } catch (InterruptedException e) {}
        WaitUtils.waitForVisibility(driver, logoutLink);
        new org.openqa.selenium.interactions.Actions(driver).moveToElement(logoutLink).click().perform();
        logger.info("Logged out");
        return new LoginPage(driver);
    }

    /**
     * Closes the burger menu.
     */
    public HomePage closeBurgerMenu() {
        WaitUtils.waitForClickability(driver, closeMenuButton);
        closeMenuButton.click();
        logger.info("Closed burger menu");
        return this;
    }

    // ==================== Page State ====================

    /**
     * Gets the page header title text.
     */
    public String getPageTitleText() {
        WaitUtils.waitForVisibility(driver, pageTitle);
        return pageTitle.getText();
    }

    /**
     * Checks if the inventory page is loaded.
     */
    public boolean isPageLoaded() {
        try {
            return pageTitle.isDisplayed() && pageTitle.getText().equals("Products");
        } catch (Exception e) {
            return false;
        }
    }
}
