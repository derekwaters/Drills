package com.frisbeeworld.drills;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.frisbeeworld.drills.database.Drill;

import java.util.List;

/**
 * Created by Derek on 17/04/2017.
 */

public class PickDrillGridAdapter extends ArrayAdapter<Drill> {
    public PickDrillGridAdapter(Context context, int resource, List<Drill> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
