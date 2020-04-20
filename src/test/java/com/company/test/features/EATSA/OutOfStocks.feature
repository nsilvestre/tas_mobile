Feature: Out of Stock

  As a User
  I would like to remove and edit an item from the cart
  and can navigate to the checkout screen
  So that I can purchase my order

  @working
  Scenario Outline: Validate out of stock item cannot be added to cart
    Given <config> appium driver

    When I select <partner> partner
    And I mark <item> item as out of stock
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    Then Nutrition Details alert is shown
    And I tap Close alert button
    And I go back
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I mark <item> item as in stock
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    And I tap add from Item Details screen
    And I navigate to the cart
    And I tap <checkoutType> checkout type
    And I pay the order
    Then the order is created

    Examples:
      | config                     | partner | category        | subcategory     | item                 | checkoutType |
      | eatsa_simulated_iPad_air_2 | ROTI    | Modern Classics | Modern Classics | Signature Rice Plate | Dine in      |


  @working
  Scenario Outline: Validate item with an out of stock modifier can be added to the cart
    Given <config> appium driver

    When I select <partner> partner
    And I mark <modifier> modifier as out of stock
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    And I tap add from Item Details screen
    Then Out of Stock alert is shown
    And I tap Yes
    And I navigate to the cart
    And I tap <checkoutType> checkout type
    And I pay the order
    Then the order is created
    And I mark <modifier> modifier as in stock

    Examples:
      | config                     | partner | category        | subcategory     | item                 | modifier | checkoutType |
      | eatsa_simulated_iPad_air_2 | ROTI    | Modern Classics | Modern Classics | Signature Rice Plate | Pita     | Dine in      |


  @working
  Scenario Outline: Validate when item goes OOS while in the cart
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    And I tap add from Item Details screen
    And I navigate to the cart
    And I mark <item> item as out of stock
    And I tap <checkoutType> checkout type
    Then alert is shown indicating that <item> is OOS
    And I tap Okay
    And I mark <item> item as in stock
    And I tap <checkoutType> checkout type
    And I mark <item> item as out of stock
    And I pay the order
    Then alert is shown indicating that <item> is OOS
    And I mark <item> item as in stock
    And I tap Okay
    And I pay the order
    And the order is created


    Examples:
      | config                     | partner | category        | subcategory     | item                 | checkoutType |
      | eatsa_simulated_iPad_air_2 | ROTI    | Modern Classics | Modern Classics | Signature Rice Plate | Dine in      |
