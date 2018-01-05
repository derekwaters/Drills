package com.frisbeeworld.drills;

import com.frisbeeworld.drills.database.Drill;
import com.frisbeeworld.drills.database.DrillActivity;
import com.frisbeeworld.drills.database.Session;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Derek on 10/04/2017.
 */

public class DrillsDatastore {

    private static DrillsDatastore singleton;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference drillsReference;
    private ChildEventListener drillsEventListener;

    private DatabaseReference sessionsReference;
    private ChildEventListener sessionEventListener;


    private Session currentSession;
    private ArrayList<Drill> drills;
    private ArrayList<String> tags;
    private ArrayList<Session> sessions;
    private HashMap<String, Drill> drillMap;

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

    public ArrayList<Session> getSessions() { return sessions; }

    public Drill getDrill(String drillId) { return drillMap.get(drillId); }

    private DrillsDatastore() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        drillsReference = firebaseDatabase.getReference().child("drills");
        drillsEventListener = null;

        sessionsReference = firebaseDatabase.getReference().child("sessions");
        sessionEventListener = null;


        drills = new ArrayList<>();
        tags = new ArrayList<>();
        sessions = new ArrayList<>();
        drillMap = new HashMap<>();
    }

    public void updateSession (Session session) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference sessionReference = db.getReference().child("sessions").
                child(session.getId());
        sessionReference.setValue(session);
    }

    public DrillActivity addActivity (Session session) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference activRef = db.getReference().child("sessions").
                child(session.getId()).child("activities");
        DatabaseReference newActivityRef = activRef.push();
        DrillActivity newActivity = new DrillActivity();
        newActivity.setId(newActivityRef.getKey());
        return newActivity;
    }

    public DrillActivity updateActivity (Session session, DrillActivity activity) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference activRef = db.getReference().child("sessions").
                child(session.getId()).child("activities").child(activity.getId());
        activRef.setValue(activity);
        return activity;
    }

    public DrillActivity removeActivity (Session session, DrillActivity activity) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference activRef = db.getReference().child("sessions").
                child(session.getId()).child("activities").child(activity.getId());
        activRef.removeValue();
        return activity;
    }

    public void setupDatabaseListeners(final SessionAdapter sessionAdapter) {
        if (drillsEventListener == null) {
            drillsEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Drill newDrill = dataSnapshot.getValue(Drill.class);
                    newDrill.setId(dataSnapshot.getKey());
                    drills.add(newDrill);
                    drillMap.put(newDrill.getId(), newDrill);

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

        if (sessionEventListener == null) {

            sessionEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    if (dataSnapshot.hasChildren()) {
                        Session newSession = dataSnapshot.getValue(Session.class);
                        newSession.setId(dataSnapshot.getKey());
                        sessionAdapter.add(newSession);
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
            sessionsReference.addChildEventListener(sessionEventListener);
        }
    }

    public void detachDatabaseListeners() {
        if (drillsEventListener != null) {
            drillsReference.removeEventListener(drillsEventListener);
            drillsEventListener = null;
        }

        if (sessionEventListener != null) {
            sessionsReference.removeEventListener(sessionEventListener);
            sessionEventListener = null;
        }
    }
}
