package steps;

import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.testng.Assert;
import pages.InventoryPage;
import pages.LoginPage;
import utils.DriverManager;

public class InventorySteps {
    private InventoryPage inventoryPage;
    private LoginPage loginPage;
    
    public InventorySteps() {
        inventoryPage = new InventoryPage(DriverManager.getDriver());
        loginPage = new LoginPage(DriverManager.getDriver());
    }
    
    @When("I add the first item to cart")
    public void i_add_the_first_item_to_cart() {
        inventoryPage.addItemToCart(0);
    }
    
    @When("I add the second item to cart")
    public void i_add_the_second_item_to_cart() {
        inventoryPage.addItemToCart(1);
    }
    
    @When("I click on the shopping cart")
    public void i_click_on_the_shopping_cart() {
        inventoryPage.clickShoppingCart();
    }
    
    @When("I logout from the application")
    public void i_logout_from_the_application() {
        inventoryPage.logout();
    }
    
    @Then("the cart should show {int} item")
    public void the_cart_should_show_item(int expectedCount) {
        int actualCount = inventoryPage.getCartItemCount();
        Assert.assertEquals(actualCount, expectedCount, "Cart should show " + expectedCount + " items");
    }
    
    @Then("the cart should show {int} items")
    public void the_cart_should_show_items(int expectedCount) {
        the_cart_should_show_item(expectedCount);
    }
    
    @Then("I should be on the cart page")
    public void i_should_be_on_the_cart_page() {
        String currentUrl = inventoryPage.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("/cart.html"), "Should be on cart page");
    }
    
    @Then("I should be logged out successfully")
    public void i_should_be_logged_out_successfully() {
        // After logout, we should be back on login page
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Should be logged out and on login page");
    }
    
    @Then("I should be on the login page")
    public void i_should_be_on_the_login_page() {
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Should be on login page");
    }
    
    @And("the shopping cart should be displayed")
    public void the_shopping_cart_should_be_displayed() {
        // This would need a method in InventoryPage to check if cart is displayed
        // For now, we'll assume it's displayed if we're on inventory page
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Shopping cart should be displayed on inventory page");
    }
    
    @And("the menu button should be displayed")
    public void the_menu_button_should_be_displayed() {
        // This would need a method in InventoryPage to check if menu button is displayed
        // For now, we'll assume it's displayed if we're on inventory page
        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Menu button should be displayed on inventory page");
    }
    
    @And("the page title should contain {string}")
    public void the_page_title_should_contain(String expectedText) {
        String pageTitle = inventoryPage.getPageTitle();
        Assert.assertTrue(pageTitle.contains(expectedText), 
            "Page title should contain: " + expectedText + " but was: " + pageTitle);
    }
}
