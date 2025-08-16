package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.LoginPage;
import pages.InventoryPage;
import utils.ConfigManager;
import utils.DriverManager;

public class LoginSteps {
    private WebDriver driver;
    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private ConfigManager config;
    
    @Before
    public void setUp() {
        config = ConfigManager.getInstance();
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
    }
    
    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }
    
    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        String url = config.getAppUrl();
        loginPage.navigateToLoginPage(url);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
    }
    
    @When("I enter valid username")
    public void i_enter_valid_username() {
        String username = config.getUsername();
        loginPage.enterUsername(username);
    }
    
    @When("I enter valid password")
    public void i_enter_valid_password() {
        String password = config.getPassword();
        loginPage.enterPassword(password);
    }
    
    @When("I enter username {string}")
    public void i_enter_username(String username) {
        loginPage.enterUsername(username);
    }
    
    @When("I enter password {string}")
    public void i_enter_password(String password) {
        loginPage.enterPassword(password);
    }
    
    @When("I click the login button")
    public void i_click_the_login_button() {
        loginPage.clickLoginButton();
    }
    
    @When("I login with valid credentials")
    public void i_login_with_valid_credentials() {
        String username = config.getUsername();
        String password = config.getPassword();
        loginPage.login(username, password);
    }
    
    @When("I login with username {string} and password {string}")
    public void i_login_with_username_and_password(String username, String password) {
        loginPage.login(username, password);
    }
    
    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        Assert.assertTrue(inventoryPage.isLoggedIn(), "User should be logged in successfully");
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Inventory page should be displayed");
    }
    
    @Then("I should see the inventory page")
    public void i_should_see_the_inventory_page() {
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Inventory page should be displayed");
    }
    
    @Then("I should see an error message")
    public void i_should_see_an_error_message() {
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
    }
    
    @Then("I should remain on the login page")
    public void i_should_remain_on_the_login_page() {
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Should remain on login page");
    }
    
    @And("the error message should contain {string}")
    public void the_error_message_should_contain(String expectedMessage) {
        String actualMessage = loginPage.getErrorMessage();
        Assert.assertTrue(actualMessage.contains(expectedMessage), 
            "Error message should contain: " + expectedMessage + " but was: " + actualMessage);
    }
    
    @And("I should see {int} inventory items")
    public void i_should_see_inventory_items(int expectedCount) {
        int actualCount = inventoryPage.getNumberOfItems();
        Assert.assertEquals(actualCount, expectedCount, "Should see " + expectedCount + " inventory items");
    }
    
    @And("the page title should be {string}")
    public void the_page_title_should_be(String expectedTitle) {
        String actualTitle = loginPage.getPageTitle();
        Assert.assertEquals(actualTitle, expectedTitle, "Page title should match");
    }
    
    @And("the username field should be displayed")
    public void the_username_field_should_be_displayed() {
        Assert.assertTrue(loginPage.isUsernameFieldDisplayed(), "Username field should be displayed");
    }
    
    @And("the password field should be displayed")
    public void the_password_field_should_be_displayed() {
        Assert.assertTrue(loginPage.isPasswordFieldDisplayed(), "Password field should be displayed");
    }
}
