Feature: Checkout

  As a user
  I would like to have a checkout screen
  So that I can edit my email, add a bag, add a promo and pay the order

  @working
  Scenario Outline: Edit email from checkout
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    And I tap add from Item Details screen
    And I navigate to the cart
    Then the number of items in the cart is 1
    And I tap <checkoutType> checkout type
    And I tap the email from Checkout screen
    And I tap Done
    Then Email Error alert is shown
    And I enter an email from alert
    And I tap Done
    Then the email is shown
    And I pay the order
    Then the order is created

    Examples:
      | config                     | partner | category        | subcategory     | item                 | checkoutType |
      | eatsa_simulated_iPad_air_2 | ROTI    | Modern Classics | Modern Classics | Signature Rice Plate | Dine in      |
