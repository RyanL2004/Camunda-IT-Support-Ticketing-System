package io.camunda.getstarted.whosfifty;


import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1



import java.time.LocalDate;

public class Ticket {
    @JsonProperty("id")
    private long id;
    @JsonProperty("description")
    private String description;
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("effect")
    private String effect;
    @JsonProperty("issueDate")
    private LocalDate issueDate;
    @JsonProperty("assigned")
    private boolean assigned;
    @JsonProperty("assignedTo")
    private String assignedTo;
    @JsonProperty("status")
    private String status; // Add status field

    // Constructor
    public Ticket() {
    }

    public Ticket(String description, String priority, String effect, LocalDate issueDate) {
        this.description = description;
        this.priority = priority;
        this.effect = effect;
        this.issueDate = issueDate;
        this.assigned = false;
        this.assignedTo = null;
        this.status = "Open"; // Set initial status to "Open"
    }

    public void setCustomId(long id){
        this.id = id;
    }

    // Add toString() method if needed for debugging
    @Override
    public String toString() {
        return "Ticket || \n" + "Description: " + description + "\nPriority:" + priority + "\nEffect:" + effect + "\nIssueDate:" + issueDate + "\nStatus:" + status + "\n\n";
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

