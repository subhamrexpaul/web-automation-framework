package com.webautomation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotUtils - Utility class for capturing and saving screenshots.
 * Automatically creates timestamped filenames and ensures output directory exists.
 */
public class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "screenshots";

    private ScreenshotUtils() {
        // Prevent instantiation
    }

    /**
     * Captures a screenshot and saves it with a timestamped filename.
     *
     * @param driver   the WebDriver instance
     * @param testName name of the test (used in filename)
     * @return the absolute path to the saved screenshot
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            // Ensure screenshot directory exists
            Path screenshotDir = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(screenshotDir)) {
                Files.createDirectories(screenshotDir);
                logger.info("Created screenshot directory: {}", screenshotDir.toAbsolutePath());
            }

            // Generate timestamped filename
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            Path destination = screenshotDir.resolve(fileName);

            // Capture and save screenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            String absolutePath = destination.toAbsolutePath().toString();
            logger.info("Screenshot captured: {}", absolutePath);
            return absolutePath;

        } catch (IOException e) {
            logger.error("Failed to capture screenshot for test '{}': {}", testName, e.getMessage());
            return null;
        }
    }

    /**
     * Captures a screenshot and returns it as a Base64-encoded string.
     * Useful for embedding in reports.
     *
     * @param driver the WebDriver instance
     * @return Base64-encoded screenshot string
     */
    public static String captureScreenshotAsBase64(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            logger.error("Failed to capture Base64 screenshot: {}", e.getMessage());
            return null;
        }
    }
}
