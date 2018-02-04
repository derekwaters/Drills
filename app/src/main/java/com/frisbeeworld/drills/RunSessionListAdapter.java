package com.frisbeeworld.drills;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.frisbeeworld.drills.database.Drill;
import com.frisbeeworld.drills.database.DrillActivity;
import com.frisbeeworld.drills.database.Session;

/**
 * Created by Derek on 4/02/2018.
 */

public class RunSessionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_CURRENT_ACTIVITY = 1;
    private static final int VIEW_TYPE_UPCOMING_ACTIVITY = 2;

    private RecyclerView parentRecyclerView;

    private int activeActivityOffset = 0;


    public class ActivityViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public TextView textDescription;
        public TextView textNotes;
        public TextView textTiming;
        public TextView textPeople;
        public Button btnEdit;
        public Button btnRemove;
        public int currentPosition;

        public ActivityViewHolder(View v) {
            super(v);
            textName = (TextView)v.findViewById(R.id.text_drill_name);
            textDescription = (TextView)v.findViewById(R.id.text_drill_description);
            textNotes = (TextView)v.findViewById(R.id.text_drill_notes);
            textTiming = (TextView)v.findViewById(R.id.text_drill_timing);
            textPeople = (TextView)v.findViewById(R.id.text_drill_people);
        }

        public void refreshActivityInfo (boolean isCurrentActivity, DrillActivity activity) {

            Drill theDrill = DrillsDatastore.getDatastore().getDrill(activity.getDrillId());
            if (theDrill != null)
            {
                textName.setVisibility(View.VISIBLE);
                textName.setText(theDrill.getName());
                if (isCurrentActivity)
                {
                    textDescription.setVisibility(View.VISIBLE);
                    textPeople.setVisibility(View.VISIBLE);
                    textDescription.setText(theDrill.getDescription());
                    textPeople.setText(Integer.toString(theDrill.getPeople()));
                }
            }
            else
            {
                textName.setVisibility(View.GONE);
                if (isCurrentActivity)
                {
                    textDescription.setVisibility(View.GONE);
                    textPeople.setVisibility(View.GONE);
                }
            }
            if (isCurrentActivity)
            {
                textNotes.setText(activity.getNotes());
            }
            textTiming.setText(Session.formatDuration(activity.getDuration()));
        }
    }

    public RunSessionListAdapter() {
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView;
        switch (viewType) {
            case VIEW_TYPE_CURRENT_ACTIVITY:
                newView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.runsession_activity_current, parent, false);
                return new ActivityViewHolder(newView);
            case VIEW_TYPE_UPCOMING_ACTIVITY:
                newView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.runsession_activity_upcoming, parent, false);
                return new ActivityViewHolder(newView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
        {
            return VIEW_TYPE_CURRENT_ACTIVITY;
        }
        else
        {
            return VIEW_TYPE_UPCOMING_ACTIVITY;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        ActivityViewHolder activityHolder = (ActivityViewHolder)holder;

        DrillActivity activity = currentSession.getActivity(activeActivityOffset + position);
        activityHolder.refreshActivityInfo(position == 0, activity);
    }

    @Override
    public int getItemCount() {
        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        return currentSession.getActivities().size() - activeActivityOffset;
    }

    public void setCurrentActivityPosition (int newPosition)
    {
        if (newPosition != activeActivityOffset)
        {
            activeActivityOffset = newPosition;
            this.notifyDataSetChanged();
        }
    }
}
