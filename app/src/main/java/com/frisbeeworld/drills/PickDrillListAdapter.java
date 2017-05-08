package com.frisbeeworld.drills;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.adroitandroid.chipcloud.ChipCloud;
import com.adroitandroid.chipcloud.ChipListener;
import com.frisbeeworld.drills.database.Drill;

/**
 * Created by Derek on 17/04/2017.
 */

public class PickDrillListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_TAG_HEADER = 1;
    private static final int VIEW_TYPE_DRILL = 2;


    public static class TagViewHolder extends RecyclerView.ViewHolder {

        public ChipCloud chipCloud;
        public TagViewHolder(View v) {
            super(v);

            chipCloud = (ChipCloud)v.findViewById(R.id.chipcloud_tags);

            chipCloud.setChipListener(new ChipListener() {
                @Override
                public void chipSelected(int i) {
                    // TODO: filtering
                }

                @Override
                public void chipDeselected(int i) {
                    // TODO: filtering
                }
            });
        }
    }

    public static class DrillViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public ImageView imagePreview;
        public TextView textTiming;
        public TextView textDescription;
        public ChipCloud chipsTags;

        public DrillViewHolder(View v) {
            super(v);
            textName = (TextView)v.findViewById(R.id.text_drill_name);
            imagePreview = (ImageView)v.findViewById(R.id.image_drill_preview);
            textTiming = (TextView)v.findViewById(R.id.text_drill_timing);
            textDescription = (TextView)v.findViewById(R.id.text_drill_description);
            chipsTags = (ChipCloud)v.findViewById(R.id.chipcloud_drill_tags);
        }
    }


    public PickDrillListAdapter() {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView;
        switch (viewType) {
            case VIEW_TYPE_TAG_HEADER:
                newView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.pickdrill_item_tags, parent, false);
                return new TagViewHolder(newView);
            case VIEW_TYPE_DRILL:
                newView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.pickdrill_item_drill, parent, false);
                return new DrillViewHolder(newView);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TAG_HEADER : VIEW_TYPE_DRILL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0)
        {
            TagViewHolder tagViewHolder = (TagViewHolder)holder;

            for (String tagName : DrillsDatastore.getDatastore().getTags())
            {
                tagViewHolder.chipCloud.addChip(tagName);
            }
        }
        else
        {
            Drill theDrill = DrillsDatastore.getDatastore().getDrills().get(position - 1);

            DrillViewHolder drillViewHolder = (DrillViewHolder)holder;
            drillViewHolder.textName.setText(theDrill.getName());
            drillViewHolder.textDescription.setText(theDrill.getDescription());

            String timing = Integer.toString(theDrill.getMinTime()) + " - " +
                Integer.toString(theDrill.getMaxTime()) + " mins";
            drillViewHolder.textTiming.setText(timing);
            int index = 0;
            for (String tagName : theDrill.getTags())
            {
                drillViewHolder.chipsTags.addChip(tagName);
                drillViewHolder.chipsTags.setMode(ChipCloud.Mode.NONE);
                index++;
            }
        }
    }

    @Override
    public int getItemCount() {
        return DrillsDatastore.getDatastore().getDrills().size() + 1;
    }
}
