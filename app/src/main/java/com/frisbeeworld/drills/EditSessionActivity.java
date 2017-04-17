package com.frisbeeworld.drills;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.frisbeeworld.drills.database.Session;

public class EditSessionActivity extends AppCompatActivity {

    private static final int RC_PICK_DRILLS = 1;

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

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();

        getSupportActionBar().setTitle(currentSession.getName());
    }

}
