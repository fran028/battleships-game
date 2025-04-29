/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JTextField;
/**
 *
 * @author franc
 */
// Class that manages the user inputs
public class Controller implements Observer {
    Model model; // Initiate Game Model
    CLI cli; // Initiate CLI
    View view;  // Initiate View
    
    public Controller(Model model){
        this.model = model; // Load Model
        this.cli = new CLI(model); // Creates CLI
        this.model.addObserver(this); // Adds observet to the model
        
    } 
    
    // Loads view variable
    public void setView(View view) {
        this.view = view;  
    }
    
    // Loads CLI variable
    public void setCLI(CLI cli) {
        this.cli = cli;
    }
    
    // Starts Game
    public void startGame(boolean RandomShips) { 
        // Add ships to board throught the model
        if(RandomShips){ // Use the project ship creator
            this.model.generateRandomShips();
        } else { // Uses the file to load ships
            this.model.loadShipFile(); // Access file and loads ships to model
            if(!this.model.checkShipsRequired()){  // Check if the file has the minimum requiered ships
                this.cli.displayMessage("There are not enought Ships");
                System.exit(0); // Stops program if the condition is not met
            }
            if(!this.model.addShipsToBoard()){ // Check and adds ship to board
                this.cli.displayMessage("Ships possitioning is incorrect"); 
                System.exit(0); // Stops program if the condition is not met
            }
        } 
        
        // Starts CLI Version of the Game
        if (view == null) {
            cli.displayMessage("Game Started"); // Start message 
            this.cli.update(); // Updates and prints board
            
            // Continues until game is over 
            while (!this.model.isGameOver()) { 
                String target = this.cli.getUserInput(); // ASk user for input
                if("EXIT".equals(target)|| "END".equals(target)){ // User ended the game
                    this.cli.displayBoardWithShips(); // Prints board with ships
                    this.cli.displayEndMessage(); // Messages the end of the game
                    break; // Stops loop
                }
                if("SHOW".equals(target)){ // The user wants to see the ships
                    this.cli.displayBoardWithShips(); // Shows board with ships
                    continue;
                }
               
                ShotResult result = model.fireShot(target); // Fires shot and gets result
                switch(result){
                    case ERROR: // The input was not valid
                        this.cli.displayErrorMessage("Wrong Coordinates");
                        break;
                    default: // Prints the result of the shot
                        this.cli.displayShotMessage(result, target);
                        break;
                }
            }
            if(this.model.isGameOver()){ // Check if the game was won
                this.cli.displayWinMessage(); // Prints winning message
            }
            this.cli.closeScanner(); // Terminates scanner
        }  
        
    } 
    
    // Checks for game updates
    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            if (this.view != null) {
                this.view.update(o, arg); // Calls view update
            } else if (this.cli != null) {
                this.cli.update(); // Calls cli Updates 
            }
        } 
    }

    // Manages the shot and accs acordingly
    public void handleShot(String target){  
        ShotResult result = this.model.fireShot(target);  
        if (this.model.isGameOver()) {
            if(this.view != null){
                System.out.println("YOU WON!!!");
                this.view.update(this.model, result);
            }
        }
    }
    
}
