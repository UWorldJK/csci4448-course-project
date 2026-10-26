package ticketsystem.tickets;

import ticketsystem.developer.Developer;

import java.time.OffsetDateTime;

public abstract class Ticket {
    Developer assignee;
    Developer assignedBy;
    String featureType;
    String taskDescription;
    OffsetDateTime dueDate;
    Priority priority;
    Integer pointEstimate;

    private Ticket() {}

    public static class TicketBuilder {

    }
}
