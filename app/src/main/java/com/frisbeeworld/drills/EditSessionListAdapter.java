package com.frisbeeworld.drills;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.frisbeeworld.drills.database.Drill;
import com.frisbeeworld.drills.database.DrillActivity;
import com.frisbeeworld.drills.database.Session;

/**
 * Created by Derek on 31/12/2017.
 */


public class EditSessionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ACTIVITY = 1;
    private static final int VIEW_TYPE_HEADER = 2;

    private RecyclerView parentRecyclerView;
    private ActivityOnClickListener onClickListener;
    private EditSessionActivity parentActivity;

    private EditActivityOnClickListener onEditActivityListener;
    private RemoveActivityOnClickListener onRemoveActivityListener;

    public class ActivityOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            EditSessionListAdapter.this.onActivityClicked(v);
        }
    }

    public class EditActivityOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            EditSessionListAdapter.this.onEditActivity(v);
        }
    }

    public class RemoveActivityOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            EditSessionListAdapter.this.onRemoveActivity(v);
        }
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public ImageView imagePreview;
        public TextView textTiming;
        public TextView textPeople;
        public TextView textDescription;
        public Button btnEdit;
        public Button btnRemove;
        public ChipCloud chipsTags;

        public ActivityViewHolder(View v) {
            super(v);
            textName = (TextView)v.findViewById(R.id.text_drill_name);
            imagePreview = (ImageView)v.findViewById(R.id.image_drill_preview);
            textTiming = (TextView)v.findViewById(R.id.text_drill_timing);
            textPeople = (TextView)v.findViewById(R.id.text_drill_people);
            textDescription = (TextView)v.findViewById(R.id.text_drill_description);
            chipsTags = (ChipCloud)v.findViewById(R.id.chipcloud_drill_tags);
            btnEdit = (Button)v.findViewById(R.id.activity_edit_btn);
            btnRemove = (Button)v.findViewById(R.id.activity_remove_btn);
        }

        public void refreshActivityInfo (DrillActivity activity) {
            Drill theDrill = DrillsDatastore.getDatastore().getDrill(activity.getDrillId());

            textName.setText(theDrill.getName());
            textDescription.setText(theDrill.getDescription());

            String timing = Integer.toString(theDrill.getMinTime()) + " - " +
                    Integer.toString(theDrill.getMaxTime()) + " mins";
            textTiming.setText(timing);
            textPeople.setText(Integer.toString(theDrill.getPeople()));
            // btnEdit.setOnClickListener(EditSessionListAdapter.this.onEditActivityListener);
            // btnRemove.setOnClickListener(this.onRemoveActivityListener);
            int index = 0;
            if (chipsTags.getChildCount() == 0) {
                chipsTags.removeAllViews();
                for (String tagName : theDrill.getTags()) {
                    chipsTags.addChip(tagName);
                    chipsTags.setMode(ChipCloud.Mode.NONE);
                    index++;
                }
            }
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView textSessionName;
        public TextView textSessionLocation;
        public TextView textSessionDateTime;
        public TextView textSessionDuration;

        public void refreshHeaderInfo (Session currentSession)
        {
            this.textSessionName.setText(currentSession.getName());
            this.textSessionLocation.setText(currentSession.getLocation());
            this.textSessionDateTime.setText(currentSession.getStartTimeString());
            this.textSessionDuration.setText(Session.formatDuration(currentSession.getDuration()));
        }

        public HeaderViewHolder(View v) {
            super(v);

            textSessionName = (TextView)v.findViewById(R.id.text_session_name);
            textSessionLocation = (TextView)v.findViewById(R.id.text_session_location);
            textSessionLocation.setClickable(true);
            textSessionLocation.setMovementMethod(LinkMovementMethod.getInstance());
            textSessionDateTime = (TextView)v.findViewById(R.id.text_session_datetime);
            textSessionDuration = (TextView)v.findViewById(R.id.text_session_duration);
        }
    }

    public EditSessionListAdapter(EditSessionActivity parent) {
        this.onClickListener = new ActivityOnClickListener();
        this.onEditActivityListener = new EditActivityOnClickListener();
        this.onRemoveActivityListener = new RemoveActivityOnClickListener();
        this.parentActivity = parent;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecyclerView = recyclerView;
    }

    public void onActivityClicked (View v)
    {
        int itemPosition = parentRecyclerView.getChildLayoutPosition(v);

        // TODO: Implement something when hitting a card
    }

    public void onEditActivity (View v)
    {
        int itemPosition = parentRecyclerView.getChildLayoutPosition(v);

        // TODO: Implement editing of the activity
    }

    public void onRemoveActivity (View v)
    {
        int itemPosition = parentRecyclerView.getChildLayoutPosition(v);

        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        currentSession.getActivities().remove(itemPosition - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView;
        switch (viewType) {
            case VIEW_TYPE_ACTIVITY:
                newView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.editsession_item_drillactivity, parent, false);
                // newView.setOnClickListener(this.onClickListener);
                return new ActivityViewHolder(newView);
            case VIEW_TYPE_HEADER:
                newView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.editsession_item_header, parent, false);
                // newView.setOnClickListener(this.onClickListener);
                return new HeaderViewHolder(newView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
        {
            return VIEW_TYPE_HEADER;
        }
        else
        {
            return VIEW_TYPE_ACTIVITY;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();

        if (position == 0)
        {
            HeaderViewHolder header = (HeaderViewHolder)holder;
            header.refreshHeaderInfo(currentSession);
        }
        else
        {
            DrillActivity activity = currentSession.getActivities().get(position - 1);

            ActivityViewHolder activityView = (ActivityViewHolder)holder;
            activityView.refreshActivityInfo(activity);
        }
    }

    @Override
    public int getItemCount() {
        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        return currentSession.getActivities().size() + 1;
    }
}
