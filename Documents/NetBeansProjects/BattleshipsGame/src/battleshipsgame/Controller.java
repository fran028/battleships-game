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
public class Controller implements Observer {
    Model model; 
    CLI cli; 
    View view;  
    
    public Controller(Model model){
        this.model = model;
        this.cli = new CLI(model);
        this.model.addObserver(this); 
        
    } 

    public void setView(View view) {
        this.view = view;  
    }
    
    public void setCLI(CLI cli) {
        this.cli = cli;
    }

    public void startGame(boolean RandomShips) { 
        if(RandomShips){
            this.model.generateRandomShips();
        } else {
            this.model.loadShipFile(); 

            if(this.model.checkShipsRequired()){ 
                this.model.addShipsToBoard(); 
            } else {
                this.cli.displayMessage("There are not enought Ships");
                System.exit(0);
            }
        } 
        
            if (view == null) {
                cli.displayMessage("Game Started");
                // CLI mode
                this.cli.update();
                while (!this.model.isGameOver()) { 
                    String target = this.cli.getUserInput();
                    ShotResult result = model.fireShot(target); 
                }
                this.cli.displayWinMessage();
                this.cli.closeScanner();
            }  
        
    } 
 
    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            if (this.view != null) {
                this.view.update(o, arg);
            } else if (this.cli != null) {
                this.cli.update();
            }
        } 
    }


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
