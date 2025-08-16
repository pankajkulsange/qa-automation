Feature: Simple Cart Test
  As a user
  I want to test basic cart functionality
  So that I can verify the cart operations work

  Background:
    Given I am on the login page
    When I login with valid credentials
    Then I should be logged in successfully

  @simple @cart
  Scenario: Simple cart test
    When I add the first item to cart
    Then the cart should show 1 item
