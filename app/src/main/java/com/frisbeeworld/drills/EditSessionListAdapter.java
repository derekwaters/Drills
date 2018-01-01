package com.frisbeeworld.drills;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.frisbeeworld.drills.database.DrillActivity;
import com.frisbeeworld.drills.database.Session;

import java.util.ArrayList;

/**
 * Created by Derek on 31/12/2017.
 */

public class EditSessionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ACTIVITY = 1;

    private RecyclerView parentRecyclerView;
    private ArrayList<DrillActivity> activityList;
    private ActivityOnClickListener onClickListener;
    private EditSessionActivity parentActivity;

    public class ActivityOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            EditSessionListAdapter.this.onActivityClicked(v);
        }
    }

    public static class ActivityViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public ImageView imagePreview;
        public TextView textTiming;
        public TextView textDescription;
        public ChipCloud chipsTags;

        public ActivityViewHolder(View v) {
            super(v);
            textName = (TextView)v.findViewById(R.id.text_drill_name);
            imagePreview = (ImageView)v.findViewById(R.id.image_drill_preview);
            textTiming = (TextView)v.findViewById(R.id.text_drill_timing);
            textDescription = (TextView)v.findViewById(R.id.text_drill_description);
            chipsTags = (ChipCloud)v.findViewById(R.id.chipcloud_drill_tags);
        }
    }


    public EditSessionListAdapter(EditSessionActivity parent) {
        this.activityList = new ArrayList<DrillActivity>();
        this.onClickListener = new ActivityOnClickListener();
        this.parentActivity = parent;

        //for (Drill eachDrill : DrillsDatastore.getDatastore().getDrills())
        //{
        //    this.filteredList.add(eachDrill);
        //}
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        parentRecyclerView = recyclerView;
    }

    public void onActivityClicked (View v)
    {
        int itemPosition = parentRecyclerView.getChildLayoutPosition(v);

        // Drill theDrill = this.filteredList.get(itemPosition - 1);

        // parentActivity.returnResult(theDrill);
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
/*
        Drill theDrill = this.filteredList.get(position - 1);

        ActivityViewHolder activityViewHolder = (ActivityViewHolder)holder;
        do_stuff_here();
        drillViewHolder.textName.setText(theDrill.getName());
        drillViewHolder.textDescription.setText(theDrill.getDescription());

        String timing = Integer.toString(theDrill.getMinTime()) + " - " +
                Integer.toString(theDrill.getMaxTime()) + " mins";
        drillViewHolder.textTiming.setText(timing);
        int index = 0;
        if (drillViewHolder.chipsTags.getChildCount() == 0) {
            drillViewHolder.chipsTags.removeAllViews();
            for (String tagName : theDrill.getTags()) {
                drillViewHolder.chipsTags.addChip(tagName);
                drillViewHolder.chipsTags.setMode(ChipCloud.Mode.NONE);
                index++;
            }
        }
        */
    }

    @Override
    public int getItemCount() {
        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        return currentSession.getActivities().size();
    }
}
