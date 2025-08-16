package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class InventoryPage {
    private WebDriver driver;
    private WebDriverWait wait;
    
    @FindBy(css = ".inventory_list .inventory_item")
    private List<WebElement> inventoryItems;
    
    @FindBy(css = ".shopping_cart_link")
    private WebElement shoppingCartLink;
    
    @FindBy(css = ".shopping_cart_badge")
    private WebElement cartBadge;
    
    @FindBy(css = ".title")
    private WebElement pageTitle;
    
    @FindBy(css = ".bm-burger-button")
    private WebElement menuButton;
    
    @FindBy(id = "logout_sidebar_link")
    private WebElement logoutLink;
    
    @FindBy(css = ".inventory_item_name")
    private List<WebElement> itemNames;
    
    @FindBy(css = ".inventory_item_price")
    private List<WebElement> itemPrices;
    
    @FindBy(css = ".btn_inventory")
    private List<WebElement> addToCartButtons;
    
    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    
    public boolean isInventoryPageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.getText().contains("Products");
        } catch (Exception e) {
            return false;
        }
    }
    
    public int getNumberOfItems() {
        wait.until(ExpectedConditions.visibilityOfAllElements(inventoryItems));
        return inventoryItems.size();
    }
    
    public void clickShoppingCart() {
        wait.until(ExpectedConditions.elementToBeClickable(shoppingCartLink));
        shoppingCartLink.click();
    }
    
    public int getCartItemCount() {
        try {
            if (cartBadge.isDisplayed()) {
                return Integer.parseInt(cartBadge.getText());
            }
        } catch (Exception e) {
            // Cart badge not visible means 0 items
        }
        return 0;
    }
    
    public void addItemToCart(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < addToCartButtons.size()) {
            WebElement addButton = addToCartButtons.get(itemIndex);
            wait.until(ExpectedConditions.elementToBeClickable(addButton));
            addButton.click();
        }
    }
    
    public String getItemName(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < itemNames.size()) {
            return itemNames.get(itemIndex).getText();
        }
        return "";
    }
    
    public String getItemPrice(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < itemPrices.size()) {
            return itemPrices.get(itemIndex).getText();
        }
        return "";
    }
    
    public void openMenu() {
        wait.until(ExpectedConditions.elementToBeClickable(menuButton));
        menuButton.click();
    }
    
    public void logout() {
        openMenu();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        logoutLink.click();
    }
    
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    public boolean isLoggedIn() {
        try {
            return isInventoryPageDisplayed() && shoppingCartLink.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
