/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;

/**
 *
 * @author franc
 */
public class Ship {
    int lenght;
    boolean sunk;
    Coordinate coordinate;
    Orientation orientation;
    int hits;
    
    public Ship(int lenght, Coordinate coordinate, Orientation orientation){
        this.lenght = lenght;
        this.sunk = false;
        this.coordinate = coordinate;
        this.orientation = orientation;
        this.hits = 0;
    }
    
    public void Hitship(){
        this.hits++;
        if(this.hits >= this.lenght){
            this.sunk = true;
        }
    }
    
    public boolean isSunk() {
        return sunk;
    }
    
    public void setSunk(boolean sunk){
        this.sunk = sunk;
    }
    
    public int GetLenght(){
        return this.lenght;
    }
    
    public Coordinate GetCoordinate(){
        return this.coordinate;
    }
    
    public Orientation GetOrientation(){
        return this.orientation;
    }
}
