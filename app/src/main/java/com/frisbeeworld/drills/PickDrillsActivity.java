package com.frisbeeworld.drills;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.frisbeeworld.drills.database.Drill;

import java.util.ArrayList;
import java.util.List;

public class PickDrillsActivity extends AppCompatActivity {

    private GridView drillsGrid;
    private PickDrillGridAdapter drillsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_drills);

        List<Drill> drillList = new ArrayList<>();
        drillsGrid = (GridView)findViewById(R.id.pickdrill_grid);
        drillsAdapter = new PickDrillGridAdapter(this, R.layout.item_drill, drillList);
        drillsGrid.setAdapter(drillsAdapter);

    }
}
