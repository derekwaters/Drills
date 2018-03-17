package com.frisbeeworld.drills;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frisbeeworld.drills.database.Session;

public class RunSessionActivity extends AppCompatActivity {

    private static final String TAG = RunSessionActivity.class.getSimpleName();
    public static final String  SESSION_ID = "SessionId";
    private final static int    MSG_UPDATE_TIME = 0;

    // DEBUG ONLY!
    // private static final int    SECS_PER_MIN = 5;
    private final static int    SECS_PER_MIN = 60;

    private Session session;

    private TextView textTimer;
    private TextView textTotalTime;
    private TextView textRemainingTime;
    private ProgressBar progressTimer;
    private ImageButton btnStartStop;
    private ImageButton btnReset;
    private RecyclerView listActivities;
    private RunSessionListAdapter activityAdapter;


    // Runtime countdown
    private boolean         timerRunning;
    private long            timerRemainingTime;
    private CountDownTimer  timer;
    private int             currentSessionActivity;
    private long            totalExpiredTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = DrillsDatastore.getDatastore().getCurrentSession();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(session.getName());

        textTimer = (TextView)findViewById(R.id.txtTimer);
        textTotalTime = (TextView)findViewById(R.id.txtTotalTime);
        textRemainingTime = (TextView)findViewById(R.id.txtRemainingTime);
        progressTimer = (ProgressBar)findViewById(R.id.progressTimer);
        btnStartStop = (ImageButton)findViewById(R.id.btnStartStop);
        btnReset = (ImageButton)findViewById(R.id.btnReset);
        listActivities = (RecyclerView)findViewById(R.id.sessionactivity_list);

        activityAdapter = new RunSessionListAdapter();
        listActivities.setLayoutManager(new LinearLayoutManager(this));
        listActivities.setAdapter(activityAdapter);

        progressTimer.setMax(session.getDuration() * SECS_PER_MIN);
        progressTimer.setProgress(0);

        timerRunning = false;

        currentSessionActivity = 0;
        totalExpiredTime = 0;
        timerRemainingTime = session.getActivity(currentSessionActivity).getDuration() * SECS_PER_MIN;

        updateSessionUI();

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning)
                {
                    timer.cancel();
                    timer = null;
                    timerRunning = false;
                }

                updateUIStopRun();
                progressTimer.setProgress(0);

                currentSessionActivity = 0;
                totalExpiredTime = 0;
                timerRemainingTime = session.getActivity(currentSessionActivity).getDuration() * SECS_PER_MIN;

                updateSessionUI();
            }
        });

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timerRunning)
                {
                    updateUIStopRun();
                    timer.cancel();
                    timer = null;
                    timerRunning = false;
                }
                else
                {
                    updateUIStartRun();
                    startNewTimer();
                    timerRunning = true;

                }
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateUIStopRun();
    }

    private void startNewTimer ()
    {
        timer = new CountDownTimer(timerRemainingTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished)
            {
                timerRemainingTime = millisUntilFinished / 1000;
                updateSessionUI();
            }

            @Override
            public void onFinish()
            {
                fireAlarm();
                timerFinished();
            }
        };
        timer.start();
    }

    private void fireAlarm ()
    {
        Vibrator vibrate = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);

        final Thread thisThread = Thread.currentThread();

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.in_call_alarm);
        if (mediaPlayer != null) {
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    thisThread.interrupt();
                }
            });
            long[] pattern = {50, 500};
            vibrate.vibrate(pattern, 0);

            mediaPlayer.start();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {

            }
            mediaPlayer.stop();

            vibrate.cancel();
            mediaPlayer.release();
        }
    }

    private void timerFinished ()
    {
        // Clear the UI? Do a session finished thingo?

        totalExpiredTime += session.getActivity(currentSessionActivity).getDuration() * SECS_PER_MIN;

        currentSessionActivity++;
        if (currentSessionActivity == session.getActivities().size())
        {
            // We're all done!

        }
        else
        {
            updateSessionUI();
            timerRemainingTime = session.getActivity(currentSessionActivity).getDuration() * SECS_PER_MIN;
            timer = null;
            startNewTimer();
        }
    }

    /**
     * Updates the UI when a run starts
     */
    private void updateUIStartRun() {
        btnStartStop.setImageResource(R.drawable.ic_pause_black_48dp);
    }

    /**
     * Updates the UI when a run stops
     */
    private void updateUIStopRun() {
        btnStartStop.setImageResource(R.drawable.ic_play_arrow_black_48dp);
    }

    private void updateSessionUI ()
    {
        long totalSeconds = session.getDuration() * SECS_PER_MIN;
        long remainingSeconds = totalSeconds - totalExpiredTime -
                (session.getActivity(currentSessionActivity).getDuration() * SECS_PER_MIN)
                + timerRemainingTime;
        long elapsedSeconds = totalSeconds - remainingSeconds;

        progressTimer.setProgress((int)elapsedSeconds);

        activityAdapter.setCurrentActivityPosition(currentSessionActivity);

        textTimer.setText(Session.formatTimer(timerRemainingTime));
        textTotalTime.setText(Session.formatTimer(totalSeconds));
        textRemainingTime.setText(Session.formatTimer(remainingSeconds));
    }
};


