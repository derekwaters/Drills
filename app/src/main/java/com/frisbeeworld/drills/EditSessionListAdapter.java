package com.frisbeeworld.drills;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    public class ActivityOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            EditSessionListAdapter.this.onActivityClicked(v);
        }
    }

    public class ActivityViewHolder extends RecyclerView.ViewHolder {

        public TextView textName;
        public TextView textDescription;
        public TextView textNotes;
        public TextView textTiming;
        public TextView textPeople;
        public Button btnEdit;
        public Button btnRemove;
        public int currentPosition;
        public String currentActivityId;

        public ActivityViewHolder(View v) {
            super(v);
            textName = (TextView)v.findViewById(R.id.text_drill_name);
            textDescription = (TextView)v.findViewById(R.id.text_drill_description);
            textNotes = (TextView)v.findViewById(R.id.text_drill_notes);
            textTiming = (TextView)v.findViewById(R.id.text_drill_timing);
            textPeople = (TextView)v.findViewById(R.id.text_drill_people);
            btnEdit = (Button)v.findViewById(R.id.activity_edit_btn);
            btnRemove = (Button)v.findViewById(R.id.activity_remove_btn);
        }

        public void refreshActivityInfo (DrillActivity activity, int currentPosition) {

            Drill theDrill = DrillsDatastore.getDatastore().getDrill(activity.getDrillId());
            if (theDrill != null)
            {
                textName.setVisibility(View.VISIBLE);
                textDescription.setVisibility(View.VISIBLE);
                textPeople.setVisibility(View.VISIBLE);
                textName.setText(theDrill.getName());
                textDescription.setText(theDrill.getDescription());
                textPeople.setText(Integer.toString(theDrill.getPeople()));
            }
            else
            {
                textName.setVisibility(View.GONE);
                textDescription.setVisibility(View.GONE);
                textPeople.setVisibility(View.GONE);

            }

            this.currentPosition = currentPosition;
            this.currentActivityId = activity.getId();

            textNotes.setText(activity.getNotes());

            textTiming.setText(Session.formatDuration(activity.getDuration()));
            btnEdit.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       EditSessionListAdapter.this.onEditActivity(currentActivityId);
                   }
               });
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditSessionListAdapter.this.onRemoveActivity(currentActivityId);
                }
            });
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView textSessionName;
        public TextView textSessionLocation;
        public TextView textSessionDateTime;
        public TextView textSessionDuration;

        public void refreshHeaderInfo (final Context context, final Session currentSession)
        {
            this.textSessionName.setText(currentSession.getName());
            this.textSessionLocation.setText(currentSession.getLocation());
            this.textSessionLocation.setPaintFlags(
                    this.textSessionLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG
            );
            this.textSessionLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri.Builder builder = new Uri.Builder();
                    Uri geoLoc = builder.scheme("geo").path("0,0").appendQueryParameter("q",
                            currentSession.getLocation()).build();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(geoLoc);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            });
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
    }

    public void onEditActivity (String activityId)
    {
        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        DrillActivity activity = currentSession.getActivity(activityId);

        parentActivity.editActivity(activity);
    }

    public void onRemoveActivity (String activityId)
    {
        parentActivity.removeActivity(activityId);
        this.notifyDataSetChanged();
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
            header.refreshHeaderInfo(parentRecyclerView.getContext(), currentSession);
        }
        else
        {
            DrillActivity activity = currentSession.getActivityByPosition(position - 1);

            ActivityViewHolder activityView = (ActivityViewHolder)holder;
            activityView.refreshActivityInfo(activity, position - 1);
        }
    }

    @Override
    public int getItemCount() {
        Session currentSession = DrillsDatastore.getDatastore().getCurrentSession();
        return currentSession.getActivities().size() + 1;
    }
}
