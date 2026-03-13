package com.webautomation.pages;

import com.webautomation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CartPage - Page Object for the SauceDemo cart page.
 * Displays items added to the shopping cart.
 */
public class CartPage {

    private static final Logger logger = LogManager.getLogger(CartPage.class);
    private final WebDriver driver;

    // ==================== Web Elements ====================

    @FindBy(className = "title")
    private WebElement pageTitle;

    @FindBy(className = "cart_item")
    private List<WebElement> cartItems;

    @FindBy(className = "inventory_item_name")
    private List<WebElement> cartItemNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> cartItemPrices;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    @FindBy(id = "continue-shopping")
    private WebElement continueShoppingButton;

    @FindBy(className = "cart_quantity")
    private List<WebElement> cartQuantities;

    // ==================== Constructor ====================

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("CartPage initialized");
    }

    // ==================== Actions ====================

    /**
     * Clicks the Checkout button.
     */
    public CheckoutPage clickCheckout() {
        WaitUtils.waitForClickability(driver, checkoutButton);
        checkoutButton.click();
        logger.info("Clicked checkout button");
        return new CheckoutPage(driver);
    }

    /**
     * Clicks the Continue Shopping button to return to inventory.
     */
    public HomePage continueShopping() {
        WaitUtils.waitForClickability(driver, continueShoppingButton);
        continueShoppingButton.click();
        logger.info("Clicked continue shopping");
        return new HomePage(driver);
    }

    /**
     * Removes an item from the cart by its name.
     *
     * @param productName the name of the product to remove
     */
    public CartPage removeItem(String productName) {
        String buttonId = "remove-" + productName.toLowerCase().replace(" ", "-");
        WebElement removeButton = driver.findElement(By.id(buttonId));
        WaitUtils.waitForClickability(driver, removeButton);
        removeButton.click();
        logger.info("Removed item from cart: {}", productName);
        return this;
    }

    // ==================== Getters ====================

    /**
     * Gets the number of items in the cart.
     */
    public int getCartItemCount() {
        int count = cartItems.size();
        logger.info("Cart item count: {}", count);
        return count;
    }

    /**
     * Gets the list of product names in the cart.
     */
    public List<String> getCartItemNames() {
        List<String> names = cartItemNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        logger.info("Cart items: {}", names);
        return names;
    }

    /**
     * Gets the list of product prices in the cart.
     */
    public List<String> getCartItemPrices() {
        return cartItemPrices.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a specific product is in the cart.
     *
     * @param productName the product name to check
     * @return true if the product is in the cart
     */
    public boolean isProductInCart(String productName) {
        return getCartItemNames().contains(productName);
    }

    /**
     * Checks if the cart is empty.
     */
    public boolean isCartEmpty() {
        return cartItems.isEmpty();
    }

    /**
     * Gets the page title text.
     */
    public String getPageTitleText() {
        WaitUtils.waitForVisibility(driver, pageTitle);
        return pageTitle.getText();
    }

    /**
     * Checks if the cart page is loaded.
     */
    public boolean isPageLoaded() {
        try {
            return pageTitle.isDisplayed() && pageTitle.getText().equals("Your Cart");
        } catch (Exception e) {
            return false;
        }
    }
}
