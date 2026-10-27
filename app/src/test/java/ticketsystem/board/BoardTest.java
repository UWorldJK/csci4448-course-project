package ticketsystem.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ticketsystem.developer.Developer;
import ticketsystem.factory.TicketFactory;
import ticketsystem.tickets.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;
    private TicketFactory ticketFactory;
    private Developer dev1;
    private Developer dev2;

    @BeforeEach
    void setUp() {
        ticketFactory = new TicketFactory();
        dev1 = new Developer("John");
        dev2 = new Developer("Jane");

        // Create a board with common column types
        List<ColumnType> columnTypes = new ArrayList<>();
        columnTypes.add(ColumnType.BACKLOG);
        columnTypes.add(ColumnType.TODO);
        columnTypes.add(ColumnType.IN_PROGRESS);
        columnTypes.add(ColumnType.IN_REVIEW);
        columnTypes.add(ColumnType.DONE);

        board = Board.Builder.getNewBuilder("Project Board", ticketFactory)
                .createNColumns(columnTypes)
                .addTeamDevelopers(List.of(dev1, dev2))
                .build();
    }

    @Test
    void testBoardCreation() {
        assertNotNull(board);
    }

    @Test
    void testBoardWithMultipleDevelopers() {
        Developer dev3 = new Developer("Bob");
        List<ColumnType> columnTypes = new ArrayList<>();
        columnTypes.add(ColumnType.TODO);
        columnTypes.add(ColumnType.DONE);

        Board teamBoard = Board.Builder.getNewBuilder("Team Board", ticketFactory)
                .createNColumns(columnTypes)
                .addTeamDevelopers(List.of(dev1, dev2, dev3))
                .build();

        assertNotNull(teamBoard);
    }

    @Test
    void testBoardWithTickets() {
        Story story = (Story) ticketFactory.createStory(
                "User login feature",
                "Feature",
                "Implement user login",
                dev1,
                Priority.HIGH
        );

        BugFix bugFix = (BugFix) ticketFactory.createBugFix(
                "Login button broken",
                "Bug",
                "Fix login button",
                dev1,
                Priority.HIGHEST
        );

        Map<ColumnType, List<Ticket>> ticketsToAdd = new EnumMap<>(ColumnType.class);
        List<Ticket> backlogTickets = new ArrayList<>();
        backlogTickets.add(story);
        backlogTickets.add(bugFix);
        ticketsToAdd.put(ColumnType.BACKLOG, backlogTickets);

        Board boardWithTickets = Board.Builder.getNewBuilder("Board with Tickets", ticketFactory)
                .createNColumns(List.of(ColumnType.BACKLOG, ColumnType.DONE))
                .addTeamDevelopers(List.of(dev1))
                .addTickets(ticketsToAdd)
                .build();

        assertNotNull(boardWithTickets);
    }

    @Test
    void testBoardWithSingleColumn() {
        Board singleColumnBoard = Board.Builder.getNewBuilder("Single Column", ticketFactory)
                .createNColumns(List.of(ColumnType.TODO))
                .build();

        assertNotNull(singleColumnBoard);
    }

    @Test
    void testBoardWithAllColumnTypes() {
        List<ColumnType> allColumns = new ArrayList<>();
        for (ColumnType type : ColumnType.values()) {
            allColumns.add(type);
        }

        Board fullBoard = Board.Builder.getNewBuilder("Full Board", ticketFactory)
                .createNColumns(allColumns)
                .build();

        assertNotNull(fullBoard);
    }

    @Test
    void testBoardBuilderReturnsBoard() {
        Board testBoard = Board.Builder.getNewBuilder("Test", ticketFactory)
                .createNColumns(List.of(ColumnType.TODO))
                .build();

        assertNotNull(testBoard);
    }

    @Test
    void testBoardWithNoDevelopers() {
        Board emptyDevBoard = Board.Builder.getNewBuilder("No Devs", ticketFactory)
                .createNColumns(List.of(ColumnType.TODO, ColumnType.DONE))
                .build();

        assertNotNull(emptyDevBoard);
    }

    @Test
    void testBoardBuilderChaining() {
        Board chainedBoard = Board.Builder.getNewBuilder("Chained", ticketFactory)
                .createNColumns(List.of(ColumnType.BACKLOG, ColumnType.TODO))
                .addTeamDevelopers(List.of(dev1, dev2))
                .build();

        assertNotNull(chainedBoard);
    }

    @Test
    void testMultipleBoardsAreIndependent() {
        Board board1 = Board.Builder.getNewBuilder("Board 1", ticketFactory)
                .createNColumns(List.of(ColumnType.TODO))
                .addTeamDevelopers(List.of(dev1))
                .build();

        Board board2 = Board.Builder.getNewBuilder("Board 2", ticketFactory)
                .createNColumns(List.of(ColumnType.TODO, ColumnType.DONE))
                .addTeamDevelopers(List.of(dev2))
                .build();

        assertNotNull(board1);
        assertNotNull(board2);
        assertNotEquals(board1, board2);
    }

    @Test
    void testBoardWithMultipleStories() {
        List<Ticket> stories = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Ticket story = ticketFactory.createStory(
                    "Feature " + i,
                    "Feature",
                    "Story " + i,
                    dev1,
                    Priority.MEDIUM
            );
            stories.add(story);
        }

        Map<ColumnType, List<Ticket>> ticketsMap = new EnumMap<>(ColumnType.class);
        ticketsMap.put(ColumnType.BACKLOG, stories);

        Board storyBoard = Board.Builder.getNewBuilder("Story Board", ticketFactory)
                .createNColumns(List.of(ColumnType.BACKLOG, ColumnType.DONE))
                .addTickets(ticketsMap)
                .build();

        assertNotNull(storyBoard);
    }

    @Test
    void testBoardWithMixedTicketTypes() {
        List<Ticket> mixedTickets = new ArrayList<>();

        Story story = (Story) ticketFactory.createStory(
                "Auth feature",
                "Feature",
                "Implement auth",
                dev1,
                Priority.HIGH
        );

        BugFix bugFix = (BugFix) ticketFactory.createBugFix(
                "Security vulnerability",
                "Bug",
                "Fix security issue",
                dev1,
                Priority.HIGHEST
        );

        Epic epic = (Epic) ticketFactory.createEpic(
                "Release v2.0",
                "Q2 Release",
                "Epic",
                "Major release",
                dev1,
                Priority.HIGH
        );

        mixedTickets.add(story);
        mixedTickets.add(bugFix);
        mixedTickets.add(epic);

        Map<ColumnType, List<Ticket>> ticketsMap = new EnumMap<>(ColumnType.class);
        ticketsMap.put(ColumnType.BACKLOG, mixedTickets);

        Board mixedBoard = Board.Builder.getNewBuilder("Mixed Board", ticketFactory)
                .createNColumns(List.of(ColumnType.BACKLOG, ColumnType.TODO, ColumnType.DONE))
                .addTeamDevelopers(List.of(dev1, dev2))
                .addTickets(ticketsMap)
                .build();

        assertNotNull(mixedBoard);
    }

    @Test
    void testBoardWithDuplicateColumnTypes() {
        // If the builder allows adding the same column type multiple times,
        // it should handle it gracefully (likely the last one wins in EnumMap)
        List<ColumnType> duplicateColumns = new ArrayList<>();
        duplicateColumns.add(ColumnType.TODO);
        duplicateColumns.add(ColumnType.DONE);
        duplicateColumns.add(ColumnType.TODO); // Duplicate

        Board dupBoard = Board.Builder.getNewBuilder("Dup", ticketFactory)
                .createNColumns(duplicateColumns)
                .build();

        assertNotNull(dupBoard);
    }
}
