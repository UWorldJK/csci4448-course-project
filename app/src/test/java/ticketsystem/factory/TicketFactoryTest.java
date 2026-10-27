package ticketsystem.factory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ticketsystem.developer.Developer;
import ticketsystem.tickets.*;

import static org.junit.jupiter.api.Assertions.*;

class TicketFactoryTest {
    private TicketFactory factory;
    private Developer developer;

    @BeforeEach
    void setUp() {
        factory = new TicketFactory();
        developer = new Developer("TestDeveloper");
    }

    @Test
    void testFactoryCreation() {
        assertNotNull(factory);
    }

    @Test
    void testCreateStory() {
        Ticket ticket = factory.createStory(
                "User authentication",
                "Feature",
                "Implement user login",
                developer,
                Priority.HIGH
        );

        assertNotNull(ticket);
        assertInstanceOf(Story.class, ticket);
        Story story = (Story) ticket;
        assertEquals("User authentication", story.getFeatureDetails());
        assertEquals("Feature", story.getFeatureType());
        assertEquals("Implement user login", story.getTaskDescription());
        assertEquals(Priority.HIGH, story.getPriority());
        assertEquals(developer, story.getAssignedBy());
    }

    @Test
    void testCreateBugFix() {
        Ticket ticket = factory.createBugFix(
                "Login page crashes",
                "Bug",
                "Fix login crash",
                developer,
                Priority.HIGHEST
        );

        assertNotNull(ticket);
        assertInstanceOf(BugFix.class, ticket);
        BugFix bugFix = (BugFix) ticket;
        assertEquals("Login page crashes", bugFix.getBugDescription());
        assertEquals("Bug", bugFix.getFeatureType());
        assertEquals("Fix login crash", bugFix.getTaskDescription());
        assertEquals(Priority.HIGHEST, bugFix.getPriority());
        assertEquals(developer, bugFix.getAssignedBy());
    }

    @Test
    void testCreateEpic() {
        Ticket ticket = factory.createEpic(
                "Implement user dashboard",
                "Release v2.0",
                "Epic",
                "Dashboard Epic",
                developer,
                Priority.HIGH
        );

        assertNotNull(ticket);
        assertInstanceOf(Epic.class, ticket);
        Epic epic = (Epic) ticket;
        assertEquals("Implement user dashboard", epic.getEpicGoals());
        assertEquals("Release v2.0", epic.getReleaseName());
        assertEquals("Epic", epic.getFeatureType());
        assertEquals("Dashboard Epic", epic.getTaskDescription());
        assertEquals(Priority.HIGH, epic.getPriority());
        assertEquals(developer, epic.getAssignedBy());
    }

    @Test
    void testCreateMultipleStories() {
        Ticket story1 = factory.createStory(
                "Feature 1",
                "Feature",
                "Story 1",
                developer,
                Priority.MEDIUM
        );

        Ticket story2 = factory.createStory(
                "Feature 2",
                "Feature",
                "Story 2",
                developer,
                Priority.LOW
        );

        assertNotNull(story1);
        assertNotNull(story2);
        assertNotEquals(story1, story2);

        Story s1 = (Story) story1;
        Story s2 = (Story) story2;
        assertEquals("Feature 1", s1.getFeatureDetails());
        assertEquals("Feature 2", s2.getFeatureDetails());
    }

    @Test
    void testCreateMultipleBugFixes() {
        Ticket bugFix1 = factory.createBugFix(
                "Bug 1",
                "Bug",
                "Fix 1",
                developer,
                Priority.HIGH
        );

        Ticket bugFix2 = factory.createBugFix(
                "Bug 2",
                "Bug",
                "Fix 2",
                developer,
                Priority.MEDIUM
        );

        assertNotNull(bugFix1);
        assertNotNull(bugFix2);

        BugFix b1 = (BugFix) bugFix1;
        BugFix b2 = (BugFix) bugFix2;
        assertEquals("Bug 1", b1.getBugDescription());
        assertEquals("Bug 2", b2.getBugDescription());
    }

    @Test
    void testCreateMultipleEpics() {
        Ticket epic1 = factory.createEpic(
                "Goals 1",
                "Release 1",
                "Epic",
                "Epic 1",
                developer,
                Priority.HIGHEST
        );

        Ticket epic2 = factory.createEpic(
                "Goals 2",
                "Release 2",
                "Epic",
                "Epic 2",
                developer,
                Priority.LOW
        );

        assertNotNull(epic1);
        assertNotNull(epic2);

        Epic e1 = (Epic) epic1;
        Epic e2 = (Epic) epic2;
        assertEquals("Goals 1", e1.getEpicGoals());
        assertEquals("Goals 2", e2.getEpicGoals());
        assertEquals("Release 1", e1.getReleaseName());
        assertEquals("Release 2", e2.getReleaseName());
    }

    @Test
    void testCreateMixedTicketTypes() {
        Ticket story = factory.createStory(
                "Feature details",
                "Feature",
                "Story task",
                developer,
                Priority.HIGH
        );

        Ticket bugFix = factory.createBugFix(
                "Bug description",
                "Bug",
                "Bug fix task",
                developer,
                Priority.HIGHEST
        );

        Ticket epic = factory.createEpic(
                "Epic goals",
                "Release name",
                "Epic",
                "Epic task",
                developer,
                Priority.MEDIUM
        );

        assertInstanceOf(Story.class, story);
        assertInstanceOf(BugFix.class, bugFix);
        assertInstanceOf(Epic.class, epic);

        // All should inherit from Ticket
        assertInstanceOf(Ticket.class, story);
        assertInstanceOf(Ticket.class, bugFix);
        assertInstanceOf(Ticket.class, epic);
    }

    @Test
    void testFactoryWithDifferentPriorities() {
        Ticket highest = factory.createStory("F", "F", "T", developer, Priority.HIGHEST);
        Ticket high = factory.createStory("F", "F", "T", developer, Priority.HIGH);
        Ticket medium = factory.createStory("F", "F", "T", developer, Priority.MEDIUM);
        Ticket low = factory.createStory("F", "F", "T", developer, Priority.LOW);
        Ticket lowest = factory.createStory("F", "F", "T", developer, Priority.LOWEST);

        assertEquals(Priority.HIGHEST, highest.getPriority());
        assertEquals(Priority.HIGH, high.getPriority());
        assertEquals(Priority.MEDIUM, medium.getPriority());
        assertEquals(Priority.LOW, low.getPriority());
        assertEquals(Priority.LOWEST, lowest.getPriority());
    }

    @Test
    void testFactoryWithDifferentDevelopers() {
        Developer dev1 = new Developer("Developer1");
        Developer dev2 = new Developer("Developer2");
        Developer dev3 = new Developer("Developer3");

        Ticket ticket1 = factory.createStory("F", "F", "T", dev1, Priority.HIGH);
        Ticket ticket2 = factory.createBugFix("B", "B", "T", dev2, Priority.HIGH);
        Ticket ticket3 = factory.createEpic("G", "R", "E", "T", dev3, Priority.HIGH);

        assertEquals(dev1, ticket1.getAssignedBy());
        assertEquals(dev2, ticket2.getAssignedBy());
        assertEquals(dev3, ticket3.getAssignedBy());
    }

    @Test
    void testStoryCreatedByFactoryCanBeModified() {
        Ticket ticket = factory.createStory(
                "Initial feature",
                "Feature",
                "Task",
                developer,
                Priority.MEDIUM
        );

        Developer newAssignee = new Developer("NewDeveloper");
        ticket.setAssignee(newAssignee);
        ticket.setPointValue(8);

        assertEquals(newAssignee, ticket.getAssignee());
        assertEquals(8, ticket.getPointValue());
    }

    @Test
    void testBugFixCreatedByFactoryCanBeModified() {
        Ticket ticket = factory.createBugFix(
                "Bug to fix",
                "Bug",
                "Task",
                developer,
                Priority.HIGH
        );

        Developer assignee = new Developer("Fixer");
        ticket.setAssignee(assignee);
        ticket.setPointValue(5);

        assertEquals(assignee, ticket.getAssignee());
        assertEquals(5, ticket.getPointValue());
    }

    @Test
    void testEpicCreatedByFactoryCanHaveStoriesAdded() {
        Ticket epicTicket = factory.createEpic(
                "Release goals",
                "v3.0",
                "Epic",
                "Major release",
                developer,
                Priority.HIGH
        );

        Epic epic = (Epic) epicTicket;

        Story story = (Story) factory.createStory(
                "Feature for epic",
                "Feature",
                "Task in epic",
                developer,
                Priority.MEDIUM
        );

        epic.addStory(story);

        assertEquals(1, epic.getStories().size());
        assertEquals(story, epic.getStories().get(0));
    }

    @Test
    void testFactoryIsStateless() {
        TicketFactory factory1 = new TicketFactory();
        TicketFactory factory2 = new TicketFactory();

        Ticket ticket1 = factory1.createStory("F", "F", "T", developer, Priority.HIGH);
        Ticket ticket2 = factory2.createStory("F", "F", "T", developer, Priority.HIGH);

        // Both factories create equivalent objects
        assertEquals(ticket1.getFeatureType(), ticket2.getFeatureType());
        assertEquals(ticket1.getPriority(), ticket2.getPriority());
    }

    @Test
    void testFactoryCreatesDistinctInstances() {
        Ticket ticket1 = factory.createStory("F", "F", "T", developer, Priority.HIGH);
        Ticket ticket2 = factory.createStory("F", "F", "T", developer, Priority.HIGH);

        assertNotSame(ticket1, ticket2);
        assertNotEquals(ticket1, ticket2);
    }

    @Test
    void testComplexStoryCreation() {
        Ticket complexStory = factory.createStory(
                "Complex user authentication with OAuth2, multi-factor authentication, and session management",
                "Feature",
                "Implement comprehensive authentication system",
                developer,
                Priority.HIGHEST
        );

        Story story = (Story) complexStory;
        assertEquals(
                "Complex user authentication with OAuth2, multi-factor authentication, and session management",
                story.getFeatureDetails()
        );
    }

    @Test
    void testComplexBugFixCreation() {
        Ticket complexBug = factory.createBugFix(
                "Memory leak in image processing module when handling files > 100MB on Windows 10 with Chrome 120+",
                "CriticalBug",
                "Investigate and fix memory leak in image processing",
                developer,
                Priority.HIGHEST
        );

        BugFix bugFix = (BugFix) complexBug;
        assertTrue(bugFix.getBugDescription().contains("Memory leak"));
        assertEquals(Priority.HIGHEST, bugFix.getPriority());
    }
}
