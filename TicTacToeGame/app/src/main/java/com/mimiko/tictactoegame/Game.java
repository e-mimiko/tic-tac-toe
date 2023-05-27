package com.mimiko.tictactoegame;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;

public class Game implements Serializable {

    private class Player implements Serializable{
        private String name;
        private int wins = 0;
        Player(String name){
            this.name = name;
        }
    }
    public Player playerOne;
    public Player playerTwo;
    public String gameBoard;



    Game(){
        this.playerOne = new Player("");
        this.playerTwo = new Player("Android");
        this.gameBoard = "---------";
    }

    public void setPlayerOneName(String name){
        this.playerOne.name = (name);
    }
    public void setPlayerOneWin(int wins){
        this.playerOne.wins = (wins);
    }
    public void setPlayerTwoWin(int wins){
        this.playerTwo.wins = (wins);
    }

    public int getPlayerOneWin(){
        return this.playerOne.wins;
    }
    public String getPlayerOneName(){
        return this.playerOne.name;
    }

    public int getPlayerTwoWin(){
        return this.playerTwo.wins;
    }
    public String getPlayerTwoName(){
        return this.playerTwo.name;
    }

}

