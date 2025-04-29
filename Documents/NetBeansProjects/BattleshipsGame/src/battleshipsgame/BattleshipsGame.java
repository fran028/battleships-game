/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package battleshipsgame;

import java.util.Scanner;

/**
 *
 * @author franc
 */
// Main file of the project
public class BattleshipsGame { 
    
    public static void main(String[] args) { 
        // Initiate model and controller
        Model model = new Model(10);
        Controller controller = new Controller(model);
        
        // Ask user which version to play
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose interface:");
        System.out.println("1. GUI");
        System.out.println("2. CLI");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt(); 
        
        switch (choice) {
            case 1: // GUI Version
                View view = new View(model, controller); // Create and show the view
                controller.setView(view); // Pass view to controller
                break;
            case 2: // CLI Version
                CLI cli = new CLI(model); // Create the CLI
                controller.setCLI(cli); // Pass CLI to controller
                break;
            default: // By default the CLI Version is used
                System.out.println("Invalid choice. Using CLI.");
                CLI defaultCli = new CLI(model); // Create the CLI
                controller.setCLI(defaultCli); // Pass CLI to controller
                break;
        }  
        
        //Ask the user how to place ships on board 
        System.out.println("How to generate Ships");
        System.out.println("1. Automatic");
        System.out.println("2. File");
        System.out.print("Enter your choice (1 or 2): ");
        int random = scanner.nextInt();  
        boolean RandomShips;
        switch (random) {
            case 1: // uses the system automatic placement
                RandomShips = true;
                break;
            case 2: // uses the file in system (shipsList.csv)
                RandomShips = false;
                break;
            default: // uses the automatic by default
                System.out.println("Invalid choice. Using Automatic Generation.");
                RandomShips = false;
                break;
        }   
        // Start game
        controller.startGame(RandomShips); 
        scanner.close();
    }
    
}
