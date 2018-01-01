package com.frisbeeworld.drills;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.frisbeeworld.drills.database.Drill;

public class PickDrillsActivity extends AppCompatActivity {

    public static final String SELECTED_DRILL_ID = "SELECTED_DRILL_ID";

    private RecyclerView drillsList;
    private RecyclerView.Adapter drillsAdapter;
    private RecyclerView.LayoutManager drillsLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_drills);

        drillsList = (RecyclerView)findViewById(R.id.pickdrill_list);

        // This can improve performance if content changes don't change
        // the layout size, though I think it will in our case.
        // drillsList.setHasFixedSize(true);

        drillsLayoutManager = new LinearLayoutManager(this);
        drillsList.setLayoutManager(drillsLayoutManager);

        drillsAdapter = new PickDrillListAdapter(this);
        drillsList.setAdapter(drillsAdapter);

        drillsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(PickDrillsActivity.this, "Card selected?!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void returnResult (Drill selectedDrill)
    {
        Bundle conData = new Bundle();
        conData.putString(SELECTED_DRILL_ID, selectedDrill.getId());
        Intent intent = new Intent();
        intent.putExtras(conData);
        setResult(RESULT_OK, intent);
        finish();
    }
}
