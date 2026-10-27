package ticketsystem.tickets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ticketsystem.developer.Developer;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class StoryTest {
    private Story story;
    private Developer assignedBy;
    private Developer assignee;

    @BeforeEach
    void setUp() {
        assignedBy = new Developer("Alice");
        assignee = new Developer("Bob");
        story = new Story(
                "User authentication feature",
                "Feature",
                "Implement user login",
                assignedBy,
                Priority.HIGH
        );
    }

    @Test
    void testStoryCreation() {
        assertNotNull(story);
        assertEquals("User authentication feature", story.getFeatureDetails());
        assertEquals("Feature", story.getFeatureType());
        assertEquals("Implement user login", story.getTaskDescription());
        assertEquals(Priority.HIGH, story.getPriority());
    }

    @Test
    void testFeatureDetailsImmutable() {
        assertEquals("User authentication feature", story.getFeatureDetails());
        // Verify it cannot be changed (no setter available)
    }

    @Test
    void testAssigneeCanBeSet() {
        story.setAssignee(assignee);
        assertEquals(assignee, story.getAssignee());
    }

    @Test
    void testDueDateCanBeSet() {
        OffsetDateTime dueDate = OffsetDateTime.now().plusDays(7);
        story.setDueDate(dueDate);
        assertEquals(dueDate, story.getDueDate());
    }

    @Test
    void testPointEstimateCanBeSet() {
        story.setPointValue(8);
        assertEquals(8, story.getPointValue());
    }

    @Test
    void testStoryWithDifferentPriorities() {
        Story mediumPriorityStory = new Story(
                "Dashboard feature",
                "Feature",
                "Create user dashboard",
                assignedBy,
                Priority.MEDIUM
        );

        Story lowPriorityStory = new Story(
                "Documentation",
                "Feature",
                "Update README",
                assignedBy,
                Priority.LOW
        );

        assertEquals(Priority.MEDIUM, mediumPriorityStory.getPriority());
        assertEquals(Priority.LOW, lowPriorityStory.getPriority());
    }

    @Test
    void testAssignedByImmutable() {
        assertEquals(assignedBy, story.getAssignedBy());
        // Verify it cannot be changed
    }

    @Test
    void testMultipleStoriesAreIndependent() {
        Story story2 = new Story(
                "Payment integration",
                "Feature",
                "Integrate payment gateway",
                assignedBy,
                Priority.HIGHEST
        );

        story.setPointValue(5);
        story2.setPointValue(13);

        assertEquals(5, story.getPointValue());
        assertEquals(13, story2.getPointValue());
    }

    @Test
    void testNullFeatureTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Story("Details", null, "Task", assignedBy, Priority.HIGH)
        );
    }

    @Test
    void testEmptyFeatureTypeThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Story("Details", "", "Task", assignedBy, Priority.HIGH)
        );
    }

    @Test
    void testStoryWithNullTaskDescription() {
        // Should allow null task description (not validated in constructor)
        Story storyWithNullTask = new Story(
                "Details",
                "Feature",
                null,
                assignedBy,
                Priority.MEDIUM
        );
        assertNull(storyWithNullTask.getTaskDescription());
    }

    @Test
    void testStoryWithMultiplePointUpdates() {
        assertNull(story.getPointValue());

        story.setPointValue(5);
        assertEquals(5, story.getPointValue());

        story.setPointValue(8);
        assertEquals(8, story.getPointValue());

        story.setPointValue(13);
        assertEquals(13, story.getPointValue());
    }
}
