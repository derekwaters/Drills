package com.frisbeeworld.drills.database;


import com.frisbeeworld.drills.DrillsDatastore;
import com.google.firebase.database.Exclude;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
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

    // Runtime properties
    private long currentSessionRemainingTime;
    private long currentSessionStatus;

    public Session()
    {
        this.activities = new HashMap<>();
    }

    @Exclude
    public long getRemainingTime () { return currentSessionRemainingTime; }
    @Exclude
    public void setRemainingTime (long remaining) { currentSessionRemainingTime = remaining; }


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
            if (activity.getHasDuration())
            {
                duration += activity.getDuration();
            }
        }
        return duration;
    }

    public HashMap<String, DrillActivity> getActivities() { return activities; }

    public String getStartTimeString ()
    {
        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(this.startTime);
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

    public DrillActivity addActivity(String drillId, boolean hasDuration, int duration, String notes)
    {
        DrillActivity newActivity = DrillsDatastore.getDatastore().addActivity(
                DrillsDatastore.getDatastore().getCurrentSession());

        newActivity.setDuration(duration);
        newActivity.setNotes(notes);
        newActivity.setDrillId(drillId);
        // Find the maximum order
        int newOrder = 0;

        Collection<DrillActivity> activities = this.activities.values();
        for (DrillActivity activity: activities)
        {
            if (activity.getOrder() >= newOrder)
            {
                newOrder = activity.getOrder() + 1;
            }
        }
        newActivity.setOrder(newOrder);
        this.activities.put(drillId, newActivity);

        return newActivity;
    }

    public void removeActivity (String id)
    {
        this.activities.remove(id);

        DrillsDatastore.getDatastore().removeActivity(
                DrillsDatastore.getDatastore().getCurrentSession(),
                id);
    }

    public DrillActivity getActivity (String id)
    {
        return this.activities.get(id);
    }

    public DrillActivity getActivityByPosition (int position)
    {
        Set<String> keys = this.activities.keySet();
        Object[] keyStrs = keys.toArray();
        Arrays.sort(keyStrs, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                DrillActivity firstActivity = activities.get((String)o1);
                DrillActivity secondActivity = activities.get((String)o2);
                return firstActivity.getOrder() - secondActivity.getOrder();
            }
        });

        String findKey = (String)keyStrs[position];

        DrillActivity result = this.activities.get(findKey);
        result.setId(findKey);

        return result;
    }

    public DrillActivity findActivity (String id)
    {
        return this.activities.get(id);
    }
}
