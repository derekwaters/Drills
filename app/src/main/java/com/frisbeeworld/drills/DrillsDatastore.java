package com.frisbeeworld.drills;

import com.frisbeeworld.drills.database.Drill;
import com.frisbeeworld.drills.database.Session;

import java.util.ArrayList;

/**
 * Created by Derek on 10/04/2017.
 */

public class DrillsDatastore {

    private static DrillsDatastore singleton;

    private Session currentSession;
    private ArrayList<Drill> drills;
    private ArrayList<String> tags;

    public static DrillsDatastore getDatastore()
    {
        if (singleton == null)
        {
            singleton = new DrillsDatastore();
        }
        return singleton;
    }

    public Session getCurrentSession ()
    {
        return this.currentSession;
    }

    public void setCurrentSession (Session newSession)
    {
        this.currentSession = newSession;
    }

    public ArrayList<Drill> getDrills()
    {
        return drills;
    }

    public ArrayList<String> getTags()
    {
        return tags;
    }


    private DrillsDatastore()
    {
        drills = new ArrayList<>();
        tags = new ArrayList<>();

        Drill newDrill = new Drill();
        newDrill.setId(1000);
        newDrill.setVideoUrl("http://www.youtube.com/");
        newDrill.setImageUrl("http://www.google.com/");
        newDrill.setTags(new String[]{"handball", "junior", "senior"});
        newDrill.setMinTime(1);
        newDrill.setMaxTime(10);
        newDrill.setName("Handball Lines");

        drills.add(newDrill);

        tags.add("handball");
        tags.add("kick");
        tags.add("mark");
        tags.add("tackle");
        tags.add("pickup");
    }
}
