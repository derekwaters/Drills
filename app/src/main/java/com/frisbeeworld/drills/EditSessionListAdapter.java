package com.frisbeeworld.drills;

import android.support.v7.widget.RecyclerView;
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
        currentSession.getActivities().remove(itemPosition);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView;
        switch (viewType) {
            case VIEW_TYPE_ACTIVITY:
                newView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.editsession_item_drillactivity, parent, false);
                newView.setOnClickListener(this.onClickListener);
                return new ActivityViewHolder(newView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ACTIVITY;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        DrillActivity activity = currentSession.getActivities().get(position);
        Drill theDrill = DrillsDatastore.getDatastore().getDrill(activity.getDrillId());

        ActivityViewHolder activityViewHolder = (ActivityViewHolder)holder;
        activityViewHolder.textName.setText(theDrill.getName());
        activityViewHolder.textDescription.setText(theDrill.getDescription());

        String timing = Integer.toString(theDrill.getMinTime()) + " - " +
                Integer.toString(theDrill.getMaxTime()) + " mins";
        activityViewHolder.textTiming.setText(timing);
        activityViewHolder.textPeople.setText(Integer.toString(theDrill.getPeople()));
        activityViewHolder.btnEdit.setOnClickListener(this.onEditActivityListener);
        activityViewHolder.btnRemove.setOnClickListener(this.onRemoveActivityListener);
        int index = 0;
        if (activityViewHolder.chipsTags.getChildCount() == 0) {
            activityViewHolder.chipsTags.removeAllViews();
            for (String tagName : theDrill.getTags()) {
                activityViewHolder.chipsTags.addChip(tagName);
                activityViewHolder.chipsTags.setMode(ChipCloud.Mode.NONE);
                index++;
            }
        }
    }

    @Override
    public int getItemCount() {
        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        return currentSession.getActivities().size();
    }
}
