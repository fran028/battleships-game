# Battleships Game

This project implements a one-player version of the Battleships game, featuring both a Graphical User Interface (GUI) and a Command Line Interface (CLI).

## How to Play

In Battleships, the player tries to guess the hidden locations of ships on a 10x10 grid.
* Ships are placed either horizontally or vertically.
* Different ships occupy varying numbers of squares.
* The player enters coordinates to "fire" at the grid.
* The game reports "hits" and "misses".
* Sinking all ships wins the game.

## Features

* **GUI Version:**
    * Uses Model-View-Controller (MVC) architecture.
    * Player selects grid locations by clicking.
    * Displays the game board.
    * Indicates hits and misses.
    * Shows when ships are sunk.
    * Reports the number of tries at the end of the game.
* **CLI Version:**
    * Uses the same Model as the GUI.
    * Player enters coordinates as alphanumeric strings (e.g., A1, J10).
    * Displays the game board.
    * Indicates hits and misses.
    * Shows when ships are sunk.
    * Reports the number of tries at the end of the game.
* **Ship Placement:**
    * The computer secretly arranges ships at the start.
    * Ships:
        * One ship of length 5
        * One ship of length 4
        * One ship of length 3
        * Two ships of length 2
        * No ships of length 1
    * Ships can be adjacent but cannot overlap.
    * The game can load ship configurations from a text file.
    * Error messages are shown if the file format is incorrect.

## Code Structure

The project is structured with the following key components:

* **Model:** Contains the game logic and data.
    * `Model.java`: The main Model class, extending `Observable`.
    * `Board.java`: Manages the game board.
    * `Ship.java`: Represents a ship.
    * `Coordinate.java`: Handles grid coordinates.
    * `Shot.java`: Represents a player's shot.
    * `Orientation.java`: An enum for ship orientation (horizontal or vertical).
    * `ShotResult.java`: An enum for shot results (hit, miss, sink).
* **View:** (GUI Version)
    * `View.java`: The GUI, implementing `Observer` and using Swing.
* **Controller:** (GUI Version)
    * `Controller.java`: Handles user input and updates the Model and View.
* **CLI:** (CLI Version)
    * `CLI.java`: The Command Line Interface.
* `BattleshipsGame.java`: The main class to start the game.

## Files

* `shipsList.csv`: A sample text file for loading ship configurations.

## Running the Game

1.  Compile the Java source files.
2.  Run the `BattleshipsGame` class.
3.  The program will prompt you to choose between the GUI and CLI versions.
4.  The program will then ask you how you want to generate the ships on the board (automatic or file).
5.  Follow the on-screen instructions to play the game.

## Additional Notes

* The GUI version adheres to the Model-View-Controller (MVC) pattern.
* The CLI version reuses the Model.
* The code is documented.
* This README provides a basic overview; refer to the code and other documentation for more details.
