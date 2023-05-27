package com.mimiko.tictactoegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class PlayGameActivity extends AppCompatActivity implements RobotResponse{

    private String TAG  = "PlayGameActivity";
    TextView[] textViews;

    Button endGame_btn;
    Button resetGame_btn;
    private static final char X_VALUE = 'X';
    private final char EMPTY_VALUE = '-';
    private final String tie_message = "Tie";

    Game game = new Game();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        //if no intent passed, new game, else game = game from intent passed
        textViews = new TextView[]{
                (TextView) findViewById(R.id.tv_Slot1),
                (TextView) findViewById(R.id.tv_Slot2),
                (TextView) findViewById(R.id.tv_Slot3),
                (TextView) findViewById(R.id.tv_Slot4),
                (TextView) findViewById(R.id.tv_Slot5),
                (TextView) findViewById(R.id.tv_Slot6),
                (TextView) findViewById(R.id.tv_Slot7),
                (TextView) findViewById(R.id.tv_Slot8),
                (TextView) findViewById(R.id.tv_Slot9),
        };
        for (TextView textView : textViews) {
            textView.setOnClickListener(buttonListener);
        }
        endGame_btn = findViewById(R.id.btn_endGameId);
        endGame_btn.setOnClickListener(endGameButtonListener);
        resetGame_btn = findViewById(R.id.btn_resetGameId);
        resetGame_btn.setOnClickListener(resetGameButtonListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //get intent from main activity
        if (getIntent().getSerializableExtra("Game") != null){
            game = (Game) getIntent().getSerializableExtra("Game");
            if (game.getPlayerOneName().isEmpty()){
                game.setPlayerOneName("Player");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        buildBoard();
    }

    //Button to end game. Must be called for score to be updated
    View.OnClickListener endGameButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent startMainActivity = new Intent(PlayGameActivity.this, MainActivity.class);
            startMainActivity.putExtra("Game", game);
            startActivity(startMainActivity);
        }
    };

    View.OnClickListener resetGameButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            reset();
        }
    };

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView v = (TextView) view;
            for (int i=0;i<textViews.length;i++){
                if (textViews[i].getId() == v.getId()){
                    play(i);//index of x or o field
                    break;
                }
            }
        }
    };
    public void play(int index){
        boolean canPlay = checkPlayValid(index);
        //get first player to play and check if player won
        if (canPlay){
            playGame(index, X_VALUE);
            if (isWin()){
                int playerOneWin = game.getPlayerOneWin()+1;
                game.setPlayerOneWin(playerOneWin);
                String won_message = "You win";
                Toast.makeText(this, won_message, Toast.LENGTH_SHORT).show();
//                reset();
            }
            else if (isAFilledBoard()){
                Toast.makeText(this, tie_message, Toast.LENGTH_SHORT).show();
//                reset();
            }
            else{
                //thread started for android to choose a slot to play
                new AndroidPlay( this).execute(game.gameBoard.length());
            }

        }
    }

    private void buildBoard(){
        for (int i = 0; i < game.gameBoard.length(); i++){
            if (!checkPlayValid(i)){
                textViews[i].setText(String.valueOf(game.gameBoard.charAt(i)));
            }
        }
    }
    //returns true if there are empty slots left to play
    private boolean checkPlayValid(int index){
        return game.gameBoard.charAt(index) == EMPTY_VALUE;
    }
    //updates game board and sets the played slot
    private void playGame(int index, char tic){
        updateBoard(index, tic);
        textViews[index].setText(String.valueOf(tic));
    }
    //returns true if board is filled
    private boolean isAFilledBoard(){
        for (int i=0;i<game.gameBoard.length();i++){
            if (checkPlayValid(i)){
                return false;
            }
        }
        return true;
    }

    //returns true if there is a win
    private boolean isWin(){
        boolean isAWin = false;
        //check diagonal left
        boolean diagonalLeft = game.gameBoard.charAt(0) == game.gameBoard.charAt(4) && game.gameBoard.charAt(4) == game.gameBoard.charAt(8) && game.gameBoard.charAt(0) != EMPTY_VALUE;
        boolean diagonalRight = game.gameBoard.charAt(2) == game.gameBoard.charAt(4) && game.gameBoard.charAt(4) == game.gameBoard.charAt(6) && game.gameBoard.charAt(2) != EMPTY_VALUE;
        boolean firstRow = game.gameBoard.charAt(0) == game.gameBoard.charAt(1) && game.gameBoard.charAt(1) == game.gameBoard.charAt(2) && game.gameBoard.charAt(0) != EMPTY_VALUE;
        boolean secondRow = game.gameBoard.charAt(3) == game.gameBoard.charAt(4) && game.gameBoard.charAt(4) == game.gameBoard.charAt(5) && game.gameBoard.charAt(3) != EMPTY_VALUE;
        boolean thirdRow = game.gameBoard.charAt(6) == game.gameBoard.charAt(7) && game.gameBoard.charAt(7) == game.gameBoard.charAt(8) && game.gameBoard.charAt(6) != EMPTY_VALUE;
        boolean firstColumn = game.gameBoard.charAt(0) == game.gameBoard.charAt(3) && game.gameBoard.charAt(3) == game.gameBoard.charAt(6) && game.gameBoard.charAt(0) != EMPTY_VALUE;
        boolean secondColumn = game.gameBoard.charAt(1) == game.gameBoard.charAt(4) && game.gameBoard.charAt(4) == game.gameBoard.charAt(7) && game.gameBoard.charAt(1) != EMPTY_VALUE;
        boolean thirdColumn = game.gameBoard.charAt(2) == game.gameBoard.charAt(5) && game.gameBoard.charAt(5) == game.gameBoard.charAt(8) && game.gameBoard.charAt(2) != EMPTY_VALUE;
        if (diagonalLeft || diagonalRight || firstRow || secondRow || thirdRow || firstColumn || secondColumn || thirdColumn){
            isAWin = true;
        }
        return isAWin;
    }

    //updates game board
    private void updateBoard(int index, char Tic){
        StringBuilder st = new StringBuilder(game.gameBoard);
        st.setCharAt(index, Tic);
        game.gameBoard = String.valueOf(st);
    }
    //resets game board and slots
    public void reset(){
        game.gameBoard = "---------";
        for (TextView textView : textViews){
            textView.setText("");
        }
    }

    //plays game for android and checks if there's winner
    @Override
    public void processFinish(Integer integer) {
        char o_VALUE = 'O';
        playGame(integer, o_VALUE);
            if (isWin()){
                String loss_message = "Android wins";
                Toast.makeText(PlayGameActivity.this, loss_message, Toast.LENGTH_SHORT).show();
                int playerTwoWin = game.getPlayerTwoWin()+1;
                game.setPlayerTwoWin(playerTwoWin);
//                reset();
            }
            else if  (isAFilledBoard()){
                Toast.makeText(this, tie_message, Toast.LENGTH_SHORT).show();
//                reset();
            }
    }

    //Thread to choose a slot for Android to play
    class AndroidPlay extends AsyncTask<Integer,Void,Integer>{

        public RobotResponse robotPlay = null;

        public AndroidPlay(RobotResponse robotPlay){
            this.robotPlay = robotPlay;
        }
        @Override
        protected Integer doInBackground(Integer... ints) {
            int boardLength = ints[0];
            Random random = new Random();
            int[] generatedValues;
            //some smart plays for android
            if(game.gameBoard.charAt(4) != X_VALUE && game.gameBoard.charAt(4) == EMPTY_VALUE){
                return 4;
            }
            if(game.gameBoard.charAt(0) != X_VALUE && game.gameBoard.charAt(3) != X_VALUE && game.gameBoard.charAt(6) != X_VALUE){
                if (game.gameBoard.charAt(6) == EMPTY_VALUE){
                    return 6;
                }
            }
            //random plays for android
            generatedValues =  random.ints(0,boardLength).distinct().limit(boardLength-2).toArray();
            for (int generatedValue : generatedValues) {
                if (checkPlayValid(generatedValue)) {
                    return generatedValue;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            robotPlay.processFinish(integer);
        }


    }

}