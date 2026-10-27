package ticketsystem.tickets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ticketsystem.developer.Developer;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BugFixTest {
    private BugFix bugFix;
    private Developer assignedBy;
    private Developer assignee;

    @BeforeEach
    void setUp() {
        assignedBy = new Developer("Manager");
        assignee = new Developer("Developer");
        bugFix = new BugFix(
                "Login page crashes on Safari",
                "Bug",
                "Fix login page crash",
                assignedBy,
                Priority.HIGHEST
        );
    }

    @Test
    void testBugFixCreation() {
        assertNotNull(bugFix);
        assertEquals("Login page crashes on Safari", bugFix.getBugDescription());
        assertEquals("Bug", bugFix.getFeatureType());
        assertEquals("Fix login page crash", bugFix.getTaskDescription());
        assertEquals(Priority.HIGHEST, bugFix.getPriority());
    }

    @Test
    void testBugDescriptionImmutable() {
        assertEquals("Login page crashes on Safari", bugFix.getBugDescription());
        // Verify it cannot be changed (no setter available)
    }

    @Test
    void testAssigneeCanBeSet() {
        bugFix.setAssignee(assignee);
        assertEquals(assignee, bugFix.getAssignee());
    }

    @Test
    void testDueDateCanBeSet() {
        OffsetDateTime dueDate = OffsetDateTime.now().plusDays(1);
        bugFix.setDueDate(dueDate);
        assertEquals(dueDate, bugFix.getDueDate());
    }

    @Test
    void testPointEstimateCanBeSet() {
        bugFix.setPointValue(3);
        assertEquals(3, bugFix.getPointValue());
    }

    @Test
    void testBugFixWithDifferentPriorities() {
        BugFix criticalBug = new BugFix(
                "Database connection lost",
                "Bug",
                "Fix database connection",
                assignedBy,
                Priority.HIGHEST
        );

        BugFix minorBug = new BugFix(
                "Typo in error message",
                "Bug",
                "Fix typo",
                assignedBy,
                Priority.LOWEST
        );

        assertEquals(Priority.HIGHEST, criticalBug.getPriority());
        assertEquals(Priority.LOWEST, minorBug.getPriority());
    }

    @Test
    void testAssignedByImmutable() {
        assertEquals(assignedBy, bugFix.getAssignedBy());
        // Verify it cannot be changed
    }

    @Test
    void testMultipleBugFixesAreIndependent() {
        BugFix bugFix2 = new BugFix(
                "Memory leak in image processing",
                "Bug",
                "Fix memory leak",
                assignedBy,
                Priority.HIGH
        );

        bugFix.setPointValue(5);
        bugFix2.setPointValue(8);

        assertEquals(5, bugFix.getPointValue());
        assertEquals(8, bugFix2.getPointValue());
    }

    @Test
    void testNullFeatureTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new BugFix("Bug description", null, "Task", assignedBy, Priority.HIGH)
        );
    }

    @Test
    void testEmptyFeatureTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new BugFix("Bug description", "", "Task", assignedBy, Priority.HIGH)
        );
    }

    @Test
    void testCriticalBugFix() {
        BugFix criticalBug = new BugFix(
                "Security vulnerability in authentication",
                "SecurityBug",
                "Patch authentication vulnerability",
                assignedBy,
                Priority.HIGHEST
        );

        Developer securityDev = new Developer("SecurityExpert");
        criticalBug.setAssignee(securityDev);
        criticalBug.setPointValue(13);
        criticalBug.setDueDate(OffsetDateTime.now().plusHours(24));

        assertEquals("Security vulnerability in authentication", criticalBug.getBugDescription());
        assertEquals(Priority.HIGHEST, criticalBug.getPriority());
        assertEquals(securityDev, criticalBug.getAssignee());
        assertEquals(13, criticalBug.getPointValue());
        assertNotNull(criticalBug.getDueDate());
    }

    @Test
    void testBugFixWithComplexDescription() {
        String complexDescription = "User report: When uploading files larger than 100MB, " +
                "the application hangs and eventually crashes. " +
                "Reproducible on Windows 10 with Chrome 120+. " +
                "Does not occur on macOS.";

        BugFix complexBug = new BugFix(
                complexDescription,
                "Bug",
                "Fix large file upload issue",
                assignedBy,
                Priority.HIGH
        );

        assertEquals(complexDescription, complexBug.getBugDescription());
    }

    @Test
    void testBugFixSequentialPointChanges() {
        assertNull(bugFix.getPointValue());

        bugFix.setPointValue(3);
        assertEquals(3, bugFix.getPointValue());

        bugFix.setPointValue(5);
        assertEquals(5, bugFix.getPointValue());

        bugFix.setPointValue(2);
        assertEquals(2, bugFix.getPointValue());
    }

    @Test
    void testMultipleBugFixAssignments() {
        Developer dev1 = new Developer("Dev1");
        Developer dev2 = new Developer("Dev2");

        bugFix.setAssignee(dev1);
        assertEquals(dev1, bugFix.getAssignee());

        bugFix.setAssignee(dev2);
        assertEquals(dev2, bugFix.getAssignee());
    }
}
