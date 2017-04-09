package com.frisbeeworld.drills;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frisbeeworld.drills.database.Session;

import java.util.Date;
import java.util.List;

/**
 * Created by Derek on 9/04/2017.
 */

public class SessionAdapter extends ArrayAdapter<Session> {
    public SessionAdapter(Context context, int resource, List<Session> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_session, parent, false);
        }

        TextView teamName = (TextView)convertView.findViewById(R.id.textTeamName);
        TextView sessionName = (TextView)convertView.findViewById(R.id.textSessionName);
        TextView sessionDateTime = (TextView)convertView.findViewById(R.id.textSessionDateTime);
        TextView sessionDuration = (TextView)convertView.findViewById(R.id.textSessionDuration);
        TextView sessionLocation = (TextView)convertView.findViewById(R.id.textSessionLocation);

        Session session = getItem(position);

        teamName.setText("Caulfield Bears U8s");
        sessionName.setText(session.getName());
        Date startTime = session.getStartTime();
        String startTimeStr = "";
        if (startTime != null)
        {
            startTimeStr = startTime.toString();
        }
        sessionDateTime.setText(startTimeStr);
        sessionDuration.setText(Integer.toString(session.getDuration()));
        sessionLocation.setText(session.getLocation());

        return convertView;
    }
}
