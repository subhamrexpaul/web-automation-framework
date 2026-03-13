package com.webautomation.pages;

import com.webautomation.utils.WaitUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SearchResultsPage - Page Object for handling product filtering/sorting results
 * on the SauceDemo inventory page.
 * Works alongside HomePage for sort-specific verification scenarios.
 */
public class SearchResultsPage {

    private static final Logger logger = LogManager.getLogger(SearchResultsPage.class);
    private final WebDriver driver;

    // ==================== Web Elements ====================

    @FindBy(className = "inventory_item_name")
    private List<WebElement> productNames;

    @FindBy(className = "inventory_item_price")
    private List<WebElement> productPrices;

    @FindBy(className = "product_sort_container")
    private WebElement sortDropdown;

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    // ==================== Constructor ====================

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        logger.info("SearchResultsPage initialized");
    }

    // ==================== Sort Actions ====================

    /**
     * Sorts products by Name (A to Z).
     */
    public SearchResultsPage sortByNameAZ() {
        selectSortOption("az");
        logger.info("Sorted by Name A-Z");
        return this;
    }

    /**
     * Sorts products by Name (Z to A).
     */
    public SearchResultsPage sortByNameZA() {
        selectSortOption("za");
        logger.info("Sorted by Name Z-A");
        return this;
    }

    /**
     * Sorts products by Price (Low to High).
     */
    public SearchResultsPage sortByPriceLowHigh() {
        selectSortOption("lohi");
        logger.info("Sorted by Price Low-High");
        return this;
    }

    /**
     * Sorts products by Price (High to Low).
     */
    public SearchResultsPage sortByPriceHighLow() {
        selectSortOption("hilo");
        logger.info("Sorted by Price High-Low");
        return this;
    }

    private void selectSortOption(String value) {
        WaitUtils.waitForVisibility(driver, sortDropdown);
        Select select = new Select(sortDropdown);
        select.selectByValue(value);
    }

    // ==================== Getters ====================

    /**
     * Gets all product names in displayed order.
     */
    public List<String> getProductNames() {
        WaitUtils.waitForAllVisible(driver, productNames);
        return productNames.stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    /**
     * Gets all product prices in displayed order.
     */
    public List<Double> getProductPrices() {
        WaitUtils.waitForAllVisible(driver, productPrices);
        return productPrices.stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .collect(Collectors.toList());
    }

    /**
     * Gets the currently selected sort option.
     */
    public String getActiveSortOption() {
        Select select = new Select(sortDropdown);
        return select.getFirstSelectedOption().getText();
    }

    /**
     * Gets the total number of displayed products.
     */
    public int getResultCount() {
        return inventoryItems.size();
    }

    /**
     * Checks if the products are sorted alphabetically A-Z.
     */
    public boolean isSortedByNameAscending() {
        List<String> names = getProductNames();
        for (int i = 0; i < names.size() - 1; i++) {
            if (names.get(i).compareToIgnoreCase(names.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the products are sorted alphabetically Z-A.
     */
    public boolean isSortedByNameDescending() {
        List<String> names = getProductNames();
        for (int i = 0; i < names.size() - 1; i++) {
            if (names.get(i).compareToIgnoreCase(names.get(i + 1)) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the products are sorted by price ascending.
     */
    public boolean isSortedByPriceAscending() {
        List<Double> prices = getProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) > prices.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the products are sorted by price descending.
     */
    public boolean isSortedByPriceDescending() {
        List<Double> prices = getProductPrices();
        for (int i = 0; i < prices.size() - 1; i++) {
            if (prices.get(i) < prices.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
