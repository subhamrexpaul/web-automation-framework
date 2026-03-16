package com.webautomation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.webautomation.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.lang.reflect.Method;

/**
 * TestListener - TestNG ITestListener implementation for ExtentReports integration.
 * Automatically logs test results, captures screenshots on failure,
 * and embeds them into the HTML report.
 */
public class TestListener implements ITestListener {

    private static final Logger logger = LogManager.getLogger(TestListener.class);
    private static final ExtentReports extent = ExtentReportManager.getInstance();
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest test = extent.createTest(testName,
                description != null ? description : "Test: " + testName);

        // Add test categories from TestNG groups
        String[] groups = result.getMethod().getGroups();
        for (String group : groups) {
            test.assignCategory(group);
        }

        extentTest.set(test);
        logger.info("Test Started: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        extentTest.get().log(Status.PASS, "Test PASSED: " + testName);
        logger.info("Test Passed: {}", testName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentTest test = extentTest.get();

        // Log the failure message and exception
        test.log(Status.FAIL, "Test FAILED: " + testName);
        test.log(Status.FAIL, "Cause: " + result.getThrowable().getMessage());

        // Capture and attach screenshot
        try {
            WebDriver driver = getDriverFromResult(result);
            if (driver != null) {
                String base64Screenshot = ScreenshotUtils.captureScreenshotAsBase64(driver);
                if (base64Screenshot != null) {
                    test.addScreenCaptureFromBase64String(base64Screenshot, "Screenshot on failure");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to capture screenshot for report: {}", e.getMessage());
            test.log(Status.WARNING, "Could not capture screenshot: " + e.getMessage());
        }

        logger.error("Test Failed: {} - {}", testName, result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        extentTest.get().log(Status.SKIP, "Test SKIPPED: " + testName);
        if (result.getThrowable() != null) {
            extentTest.get().log(Status.SKIP, "Reason: " + result.getThrowable().getMessage());
        }
        logger.warn("Test Skipped: {}", testName);
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("========== Test Suite Started: {} ==========", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========== Test Suite Finished: {} ==========", context.getName());
        ExtentReportManager.flushReports();
    }

    /**
     * Attempts to get the WebDriver instance from the test class via reflection.
     */
    private WebDriver getDriverFromResult(ITestResult result) {
        try {
            Object testInstance = result.getInstance();
            Method getDriverMethod = testInstance.getClass().getSuperclass()
                    .getDeclaredMethod("getDriver");
            getDriverMethod.setAccessible(true);
            return (WebDriver) getDriverMethod.invoke(testInstance);
        } catch (Exception e) {
            logger.warn("Could not retrieve WebDriver for screenshot: {}", e.getMessage());
            return null;
        }
    }
}
