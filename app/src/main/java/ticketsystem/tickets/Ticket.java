package ticketsystem.tickets;

import ticketsystem.developer.Developer;

import java.time.OffsetDateTime;

public abstract class Ticket {
    private final String featureType;
    private final String taskDescription;
    private final Developer assignedBy;
    private final Priority priority;

    private Developer assignee;
    private OffsetDateTime dueDate;
    private Integer pointEstimate;

    public Ticket(String featureType, String taskDescription, Developer assignedBy, Priority priority) {
        if (featureType == null || featureType.isEmpty()) {
            throw new IllegalArgumentException("Feature Type is Required");
        }
        this.featureType = featureType;
        this.taskDescription = taskDescription;
        this.assignedBy = assignedBy;
        this.priority = priority;
    }

    public String getFeatureType() { return featureType; }
    public String getTaskDescription() { return taskDescription; }
    public Developer getAssignedBy() { return assignedBy; }
    public Priority getPriority() { return priority; }

    public Developer getAssignee() { return assignee; }
    public void setAssignee(Developer assignee) { this.assignee = assignee; }

    public OffsetDateTime getDueDate() { return dueDate; }
    public void setDueDate(OffsetDateTime dueDate) { this.dueDate = dueDate; }

    public Integer getPointValue() { return pointEstimate; }
    public void setPointValue(Integer pointEstimate) {
        this.pointEstimate = pointEstimate;
    }
}
