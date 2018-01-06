package com.frisbeeworld.drills.database;


import com.frisbeeworld.drills.DrillsDatastore;
import com.google.firebase.database.Exclude;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class Session {

    private String id;
    private int groupId;
    private String name;
    private String location;
    private Date startTime;
    private HashMap<String, DrillActivity> activities;

    public Session()
    {
        this.activities = new HashMap<>();
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
        int duration = 0;
        Collection<DrillActivity> activities = this.activities.values();
        for (DrillActivity activity: activities)
        {
            duration += activity.getDuration();
        }
        return duration;
    }

    public HashMap<String, DrillActivity> getActivities() { return activities; }

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

    public static String formatTimer (long seconds)
    {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }

    public DrillActivity addActivity(String drillId, int duration, String notes)
    {
        DrillActivity newActivity = DrillsDatastore.getDatastore().addActivity(
                DrillsDatastore.getDatastore().getCurrentSession());

        newActivity.setDuration(duration);
        newActivity.setNotes(notes);
        newActivity.setDrillId(drillId);
        this.activities.put(drillId, newActivity);

        return newActivity;
    }

    public void removeActivity (int position)
    {
        Set<String> keys = this.activities.keySet();
        String removeKey = (String)keys.toArray()[position];

        DrillActivity theActivity = this.activities.get(removeKey);

        this.activities.remove(removeKey);

        DrillsDatastore.getDatastore().removeActivity(
                DrillsDatastore.getDatastore().getCurrentSession(),
                theActivity);
    }

    public DrillActivity getActivity (int position)
    {
        Set<String> keys = this.activities.keySet();
        String findKey = (String)keys.toArray()[position];

        DrillActivity result = this.activities.get(findKey);
        result.setId(findKey);

        return result;
    }

    public DrillActivity findActivity (String id)
    {
        return this.activities.get(id);
    }
}
