Feature: Account Management

  As a user
  I would like to have a Profile Screen
  So that I can add/edit my Name and Email
  and see my credit card numbers

  @working
  Scenario Outline: Validate that name can be added/edited - DT
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I navigate to the User Information screen
    And I tap the name
    And I enter a name and last name
    And I save the changes
    Then the name is saved

    Examples:
      | config                     | partner |
      | eatsa_simulated_iPad_air_2 | ROTI    |
      | eatsa_simulated_iPad_air_2 | WOWBAO  |


  @working
  Scenario Outline: Validate that email can be added/edited
    Given <config> appium driver

    When I select <partner> partner
    And I swipe to login
    And I navigate to the User Information screen
    And I tap the email
    And I enter an email
    And I save the changes
    Then the email is saved

    Examples:
      | config                     | partner |
      | eatsa_simulated_iPad_air_2 | ROTI    |
      | eatsa_simulated_iPad_air_2 | WOWBAO  |


