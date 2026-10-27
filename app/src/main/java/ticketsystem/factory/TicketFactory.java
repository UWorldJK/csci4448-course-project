package ticketsystem.factory;

import ticketsystem.developer.Developer;
import ticketsystem.tickets.*;

public class TicketFactory {
    public TicketFactory() {
    }

    public Ticket createEpic(String epicGoals, String releaseName, String featureType, String taskDescription,
                             Developer assignedBy, Priority priority) {
        return new Epic(epicGoals, releaseName, featureType, taskDescription, assignedBy, priority);
    }

    public Ticket createStory(String featureDetails, String featureType, String taskDescription,
                              Developer assignedBy, Priority priority) {
        return new Story(featureDetails, featureType, taskDescription, assignedBy, priority);
    }

    public Ticket createBugFix(String bugDescription, String featureType, String taskDescription,
                         Developer assignedBy, Priority priority) {
        return new BugFix(bugDescription, featureType, taskDescription, assignedBy, priority);
    }
}
