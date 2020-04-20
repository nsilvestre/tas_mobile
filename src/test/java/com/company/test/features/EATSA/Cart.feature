Feature: Cart

  As a User
  I would like to remove and edit an item from the cart
  and can navigate to the checkout screen
  So that I can purchase my order

  @working
  Scenario Outline: Remove item from cart
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    And I tap add from Item Details screen
    And I select <item> item
    And I tap add from Item Details screen
    And I navigate to the cart
    And I select an item from the cart
    And I tap remove from Cart Item alert
    Then the number of items in the cart is 1

    Examples:
      | config                     | partner | category        | subcategory     | item                 | checkoutType |
      | eatsa_simulated_iPad_air_2 | ROTI    | Modern Classics | Modern Classics | Signature Rice Plate | Dine in      |

  @working
  Scenario Outline: Order an item with Name and Special Requests
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    And I tap add from Item Details screen
    And I navigate to the cart
    And I tap 'Add name or Special request'
    And I enter a made for name
    And I enter special instructions
    And I tap Submit
    And I tap <checkoutType> checkout type
    And I pay the order
    Then the order is created

    Examples:
      | config                     | partner | category        | subcategory     | item                 | checkoutType |
      | eatsa_simulated_iPad_air_2 | ROTI    | Modern Classics | Modern Classics | Signature Rice Plate | Dine in      |


