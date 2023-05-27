# tic-tac-toe game

## Demo
https://youtube.com/shorts/8RT-JjzZBZ8

## Features
The tic tac toe game app has the following features:

1. Main menu activity (initially launched activity)

   - Displays a welcome message and ListView with three items.

   - When selected, each list item will launch another activity:

     - Enter names – launches EnterNamesActivity

     - Play game – launches PlayGameActivity and displays the game board

     - Standings – launches ShowStandingsActivity

2. EnterNamesActivity

   - Displays a simple form

     - Label and text field for player’s name

     - Button ('Save') for saving the name and finishing the activity

3. PlayGameActivity

   - Shows the game board, a 3×3 grid of labels.

   - Each label can show blank (no text), an X, or an O.

     - Labels must have sufficiently large font size to make the game easily playable

     - A blank label places an X or O in that label on click, depending on who is playing

       - Player should place an X
       - Android should place an O
       - Player plays first, followed by turns alternating with Android

     - Game continues until one player places their symbol (X or O) on three adjacent spaces in a row, column, or diagonal.

4. ShowStandingsActivity

   - Displays the number of wins for players and Android, as well as their names, for example:

     - Bob: 24 wins

     - Sandra: 27 wins

     - Android: 20 Wins

5. Game Data

   - Player names and scores should be stored in a local file.

   - Algorithm for Android game play should run in an AsyncTask thread.
