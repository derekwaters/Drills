package com.frisbeeworld.drills;

import com.frisbeeworld.drills.database.Drill;
import com.frisbeeworld.drills.database.Session;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Derek on 10/04/2017.
 */

public class DrillsDatastore {

    private static DrillsDatastore singleton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference drillsReference;
    private ChildEventListener drillsEventListener;

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


    private DrillsDatastore() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        drillsReference = firebaseDatabase.getReference().child("drills");
        drillsEventListener = null;

        drills = new ArrayList<>();
        tags = new ArrayList<>();
    }

    public void setupDatabaseListeners() {
        if (drillsEventListener == null) {
            drillsEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Drill newDrill = dataSnapshot.getValue(Drill.class);
                    //newSession.setId(dataSnapshot.getKey());
                    drills.add(newDrill);

                    for (String tagName : newDrill.getTags())
                    {
                        if (!tags.contains(tagName))
                        {
                            tags.add(tagName);
                        }
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            drillsReference.addChildEventListener(drillsEventListener);
        }
    }

    public void detachDatabaseListeners() {
        if (drillsEventListener != null) {
            drillsReference.removeEventListener(drillsEventListener);
            drillsEventListener = null;
        }
    }

/*

    Drill newDrill = new Drill();
        newDrill.setId(1000);
        newDrill.setVideoUrl("http://www.youtube.com/");
        newDrill.setImageUrl("http://www.google.com/");
        newDrill.setTags(new String[]{"handball", "junior", "senior"});
        newDrill.setMinTime(1);
        newDrill.setMaxTime(10);
        newDrill.setName("Handball Lines");
        newDrill.setDescription("A description goes here. Something about how the drill runs");

        drills.add(newDrill);


        newDrill = new Drill();
        newDrill.setId(1001);
        newDrill.setVideoUrl("http://www.youtube.com/");
        newDrill.setImageUrl("http://www.google.com/");
        newDrill.setTags(new String[]{"kicking", "junior", "senior"});
        newDrill.setMinTime(15);
        newDrill.setMaxTime(20);
        newDrill.setName("Kick To Kick");
        newDrill.setDescription("A description goes here. Something about how the drill runs");

        drills.add(newDrill);


        tags.add("handball");
        tags.add("kick");
        tags.add("mark");
        tags.add("tackle");
        tags.add("pickup");
    }
    */
}
