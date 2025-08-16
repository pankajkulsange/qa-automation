package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    @FindBy(id = "user-name")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(id = "login-button")
    private WebElement loginButton;
    
    @FindBy(css = ".error-message-container")
    private WebElement errorMessage;
    
    @FindBy(css = ".login_logo")
    private WebElement loginLogo;
    
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    public void navigateToLoginPage(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOf(loginLogo));
    }
    
    public void enterUsername(String username) {
        wait.until(ExpectedConditions.elementToBeClickable(usernameField));
        usernameField.clear();
        usernameField.sendKeys(username);
    }
    
    public void enterPassword(String password) {
        wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        passwordField.clear();
        passwordField.sendKeys(password);
    }
    
    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }
    
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
    
    public boolean isLoginPageDisplayed() {
        try {
            return loginLogo.isDisplayed() && loginButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getErrorMessage() {
        if (isErrorMessageDisplayed()) {
            return errorMessage.getText();
        }
        return "";
    }
    
    public boolean isUsernameFieldDisplayed() {
        try {
            return usernameField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isPasswordFieldDisplayed() {
        try {
            return passwordField.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
