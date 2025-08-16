Feature: Inventory Page Functionality
  As a logged in user
  I want to interact with the inventory page
  So that I can view and manage products

  Background:
    Given I am on the login page
    When I login with valid credentials
    Then I should be logged in successfully

  @smoke @inventory
  Scenario: Verify inventory page is displayed after login
    Then I should see the inventory page
    And I should see 6 inventory items

  @regression @inventory
  Scenario: Add item to cart
    When I add the first item to cart
    Then the cart should show 1 item

  @regression @inventory
  Scenario: Add multiple items to cart
    When I add the first item to cart
    And I add the second item to cart
    Then the cart should show 2 items

  @regression @inventory
  Scenario: View shopping cart
    When I click on the shopping cart
    Then I should be on the cart page

  @regression @inventory
  Scenario: Logout from inventory page
    When I logout from the application
    Then I should be logged out successfully
    And I should be on the login page

  @ui @inventory
  Scenario: Verify inventory page elements
    Then the shopping cart should be displayed
    And the menu button should be displayed
    And the page title should contain "Products"
