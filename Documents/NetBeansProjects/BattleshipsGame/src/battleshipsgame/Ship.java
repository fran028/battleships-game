/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;

/**
 *
 * @author franc
 */
// Class that contains the game ships information and methods
public class Ship {
    int lenght; // Ship size, how many spaces it ocupies
    boolean sunk; // If the ship is sunk (True) or not (False)
    Coordinate coordinate; // Ships starting coordinate on the board
    Orientation orientation; // Direction in which the ship is facing
    int hits; // Shots that hit the ship
    
    // Initiate ship
    public Ship(int lenght, Coordinate coordinate, Orientation orientation){
        this.lenght = lenght;
        this.sunk = false; // False is that it is not sunk
        this.coordinate = coordinate;
        this.orientation = orientation;
        this.hits = 0; // Starts with cero hits
    }
    
    // Adds hit to ship
    // Check hit amount and sunks ship
    public void hitShip(){
        this.hits++;
        if(this.hits >= this.lenght){
            setSunk(true);
        }
    }
    
    // Return if the ship is sunk (True) or not (False)
    public boolean isSunk() {
        return sunk;
    }
    
    // Changes if ship is sunk or not
    public void setSunk(boolean sunk){
        this.sunk = sunk;
    }
    
    // Return the size of the ship
    public int getLenght(){
        return this.lenght;
    }
    
    // Returns the initial coordinates of the ship
    public Coordinate getCoordinate(){
        return this.coordinate;
    }
    
    // Returns the orientation of the ship
    public Orientation getOrientation(){
        return this.orientation;
    }
}
