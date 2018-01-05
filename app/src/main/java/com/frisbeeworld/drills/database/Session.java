package com.frisbeeworld.drills.database;


import android.util.Log;

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
        int duration = 0;
        for (DrillActivity activity: this.activities)
        {
            duration += activity.getDuration();
        }
        return duration;
    }

    public ArrayList<DrillActivity> getActivities() { return activities; }

    public void removeActivity (int position)
    {
        DrillActivity removed = this.activities.remove(position);
        Log.v("Drills", "removed item " + removed.getDrillId());
    }

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

    public void addActivity(String drillId, int duration, String notes)
    {
        Drill theDrill = DrillsDatastore.getDatastore().getDrill(drillId);

        DrillActivity newActivity = DrillsDatastore.getDatastore().addActivity(
                DrillsDatastore.getDatastore().getCurrentSession());

        newActivity.setDuration(duration);
        newActivity.setNotes(notes);
        newActivity.setDrillId(theDrill.getId());
        this.activities.add(newActivity);
    }

    public DrillActivity getActivity (int position)
    {
        return this.activities.get(position);
    }

    public DrillActivity findActivity (String id)
    {
        for (DrillActivity activity : this.activities)
        {
            if (activity.getId() == id)
            {
                return activity;
            }
        }
        return null;
    }
}
