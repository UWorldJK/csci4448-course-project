Feature: Details of Ticket Creation
  As a scrum master I want to be able to create Tickets
  I want to ensure the lifecycle of a ticket created by me and assigned to a Developer
  I want to update, retrieve and analyze tickets

  Scenario: Creating a BugFix ticket
    Given a kanban board with the following columns
      | TODO |
      | IN_PROGRESS |
      | DONE |
    And a BugFix ticket in the Backlog
    And a developer John is assigned to the BugFix ticket
    When John finishes the BugFix ticket
    Then Done column contains BugFix ticket
    And BugFix status is set to Done

  Scenario: Creating a Story ticket
    Given a kanban board with the following columns
      | TODO |
      | IN_PROGRESS |
      | IN_REVIEW |
      | DONE |
    And a Story ticket in the Backlog with details "User authentication feature"
    And a developer Alice is assigned to the Story ticket
    When Alice moves the Story to In Progress
    And Alice completes the Story and moves it to Done
    Then Done column contains Story ticket
    And Story status is set to Done

  Scenario: Creating an Epic ticket with stories
    Given a kanban board with the following columns
      | BACKLOG |
      | TODO |
      | IN_PROGRESS |
      | DONE |
    And an Epic ticket "Release v2.0" with goal "Implement user dashboard"
    And the Epic contains 2 Story tickets
    When stories are marked as complete
    Then Epic column reflects completed stories
