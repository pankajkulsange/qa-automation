package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static final Properties properties = new Properties();
    private static ConfigManager instance;
    
    private ConfigManager() {
        loadProperties();
    }
    
    public static ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }
    
    private void loadProperties() {
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
    }
    
    public String getProperty(String key) {
        // First check system properties, then environment variables, then config file
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        
        value = System.getenv(key.toUpperCase().replace(".", "_"));
        if (value != null) {
            return value;
        }
        
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }
    
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }
    
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }
    
    public int getIntProperty(String key) {
        String value = getProperty(key);
        return Integer.parseInt(value);
    }
    
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }
    
    // Convenience methods for common properties
    public String getAppUrl() {
        return getProperty("app.url");
    }
    
    public String getUsername() {
        return getProperty("app.username");
    }
    
    public String getPassword() {
        return getProperty("app.password");
    }
    
    public String getBrowser() {
        String browser = getProperty("browser", "chrome");
        return browser.isEmpty() ? "chrome" : browser;
    }
    
    public boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }
    
    public String getEnvironment() {
        return getProperty("env", "local");
    }
    
    public int getThreads() {
        return getIntProperty("threads", 1);
    }
    
    public String getSeleniumGridUrl() {
        return getProperty("selenium.grid.url", "http://localhost:4444/wd/hub");
    }
    
    public boolean isSeleniumGridEnabled() {
        return getBooleanProperty("selenium.grid.enabled", false);
    }
}
