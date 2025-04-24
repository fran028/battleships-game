/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;

/**
 *
 * @author franc
 */
public class Shot {
    Coordinate coordinate;
    ShotResult result;
    
    public Shot(Coordinate coordinate, ShotResult result){
        this.coordinate = coordinate;
        this.result = result;
    }
    
    public ShotResult getResult(){
        return this.result;
    }
    
    public Coordinate getCoordinate(){
        return this.coordinate;
    }
}


