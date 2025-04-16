/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;

/**
 *
 * @author franc
 */
public class Controller {
    Model model; 
    CLI cli;
    
    
    public Controller(Model model){
        this.model = model;
        this.cli = new CLI(model);
    }
    
    public void StartGame(){
        model.LoadShipFile();
        model.AddShipsToBoard();
        cli.displayBoard();
        
        while(!model.isGameOver()){
            String target = cli.getUserInput();
            ShotResult result = model.FireShot(target);
            System.out.println("You fired at " + target + " and it was a " + result); 
            cli.displayBoard();
        }
        cli.displayWinMessage();
    }
}
