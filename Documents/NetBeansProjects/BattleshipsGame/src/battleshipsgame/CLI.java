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
// Class that contains the functionality of the CLI Version of the game
public class CLI {
    Model model; // Initiate Game Model
    Scanner scanner; // Initiate Game Scanner
    
    // Initate class method
    public CLI(Model model) {
        this.model = model; // Load Model
        this.scanner = new Scanner(System.in); // Start Scanner
    }
    
    // Every time the board is changed by the model it prints it
    public void update() {
        //displayBoard();
        //displayBoard();
        printBoardByLine(false);
    }
    
    // Call the method that prints the board
    public void displayBoard() {
        printBoardCLI(false);
    }  
    
    // Call the method that prints the board that shows the ships
    public void displayBoardWithShips(){
        printBoardCLI(true);
    }
    
    // Print the Board line by line (A1: Water, A2: Hit, etc)  
    public void printBoardByLine(boolean withShips){
        int size = this.model.board.gridSize; 
        for(int i = 0; i < size; i++){   
            for(int j = 0; j < size; j++){
                String column = String.valueOf((char) ('A' + j));
                int row = i+1;
                switch(this.model.board.grid[i][j]){
                    case " ":
                        //System.out.printf("WATER");
                        break;
                    case "s":
                        if(withShips){
                            
                            System.out.print(column+row+": ");
                            System.out.printf("SHIP");
                            System.out.printf("\n");
                        } else {
                            //System.out.printf("WATER");
                        }
                        break;
                    case "h":
                        
                        System.out.print(column+row+": ");
                        System.out.printf("HIT");
                        System.out.printf("\n");
                        break;
                    case "m":
                        
                        System.out.print(column+row+": ");
                        System.out.printf("MISS");
                        System.out.printf("\n");
                        break;
                    default: 
                        //System.out.printf("WATER");
                        break;
                    
                }   
            }  
        }
    }
    
    // Prints the Board formatted to resemble an actual board 
    // Adds the row and column values
    // Draws all positions in between '|'
    public void printBoardCLI(boolean withShips){
        int size = this.model.board.gridSize;
        System.out.println("Battleship Board");
        System.out.printf("|");
        for(int top = 0; top < size*2+1; top++){
            System.out.printf("-");
        }  
        System.out.printf("|");
        System.out.print("\n");
        System.out.println("XX|A|B|C|D|E|F|G|H|I|J|");
       
        for(int i = 0; i < size; i++){ 
            if(i < 9){
                System.out.print("0");
            }
            System.out.print(i+1);
            System.out.printf("|");
            for(int j = 0; j < size; j++){
                switch(this.model.board.grid[i][j]){
                    case " ":
                        System.out.printf(" ");
                        break;
                    case "s":
                        if(withShips){
                            System.out.printf("s");
                        } else {
                            System.out.printf(" ");
                        }
                        break;
                    case "h":
                        System.out.printf("h");
                        break;
                    case "m":
                        System.out.printf("m");
                        break;
                    default: 
                        System.out.printf(" ");
                        break;
                    
                } 
                System.out.printf("|");
            }
            
            System.out.printf("\n"); 
        }
        System.out.printf("|");
        for(int top = 0; top < size*2+1; top++){
            System.out.printf("-");
        }  
        System.out.printf("|\n");
    }
    
    // Prints the message passed as paramter
    public void displayMessage(String message) {
        System.out.println(message);
    }
 
    // Prints options and asks for the user input
    public String getUserInput() {
        System.out.println("To end game write 'EXIT' or 'END'."); // How to exit the game
        System.out.println("To show ships write 'SHOW'."); // How to show ships
        System.out.print("Enter row and column to fire (e.g., A1, J10): "); // How to input a position to fire to
        String coord = scanner.nextLine(); 
        //scanner.nextLine(); // Consume newline
        return coord;
    } 
    
    // Prints the position and result of the shot that are passed as variable
    public void displayShotMessage(ShotResult result, String target){
        String shotMessage = "Shot to ("+target+"): " + result;
        displayMessage(shotMessage);
    }
    
    // Print the error passed as a paramter
    public void displayErrorMessage(String error){
        String errorMessage = "Error!: " + error;
        displayMessage(errorMessage);
    }
    
    // Print the game winning message
    public void displayWinMessage(){ 
        displayMessage("--- CONGRATULATIONS YOU WON ---"); 
        displayMessage("--- THANKS FOR PLAYING ---");
    }
    
    // Prints the game ending message
    public void displayEndMessage(){ 
        displayMessage("--- GAME ENDED ---");
        displayMessage("--- THANKS FOR PLAYING ---");
    }
    
    // Terminates scannes
    public void closeScanner() {
        scanner.close();
    }
}
