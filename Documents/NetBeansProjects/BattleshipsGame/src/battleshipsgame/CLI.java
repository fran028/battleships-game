/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;

import java.util.Scanner;

/**
 *
 * @author franc
 */
public class CLI {
    Model model;
    Scanner scanner;

    public CLI(Model model) {
        this.model = model;
        this.scanner = new Scanner(System.in);
    }

    public void update() {
        displayBoard();
    }
 
    public void displayBoard() {
        this.model.board.PrintBoardCLI(false);
    } 
    
    public void displayBoardWithShips(){
        this.model.board.PrintBoardCLI(true);
    }
 
    public String getUserInput() {
        System.out.print("Enter row and column to fire (e.g., A1, J10): ");
        String coord = scanner.nextLine(); 
        scanner.nextLine(); // Consume newline
        return coord;
    }
 
    public void displayMessage(String message) {
        System.out.println(message);
    }
    
    public void displayWinMessage(){
        String message = "--- Congratulaions you won!!! ---";
        System.out.println(message);
    }

    public void closeScanner() {
        scanner.close();
    }
}
