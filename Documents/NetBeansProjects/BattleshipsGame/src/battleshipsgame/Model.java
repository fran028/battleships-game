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
// Class that manages most functionalities of the game
// Board creation, ship placement, shot made, etc
public class Model extends Observable {
    Board board; // Initiate Game Board
    Ship[] ships = new Ship[5]; // Create empty ship list
    private int shotsFiredCount = 0; // Count shots fired by the user
    private int sunkShipsCount = 0; // Track sunk ships
    
    public Model(int gridSize){ 
        this.board = new Board(gridSize);   
    } 
    
    // Returns the amount of shot fired
    public int getShotsFiredCount(){
        return this.shotsFiredCount;
    }
    
    // Returns the number of ships sunked
    public int getSunkShipsCount(){
        return this.sunkShipsCount;
    }
    
    // Creates ships with random position that can be placed on the Board
    public void generateRandomShips(){
        Random random = new Random();
        int shipCount = 0; // Ships placed
        int[] shipSizes = {5, 4, 3, 2, 2}; // List of the ships sizes 
        
        while(shipCount < 5){
            // Randomly selects the horientation of the ship
            int randomHorientation = random.nextInt(10); // Generate random number between 0 and 9
            Orientation orientation; // Initiates empty orientation vairbale
            if(randomHorientation >= 5){ // 50/50 Chance
                orientation = VERTICAL; 
            } else {
                orientation = HORIZONTAL; 
            } 
            
            int shipLength = shipSizes[shipCount]; // Gets ship size
            Coordinate coord = new Coordinate(0,0); // Initite empty coordinates
            boolean shipSet = false; 
            while(!shipSet){ // Loops until the ship can be placed in the board 
                coord = new Coordinate(random.nextInt(board.gridSize), random.nextInt(board.gridSize)); // Generates random coordinate 
                shipSet = board.checkShipPosition(coord, orientation,shipLength);  // Checks if position is valid
            }
            System.out.printf("At %s, orientation: %s, length: %d%n", coord.printCoordinateString(), orientation, shipLength);
            Ship tempShip = new Ship(shipLength, coord, orientation); // Creates ship
            board.placeShip(tempShip); // Palces ship on board
            this.ships[shipCount] = tempShip; // Adds new ship to ship list
            shipCount++; // Increase ship count
        }  
    }
    
    // Access the file and adds ships to ship list
    // The file should have in each type a ship (size, row, column, orientation)
    // Return a boolean depending if the content of the file is correct (True) or not (False)
    public boolean loadShipFile(){
        String csvFile = "shipsList.csv"; // File name

        try (InputStream inputStream = Model.class.getResourceAsStream(csvFile); 
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) { // Open File

            if (inputStream == null) { // Error opening the file
                System.err.println("Could not find the CSV file: " + csvFile);
                return false;
            }

            String line;
            int shipCount = 0; // Count ships on file that were added to the board
            // Loops through each line of the file
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // Divides content of line by ","
                int lenght = Integer.parseInt(data[0]); // First value is an int with the ship size 
                // Second and third values are row and column
                Coordinate coord = new Coordinate(Integer.parseInt(data[2]), Integer.parseInt(data[1])); // Creates coordinate
                Orientation orientation; // Initiate empty orientation
                switch (data[3]) { // Fouth position is the orientation
                    case "v": // "v" for Vertical
                        orientation = VERTICAL;
                        break;
                    case "h": // "h" for Horizontal
                        orientation = HORIZONTAL;
                        break;
                    default: 
                        System.out.println(data[3]);
                        return false; 
                }
                
                Ship tempShip = new Ship(lenght, coord, orientation); // Creat new ship
                
                this.ships[shipCount] = tempShip; // Add ship to ship list
                
                shipCount++; // Increase ship counter 
            }

        } catch (IOException e) { // Check for errors
            // Prints error
            e.printStackTrace();
            return false;
        }
        return true; // Return true once the process is finished without errors
    }
    
    // Check if in the ship list are the ships that are required for the game
    // This are based on the size of them (5,4,3,2,2)
    // Return boolean based if the conditions are met (True) or not (False)
    public boolean checkShipsRequired(){ 
        // Initate conditions to false
        boolean hasShipFive = false;
        boolean hasShipFour = false;
        boolean hasShipThree = false;
        boolean hasShipTwoFirst = false;
        boolean hasShipTwoSecond = false;
        
        // Loop throught list
        for (Ship ship : this.ships) {
            if(ship == null){
                return false;
            }
            switch(ship.lenght){ // Checks if ship is one of the required sizes
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
                        hasShipTwoFirst = true; // First ship of size two
                    } else if(!hasShipTwoSecond) {
                        hasShipTwoSecond = true; // Second ship of size two
                    }
                    break;
                    
            }
        }
        // Return true if all conditions are true or false if only one is false
        return hasShipFive && hasShipFour && hasShipThree && hasShipTwoFirst && hasShipTwoSecond; 
    }
    
    // Add all ship in ship list to the board
    // Return a boolean based if there is an error placing ship (False) or not (True)
    public boolean addShipsToBoard(){
        for (Ship ship : this.ships) { // Loop throuhg list
            if(!this.board.placeShip(ship)){ // Place ship
                return false;
            }
        }
        setChanged(); // Model changed
        notifyObservers(); // Notify the view
        return true;
    }
    
    // Fires shot and check it result on the board
    // Return a ShotResult object that contains the result of the shot on the board
    public ShotResult fireShot(String shotMade){  
        // Decodes the input format and return the corresponding coordinate
        Coordinate shotCoordinate = decodeShotLocation(shotMade); // Save coordinate  
        if(shotCoordinate.row == -1 || shotCoordinate.column == -1){ // If coodinate has a -1 means there was an error
            setChanged(); // Model changed
            notifyObservers(ShotResult.ERROR);
            return ShotResult.ERROR;
        }
        
        ShotResult result; // Initiate ShotResult
        
        switch(board.checkShot(shotCoordinate)){ // Checks shot position on board
            case 1: // Position is a ship
                 // Adds shot to position in ship and checks if it sunked it
                int shipState = addShotToShip(shotCoordinate);
                switch(shipState){ // Check result on hit
                    case 0: // Didn't find position on ship
                        result = ShotResult.MISS; 
                        break;
                    case 1: // Ship was hit
                        result = ShotResult.HIT;  
                        break;
                    case 2: // Ship was sunk
                        result = ShotResult.SUNK;   
                        this.sunkShipsCount++;
                        break;
                    default: // By defualt it is a hit
                        result = ShotResult.HIT; 
                        break;
                }
                break;
           
            case 0: // Position is a Miss or Water
                result = ShotResult.MISS;
                break;
            case 2: // Position was already a Hit
                result = ShotResult.HIT;  
                break;
            default: // By defualt it is a Miss
                result = ShotResult.MISS;  
                break;
                
        }  
        
        Shot shot; // Initiate shot
        shot = new Shot(shotCoordinate, result); // Creates shot
        
        board.markShot(shot); // Updates board with new shot
        this.shotsFiredCount++; // Increase shot count
       
        
        setChanged(); // Model changed
        notifyObservers(result); // Notify the view, passing the result
        return result; // Return the result
    }
    
    // Check if the position correspond to a ship and mark it as a hit
    // Also checks if the ship was sunked by that shot
    // Return an int with the result of the shot (1 hit, 2 sunk, 0 miss)
    private int addShotToShip(Coordinate shotCoordinate){
        for (Ship ship : this.ships) { // Loop ships
            Coordinate shipCoordinate = ship.coordinate; // Get ship coordinates
            Orientation orientation = ship.orientation; // Get ship orientation
            int lenght = ship.lenght; // Get ship size
            
            for(int i = 0; i< lenght; i++){ // Loop through ship positions
                int row = shipCoordinate.row; // Get ship initial row
                int column = shipCoordinate.column; // Get ship initial column
                switch (orientation) { // Check orientation and move position accordingly
                    case HORIZONTAL: // Move position by column
                        column = column + i;
                        break;
                    case VERTICAL: // Move position by row
                        row = row + i;
                        break;
                    default:
                        break;
                }
                // Check if this position is the same as the shot position
                if(shotCoordinate.column == column && shotCoordinate.row == row){
                    ship.hitShip(); // Add hit to the ship
                    if(ship.isSunk()){ // Check if it sunk
                        return 2; // Returns that the ship was sunk
                    }
                    return 1; // Returns that the ship was hit
                }  
            }
        } 
        return 0; // Returns that a ship was not on the position
    }
    
    // Transform the user input into a board coordinate 'A1' -> (0,0)
    // Returns the Coordinate 
    private Coordinate decodeShotLocation(String input){  
        String columnString = input.substring(0,1); // Get First part of input as column
        String rowString = input.substring(1);   // Second part of input as row
        if(!isNumber(rowString)){  // Check if row is a number
            return new Coordinate(-1, -1); // Return coordinate with error values
        }
        if(rowString.length() > 2 || rowString.length() < 1 ){   // Check if row value is of size two
            return new Coordinate(-1, -1);// Return coordinate with error values
        }
        // Initiate column and row values
        int column;
        int row = Integer.parseInt(rowString)-1; // Add input to row
        if(row < 0 || row > 9){  // Check if row is a valid position in board
            row = -1; 
        }
        // Checks if the column is a valid position in the board
        if (columnString.length() == 1 && columnString.charAt(0) >= 'A' && columnString.charAt(0) <= 'J') {
            column = columnString.charAt(0) - 'A'; // Transform the position to a number
        } else {   
            column = -1;
        }
        
        Coordinate coordinate = new Coordinate(row, column); // Saves coordinate
        return coordinate; // Return coordinate
    }
    
    // Check if the content of the String is a number (True) or not (False)
    // Return a boolean with the result
    public static boolean isNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false; // Found a digit
            }
        }
        return true; // No digit found
    }
    
    //Check if all ships are sunk
    // Return a boolean based if the condition was met (True) or not (False)
    public boolean isGameOver() { 
        return sunkShipsCount == ships.length;
    } 
    
    // Returns the list of ships in the game
    public Ship[] getShips() {
        return this.ships;
    }
}
