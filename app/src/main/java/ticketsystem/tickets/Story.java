package ticketsystem.tickets;

import ticketsystem.developer.Developer;

public class Story extends Ticket {
    private final String featureDetails;

    public Story(String featureDetails, String featureType, String taskDescription, Developer assignedBy, Priority priority) {
        super(featureType, taskDescription, assignedBy, priority);
        this.featureDetails = featureDetails;
    }

    public String getFeatureDetails() { return featureDetails; }
}