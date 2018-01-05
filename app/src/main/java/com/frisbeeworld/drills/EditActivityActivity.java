package com.frisbeeworld.drills;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.frisbeeworld.drills.database.Drill;
import com.frisbeeworld.drills.database.DrillActivity;
import com.frisbeeworld.drills.database.Session;

import static com.frisbeeworld.drills.PickDrillsActivity.SELECTED_DRILL_ID;

public class EditActivityActivity extends AppCompatActivity {

    public static final String ACTIVITY_ID = "ACTIVITY_ID";
    public static final String ACTIVITY_DRILL_ID = "SELECTED_DRILL_ID";
    public static final String ACTIVITY_DURATION = "DRILL_DURATION";
    public static final String ACTIVITY_NOTES = "DRILL_NOTES";


    private static final int RC_PICK_DRILLS = 1;


    EditText editDrillName;
    Button  btnPickDrill;
    Button  btnSaveDrill;
    SeekBar seekDuration;
    TextView textDurationOutput;
    TextView textDurationRecommended;
    EditText editNotes;

    private Drill selectedDrill = null;
    private int minDuration = 0;
    private DrillActivity editActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editDrillName = (EditText)findViewById(R.id.editDrillName);
        btnPickDrill = (Button)findViewById(R.id.btnPickDrill);
        btnSaveDrill = (Button)findViewById(R.id.btnSaveDrill);
        seekDuration = (SeekBar)findViewById(R.id.seekBarDuration);
        textDurationOutput = (TextView)findViewById(R.id.textDurationOutput);
        textDurationRecommended = (TextView)findViewById(R.id.textDurationRecommended);
        editNotes = (EditText)findViewById(R.id.editNotes);

        btnPickDrill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickDrillsIntent = new Intent(getApplicationContext(), PickDrillsActivity.class);
                startActivityForResult(pickDrillsIntent, RC_PICK_DRILLS);
            }
        });

        btnSaveDrill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishEditActivity();
            }
        });

        seekDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
                    setDurationLabel();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        this.setSelectedDrill(null);

        this.editActivity = null;
        this.editNotes.setText("");
        this.editDrillName.setEnabled(false);

        // Determine if this is an add or edit
        String activityTitle = getResources().getString(R.string.new_activity_title);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String activityId = extras.getString(ACTIVITY_ID);
            if (activityId != null) {
                Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
                this.editActivity = currentSession.findActivity(activityId);
                this.setSelectedDrill(DrillsDatastore.getDatastore().getDrill(this.editActivity.getDrillId()));
                this.seekDuration.setProgress(this.editActivity.getDuration());
                this.setDurationLabel();
                this.editNotes.setText(this.editActivity.getNotes());
            }
        }
        getSupportActionBar().setTitle(activityTitle);
    }

    protected void finishEditActivity ()
    {
        Bundle conData = new Bundle();
        if (this.editActivity != null)
        {
            conData.putString(ACTIVITY_ID, this.editActivity.getId());
        }
        conData.putString(SELECTED_DRILL_ID, selectedDrill.getId());
        conData.putInt(ACTIVITY_DURATION, seekDuration.getProgress());
        conData.putString(ACTIVITY_NOTES, editNotes.getText().toString());
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void setSelectedDrill (Drill theDrill)
    {
        this.selectedDrill = theDrill;

        String drillName = "No Drill";
        String notes = "";
        String recommendedRange = "";
        int minDuration = 1;
        int maxDuration = 120;
        int duration = 5;
        if (selectedDrill != null)
        {
            drillName = this.selectedDrill.getName();
            minDuration = this.selectedDrill.getMinTime();
            maxDuration = this.selectedDrill.getMaxTime();
            recommendedRange = "( " + this.selectedDrill.getTimingLabel() + " )";
            duration = minDuration;
        }

        this.minDuration = minDuration;
        editDrillName.setText(drillName);
        seekDuration.setMax(maxDuration - minDuration);
        seekDuration.setProgress(duration - minDuration);
        setDurationLabel();
        textDurationRecommended.setText(recommendedRange);
    }

    protected void setDurationLabel ()
    {
        int duration = this.minDuration + seekDuration.getProgress();
        textDurationOutput.setText(Session.formatDuration(duration));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RC_PICK_DRILLS:
                Bundle res = data.getExtras();
                String pickedDrill = res.getString(SELECTED_DRILL_ID);
                Drill theDrill = DrillsDatastore.getDatastore().getDrill(pickedDrill);

                this.setSelectedDrill(theDrill);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
