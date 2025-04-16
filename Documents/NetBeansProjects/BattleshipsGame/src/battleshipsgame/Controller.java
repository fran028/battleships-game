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
    
    View view;  // Use the View class 
    
    public Controller(Model model){
        this.model = model;
        this.cli = new CLI(model);
        this.model.addObserver(this);
    } 

    public void SetView(View view) {
        this.view = view;  
    }

    public void StartGame() {
        model.LoadShipFile();
        model.AddShipsToBoard(); 
    }
 
    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            
        }
    }


    public void handleShot(String target){
         model.FireShot(target);
    }
}
