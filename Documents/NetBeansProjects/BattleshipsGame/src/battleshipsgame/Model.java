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

/**
 *
 * @author franc
 */
public class Model {
    Board board; 
    Ship[] ships = new Ship[5];
    
    public Model(int gridSize){ 
        this.board = new Board(gridSize);  
        if(LoadShipFile()){
            System.out.println("Ships loaded");
        } else {
            System.out.println("Error loading Ships");
        }
        AddShipsToBoard(); 
        this.board.PrintBoardCLI(true); 
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
    
    private void AddShipsToBoard(){
        for (Ship ship : this.ships) {
            this.board.PlaceShip(ship);
        }
    }
    
    public ShotResult FireShot(String shotMade){  
        Coordinate shotCoordinate = DecodeShotLocation(shotMade);
        ShotResult result;
                
        if(board.CheckShot(shotCoordinate)){
           result = ShotResult.HIT;  
           AddShotToShip(shotCoordinate);
        } else {
           result = ShotResult.MISS;
        }  
        
        Shot shot;
        shot = new Shot(shotCoordinate, result);
        
        board.MarkShot(shot);
        return result;
    }
    
    private boolean AddShotToShip(Coordinate shotCoordinate){
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
                    return true;
                }  
            }
        }
        return false;
    }
    
    private Coordinate DecodeShotLocation(String input){
        String rowString = input.substring(0,1); 
        String columnString = input.substring(1);  
        
        int column = Integer.parseInt(columnString);
        int row;
        
        if (rowString.length() == 1 && rowString.charAt(0) >= 'A' && rowString.charAt(0) <= 'J') {
            row = rowString.charAt(0) - 'A' + 1; 
        } else {
            System.err.println("Error: Invalid letter - " + rowString);
            row = 0;
        }
        
        Coordinate coordinate = new Coordinate(row, column);
        return coordinate;
    }
}
