/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;
import static battleshipsgame.Orientation.HORIZONTAL;
import static battleshipsgame.Orientation.VERTICAL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.List;

/**
 *
 * @author franc
 */
public class Model extends Observable {
    Board board; 
    Ship[] ships = new Ship[5]; 
    private int shotsFiredCount = 0;
    private int sunkShipsCount = 0; // Track sunk ships
    
    public Model(int gridSize){ 
        this.board = new Board(gridSize);   
    }
    
    public boolean LoadShipFile(){
        String csvFile = "../data/shipsList.csv"; // Path relative to the source packages

        try (InputStream inputStream = Model.class.getResourceAsStream(csvFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            if (inputStream == null) {
                System.err.println("Could not find the CSV file: " + csvFile);
                return false;
            }

            String line;
            int shipCount = 0;
            while ((line = reader.readLine()) != null) {
                // Process each line of the CSV file
                String[] data = line.split(","); 
                int lenght = Integer.parseInt(data[0]); 
                Coordinate coord = new Coordinate(Integer.parseInt(data[2]), Integer.parseInt(data[1]));
                Orientation orientation;
                switch (data[3]) {
                    case "v": 
                        orientation = VERTICAL;
                        break;
                    case "h":
                        orientation = HORIZONTAL;
                        break;
                    default: 
                        System.out.println(data[3]);
                        return false; 
                }
                
                Ship tempShip = new Ship(lenght, coord, orientation);
                
                this.ships[shipCount] = tempShip;
                
                shipCount++;
                // Perform further operations with the data
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public boolean CheckShipsRequired(){
        if(this.ships.length < 5){
            return false;
        }
        boolean hasShipFive = false;
        boolean hasShipFour = false;
        boolean hasShipThree = false;
        boolean hasShipTwoFirst = false;
        boolean hasShipTwoSecond = false;
        
        for (Ship ship : this.ships) {
            switch(ship.lenght){
                case 5:
                    if(!hasShipFive){
                        hasShipFive = true;
                    }
                    break;
                case 4:
                    if(!hasShipFour){
                        hasShipFour=true;
                    }
                    break;
                case 3:
                    if(!hasShipThree){
                        hasShipThree=true;
                    }
                    break;
                case 2:
                    if(!hasShipTwoFirst){
                        hasShipTwoFirst = true;
                    } else if(!hasShipTwoSecond) {
                        hasShipTwoSecond = true;
                    }
                    break;
                    
            }
        }
        return hasShipFive && hasShipFour && hasShipThree && hasShipTwoFirst && hasShipTwoSecond;
    }
    
    public void AddShipsToBoard(){
        for (Ship ship : this.ships) {
            this.board.PlaceShip(ship);
        }
        setChanged(); // Model changed
        notifyObservers(); // Notify the view
    }
    
    public ShotResult FireShot(String shotMade){  
        System.out.println("FireShot, shot: "+shotMade);
        Coordinate shotCoordinate = DecodeShotLocation(shotMade);
        
        System.out.println("CheckShot, Coordiante: Row: "+shotCoordinate.row+", Column: "+shotCoordinate.column);
        ShotResult result;
                
        if(board.CheckShot(shotCoordinate)){
            int shipState = AddShotToShip(shotCoordinate);
            switch(shipState){
                case 0: 
                    result = ShotResult.MISS; 
                    System.out.println("Error: No ship found to hit");
                    break;
                case 1: 
                    result = ShotResult.HIT;  
                    break;
                case 2:
                    result = ShotResult.SUNK;  
                    sunkShipsCount++;
                    break;
                default: 
                    result = ShotResult.HIT; 
                    break;
            }
           
        } else {
           result = ShotResult.MISS;
        }  
        
        Shot shot;
        shot = new Shot(shotCoordinate, result);
        
        board.MarkShot(shot);
        this.shotsFiredCount++;
        setChanged(); // Model changed
        notifyObservers(result); // Notify the view, passing the result
        return result;
    }
    
    private int AddShotToShip(Coordinate shotCoordinate){
        for (Ship ship : this.ships) {
            Coordinate shipCoordinate = ship.coordinate;
            Orientation orientation = ship.orientation;
            int lenght = ship.lenght;
            
            for(int i = 0; i< lenght; i++){
                int row = shipCoordinate.column;
                int column = shipCoordinate.row;
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
                if(shotCoordinate.column == column && shotCoordinate.row == row){
                    ship.Hitship();
                    if(ship.isSunk()){
                        return 2;
                    }
                    return 1;
                }  
            }
        } 
        return 0;
    }
    
    private Coordinate DecodeShotLocation(String input){
        String columnString = input.substring(0,1); 
        String rowString = input.substring(1);  
        
        int column;
        int row = Integer.parseInt(rowString)-1;
        
        if (columnString.length() == 1 && columnString.charAt(0) >= 'A' && columnString.charAt(0) <= 'J') {
            column = columnString.charAt(0) - 'A'; 
        } else {
            System.err.println("Error: Invalid letter - " + columnString);
            column = 0;
        }
        
        Coordinate coordinate = new Coordinate(row, column);
        return coordinate;
    }
    
    public boolean isGameOver() {
        return sunkShipsCount == ships.length;
    }
    
    public int getShotsFiredCount() {
        return this.shotsFiredCount;
    }
    
    public Ship[] getShips() {
        return this.ships;
    }
}
