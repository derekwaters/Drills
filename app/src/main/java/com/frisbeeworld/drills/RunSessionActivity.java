package com.frisbeeworld.drills;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frisbeeworld.drills.database.DrillActivity;
import com.frisbeeworld.drills.database.Session;

import java.lang.ref.WeakReference;

public class RunSessionActivity extends AppCompatActivity {

    private static final String TAG = RunSessionActivity.class.getSimpleName();
    public static final String  SESSION_ID = "SessionId";
    private final static int    MSG_UPDATE_TIME = 0;

    private Session session;


    private TimerService timerService;
    private boolean serviceBound;

    private TextView textTimer;
    private TextView textTotalTime;
    private TextView textRemainingTime;
    private ProgressBar progressTimer;
    private ImageButton btnStartStop;
    private RecyclerView listActivities;
    private RunSessionListAdapter activityAdapter;

    // Handler to update the UI every second when the timer is running
    private final android.os.Handler updateTimeHandler = new UIUpdateHandler(this);


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
        listActivities = (RecyclerView)findViewById(R.id.sessionactivity_list);

        activityAdapter = new RunSessionListAdapter();
        listActivities.setAdapter(activityAdapter);

        progressTimer.setMax(session.getDuration() * 60);
        progressTimer.setProgress(0);
        updateSessionUI(0);

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (serviceBound && !timerService.isTimerRunning()) {
                    if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.v(TAG, "Starting timer");
                    }
                    timerService.startTimer();
                    updateUIStartRun();
                }
                else if (serviceBound && timerService.isTimerRunning()) {
                    if (Log.isLoggable(TAG, Log.VERBOSE)) {
                        Log.v(TAG, "Stopping timer");
                    }
                    timerService.stopTimer();
                    updateUIStopRun();
                }
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Starting and binding service");
        }
        Intent i = new Intent(this, TimerService.class);
        startService(i);
        bindService(i, mConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateUIStopRun();
        if (serviceBound) {
            // If a timer is active, foreground the service, otherwise kill the service
            if (timerService.isTimerRunning()) {
                timerService.foreground();
            }
            else {
                stopService(new Intent(this, TimerService.class));
            }
            // Unbind the service
            unbindService(mConnection);
            serviceBound = false;
        }
    }

    /**
     * Updates the UI when a run starts
     */
    private void updateUIStartRun() {
        updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
        // timerButton.setText(R.string.timer_stop_button);
    }

    /**
     * Updates the UI when a run stops
     */
    private void updateUIStopRun() {
        updateTimeHandler.removeMessages(MSG_UPDATE_TIME);
        // timerButton.setText(R.string.timer_start_button);
    }

    /**
     * Updates the timer readout in the UI; the service must be bound
     */
    private void updateUITimer() {
        if (serviceBound) {
            // Update all the bits and pieces.
            long elapsedSeconds = timerService.elapsedTime();

            this.updateSessionUI(elapsedSeconds);

        }
    }

    private void updateSessionUI (long elapsedSeconds)
    {
        // DEBUG ONLY
        elapsedSeconds = elapsedSeconds * 10;
        // DEBUG ONLY
        progressTimer.setProgress((int)elapsedSeconds);
        long totalSeconds = 60 * session.getDuration();
        long remainingSeconds = totalSeconds - elapsedSeconds;
        long remainingActivitySeconds;
        String currentActivity;
        // Now work out the remaining time in the current activity
        int position = 0;
        while (true)
        {
            DrillActivity activity = session.getActivity(position);
            if (60 * activity.getDuration() > elapsedSeconds) {
                remainingActivitySeconds = (60 * activity.getDuration()) - elapsedSeconds;
                break;
            } else {
                elapsedSeconds -= (60 * activity.getDuration());
            }
        }

        activityAdapter.setCurrentActivityPosition(position);

        textTimer.setText(Session.formatTimer(remainingActivitySeconds));
        textTotalTime.setText(Session.formatTimer(totalSeconds));
        textRemainingTime.setText(Session.formatTimer(remainingSeconds));

        // String title = "Session: " + session.getName() + " " + Session.formatTimer(totalSeconds);
        // String description = "Activity: " + Session.formatTimer(remainingActivitySeconds);
        // timerService.updateNotification(title, description);
    }


    /**
     * Callback for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service bound");
            }
            TimerService.RunServiceBinder binder = (TimerService.RunServiceBinder) service;
            timerService = binder.getService();
            serviceBound = true;
            // Ensure the service is not in the foreground when bound
            timerService.background();
            // Update the UI if the service is already running the timer
            if (timerService.isTimerRunning()) {
                updateUIStartRun();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Service disconnect");
            }
            serviceBound = false;
        }
    };



    /**
     * When the timer is running, use this handler to update
     * the UI every second to show timer progress
     */
    static class UIUpdateHandler extends android.os.Handler {

        private final static int UPDATE_RATE_MS = 1000;
        private final WeakReference<RunSessionActivity> activity;

        UIUpdateHandler(RunSessionActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            if (MSG_UPDATE_TIME == message.what) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "updating time");
                }
                activity.get().updateUITimer();
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, UPDATE_RATE_MS);
            }
        }
    }


    /**
     * Timer service tracks the start and end time of timer; service can be placed into the
     * foreground to prevent it being killed when the activity goes away
     */
    public static class TimerService extends Service {

        private static final String TAG = TimerService.class.getSimpleName();

        private long startTime, endTime;

        // Is the service tracking time?
        private boolean isTimerRunning;

        // Foreground notification id
        private static final int NOTIFICATION_ID = 1;

        private Notification currentNotification;

        // Service binder
        private final IBinder serviceBinder = new RunServiceBinder();

        public class RunServiceBinder extends Binder {
            TimerService getService() {
                return TimerService.this;
            }
        }

        @Override
        public void onCreate() {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Creating service");
            }
            startTime = 0;
            endTime = 0;
            isTimerRunning = false;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Starting service");
            }
            return Service.START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Binding service");
            }
            return serviceBinder;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Destroying service");
            }
        }

        /**
         * Starts the timer
         */
        public void startTimer() {
            if (!isTimerRunning) {
                startTime = System.currentTimeMillis();
                isTimerRunning = true;
            }
            else {
                Log.e(TAG, "startTimer request for an already running timer");
            }
        }

        /**
         * Stops the timer
         */
        public void stopTimer() {
            if (isTimerRunning) {
                endTime = System.currentTimeMillis();
                isTimerRunning = false;
            }
            else {
                Log.e(TAG, "stopTimer request for a timer that isn't running");
            }
        }

        /**
         * @return whether the timer is running
         */
        public boolean isTimerRunning() {
            return isTimerRunning;
        }

        /**
         * Returns the  elapsed time
         *
         * @return the elapsed time in seconds
         */
        public long elapsedTime() {
            // If the timer is running, the end time will be zero
            return endTime > startTime ?
                    (endTime - startTime) / 1000 :
                    (System.currentTimeMillis() - startTime) / 1000;
        }

        public void updateNotification (String title, String description)
        {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(
                    NOTIFICATION_ID,
                    createNotification(title, description));
        }

        /**
         * Place the service into the foreground
         */
        public void foreground() {
            startForeground(NOTIFICATION_ID, createNotification(
                    "Timer Active", "Tap to return to the timer"
            ));
        }

        /**
         * Return the service to the background
         */
        public void background() {
            stopForeground(true);
        }

        /**
         * Creates a notification for placing the service into the foreground
         *
         * @return a notification for interacting with the service when in the foreground
         */
        private Notification createNotification(String title, String description) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setSmallIcon(R.mipmap.ic_launcher);

            Intent resultIntent = new Intent(this, RunSessionActivity.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(this, 0, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);

            return builder.build();
        }
    }
};


