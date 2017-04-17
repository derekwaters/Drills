package com.frisbeeworld.drills;

import com.frisbeeworld.drills.database.Session;

/**
 * Created by Derek on 10/04/2017.
 */

public class DrillsDatastore {

    private static DrillsDatastore singleton;

    private Session currentSession;

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






    private DrillsDatastore()
    {

    }
}
