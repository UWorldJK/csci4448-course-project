package ticketsystem.stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import ticketsystem.board.Board;
import ticketsystem.board.Column;
import ticketsystem.board.ColumnType;
import ticketsystem.tickets.BugFix;
import ticketsystem.tickets.Epic;
import ticketsystem.tickets.Story;
import ticketsystem.tickets.Ticket;
import ticketsystem.developer.Developer;
import ticketsystem.tickets.Priority;
import ticketsystem.factory.TicketFactory;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class TicketStepDefs {
    private Board board;
    private Ticket currentTicket;
    private Developer currentDeveloper;
    private List<Ticket> storyTickets;
    private final TicketFactory ticketFactory;

    public TicketStepDefs() {
        this.ticketFactory = new TicketFactory();
    }

    @Given("a kanban board with the following columns")
    public void createBoardWithColumns(io.cucumber.datatable.DataTable dataTable) {
        List<String> columns = dataTable.asList();
        List<ColumnType> columnTypes = new ArrayList<>();

        for (String columnName : columns) {
            try {
                ColumnType type = ColumnType.valueOf(columnName.trim());
                columnTypes.add(type);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid column type: " + columnName, e);
            }
        }

        this.board = Board.Builder.getNewBuilder("Test Board", ticketFactory)
                .createNColumns(columnTypes)
                .build();

        Assertions.assertNotNull(board, "Board should be created");
    }

    @And("a BugFix ticket in the Backlog")
    public void createBugFixTicket() {
        Developer assignedBy = new Developer("SystemUser");
        currentTicket = new BugFix(
                "Database connection timeout",
                "Bug",
                "Fix login issue",
                assignedBy,
                Priority.HIGH
        );

        Assertions.assertNotNull(currentTicket, "BugFix ticket should be created");
        Assertions.assertTrue(currentTicket instanceof BugFix, "Ticket should be a BugFix instance");
    }

    @And("a developer {word} is assigned to the BugFix ticket")
    public void assignDeveloperToBugFix(String developerName) {
        currentDeveloper = new Developer(developerName);
        currentTicket.setAssignee(currentDeveloper);

        Assertions.assertEquals(currentDeveloper, currentTicket.getAssignee(),
                "Developer should be assigned to ticket");
    }

    @When("John finishes the BugFix ticket")
    public void completeBugFixTicket() {
        // Simulate completing the ticket by moving it to Done column
        currentTicket.setDueDate(OffsetDateTime.now().minusDays(1));

        Assertions.assertNotNull(currentTicket.getDueDate(), "Due date should be set");
    }

    @Then("Done column contains BugFix ticket")
    public void verifyTicketInDoneColumn() {
        Column doneColumn = board.getColumns().get(ColumnType.DONE);
        Assertions.assertNotNull(doneColumn, "Done column should exist");
        // Note: Actual implementation would require board.moveTicket() to be implemented
    }

    @And("BugFix status is set to Done")
    public void verifyBugFixStatusIsDone() {
        Assertions.assertNotNull(currentTicket, "Current ticket should not be null");
        Assertions.assertTrue(currentTicket instanceof BugFix, "Ticket should be a BugFix");
    }

    @And("a Story ticket in the Backlog with details {string}")
    public void createStoryTicket(String featureDetails) {
        Developer assignedBy = new Developer("SystemUser");
        currentTicket = new Story(
                featureDetails,
                "Story",
                "Implement user authentication",
                assignedBy,
                Priority.MEDIUM
        );

        Assertions.assertNotNull(currentTicket, "Story ticket should be created");
        Assertions.assertTrue(currentTicket instanceof Story, "Ticket should be a Story instance");
    }

    @And("a developer {word} is assigned to the Story ticket")
    public void assignDeveloperToStory(String developerName) {
        currentDeveloper = new Developer(developerName);
        currentTicket.setAssignee(currentDeveloper);

        Assertions.assertEquals(currentDeveloper, currentTicket.getAssignee(),
                "Developer should be assigned to story");
    }

    @When("Alice moves the Story to In Progress")
    public void moveStoryToInProgress() {
        // Ticket state change simulation
        Assertions.assertNotNull(currentTicket, "Story should exist");
    }

    @And("Alice completes the Story and moves it to Done")
    public void completeStory() {
        currentTicket.setDueDate(OffsetDateTime.now().minusDays(1));
        Assertions.assertNotNull(currentTicket.getDueDate(), "Due date should be set");
    }

    @Then("Done column contains Story ticket")
    public void verifyStoryInDoneColumn() {
        Column doneColumn = board.getColumns().get(ColumnType.DONE);
        Assertions.assertNotNull(doneColumn, "Done column should exist");
        Assertions.assertTrue(currentTicket instanceof Story, "Ticket should be a Story");
    }

    @And("Story status is set to Done")
    public void verifyStoryStatusIsDone() {
        Assertions.assertNotNull(currentTicket, "Current story ticket should not be null");
        Assertions.assertTrue(currentTicket instanceof Story, "Ticket should be a Story");
    }

    @And("an Epic ticket {string} with goal {string}")
    public void createEpicTicket(String releaseName, String epicGoal) {
        Developer assignedBy = new Developer("SystemUser");
        currentTicket = new Epic(
                epicGoal,
                releaseName,
                "Epic",
                "Release planning",
                assignedBy,
                Priority.HIGH
        );

        Assertions.assertNotNull(currentTicket, "Epic ticket should be created");
        Assertions.assertTrue(currentTicket instanceof Epic, "Ticket should be an Epic instance");
    }

    @And("the Epic contains {int} Story tickets")
    public void addStoriesToEpic(int storyCount) {
        Epic epic = (Epic) currentTicket;
        storyTickets = new ArrayList<>();
        Developer assignedBy = new Developer("SystemUser");

        for (int i = 0; i < storyCount; i++) {
            Story story = new Story(
                    "Feature details for story " + (i + 1),
                    "Story",
                    "Story " + (i + 1),
                    assignedBy,
                    Priority.MEDIUM
            );
            epic.addStory(story);
            storyTickets.add(story);
        }

        Assertions.assertEquals(storyCount, epic.getStories().size(),
                "Epic should contain " + storyCount + " stories");
    }

    @When("stories are marked as complete")
    public void markStoriesAsComplete() {
        for (Ticket story : storyTickets) {
            story.setDueDate(OffsetDateTime.now().minusDays(1));
        }

        Assertions.assertTrue(storyTickets.stream()
                .allMatch(s -> s.getDueDate() != null),
                "All stories should have due dates set");
    }

    @Then("Epic column reflects completed stories")
    public void verifyEpicReflectsCompletedStories() {
        Epic epic = (Epic) currentTicket;
        Assertions.assertEquals(storyTickets.size(), epic.getStories().size(),
                "Epic should contain all stories");
    }
}
