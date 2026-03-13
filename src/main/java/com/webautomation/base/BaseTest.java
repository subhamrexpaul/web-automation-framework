package com.webautomation.base;

import com.webautomation.config.ConfigReader;
import com.webautomation.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest - Abstract base class for all test classes.
 * Handles WebDriver lifecycle (setup/teardown), navigation to base URL,
 * and screenshot capture on test failure.
 * Uses ThreadLocal for thread-safe parallel execution.
 */
public abstract class BaseTest {

    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Returns the WebDriver instance for the current thread.
     */
    protected WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Setup method executed before each test method.
     * Initializes WebDriver and navigates to the base URL.
     */
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        logger.info("========== Test Setup Started ==========");
        WebDriver driver = DriverFactory.createDriver();
        driverThreadLocal.set(driver);

        String baseUrl = ConfigReader.getInstance().getBaseUrl();
        driver.get(baseUrl);
        logger.info("Navigated to: {}", baseUrl);
        logger.info("========== Test Setup Completed ==========");
    }

    /**
     * Teardown method executed after each test method.
     * Captures screenshot on failure and quits the WebDriver.
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        logger.info("========== Test Teardown Started ==========");

        WebDriver driver = getDriver();
        if (driver != null) {
            // Capture screenshot on test failure
            if (result.getStatus() == ITestResult.FAILURE) {
                logger.error("Test FAILED: {}", result.getName());
                if (ConfigReader.getInstance().isScreenshotOnFailure()) {
                    String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getName());
                    logger.info("Failure screenshot saved: {}", screenshotPath);
                }
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                logger.info("Test PASSED: {}", result.getName());
            } else if (result.getStatus() == ITestResult.SKIP) {
                logger.warn("Test SKIPPED: {}", result.getName());
            }

            driver.quit();
            driverThreadLocal.remove();
            logger.info("Browser closed successfully");
        }

        logger.info("========== Test Teardown Completed ==========");
    }
}
