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
import static java.lang.Math.random;
import java.util.Observable;
import java.util.List;
import java.util.Random;
import java.io.FileWriter; 
import java.io.BufferedWriter; 
import java.io.InputStream;
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
        
    public int getShotsFiredCount(){
        return this.shotsFiredCount;
    }
    
    public int getSunkShipsCount(){
        return this.sunkShipsCount;
    }
    
    public void generateRandomShips(){
        Random random = new Random();
        int shipCount = 0;
        int[] shipSizes = {5, 4, 3, 2, 2};
        //try (InputStream inputStream = Model.class.getResourceAsStream(csvFile); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        
        while(shipCount < 5){
            int randomHorientation = random.nextInt(10);
            Orientation orientation;
            if(randomHorientation >= 5){
                orientation = VERTICAL; 
            } else {
                orientation = HORIZONTAL; 
            } 

            int shipLength = shipSizes[shipCount];
            Coordinate coord = new Coordinate(0,0);
            boolean shipSet = false;
            while(!shipSet){  
                coord = new Coordinate(random.nextInt(board.gridSize), random.nextInt(board.gridSize));  
                shipSet = board.checkShipPosition(coord, orientation,shipLength);  
            }
            System.out.printf("At %s, orientation: %s, length: %d%n", coord.printCoordinateString(), orientation, shipLength);
            Ship tempShip = new Ship(shipLength, coord, orientation);
            board.placeShip(tempShip);
            this.ships[shipCount] = tempShip; 
            shipCount++;
        }  
    }
    
    public boolean loadShipFile(){
        String csvFile = "shipsList.csv"; // Path relative to the source packages

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
    
    public boolean checkShipsRequired(){ 
        boolean hasShipFive = false;
        boolean hasShipFour = false;
        boolean hasShipThree = false;
        boolean hasShipTwoFirst = false;
        boolean hasShipTwoSecond = false;
        
        for (Ship ship : this.ships) {
            if(ship == null){
                return false;
            }
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
    
    public void addShipsToBoard(){
        for (Ship ship : this.ships) {
            this.board.placeShip(ship);
        }
        setChanged(); // Model changed
        notifyObservers(); // Notify the view
    }
    
    public ShotResult fireShot(String shotMade){  
        Coordinate shotCoordinate = decodeShotLocation(shotMade);  
        ShotResult result;
        if(board.checkShot(shotCoordinate)){
            int shipState = addShotToShip(shotCoordinate);
            switch(shipState){
                case 0: 
                    result = ShotResult.MISS; 
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
        
        board.markShot(shot);
        this.shotsFiredCount++;
        setChanged(); // Model changed
        notifyObservers(result); // Notify the view, passing the result
        return result;
    }
    
    private int addShotToShip(Coordinate shotCoordinate){
        for (Ship ship : this.ships) {
            Coordinate shipCoordinate = ship.coordinate;
            Orientation orientation = ship.orientation;
            int lenght = ship.lenght;
            
            for(int i = 0; i< lenght; i++){
                int row = shipCoordinate.row;
                int column = shipCoordinate.column;
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
                    ship.hitship();
                    if(ship.isSunk()){
                        return 2;
                    }
                    return 1;
                }  
            }
        } 
        return 0;
    }
    
    private Coordinate decodeShotLocation(String input){
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
    
    public Ship[] getShips() {
        return this.ships;
    }
}
