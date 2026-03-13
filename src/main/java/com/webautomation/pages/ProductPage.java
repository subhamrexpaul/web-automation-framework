package com.webautomation.pages;

import com.webautomation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * ProductPage - Page Object for the SauceDemo product detail page.
 * Displays individual product information.
 */
public class ProductPage {

    private static final Logger logger = LogManager.getLogger(ProductPage.class);
    private final WebDriver driver;

    // ==================== Web Elements ====================

    @FindBy(css = "[data-test='inventory-item-name']")
    private WebElement productName;

    @FindBy(css = "[data-test='inventory-item-desc']")
    private WebElement productDescription;

    @FindBy(className = "inventory_details_price")
    private WebElement productPrice;

    @FindBy(css = "[data-test='add-to-cart']")
    private WebElement addToCartButton;

    @FindBy(css = "[data-test='remove']")
    private WebElement removeButton;

    @FindBy(id = "back-to-products")
    private WebElement backToProductsButton;

    @FindBy(className = "shopping_cart_badge")
    private WebElement cartBadge;

    @FindBy(className = "inventory_details_img")
    private WebElement productImage;

    // ==================== Constructor ====================

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("ProductPage initialized");
    }

    // ==================== Actions ====================

    /**
     * Adds the current product to the cart.
     */
    public ProductPage addToCart() {
        WaitUtils.waitForClickability(driver, addToCartButton);
        addToCartButton.click();
        logger.info("Added product to cart from detail page");
        return this;
    }

    /**
     * Removes the current product from the cart.
     */
    public ProductPage removeFromCart() {
        WaitUtils.waitForClickability(driver, removeButton);
        removeButton.click();
        logger.info("Removed product from cart on detail page");
        return this;
    }

    /**
     * Clicks the back button to return to products/inventory page.
     */
    public HomePage backToProducts() {
        WaitUtils.waitForClickability(driver, backToProductsButton);
        backToProductsButton.click();
        logger.info("Navigated back to products page");
        return new HomePage(driver);
    }

    // ==================== Getters ====================

    /**
     * Gets the product name.
     */
    public String getProductName() {
        WaitUtils.waitForVisibility(driver, productName);
        String name = productName.getText();
        logger.info("Product name: {}", name);
        return name;
    }

    /**
     * Gets the product description.
     */
    public String getProductDescription() {
        WaitUtils.waitForVisibility(driver, productDescription);
        return productDescription.getText();
    }

    /**
     * Gets the product price as a string (e.g., "$29.99").
     */
    public String getProductPriceText() {
        WaitUtils.waitForVisibility(driver, productPrice);
        return productPrice.getText();
    }

    /**
     * Gets the product price as a double.
     */
    public double getProductPriceValue() {
        String priceText = getProductPriceText().replace("$", "");
        return Double.parseDouble(priceText);
    }

    /**
     * Gets the cart badge count.
     */
    public int getCartBadgeCount() {
        try {
            return Integer.parseInt(cartBadge.getText());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Checks if the Add to Cart button is displayed.
     */
    public boolean isAddToCartDisplayed() {
        try {
            return addToCartButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the Remove button is displayed.
     */
    public boolean isRemoveDisplayed() {
        try {
            return removeButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if the product image is displayed.
     */
    public boolean isProductImageDisplayed() {
        try {
            return productImage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
