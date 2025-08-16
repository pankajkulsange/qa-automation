package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class DriverManager {
    private static WebDriver driver;
    private static final ConfigManager config = ConfigManager.getInstance();
    
    public static WebDriver getDriver() {
        if (driver == null) {
            driver = createDriver();
        }
        return driver;
    }
    
    public static WebDriver createDriver() {
        String browser = config.getBrowser().toLowerCase();
        boolean headless = config.isHeadless();
        boolean useGrid = config.isSeleniumGridEnabled();
        
        if (useGrid) {
            return createRemoteDriver(browser, headless);
        } else {
            return createLocalDriver(browser, headless);
        }
    }
    
    private static WebDriver createLocalDriver(String browser, boolean headless) {
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                if (headless) {
                    chromeOptions.addArguments("--headless");
                }
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("--window-size=1920,1080");
                return new ChromeDriver(chromeOptions);
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("--headless");
                }
                return new FirefoxDriver(firefoxOptions);
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                if (headless) {
                    edgeOptions.addArguments("--headless");
                }
                return new EdgeDriver(edgeOptions);
                
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
    
    private static WebDriver createRemoteDriver(String browser, boolean headless) {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            
            switch (browser) {
                case "chrome":
                    capabilities.setBrowserName("chrome");
                    ChromeOptions chromeOptions = new ChromeOptions();
                    if (headless) {
                        chromeOptions.addArguments("--headless");
                    }
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    capabilities.merge(chromeOptions);
                    break;
                    
                case "firefox":
                    capabilities.setBrowserName("firefox");
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if (headless) {
                        firefoxOptions.addArguments("--headless");
                    }
                    capabilities.merge(firefoxOptions);
                    break;
                    
                case "edge":
                    capabilities.setBrowserName("edge");
                    EdgeOptions edgeOptions = new EdgeOptions();
                    if (headless) {
                        edgeOptions.addArguments("--headless");
                    }
                    capabilities.merge(edgeOptions);
                    break;
                    
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            
            String gridUrl = config.getSeleniumGridUrl();
            return new RemoteWebDriver(new URL(gridUrl), capabilities);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create remote WebDriver", e);
        }
    }
    
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
    
    public static void closeDriver() {
        if (driver != null) {
            driver.close();
        }
    }
}
