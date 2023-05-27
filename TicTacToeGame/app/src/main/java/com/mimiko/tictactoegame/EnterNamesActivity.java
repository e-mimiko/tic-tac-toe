package com.mimiko.tictactoegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class EnterNamesActivity extends AppCompatActivity {

    private EditText name_field;
    private ArrayList<String> standings = new ArrayList<String>();
    Game game = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_names);
        name_field = findViewById(R.id.ed_nameId);
        Button save_btn = findViewById(R.id.btn_saveId);
        save_btn.setOnClickListener(saveBtnListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //get intent from main activity
        if (getIntent().getSerializableExtra("Game") != null){
            game = (Game) getIntent().getSerializableExtra("Game");
        }
    }

    View.OnClickListener saveBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getName();
            Intent mainActivityIntent = new Intent(EnterNamesActivity.this, MainActivity.class);
            mainActivityIntent.putExtra("Game", game);
            startActivity(mainActivityIntent);
        }
    };

    //Function to store player name inside game class
    private void getName(){
        String name = name_field.getText().toString();
        if (!name.isEmpty()){
            game.setPlayerOneName(name);
            game.setPlayerOneWin(0);
        }
    }

}