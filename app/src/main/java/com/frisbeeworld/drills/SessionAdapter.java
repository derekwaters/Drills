package com.frisbeeworld.drills;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frisbeeworld.drills.database.Session;

/**
 * Created by Derek on 9/04/2017.
 */

public class SessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerView parentRecyclerView;
    private SessionOnClickListener onClickListener;
    private MainActivity parentActivity;

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
        public int currentPosition;
/*
        final Session session = getItem(position);

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
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    getContext().startActivity(intent);
                }
            }
        });
*/



        public SessionViewHolder(View v) {
            super(v);

            teamName = (TextView)v.findViewById(R.id.textTeamName);
            sessionName = (TextView)v.findViewById(R.id.textSessionName);
            sessionDateTime = (TextView)v.findViewById(R.id.textSessionDateTime);
            sessionDuration = (TextView)v.findViewById(R.id.textSessionDuration);
            sessionLocation = (TextView)v.findViewById(R.id.textSessionLocation);

            // TODO: Add these
            // btnEdit = (Button)v.findViewById(R.id.session_edit_btn);
            // btnRemove = (Button)v.findViewById(R.id.session_remove_btn);
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

            this.currentPosition = currentPosition;

            /*
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditSessionListAdapter.this.onEditActivity(ActivityViewHolder.this.currentPosition);
                }
            });
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditSessionListAdapter.this.onRemoveActivity(ActivityViewHolder.this.currentPosition);
                }
            });
            */
        }
    }

    public SessionAdapter(MainActivity parent) {
        this.onClickListener = new SessionOnClickListener();
        this.parentActivity = parent;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecyclerView = recyclerView;
    }

    public void onSessionClicked (View v)
    {
        int itemPosition = parentRecyclerView.getChildLayoutPosition(v);

        Session session = DrillsDatastore.getDatastore().getSessionAtPosition(itemPosition);
        DrillsDatastore.getDatastore().setCurrentSession(session);
        Intent editSessionIntent = new Intent(parentRecyclerView.getContext(),
                EditSessionActivity.class);
        parentRecyclerView.getContext().startActivity(editSessionIntent);
    }

    /*
    public void onEditActivity (int position)
    {
        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        DrillActivity activity = currentSession.getActivity(position);

        parentActivity.editActivity(activity);
    }

    public void onRemoveActivity (int position)
    {
        parentActivity.removeActivity(position);
        this.notifyItemRemoved(position);
        this.notifyDataSetChanged();
    }
    */

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

        Session theSession = DrillsDatastore.getDatastore().getSessionAtPosition(position);
        SessionViewHolder sessionView = (SessionViewHolder)holder;
        sessionView.refreshInfo(theSession, position, parentRecyclerView.getContext());
    }

    @Override
    public int getItemCount() {
        return DrillsDatastore.getDatastore().getSessions().size();
    }
}
