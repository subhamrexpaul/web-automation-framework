package com.webautomation.utils;

import com.webautomation.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * WaitUtils - Utility class providing explicit wait methods.
 * Wraps Selenium WebDriverWait with commonly used ExpectedConditions.
 */
public class WaitUtils {

    private static final Logger logger = LogManager.getLogger(WaitUtils.class);

    private WaitUtils() {
        // Prevent instantiation
    }

    private static WebDriverWait getWait(WebDriver driver) {
        int timeout = ConfigReader.getInstance().getExplicitWait();
        return new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    private static WebDriverWait getWait(WebDriver driver, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Waits until the element is visible on the page.
     */
    public static WebElement waitForVisibility(WebDriver driver, WebElement element) {
        logger.debug("Waiting for element visibility: {}", element);
        return getWait(driver).until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until the element is visible with a custom timeout.
     */
    public static WebElement waitForVisibility(WebDriver driver, WebElement element, int timeoutSeconds) {
        logger.debug("Waiting for element visibility (timeout: {}s): {}", timeoutSeconds, element);
        return getWait(driver, timeoutSeconds).until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Waits until the element is clickable.
     */
    public static WebElement waitForClickability(WebDriver driver, WebElement element) {
        logger.debug("Waiting for element clickability: {}", element);
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until the element is clickable with a custom timeout.
     */
    public static WebElement waitForClickability(WebDriver driver, WebElement element, int timeoutSeconds) {
        logger.debug("Waiting for element clickability (timeout: {}s): {}", timeoutSeconds, element);
        return getWait(driver, timeoutSeconds).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits until all elements in the list are visible.
     */
    public static List<WebElement> waitForAllVisible(WebDriver driver, List<WebElement> elements) {
        logger.debug("Waiting for all elements visibility");
        return getWait(driver).until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    /**
     * Waits until the page title contains the given text.
     */
    public static boolean waitForTitleContains(WebDriver driver, String titlePart) {
        logger.debug("Waiting for title to contain: {}", titlePart);
        return getWait(driver).until(ExpectedConditions.titleContains(titlePart));
    }

    /**
     * Waits until the URL contains the given text.
     */
    public static boolean waitForUrlContains(WebDriver driver, String urlPart) {
        logger.debug("Waiting for URL to contain: {}", urlPart);
        return getWait(driver).until(ExpectedConditions.urlContains(urlPart));
    }

    /**
     * Waits until the element becomes invisible / disappears.
     */
    public static boolean waitForInvisibility(WebDriver driver, WebElement element) {
        logger.debug("Waiting for element invisibility: {}", element);
        return getWait(driver).until(ExpectedConditions.invisibilityOf(element));
    }

    /**
     * Waits until the element contains the expected text.
     */
    public static boolean waitForTextPresent(WebDriver driver, WebElement element, String text) {
        logger.debug("Waiting for text '{}' in element: {}", text, element);
        return getWait(driver).until(ExpectedConditions.textToBePresentInElement(element, text));
    }
}
