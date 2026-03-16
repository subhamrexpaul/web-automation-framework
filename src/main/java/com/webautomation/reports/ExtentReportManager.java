package com.webautomation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ExtentReportManager - Manages ExtentReports instance lifecycle.
 * Configures the Spark HTML reporter with dark theme for rich test reporting.
 */
public class ExtentReportManager {

    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extentReports;
    private static final String REPORT_PATH = "reports/AutomationReport.html";

    private ExtentReportManager() {
        // Prevent instantiation
    }

    /**
     * Returns the singleton ExtentReports instance.
     * Creates and configures it if not already initialized.
     */
    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            extentReports = createInstance();
        }
        return extentReports;
    }

    private static ExtentReports createInstance() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);

        // Configure reporter styling
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Web Automation Test Report");
        sparkReporter.config().setReportName("Automation Execution Report");
        sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a");
        sparkReporter.config().setEncoding("utf-8");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // System info displayed in the report
        extent.setSystemInfo("Application", "SauceDemo E-Commerce");
        extent.setSystemInfo("Framework", "Selenium WebDriver + TestNG");
        extent.setSystemInfo("Author", "Automation Team");
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));

        logger.info("ExtentReports initialized. Report will be saved to: {}", REPORT_PATH);
        return extent;
    }

    /**
     * Flushes the report (writes all data to the HTML file).
     * Must be called after all tests complete.
     */
    public static void flushReports() {
        if (extentReports != null) {
            extentReports.flush();
            logger.info("ExtentReports flushed. Report saved to: {}", REPORT_PATH);
        }
    }
}
