/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;

/**
 *
 * @author franc
 */
// Class that contains information of the shot made by the user
public class Shot {
    Coordinate coordinate; // Shot location in the board
    ShotResult result; // Result of the shot, if it was a hit, miss or sunk
    
    // Initiate the Shot
    public Shot(Coordinate coordinate, ShotResult result){
        this.coordinate = coordinate;
        this.result = result;
    }
    
    // Return the result of the shot 
    public ShotResult getResult(){
        return this.result;
    }
    
    // Returns the position of the shot in the board
    public Coordinate getCoordinate(){
        return this.coordinate;
    }
}


