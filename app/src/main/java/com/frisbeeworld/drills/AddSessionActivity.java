package com.frisbeeworld.drills;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.frisbeeworld.drills.database.Session;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class AddSessionActivity extends AppCompatActivity {

    private TextView textSessionName;
    private Button btnAddSession;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference sessionsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseDatabase = FirebaseDatabase.getInstance();
        sessionsReference = firebaseDatabase.getReference().child("sessions");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textSessionName = (TextView)findViewById(R.id.textSessionName);
        btnAddSession = (Button)findViewById(R.id.btnAddSession);

        btnAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session addSession = new Session();
                addSession.setDuration(60);
                addSession.setStartTime(new Date());
                addSession.setLocation("Koornang Park");
                addSession.setName(textSessionName.getText().toString());

                DatabaseReference newSession = sessionsReference.push();
                addSession.setId(newSession.getKey());
                newSession.setValue(addSession);

                finish();
            }
        });
    }

}
