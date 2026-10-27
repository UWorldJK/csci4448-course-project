package ticketsystem.tickets;

import ticketsystem.developer.Developer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Epic extends Ticket {
    private final String epicGoals;
    private final String releaseName;

    private final List<Story> stories = new ArrayList<>();
    private final List<BugFix> bugFixes = new ArrayList<>();

    public Epic(String epicGoals, String releaseName, String featureType, String taskDescription, Developer assignedBy, Priority priority) {
        super(featureType, taskDescription, assignedBy, priority);
        this.epicGoals = epicGoals;
        this.releaseName = releaseName;
    }

    public String getEpicGoals() { return epicGoals; }
    public String getReleaseName() { return releaseName; }

    public void addStory(Story story) { stories.add(story); }
    public void addBugFix(BugFix bugFix) { bugFixes.add(bugFix); }

    public List<Story> getStories() { return Collections.unmodifiableList(stories); }
    public List<BugFix> getBugFixes() { return Collections.unmodifiableList(bugFixes); }
}