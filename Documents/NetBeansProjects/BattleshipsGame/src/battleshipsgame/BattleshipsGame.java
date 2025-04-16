/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package battleshipsgame;

/**
 *
 * @author franc
 */
public class BattleshipsGame {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { 
        Model model = new Model(10);
        Controller controller = new Controller(model);
        View view = new View(model, controller); // Create and show the view
        controller.StartGame();
    }
    
}
