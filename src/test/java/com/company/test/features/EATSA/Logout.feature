Feature: Logout

  As a user
  I would like to have logout button
  So that I can leave the order process

  @working
  Scenario Outline: Validate Close and LogOut-Cancel Order buttons
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Close button
    Then MainMenu screen is shown
    Then I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    Then LogOut alert is shown
    And I tap Cancel
    Then User Information Screen is shown
    Then I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown

    Examples:
      | config                     | partner |
      | eatsa_simulated_iPad_air_2 | ROTI    |
      | eatsa_simulated_iPad_air_2 | WOWBAO  |

  @working
  Scenario Outline: Validate app behaves correctly after multiple logouts
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown
    And I swipe to login
    And I navigate to the User Information screen
    And I tap Log Out - Cancel Order
    And I tap Log out
    Then Login screen is shown

    Examples:
      | config                     | partner |
      | eatsa_simulated_iPad_air_2 | ROTI    |
