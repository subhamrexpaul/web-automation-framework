package com.webautomation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Singleton class to load and provide access to configuration properties.
 * Reads from config.properties file and exposes typed getter methods.
 */
public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
            fis.close();
            logger.info("Configuration loaded successfully from config.properties");
        } catch (IOException e) {
            logger.error("Failed to load config.properties: {}", e.getMessage());
            throw new RuntimeException("Could not load config.properties", e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getBaseUrl() {
        return properties.getProperty("base.url", "https://www.saucedemo.com/");
    }

    public String getBrowser() {
        String browser = System.getProperty("browser");
        if (browser != null) {
            return browser;
        }
        return properties.getProperty("browser", "chrome");
    }

    public boolean isHeadless() {
        String headless = System.getProperty("headless");
        if (headless != null) {
            return Boolean.parseBoolean(headless);
        }
        return Boolean.parseBoolean(properties.getProperty("headless", "true"));
    }

    public int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "10"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait", "15"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(properties.getProperty("page.load.timeout", "30"));
    }

    public String getScreenshotPath() {
        return properties.getProperty("screenshot.path", "screenshots/");
    }

    public boolean isScreenshotOnFailure() {
        return Boolean.parseBoolean(properties.getProperty("screenshot.on.failure", "true"));
    }

    public String getTestDataPath() {
        return properties.getProperty("test.data.path", "src/test/resources/testdata/");
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
