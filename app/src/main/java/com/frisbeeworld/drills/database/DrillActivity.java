package com.frisbeeworld.drills.database;

import com.google.firebase.database.Exclude;

public class DrillActivity {
    private String id;
    private String drillId;
    private int groupId;
    private int order;
    private boolean hasDuration;
    private int duration;
    private String notes;

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDrillId() {
        return drillId;
    }

    public void setDrillId(String drillId) {
        this.drillId = drillId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public boolean getHasDuration() { return hasDuration; }

    public void setHasDuration(boolean hasDuration) { this.hasDuration = hasDuration; }

    public int getDuration() {
        return (this.hasDuration ? duration : 0);
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

    public int getOrder() { return order; }

    public void setOrder (int order) { this.order = order; }

    public void DrillActivity ()
    {
        this.hasDuration = true;
        this.duration = 5;
    }
}
