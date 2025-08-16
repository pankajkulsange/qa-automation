Feature: Login Functionality
  As a user
  I want to be able to login to the application
  So that I can access the inventory page

  Background:
    Given I am on the login page

  @smoke @login
  Scenario: Successful login with valid credentials
    When I login with valid credentials
    Then I should be logged in successfully
    And I should see the inventory page
    And I should see 6 inventory items

  @smoke @login
  Scenario: Login with valid username and password separately
    When I enter valid username
    And I enter valid password
    And I click the login button
    Then I should be logged in successfully
    And I should see the inventory page

  @regression @login
  Scenario: Login with invalid username
    When I enter username "invalid_user"
    And I enter valid password
    And I click the login button
    Then I should see an error message
    And I should remain on the login page
    And the error message should contain "Epic sadface"

  @regression @login
  Scenario: Login with invalid password
    When I enter valid username
    And I enter password "wrong_password"
    And I click the login button
    Then I should see an error message
    And I should remain on the login page
    And the error message should contain "Epic sadface"

  @regression @login
  Scenario: Login with empty username
    When I enter username ""
    And I enter valid password
    And I click the login button
    Then I should see an error message
    And I should remain on the login page
    And the error message should contain "Epic sadface"

  @regression @login
  Scenario: Login with empty password
    When I enter valid username
    And I enter password ""
    And I click the login button
    Then I should see an error message
    And I should remain on the login page
    And the error message should contain "Epic sadface"

  @regression @login
  Scenario: Login with locked out user
    When I login with username "locked_out_user" and password "secret_sauce"
    Then I should see an error message
    And I should remain on the login page
    And the error message should contain "Epic sadface"

  @regression @login
  Scenario: Login with problem user
    When I login with username "problem_user" and password "secret_sauce"
    Then I should be logged in successfully
    And I should see the inventory page

  @regression @login
  Scenario: Login with performance glitch user
    When I login with username "performance_glitch_user" and password "secret_sauce"
    Then I should be logged in successfully
    And I should see the inventory page

  @ui @login
  Scenario: Verify login page elements are displayed
    Then the username field should be displayed
    And the password field should be displayed
    And the page title should be "Swag Labs"
