/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;

/**
 *
 * @author franc
 */
public class Coordinate {
    public int row;
    public int column;
    
    public Coordinate(int row, int column){
        this.row = row;
        this.column = column;
    }
    
    public String PrintCoordinate(){
        String formattedString = String.format("( %d, %d)", this.row, this.column);
        return formattedString;
    }
     
}
