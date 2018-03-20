package com.frisbeeworld.drills;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.frisbeeworld.drills.database.DrillActivity;
import com.frisbeeworld.drills.database.Session;

public class EditSessionActivity extends AppCompatActivity {

    private static final int RC_NEW_ACTIVITY = 1;
    private static final int RC_EDIT_ACTIVITY = 2;

    private RecyclerView sessionList;
    private RecyclerView.Adapter sessionAdapter;
    private RecyclerView.LayoutManager sessionLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addActivity();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();

        getSupportActionBar().setTitle(currentSession.getName());


        sessionList = (RecyclerView)findViewById(R.id.sessionactivity_list);

        // This can improve performance if content changes don't change
        // the layout size, though I think it will in our case.
        // drillsList.setHasFixedSize(true);

        sessionLayoutManager = new LinearLayoutManager(this);
        sessionList.setLayoutManager(sessionLayoutManager);

        sessionAdapter = new EditSessionListAdapter(this);
        sessionList.setAdapter(sessionAdapter);

        sessionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(EditSessionActivity.this, "Activity selected?!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_session_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_start:
                runSession();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_sign_out:
                AuthUI.getInstance().signOut(this);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Bundle res = data.getExtras();
            if (res != null) {
                String newDrillId = res.getString(EditActivityActivity.ACTIVITY_DRILL_ID);
                boolean hasDuration = res.getBoolean(EditActivityActivity.ACTIVITY_HAS_DURATION);
                int newDuration = res.getInt(EditActivityActivity.ACTIVITY_DURATION);
                String newNotes = res.getString(EditActivityActivity.ACTIVITY_NOTES);
                Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();

                switch (requestCode) {
                    case RC_NEW_ACTIVITY:
                        DrillActivity newActivity = currentSession.addActivity(newDrillId,
                                hasDuration, newDuration, newNotes);

                        DrillsDatastore.getDatastore().updateActivity(currentSession, newActivity);

                        sessionAdapter = new EditSessionListAdapter(this);
                        sessionList.setAdapter(sessionAdapter);
                        break;

                    case RC_EDIT_ACTIVITY:
                        String activityId = res.getString(EditActivityActivity.ACTIVITY_ID);

                        DrillActivity updateActivity = currentSession.findActivity(activityId);
                        updateActivity.setDrillId(newDrillId);
                        updateActivity.setDuration(newDuration);
                        updateActivity.setNotes(newNotes);
                        updateActivity.setHasDuration(hasDuration);

                        DrillsDatastore.getDatastore().updateActivity(currentSession, updateActivity);

                        sessionAdapter = new EditSessionListAdapter(this);
                        sessionList.setAdapter(sessionAdapter);
                        break;

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addActivity ()
    {
        Intent editActivityIntent = new Intent(getApplicationContext(), EditActivityActivity.class);
        startActivityForResult(editActivityIntent, RC_NEW_ACTIVITY);
    }

    public void editActivity (DrillActivity activity)
    {
        Intent editActivityIntent = new Intent(getApplicationContext(), EditActivityActivity.class);
        editActivityIntent.putExtra(EditActivityActivity.ACTIVITY_ID, activity.getId());
        startActivityForResult(editActivityIntent, RC_EDIT_ACTIVITY);
    }

    public void removeActivity (String activityId)
    {
        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        currentSession.removeActivity(activityId);
    }

    public void runSession ()
    {
        // Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        Intent runSessionIntent = new Intent(getApplicationContext(), RunSessionActivity.class);
        // runSessionIntent.putExtra(RunSessionActivity.SESSION_ID, currentSession.getId());
        startActivity(runSessionIntent);
    }
}
