package com.company.framework.dtos;


public class IssueDetailsDto {

    private int project_id;
    private int tracker_id;
    private int status_id;
    private int priority_id;
    private String subject;
    private String description;


    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getTracker_id() {
        return tracker_id;
    }

    public void setTracker_id(int tracker_id) {
        this.tracker_id = tracker_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(int priority_id) {
        this.priority_id = priority_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
