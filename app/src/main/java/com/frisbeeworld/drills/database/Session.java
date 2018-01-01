package com.frisbeeworld.drills.database;


import com.frisbeeworld.drills.DrillsDatastore;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Date;

public class Session {

    private String id;
    private int groupId;
    private String name;
    private String location;
    private Date startTime;
    private int duration;
    private ArrayList<DrillActivity> activities;

    public Session()
    {
        this.activities = new ArrayList<>();
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList<DrillActivity> getActivities() { return activities; }

    public String getStartTimeString ()
    {
        return this.startTime.toString();
    }

    public static String formatDuration (int duration)
    {
        String result = "";
        if (duration >= 60)
        {
            result = Integer.toString(duration/60) + "h ";
        }
        result += Integer.toString(duration % 60) + "m";

        return result;
    }

    public void addDrill(String drillId)
    {
        Drill theDrill = DrillsDatastore.getDatastore().getDrill(drillId);
        DrillActivity newActivity = new DrillActivity();
        newActivity.setDuration((theDrill.getMaxTime() + theDrill.getMinTime()) / 2);
        newActivity.setDrillId(theDrill.getId());
        this.duration += newActivity.getDuration();
        this.activities.add(newActivity);
    }
}
