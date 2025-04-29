/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;

import static battleshipsgame.Orientation.HORIZONTAL;
import static battleshipsgame.Orientation.VERTICAL;
import static battleshipsgame.ShotResult.MISS;

/**
 *
 * @author franc
 */
// Class of the Board used in the game 
public class Board {
    int gridSize; // Size of the board
    String[][] grid; // Board spaces
    
    // Initiates the Board
    public Board(int gridSize){
        this.gridSize = gridSize; // Set Board Size
        this.grid = new String[gridSize][gridSize]; // Create Board
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                this.grid[i][j] = "w"; //Fill it with water
            }
        }
    }
    
    // Places the Ship passed as a parameter in the Board 
    // Returns a boolean depending if the ship was placed (True) or not (False) 
    public boolean  placeShip(Ship ship){ 
        Coordinate coordinate = ship.coordinate; // Ship position on Board
        Orientation orientation = ship.orientation; // Direction the Ship is facing (Horizontal or Vertical)
        int lenght = ship.lenght; // Spaces ship occupies in the board
        
        // Check if ship can be place in the Board
        if(!checkShipPosition(coordinate, orientation, lenght)){
            return false; // Return true if ship cannot be placed
        }
        
        // Loops through all the ship spaces
        for(int i = 0; i<lenght; i++){ 
            int row = coordinate.row;
            int column = coordinate.column;
            switch (orientation) { // Checks orientation and moves accordingly 
                case HORIZONTAL: // Moves to next column
                    column = column + i;
                    break;
                case VERTICAL: // Moves to next row
                    row = row + i; 
                    break;
                default:                        
                    break;
            }
            this.grid[row][column] = "s"; // Adds Ship
        }
        
        return true; // Return true if ship is placed
        
    }
    
    // Marks the shot passed as a parameter on the board
    // Returns a boolean depedngin if the shot was marked (True) or not (False) 
    public boolean markShot(Shot shot){
        int row = shot.coordinate.row; // Shot row position
        int column = shot.coordinate.column; // Shot column position
        ShotResult result = shot.result; // Result type of the shot
        if(null == result){ // If the result is null returns false
            return false;
        } else switch (result) { // Check the type of the result and marks accordingly
            case MISS: // It missed a ship and hit water
                this.grid[row][column]="m";
                break;
            case SUNK: // It sunk a ship
                this.grid[row][column]="h";
                break;
            case HIT: // It hit a ship
                this.grid[row][column] = "h";
                break;
            default: // By default it doesn't mark the shot
                System.out.println("Shot not marked");
                return false; // Return False when the shot is marked
        }
        return true; // Return True when the shot is marked
    }
    
    // Checks the if the position in the Board passed as a paramter is a hit, ship or other 
    public int checkShot(Coordinate coordinate){
        String cellState = this.grid[coordinate.row][coordinate.column];  
        switch(cellState){
            case "s": // Is a ship
                return 1; 
            case "h": // Is a hit
                return 2; 
            default: // Is something else (miss or water)
                return 0;
        } 
    }
    
    // Checks if the position of the ship is valid 
    // Based on the coordinates, orientation and lenght that are passed as a parameter
    // Return a boolean depending if it can be placed on the board (True) or not (False)
    public boolean checkShipPosition(Coordinate coordinate, Orientation orientation, int lenght){ 
        for(int i = 0; i<lenght; i++){
            int row = coordinate.row; // Row coordinate on board
            int column = coordinate.column; // Column coordinate on board
            switch (orientation) { // Check the direction of the ship and moves accordingly
                case HORIZONTAL: // Moves to the next column
                    column = column + i; 
                    break;
                case VERTICAL: // Moves to the next row
                    row = row + i;
                    break;
                default:
                    break;
            }  
            // Check if the position is outside the board
            if(column >= this.gridSize || row >= this.gridSize ){
                return false; // Ship cannot be placed there
            }
            
            // Check if there is already a Ship in that position
            if("s".equals(this.grid[row][column])){ 
                return false; // Ship cannot be placed there
            } 
        }
        return true; // Ship can be placed there
    }    
} 
