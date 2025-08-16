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
        
        // Wait for navigation to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public int getCartItemCount() {
        try {
            // Wait for cart badge to be visible with a longer timeout
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            shortWait.until(ExpectedConditions.visibilityOf(cartBadge));
            String badgeText = cartBadge.getText().trim();
            System.out.println("Cart badge text: '" + badgeText + "'");
            return Integer.parseInt(badgeText);
        } catch (Exception e) {
            // Cart badge not visible means 0 items
            System.out.println("Cart badge not visible or error: " + e.getMessage());
            return 0;
        }
    }
    
    public void addItemToCart(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < addToCartButtons.size()) {
            WebElement addButton = addToCartButtons.get(itemIndex);
            wait.until(ExpectedConditions.elementToBeClickable(addButton));
            
            // Check if button is already "Remove" (item already in cart)
            String buttonText = addButton.getText().trim();
            System.out.println("Button text before click: '" + buttonText + "'");
            
            if (buttonText.equals("Remove")) {
                System.out.println("Item already in cart, skipping...");
                return; // Item already in cart
            }
            
            addButton.click();
            System.out.println("Button clicked, waiting for update...");
            
            // Wait for cart to update
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println("Add to cart operation completed.");
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
        
        // Wait for menu to fully open and logout link to be visible
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Try to find and click logout link with different approaches
        try {
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
            logoutLink.click();
        } catch (Exception e) {
            // If the above fails, try JavaScript click
            try {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", logoutLink);
            } catch (Exception jsException) {
                // If JavaScript also fails, try direct navigation to logout
                driver.get("https://www.saucedemo.com/");
            }
        }
    }
    
    public String getPageTitle() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            return pageTitle.getText();
        } catch (Exception e) {
            return driver.getTitle(); // fallback to browser title
        }
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
    
    public int getCartItemCountAlternative() {
        try {
            // Refresh page elements to avoid stale references
            PageFactory.initElements(driver, this);
            
            // Count items with "Remove" button text
            int count = 0;
            for (WebElement button : addToCartButtons) {
                try {
                    if (button.getText().trim().equals("Remove")) {
                        count++;
                    }
                } catch (Exception e) {
                    // Skip stale elements
                    continue;
                }
            }
            System.out.println("Alternative cart count (Remove buttons): " + count);
            return count;
        } catch (Exception e) {
            System.out.println("Error in alternative cart count: " + e.getMessage());
            return 0;
        }
    }
}
