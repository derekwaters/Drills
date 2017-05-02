package com.frisbeeworld.drills;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frisbeeworld.drills.database.Drill;

import java.util.ArrayList;
import java.util.List;

public class PickDrillsActivity extends AppCompatActivity {

    private RecyclerView drillsList;
    private RecyclerView.Adapter drillsAdapter;
    private RecyclerView.LayoutManager drillsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_drills);

        List<Drill> drillList = new ArrayList<>();
        drillsList = (RecyclerView)findViewById(R.id.pickdrill_list);

        // This can improve performance if content changes don't change
        // the layout size, though I think it will in our case.
        // drillsList.setHasFixedSize(true);

        drillsLayoutManager = new LinearLayoutManager(this);
        drillsList.setLayoutManager(drillsLayoutManager);

        drillsAdapter = new PickDrillListAdapter();
        drillsList.setAdapter(drillsAdapter);
    }
}
