package ticketsystem.tickets;

import ticketsystem.developer.Developer;

public class BugFix extends Ticket {
    private final String bugDescription;

    public BugFix(String bugDescription, String featureType, String taskDescription, Developer assignedBy, Priority priority) {
        super(featureType, taskDescription, assignedBy, priority);
        this.bugDescription = bugDescription;
    }

    public String getBugDescription() { return bugDescription; }
}