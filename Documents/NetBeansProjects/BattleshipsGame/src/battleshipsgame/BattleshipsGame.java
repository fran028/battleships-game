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
        Model model = new Model(10);
        Controller controller = new Controller(model);
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose interface:");
        System.out.println("1. GUI");
        System.out.println("2. CLI");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt(); 
        
        switch (choice) {
            case 1:
                View view = new View(model, controller); // Create and show the view
                controller.setView(view);
                break;
            case 2:
                CLI cli = new CLI(model); // Create the CLI
                controller.setCLI(cli);
                break;
            default:
                System.out.println("Invalid choice. Using CLI.");
                CLI defaultCli = new CLI(model);
                controller.setCLI(defaultCli);
                break;
        }  
        
        System.out.println("How to generate Ships");
        System.out.println("1. Automatic");
        System.out.println("2. File");
        System.out.print("Enter your choice (1 or 2): ");
        int random = scanner.nextInt();  
        boolean RandomShips;
        switch (random) {
            case 1:
                RandomShips = true;
                break;
            case 2:
                RandomShips = false;
                break;
            default:
                System.out.println("Invalid choice. Using Automatic Generation.");
                RandomShips = false;
                break;
        }   // Create and show the view
        controller.startGame(RandomShips); 
        scanner.close();
    }
    
}
