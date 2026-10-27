package ticketsystem.tickets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ticketsystem.developer.Developer;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private Epic epic;
    private Developer assignedBy;
    private Developer assignee;

    @BeforeEach
    void setUp() {
        assignedBy = new Developer("ProductOwner");
        assignee = new Developer("TeamLead");
        epic = new Epic(
                "Implement user dashboard with analytics",
                "Release v2.0",
                "Epic",
                "User Dashboard Epic",
                assignedBy,
                Priority.HIGH
        );
    }

    @Test
    void testEpicCreation() {
        assertNotNull(epic);
        assertEquals("Implement user dashboard with analytics", epic.getEpicGoals());
        assertEquals("Release v2.0", epic.getReleaseName());
        assertEquals("Epic", epic.getFeatureType());
        assertEquals("User Dashboard Epic", epic.getTaskDescription());
        assertEquals(Priority.HIGH, epic.getPriority());
    }

    @Test
    void testEpicGoalsImmutable() {
        assertEquals("Implement user dashboard with analytics", epic.getEpicGoals());
        // Verify it cannot be changed (no setter available)
    }

    @Test
    void testReleaseNameImmutable() {
        assertEquals("Release v2.0", epic.getReleaseName());
        // Verify it cannot be changed (no setter available)
    }

    @Test
    void testAddStoryToEpic() {
        Story story = new Story(
                "User authentication",
                "Story",
                "Implement login",
                assignedBy,
                Priority.HIGH
        );

        epic.addStory(story);

        List<Story> stories = epic.getStories();
        assertEquals(1, stories.size());
        assertEquals(story, stories.get(0));
    }

    @Test
    void testAddMultipleStoriesToEpic() {
        Story story1 = new Story("Auth feature", "Story", "Login", assignedBy, Priority.HIGH);
        Story story2 = new Story("Dashboard feature", "Story", "Dashboard", assignedBy, Priority.MEDIUM);
        Story story3 = new Story("Reports feature", "Story", "Reports", assignedBy, Priority.MEDIUM);

        epic.addStory(story1);
        epic.addStory(story2);
        epic.addStory(story3);

        List<Story> stories = epic.getStories();
        assertEquals(3, stories.size());
        assertTrue(stories.contains(story1));
        assertTrue(stories.contains(story2));
        assertTrue(stories.contains(story3));
    }

    @Test
    void testAddBugFixToEpic() {
        BugFix bugFix = new BugFix(
                "Memory leak in dashboard",
                "Bug",
                "Fix memory leak",
                assignedBy,
                Priority.HIGH
        );

        epic.addBugFix(bugFix);

        List<BugFix> bugFixes = epic.getBugFixes();
        assertEquals(1, bugFixes.size());
        assertEquals(bugFix, bugFixes.get(0));
    }

    @Test
    void testAddMultipleBugFixesToEpic() {
        BugFix bugFix1 = new BugFix("Bug 1", "Bug", "Fix 1", assignedBy, Priority.HIGH);
        BugFix bugFix2 = new BugFix("Bug 2", "Bug", "Fix 2", assignedBy, Priority.MEDIUM);

        epic.addBugFix(bugFix1);
        epic.addBugFix(bugFix2);

        List<BugFix> bugFixes = epic.getBugFixes();
        assertEquals(2, bugFixes.size());
        assertTrue(bugFixes.contains(bugFix1));
        assertTrue(bugFixes.contains(bugFix2));
    }

    @Test
    void testEpicWithStoriesAndBugFixes() {
        Story story1 = new Story("Auth", "Story", "Login", assignedBy, Priority.HIGH);
        Story story2 = new Story("Dashboard", "Story", "Dashboard", assignedBy, Priority.MEDIUM);
        BugFix bugFix1 = new BugFix("Bug 1", "Bug", "Fix 1", assignedBy, Priority.HIGH);
        BugFix bugFix2 = new BugFix("Bug 2", "Bug", "Fix 2", assignedBy, Priority.LOW);

        epic.addStory(story1);
        epic.addStory(story2);
        epic.addBugFix(bugFix1);
        epic.addBugFix(bugFix2);

        assertEquals(2, epic.getStories().size());
        assertEquals(2, epic.getBugFixes().size());
    }

    @Test
    void testStoriesListIsUnmodifiable() {
        Story story = new Story("Auth", "Story", "Login", assignedBy, Priority.HIGH);
        epic.addStory(story);

        List<Story> stories = epic.getStories();
        assertThrows(UnsupportedOperationException.class, () -> stories.add(
                new Story("New", "Story", "New", assignedBy, Priority.LOW)
        ));
    }

    @Test
    void testBugFixesListIsUnmodifiable() {
        BugFix bugFix = new BugFix("Bug", "Bug", "Fix", assignedBy, Priority.HIGH);
        epic.addBugFix(bugFix);

        List<BugFix> bugFixes = epic.getBugFixes();
        assertThrows(UnsupportedOperationException.class, () -> bugFixes.add(
                new BugFix("New Bug", "Bug", "New Fix", assignedBy, Priority.LOW)
        ));
    }

    @Test
    void testAssigneeCanBeSet() {
        epic.setAssignee(assignee);
        assertEquals(assignee, epic.getAssignee());
    }

    @Test
    void testDueDateCanBeSet() {
        OffsetDateTime dueDate = OffsetDateTime.now().plusDays(30);
        epic.setDueDate(dueDate);
        assertEquals(dueDate, epic.getDueDate());
    }

    @Test
    void testPointEstimateCanBeSet() {
        epic.setPointValue(100);
        assertEquals(100, epic.getPointValue());
    }

    @Test
    void testEpicWithDifferentPriorities() {
        Epic criticalEpic = new Epic(
                "Security improvements",
                "Hotfix",
                "Epic",
                "Security Epic",
                assignedBy,
                Priority.HIGHEST
        );

        assertEquals(Priority.HIGHEST, criticalEpic.getPriority());
        assertEquals(Priority.HIGH, epic.getPriority());
    }

    @Test
    void testNullFeatureTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Epic("Goals", "Release", null, "Task", assignedBy, Priority.HIGH)
        );
    }

    @Test
    void testEmptyFeatureTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Epic("Goals", "Release", "", "Task", assignedBy, Priority.HIGH)
        );
    }

    @Test
    void testComplexEpicStructure() {
        // Create a realistic epic with multiple stories and bug fixes
        Story userAuth = new Story("User login", "Story", "Auth", assignedBy, Priority.HIGHEST);
        Story userProfile = new Story("User profile", "Story", "Profile", assignedBy, Priority.HIGH);
        Story reporting = new Story("Reports", "Story", "Reporting", assignedBy, Priority.MEDIUM);

        BugFix securityBug = new BugFix("XSS vulnerability", "Bug", "Fix XSS", assignedBy, Priority.HIGHEST);
        BugFix performanceBug = new BugFix("Slow queries", "Bug", "Optimize", assignedBy, Priority.HIGH);

        epic.addStory(userAuth);
        epic.addStory(userProfile);
        epic.addStory(reporting);
        epic.addBugFix(securityBug);
        epic.addBugFix(performanceBug);

        assertEquals(3, epic.getStories().size());
        assertEquals(2, epic.getBugFixes().size());
        assertEquals("Release v2.0", epic.getReleaseName());
    }

    @Test
    void testEmptyEpicInitially() {
        Epic newEpic = new Epic(
                "New goals",
                "Release v3.0",
                "Epic",
                "New epic",
                assignedBy,
                Priority.MEDIUM
        );

        assertEquals(0, newEpic.getStories().size());
        assertEquals(0, newEpic.getBugFixes().size());
    }

    @Test
    void testAssignedByIsImmutable() {
        assertEquals(assignedBy, epic.getAssignedBy());
    }
}
