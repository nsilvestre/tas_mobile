Feature: Set up the environment selecting the Partner and its environment

  @working
  Scenario Outline: Select Partner environment and Payment
    Given <config> appium driver

    When I select the Partner
    And I navigate to the Configuration screen
    And I select the Payment section
    And I toggle Payments
    And I select a Payment
    And I close the Configuration screen
    Then Login screen is shown

    Examples:
      | config      |
      | eatsa_simulated_iPad_air_2 |