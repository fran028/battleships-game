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
public class Board {
    int gridSize;
    String[][] grid;
    
    public Board(int gridSize){
        this.gridSize = gridSize;
        this.grid = new String[gridSize][gridSize];
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                this.grid[i][j] = "w"; //add water
            }
        }
    }
    
    public boolean  placeShip(Ship ship){ 
        Coordinate coordinate = ship.coordinate;
        Orientation orientation = ship.orientation;
        int lenght = ship.lenght; 
        
        if(!checkShipPosition(coordinate, orientation, lenght)){
            return false;
        }
        
        for(int i = 0; i<lenght; i++){ 
            int row = coordinate.row;
            int column = coordinate.column;
            switch (orientation) {
                case HORIZONTAL:
                    column = column + i;
                    break;
                case VERTICAL:
                    row = row + i;
                    break;
                default:                        
                    break;
            }
            this.grid[row][column] = "s";
        }
        
        return true;
        
    }
    
    public boolean markShot(Shot shot){
        int row = shot.coordinate.row;
        int column = shot.coordinate.column;
        ShotResult result = shot.result;
        if(null == result){
            return false;
        } else switch (result) {
            case MISS:
                this.grid[row][column]="m";
                break;
            case SUNK:
                this.grid[row][column]="h";
                break;
            case HIT: 
                this.grid[row][column] = "h";
                break;
            default: 
                System.out.println("Shot not marked");
                return false;
        }
        return true;
    }
    
    public int checkShot(Coordinate coordinate){
        String cellState = this.grid[coordinate.row][coordinate.column];  
        switch(cellState){
            case "s":
                return 1; 
            case "h":
                return 2; 
            default:
                return 0;
        } 
    }
    
    public boolean checkShipPosition(Coordinate coordinate, Orientation orientation, int lenght){ 
        for(int i = 0; i<lenght; i++){
            int row = coordinate.row;
            int column = coordinate.column;
            switch (orientation) {
                case HORIZONTAL:
                    column = column + i; 
                    break;
                case VERTICAL:
                    row = row + i;
                    break;
                default:
                    break;
            }  
            // Check if it is outside the board
            if(column >= this.gridSize || row >= this.gridSize ){
                return false;
            }
            // Check if there is already a Ship in that position
             
            if("s".equals(this.grid[row][column])){ 
                return false;
            } 
        }
        return true;
    }    
} 
