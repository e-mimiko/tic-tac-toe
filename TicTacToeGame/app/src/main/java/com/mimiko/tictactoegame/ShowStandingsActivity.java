package com.mimiko.tictactoegame;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowStandingsActivity extends AppCompatActivity {
    private String TAG  = "StandingActivity";
    private ListView standingListView;
    private ArrayList<String> standings = new ArrayList<String>();

    private ArrayAdapter<String> standingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_standings);
        standingListView = findViewById(R.id.lv_scoreboardId);
        if (getIntent().getStringArrayListExtra("Standings") != null){
            standings = getIntent().getStringArrayListExtra("Standings");
        }
        standingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, standings);
        standingListView.setAdapter(standingAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}