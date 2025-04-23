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
    
    public boolean  PlaceShip(Ship ship){ 
        Coordinate coordinate = ship.coordinate;
        Orientation orientation = ship.orientation;
        int lenght = ship.lenght; 
        
        if(!CheckShipPosition(coordinate, orientation, lenght)){
            return false;
        }
        
        for(int i = 0; i<lenght; i++){ 
            int row = coordinate.column;
            int column = coordinate.row;
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
    
    public boolean MarkShot(Shot shot){
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
    
    public boolean CheckShot(Coordinate coordinate){
        String cellState = this.grid[coordinate.row][coordinate.column]; 
        System.out.println("CheckShot, cell: "+cellState);
        if("s".equals(cellState) || "h".equals(cellState)){ 
            System.out.println("CheckShot, cell: "+cellState+" shot: true");
            return true;
        } 
        System.out.println("CheckShot, cell: "+cellState+" shot: false");
        return false;
    }
    
    public boolean CheckShipPosition(Coordinate coordinate, Orientation orientation, int lenght){ 
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
            if(this.gridSize <= column || this.gridSize <= row){
                return false;
            }
            // Check if there is already a Ship in that position
            if("S".equals(this.grid[row][column])){
                return false;
            } 
        }
        return true;
    }
    
    public void PrintBoardCLI(boolean withShips){
        int size = this.gridSize;
        System.out.println("Battleship Board");
        System.out.printf("|");
        for(int top = 0; top < size*2+1; top++){
            System.out.printf("-");
        }  
        System.out.printf("|");
        System.out.print("\n");
        System.out.println("XX|A|B|C|D|E|F|G|H|I|J|");
       
        for(int i = 0; i < size; i++){ 
            if(i < 9){
                System.out.print("0");
            }
            System.out.print(i+1);
            System.out.printf("|");
            for(int j = 0; j < size; j++){
                switch(this.grid[i][j]){
                    case "w":
                        System.out.printf(" ");
                        break;
                    case "s":
                        if(withShips){
                            System.out.printf("s");
                        } else {
                            System.out.printf(" ");
                        }
                        break;
                    case "h":
                        System.out.printf("h");
                        break;
                    case "m":
                        System.out.printf("m");
                        break;
                    default: 
                        System.out.printf(" ");
                        break;
                    
                } 
                System.out.printf("|");
            }
            
            System.out.printf("\n"); 
        }
        System.out.printf("|");
        for(int top = 0; top < size*2+1; top++){
            System.out.printf("-");
        }  
        System.out.printf("|\n");
    }
     
            
} 
