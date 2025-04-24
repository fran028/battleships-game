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
        //displayBoard();
        displayBoardWithShips();
    }
 
    public void displayBoard() {
        printBoardCLI(false);
    }  
    
    public void displayBoardWithShips(){
        printBoardCLI(true);
    }
    
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
    
    public void displayMessage(String message) {
        System.out.println(message);
    }
 
    public String getUserInput() {
        System.out.print("Enter row and column to fire (e.g., A1, J10): ");
        String coord = scanner.nextLine(); 
        //scanner.nextLine(); // Consume newline
        return coord;
    } 
 
    
    public void displayWinMessage(){
        String winMessage = "--- Congratulaions you won!!! ---";
        displayMessage(winMessage);
    }

    public void closeScanner() {
        scanner.close();
    }
}
