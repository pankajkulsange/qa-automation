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
    
    // Alternative selectors to try
    @FindBy(css = "button[data-test='add-to-cart-sauce-labs-backpack']")
    private WebElement firstItemButton;
    
    @FindBy(css = "button[data-test='add-to-cart-sauce-labs-bike-light']")
    private WebElement secondItemButton;
    
    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        
        // Debug: Print page information
        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());
        System.out.println("Number of inventory buttons found: " + addToCartButtons.size());
        
        // Debug: Print button information
        for (int i = 0; i < Math.min(addToCartButtons.size(), 3); i++) {
            try {
                WebElement button = addToCartButtons.get(i);
                System.out.println("Button " + i + " text: '" + button.getText() + "', class: '" + button.getAttribute("class") + "'");
            } catch (Exception e) {
                System.out.println("Error getting button " + i + " info: " + e.getMessage());
            }
        }
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
            // First check if cart badge element exists in DOM
            System.out.println("Checking for cart badge element...");
            
            // Wait for cart badge to be visible with a longer timeout
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            shortWait.until(ExpectedConditions.visibilityOf(cartBadge));
            String badgeText = cartBadge.getText().trim();
            System.out.println("✅ Cart badge found and visible. Text: '" + badgeText + "'");
            return Integer.parseInt(badgeText);
        } catch (Exception e) {
            // Cart badge not visible means 0 items
            System.out.println("❌ Cart badge not visible or error: " + e.getMessage());
            
            // Let's also check if the cart link itself is visible
            try {
                if (shoppingCartLink.isDisplayed()) {
                    System.out.println("✅ Shopping cart link is visible");
                } else {
                    System.out.println("❌ Shopping cart link is not visible");
                }
            } catch (Exception cartLinkException) {
                System.out.println("❌ Could not check shopping cart link: " + cartLinkException.getMessage());
            }
            
            return 0;
        }
    }
    
    public void addItemToCart(int itemIndex) {
        if (itemIndex >= 0 && itemIndex < addToCartButtons.size()) {
            WebElement addButton = addToCartButtons.get(itemIndex);
            
            // Wait for button to be clickable
            wait.until(ExpectedConditions.elementToBeClickable(addButton));
            
            // Check if button is already "Remove" (item already in cart)
            String buttonText = addButton.getText().trim();
            System.out.println("Button text before click: '" + buttonText + "'");
            
            if (buttonText.equals("Remove")) {
                System.out.println("Item already in cart, skipping...");
                return; // Item already in cart
            }
            
            // Try multiple click strategies
            boolean clickSuccessful = false;
            
            // Strategy 1: Regular click
            try {
                System.out.println("Attempting regular click...");
                addButton.click();
                clickSuccessful = true;
                System.out.println("Regular click successful");
            } catch (Exception e) {
                System.out.println("Regular click failed: " + e.getMessage());
            }
            
            // Strategy 2: JavaScript click if regular click failed
            if (!clickSuccessful) {
                try {
                    System.out.println("Attempting JavaScript click...");
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", addButton);
                    clickSuccessful = true;
                    System.out.println("JavaScript click successful");
                } catch (Exception e) {
                    System.out.println("JavaScript click failed: " + e.getMessage());
                }
            }
            
            // Strategy 3: Actions click if both failed
            if (!clickSuccessful) {
                try {
                    System.out.println("Attempting Actions click...");
                    org.openqa.selenium.interactions.Actions actions = new org.openqa.selenium.interactions.Actions(driver);
                    actions.moveToElement(addButton).click().perform();
                    clickSuccessful = true;
                    System.out.println("Actions click successful");
                } catch (Exception e) {
                    System.out.println("Actions click failed: " + e.getMessage());
                }
            }
            
            if (!clickSuccessful) {
                System.out.println("❌ All click strategies failed!");
                return;
            }
            
            System.out.println("Button clicked, waiting for update...");
            
            // Wait for cart to update with multiple checks
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                    
                    // Refresh elements and check button text
                    PageFactory.initElements(driver, this);
                    if (itemIndex < addToCartButtons.size()) {
                        String newButtonText = addToCartButtons.get(itemIndex).getText().trim();
                        System.out.println("Check " + (i+1) + ": Button text = '" + newButtonText + "'");
                        
                        if (newButtonText.equals("Remove")) {
                            System.out.println("✅ Item successfully added to cart (button changed to Remove)");
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error during check " + (i+1) + ": " + e.getMessage());
                }
            }
            
            System.out.println("Add to cart operation completed.");
        }
    }
    
    // New method specifically for the first item using data-test attribute
    public void addFirstItemToCart() {
        try {
            System.out.println("Attempting to add first item using data-test selector...");
            
            // Try the specific data-test selector first
            if (firstItemButton != null) {
                wait.until(ExpectedConditions.elementToBeClickable(firstItemButton));
                String buttonText = firstItemButton.getText().trim();
                System.out.println("First item button text: '" + buttonText + "'");
                
                if (buttonText.equals("Remove")) {
                    System.out.println("First item already in cart, skipping...");
                    return;
                }
                
                firstItemButton.click();
                System.out.println("First item button clicked successfully");
                
                // Wait and check for button text change
                for (int i = 0; i < 5; i++) {
                    Thread.sleep(1000);
                    PageFactory.initElements(driver, this);
                    String newButtonText = firstItemButton.getText().trim();
                    System.out.println("Check " + (i+1) + ": First item button text = '" + newButtonText + "'");
                    
                    if (newButtonText.equals("Remove")) {
                        System.out.println("✅ First item successfully added to cart");
                        return;
                    }
                }
            }
            
            // Fallback to the original method
            System.out.println("Falling back to original method...");
            addItemToCart(0);
            
        } catch (Exception e) {
            System.out.println("Error in addFirstItemToCart: " + e.getMessage());
            // Fallback to the original method
            addItemToCart(0);
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
            
            System.out.println("Checking " + addToCartButtons.size() + " inventory buttons for 'Remove' text...");
            
            // Count items with "Remove" button text
            int count = 0;
            for (int i = 0; i < addToCartButtons.size(); i++) {
                try {
                    WebElement button = addToCartButtons.get(i);
                    String buttonText = button.getText().trim();
                    System.out.println("Button " + i + " text: '" + buttonText + "'");
                    if (buttonText.equals("Remove")) {
                        count++;
                        System.out.println("✅ Found 'Remove' button at index " + i);
                    }
                } catch (Exception e) {
                    System.out.println("❌ Error checking button " + i + ": " + e.getMessage());
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
