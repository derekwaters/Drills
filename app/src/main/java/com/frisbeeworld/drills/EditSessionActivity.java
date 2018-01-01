package com.frisbeeworld.drills;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.frisbeeworld.drills.database.Session;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditSessionActivity extends AppCompatActivity {

    private static final int RC_PICK_DRILLS = 1;

    private RecyclerView sessionList;
    private RecyclerView.Adapter sessionAdapter;
    private RecyclerView.LayoutManager sessionLayoutManager;

    private TextView textSessionName;
    private TextView textSessionLocation;
    private TextView textSessionDateTime;
    private TextView textSessionDuration;

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
                Intent pickDrillsIntent = new Intent(getApplicationContext(), PickDrillsActivity.class);
                startActivityForResult(pickDrillsIntent, RC_PICK_DRILLS);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();

        getSupportActionBar().setTitle(currentSession.getName());


        textSessionName = (TextView)findViewById(R.id.text_session_name);
        textSessionLocation = (TextView)findViewById(R.id.text_session_location);
        textSessionLocation.setClickable(true);
        textSessionLocation.setMovementMethod(LinkMovementMethod.getInstance());
        textSessionDateTime = (TextView)findViewById(R.id.text_session_datetime);
        textSessionDuration = (TextView)findViewById(R.id.text_session_duration);

        this.refreshHeaderInfo();



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

    protected void refreshHeaderInfo ()
    {
        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();

        this.textSessionName.setText(currentSession.getName());
        this.textSessionLocation.setText(currentSession.getLocation());
        this.textSessionDateTime.setText(currentSession.getStartTimeString());
        this.textSessionDuration.setText(Session.formatDuration(currentSession.getDuration()));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RC_PICK_DRILLS:
                Bundle res = data.getExtras();
                String pickedDrill = res.getString(PickDrillsActivity.SELECTED_DRILL_ID);

                Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
                currentSession.addDrill(pickedDrill);

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference sessionReference = db.getReference().child("sessions").
                        child(currentSession.getId());
                sessionReference.setValue(currentSession);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
