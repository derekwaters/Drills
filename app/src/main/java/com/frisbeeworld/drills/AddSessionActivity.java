package com.frisbeeworld.drills;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.frisbeeworld.drills.database.Session;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;

public class AddSessionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView textSessionName;
    private TextView textSessionDate;
    private TextView textSessionTime;
    private TextView textSessionLocation;
    private Button btnAddSession;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference sessionsReference;
    private Date    selectedDate;

    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectedDate = new Date();

        firebaseDatabase = FirebaseDatabase.getInstance();
        sessionsReference = firebaseDatabase.getReference().child("sessions");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textSessionName = (TextView)findViewById(R.id.textSessionName);
        textSessionDate = (TextView)findViewById(R.id.txtDate);
        textSessionTime = (TextView)findViewById(R.id.txtTime);
        textSessionLocation = (TextView)findViewById(R.id.editLocation);
        btnAddSession = (Button)findViewById(R.id.btnAddSession);


        final Calendar c = Calendar.getInstance();
        c.setTime(selectedDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);

        datePicker = new DatePickerDialog(
                this, AddSessionActivity.this, year, month, day);
        timePicker = new TimePickerDialog(
                this, AddSessionActivity.this, hour, min, true);
        refreshDateTimeFields();

        textSessionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });

        textSessionTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show();
            }
        });

        btnAddSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session addSession = new Session();


                addSession.setStartTime(new Date());
                addSession.setLocation(textSessionLocation.getText().toString());
                addSession.setName(textSessionName.getText().toString());

                DatabaseReference newSession = sessionsReference.push();
                addSession.setId(newSession.getKey());
                newSession.setValue(addSession);

                finish();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.setTime(selectedDate);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        selectedDate = c.getTime();

        refreshDateTimeFields();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        final Calendar c = Calendar.getInstance();
        c.setTime(selectedDate);
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        selectedDate = c.getTime();

        refreshDateTimeFields();
    }

    protected void refreshDateTimeFields ()
    {
        textSessionDate.setText(DateFormat.getDateInstance(DateFormat.LONG).format(selectedDate));
        textSessionTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(selectedDate));
    }
}
