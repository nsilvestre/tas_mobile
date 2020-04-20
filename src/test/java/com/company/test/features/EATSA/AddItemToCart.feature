Feature: Add item to the cart from

  As a user
  I would like to add an item to the cart
  So that I can buy it

  @working
  Scenario Outline: Add item from item details screen
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    And I tap add from Item Details screen
    And I navigate to the cart
    Then the number of items in the cart is 1
    And I tap <checkoutType> checkout type
    And I pay the order
    Then the order is created

    Examples:
      | config                     | partner | category        | subcategory     | item                 | checkoutType |
      | eatsa_simulated_iPad_air_2 | ROTI    | Modern Classics | Modern Classics | Signature Rice Plate | Dine in      |


  @working
  Scenario Outline: Add item from force customize
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    And I tap add from Force Customize screen
    Then 'complete all sections' alert is shown
    Then I complete the required sections for <item> item
    And I tap add from Force Customize screen
    And I navigate to the cart
    And I validate that <item> is in the cart
    And I tap <checkoutType> checkout type
    And I pay the order
    Then the order is created

    Examples:
      | config                     | partner | category | subcategory | item       | checkoutType |
      | eatsa_simulated_iPad_air_2 | WOWBAO  | -        | Bao         | 6-Pack Bao | Dine in      |


  @working
  Scenario Outline: Add item from re-order
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    And I tap add from Item Details screen
    And I navigate to the cart
    And I validate that <item> is in the cart
    And I tap <checkoutType> checkout type
    And I pay the order
    Then the order is created
    And I swipe to login
    And I select Order History
    And I select <category> category
    And I reorder an item
    And I navigate to the cart
    And I tap <checkoutType> checkout type
    And I pay the order
    Then the order is created

    Examples:
      | config                     | partner | category      | subcategory     | item                 | checkoutType |
      | eatsa_simulated_iPad_air_2 | ROTI    | Order History | Modern Classics | Signature Rice Plate | Take out     |

  @working
  Scenario Outline: Add item from item details screen but accesing through re-order
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I select <subcategory> subcategory
    And I select <item> item
    And I tap add from Item Details screen
    And I navigate to the cart
    And I validate that <item> is in the cart
    And I tap <checkoutType> checkout type
    And I pay the order
    Then the order is created
    And I swipe to login
    And I select Order History
    And I select <category> category
    And I tap the <item> from Re-order screen
    And I tap add from Item Details screen
    And I navigate to the cart
    And I tap <checkoutType> checkout type
    And I pay the order
    Then the order is created

    Examples:
      | config                     | partner | category      | subcategory     | item                 | checkoutType |
      | eatsa_simulated_iPad_air_2 | ROTI    | Order History | Modern Classics | Signature Rice Plate | Take out     |

