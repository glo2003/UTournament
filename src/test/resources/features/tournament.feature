Feature: tournament feature

  Scenario: Create a tournament
    Given the following participants
      | name     |
      | Alice    |
      | Bob      |
      | Caroline |
    When the user creates the tournament named Smash
    Then the tournament exists

  Scenario: Play a bracket
    Given the following participants
      | name   |
      | Dylan  |
      | Eliott |
      | Frank  |
      | Xavier |
    Given a tournament named Rocket League
    When the user plays the first playable bracket
    Then that bracket is played

  Scenario: Can delete tournament
    Given the following participants
      | name    |
      | Tommy   |
      | William |
      | Xavier  |
      | Yan     |
    Given a tournament named Smash
    When the user deletes the tournament
    Then the tournament does not exist
