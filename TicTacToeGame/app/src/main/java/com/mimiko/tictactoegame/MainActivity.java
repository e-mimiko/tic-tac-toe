package com.mimiko.tictactoegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.BufferedReader;;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private String TAG  = "MainActivity";

    private ListView menuListView;

    private ArrayAdapter<String> menuAdapter;
    private static HashMap<String, String> standingsKeyValue = new HashMap<String,String>();
    private ArrayList<String> standings = new ArrayList<String>();
    private final String[] menuArrayList = {"Enter Name", "Play Game", "Standings"};
    Game game = new Game();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //assign variables to layout views
        menuListView = findViewById(R.id.optionsListViewId);
        //display a listview with array adapter
        menuAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuArrayList);
        menuListView.setAdapter(menuAdapter);
        if (standingsKeyValue.isEmpty()){
            readFromFile2(getApplicationContext());
            //store Android win back into game player two
            for (Map.Entry<String, String> entry : standingsKeyValue.entrySet()) {
                String str = entry.getValue().trim();
                if (Objects.equals(entry.getKey(), "Android")){
                    game.setPlayerTwoWin(Integer.parseInt(str));
                }
            }
        }
        //Check for incoming intent from other activity and store game
        if (getIntent().getSerializableExtra("Game") != null){
            game = (Game) getIntent().getSerializableExtra("Game");
            standingsKeyValue.put(game.getPlayerOneName(), String.valueOf(game.getPlayerOneWin()));
            standingsKeyValue.put(game.getPlayerTwoName(), String.valueOf(game.getPlayerTwoWin()));
            for (Map.Entry<String, String> entry : standingsKeyValue.entrySet()) {
                standings.add(entry.getKey() + ": " + entry.getValue());
            }
        }
        //on list item click, call function to start the activity
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    startNameActivity();
                }
                else if (position == 1){
                    startGameActivity();
                }
                else if (position == 2){
                    startStandingsActivity();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //write to file
        writeToFile2(getApplicationContext());
    }


    //intent to start name activity
    private void startNameActivity(){
        Intent nameActivityIntent = new Intent(this, EnterNamesActivity.class);
        nameActivityIntent.putExtra("Game", game);
        startActivity(nameActivityIntent);
    }
    //intent to start game activity
    private void startGameActivity(){
        Intent gameActivityIntent = new Intent(this, PlayGameActivity.class);
        gameActivityIntent.putExtra("Game", game);
        startActivity(gameActivityIntent);
    }
    //intent to start standings activity
    private void startStandingsActivity(){
        Intent standingsActivityIntent = new Intent(this, ShowStandingsActivity.class);
        standingsActivityIntent.putExtra("Standings", standings);
        startActivity(standingsActivityIntent);
    }

    //code to write to file
    private void writeToFile2(Context context) {
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput("TicTacToeGame.txt", Context.MODE_PRIVATE));
        } catch (IOException e) {
            Log.e("Exception", "File create failed: " + e.toString());
        }
        for (Map.Entry<String, String> entry : standingsKeyValue.entrySet()) {
            String st = entry.getKey() + ":" + entry.getValue();
            if (outputStreamWriter != null){
                try {
                    outputStreamWriter.write(st);
                    outputStreamWriter.write("\n");
                } catch (IOException ex) {
                    Log.e("Exception", "File write failed: " + ex.toString());
                }
            }
        }
        if (outputStreamWriter != null) {
            try {
                outputStreamWriter.close();
            } catch (IOException ext) {
                Log.e("Exception", "File write failed: " + ext.toString());
            }
        }
    }
    //code to read from file
    private void readFromFile2(Context context) {
        try {
            InputStream inputStream = context.openFileInput("TicTacToeGame.txt");
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder st = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] separated = line.split(":");
                    standingsKeyValue.put(separated[0].trim(),separated[1].trim());
                }
            }
            if (!standingsKeyValue.isEmpty()){
                for (Map.Entry<String, String> entry : standingsKeyValue.entrySet()) {
                    standings.add(entry.getKey() + ": " + entry.getValue());
                }
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }

}