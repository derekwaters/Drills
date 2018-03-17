package com.frisbeeworld.drills;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.frisbeeworld.drills.database.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Derek on 9/04/2017.
 */

public class SessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView parentRecyclerView;
    private SessionOnClickListener onClickListener;
    private MainActivity parentActivity;

    private boolean     showOnlyFutureSessions;

    private ArrayList<Integer> sessionList;

    public class SessionOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            SessionAdapter.this.onSessionClicked(v);
        }
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {

        public TextView teamName;
        public TextView sessionName;
        public TextView sessionDateTime;
        public TextView sessionDuration;
        public TextView sessionLocation;
        public Button   btnEdit;
        public Button   btnRemove;
        public int currentPosition;
        public String currentSessionId;

        public SessionViewHolder(View v) {
            super(v);

            teamName = (TextView)v.findViewById(R.id.textTeamName);
            sessionName = (TextView)v.findViewById(R.id.textSessionName);
            sessionDateTime = (TextView)v.findViewById(R.id.textSessionDateTime);
            sessionDuration = (TextView)v.findViewById(R.id.textSessionDuration);
            sessionLocation = (TextView)v.findViewById(R.id.textSessionLocation);

            btnEdit = (Button)v.findViewById(R.id.session_edit_btn);
            btnRemove = (Button)v.findViewById(R.id.session_remove_btn);
        }

        public String getCurrentSessionId ()
        {
            return this.currentSessionId;
        }

        public void refreshInfo (final Session session, int currentPosition, final Context context) {

            teamName.setText("Caulfield Bears");
            sessionName.setText(session.getName());
            sessionDateTime.setText(session.getStartTimeString());
            sessionDuration.setText(Integer.toString(session.getDuration()));
            sessionLocation.setText(session.getLocation());
            sessionLocation.setPaintFlags(
                    sessionLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG
            );
            sessionLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri.Builder builder = new Uri.Builder();
                    Uri geoLoc = builder.scheme("geo").path("0,0").appendQueryParameter("q",
                            session.getLocation()).build();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(geoLoc);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            });

            this.currentSessionId = session.getId();
            this.currentPosition = currentPosition;

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionAdapter.this.onEditSession(currentSessionId);
                }
            });
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionAdapter.this.onRemoveSession(currentSessionId);
                }
            });
        }
    }

    public SessionAdapter(MainActivity parent) {
        this.onClickListener = new SessionOnClickListener();
        this.parentActivity = parent;

        this.showOnlyFutureSessions = true;

        sessionList = new ArrayList<>();

        this.refreshSessionList();
    }

    public void setShowOnlyFutureSessions (boolean showOnlyFutureSessions)
    {
        this.showOnlyFutureSessions = showOnlyFutureSessions;
        this.refreshSessionList();
    }

    public void refreshSessionList ()
    {
        this.sessionList.clear();

        final ArrayList<Session> sessions = DrillsDatastore.getDatastore().getSessions();
        Date rightNow = new Date();
        for (int i = 0; i < sessions.size(); i++)
        {
            Session session = sessions.get(i);
            if (!this.showOnlyFutureSessions || session.getStartTime().after(rightNow))
            {
                this.sessionList.add(i);
            }
        }

        Collections.sort(this.sessionList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                Session firstSession = sessions.get(o1);
                Session secondSession = sessions.get(o2);
                return firstSession.getStartTime().compareTo(secondSession.getStartTime());
            }
        });

        this.notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecyclerView = recyclerView;
    }

    public void onSessionClicked (View v)
    {
        SessionViewHolder sessionViewHolder = (SessionViewHolder)parentRecyclerView.getChildViewHolder(v);

        Session session = DrillsDatastore.getDatastore().getSession(sessionViewHolder.getCurrentSessionId());
        DrillsDatastore.getDatastore().setCurrentSession(session);
        Intent editSessionIntent = new Intent(parentRecyclerView.getContext(),
                EditSessionActivity.class);
        parentRecyclerView.getContext().startActivity(editSessionIntent);
    }

    public void onEditSession (String sessionId)
    {
        Session currentSession = DrillsDatastore.getDatastore().getSession(sessionId);
        parentActivity.editSession(currentSession);
    }

    public void onRemoveSession (String sessionId)
    {
        parentActivity.removeSession(sessionId);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView;

        newView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_session, parent, false);
        newView.setOnClickListener(this.onClickListener);
        return new SessionViewHolder(newView);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int index = sessionList.get(position);
        Session theSession = DrillsDatastore.getDatastore().getSessionAtPosition(index);
        SessionViewHolder sessionView = (SessionViewHolder)holder;
        sessionView.refreshInfo(theSession, position, parentRecyclerView.getContext());
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }
}
