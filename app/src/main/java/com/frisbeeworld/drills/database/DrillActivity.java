package com.frisbeeworld.drills.database;

public class DrillActivity {
    private int id;
    private int drillId;
    private int groupId;
    private int sessionId;
    private int duration;
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDrillId() {
        return drillId;
    }

    public void setDrillId(int drillId) {
        this.drillId = drillId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /*
     id
- drillId
- groupId
- duration
- location?
- notes?
     */
}
