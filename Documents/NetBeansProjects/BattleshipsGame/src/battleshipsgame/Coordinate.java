/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;

/**
 *
 * @author franc
 */
//
// Class that manages the board and ships coordinates
public class Coordinate {
    public int row; // Row in board
    public int column; // Column in board
    
    public Coordinate(int row, int column){
        this.row = row;
        this.column = column;
    }
    
    // Prints coordintate in number format '(1,1)'
    public String printCoordinate(){
        String formattedString = String.format("( %d, %d)", this.row+1, this.column+1);
        return formattedString;
    }
    
    // Prints coordintate Letter format '(1,A(1))'
    public String printCoordinateString(){ 
        String formattedString = String.format("(%d, %s(%d))", this.row+1, String.valueOf((char) ('A' + this.column)),this.column+1 );
        return formattedString;
    }
     
}
