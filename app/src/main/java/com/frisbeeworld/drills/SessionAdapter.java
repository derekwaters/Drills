package com.frisbeeworld.drills;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frisbeeworld.drills.database.Session;

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

        return convertView;
    }
}
