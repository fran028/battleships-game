/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;
import java.util.Observer;
import java.util.Observable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import javax.swing.border.EmptyBorder;
/**
 *
 * @author franc
 */
// Class that handles the visuals of the GUI Version of the game
public class View implements Observer {

    // Initiate the game panels and different visual elements
    private static final Dimension PANEL_SIZE = new Dimension(400, 400); // Size in pixels
    private Model model;
    private Controller controller;
    private JFrame frame;
    private JPanel boardPanel; // Panel for the buttons
    private JButton[][] gridButtons;
    private JPanel titlePanel; // New panel for status/message label
    private JLabel titleLabel;
    private JLabel messageLabel; // Label for messages
    private JPanel statusPanel; // New panel for status/message label
    
    // Game Color palette and fonts 
    private Color cellColorWater = Color.decode("#4D79AB"); // Blue
    private Color cellColorHit = Color.decode("#AA644D"); // Red
    private Color cellColorMiss = Color.decode("#F5E9BF"); // Yellow
    private Color cellColorShip = Color.decode("#969696");  // Gray
    private Color winColor = Color.decode("#788374");  // Green
    private Color backgroundColor = Color.decode("#2C2C3F"); // Default background color
    private Color fontColor = Color.decode("#D5E8FF"); // Oposite of background
    private Font labelFont = new Font("Arial", Font.BOLD, 14);  // Normal text style
    private Font titleFont = new Font("Arial", Font.BOLD, 24); // Title style
    
    // Counters that are displayed in game
    private JLabel shotsFiredLabel = new JLabel("Shots Fired: 0"); // shots fired by the user
    private JLabel sunkShipsLabel = new JLabel("Ships Sunk: 0"); // ships sunk by the user
    
    // New Panel for Shots/Sunk Stats
    private JPanel statsPanel;

    // Initiate view
    public View(Model model, Controller controller) {
        this.model = model;
        this.model.addObserver(this);
        this.controller = controller;
        createControls(); // Implement visuals
        controller.setView(this); // Pass view to controller
        update(model, null); // Update board to finish setup and start game
    }

    // Creates visual of the game
    private void createControls() {
        // Game title
        frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(backgroundColor);   
        
        // Top panel with title and counters
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(backgroundColor);
        
        createTitlePanel(); 
        topPanel.add(titlePanel, BorderLayout.PAGE_START); // Add title at the top
        
        // Stats Panel (Shots Fired, Ships Sunk)
        createStatsPanel();
        topPanel.add(statsPanel, BorderLayout.CENTER); // Add stats panel below title 
        
        contentPane.add(topPanel, BorderLayout.PAGE_START); // Add panel to game panel
       
        // Status Panel (Messages)
        createStatusPanel(); // Bottom panel that show a message with the different game status
        contentPane.add(statusPanel, BorderLayout.SOUTH); // Add panel to game

         // Board Panel (Grid of Buttons) - CREATE THIS FIRST
        createBoardPanel(); // Creates the board of the game
        contentPane.add(boardPanel, BorderLayout.CENTER); // Place it at the center of the game
        
        // Frame configuration
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    
    // Updates game board and stats 
    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            updateBoardDisplay(arg); // Pass the arg to updateBoardDisplay
            updateStatsPanel(); // Changes the content of the stats variable
        }
    }
    
    // Access the data in model to change the game stats
    private void updateStatsPanel() {
        shotsFiredLabel.setText("Shots Fired: " + model.getShotsFiredCount());
        sunkShipsLabel.setText("Ships Sunk: " + model.getSunkShipsCount());
    }
    
    // Makes changes to the board panel 
    // Access the board and generates again the cells inside the board
    // Changes the message in the status panel
    private void updateBoardDisplay(Object arg) {
        int gridSize = this.model.board.gridSize;
        String[][] grid = model.board.grid;
        // Update all buttons inside the board
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                JButton button = gridButtons[row][col];
                String cellState = grid[row][col];
                switch (cellState) {
                    case "w": // Water
                        button.setText("");
                        button.setBackground(cellColorWater);
                        button.setEnabled(true);
                        break;
                    case "s": // Ship
                        button.setText(""); // Hide ships during game
                        if (model.isGameOver()) {
                            button.setText("S"); // Show ships after game over 
                            button.setBackground(cellColorShip);
                        } else {
                            button.setEnabled(true); 
                            button.setBackground(cellColorWater);
                        }
                        break;
                    case "m": // Miss
                        button.setText("");
                        button.setBackground(cellColorMiss); // Yellow for miss
                        button.setEnabled(false); // once shot at cannot be clicked again
                        break;
                    case "h": // Hit
                        button.setText("");
                        button.setBackground(cellColorHit); // Red for hit
                        button.setEnabled(false); // once shot at cannot be clicked again
                        break;
                }
            }
        }

        if (arg instanceof ShotResult) { // Check if there's a ShotResult
            ShotResult result = (ShotResult) arg;
            if (model.isGameOver()) {
                messageLabel.setForeground(winColor); 
                messageLabel.setText("You won! All ships sunk in " + model.getShotsFiredCount() + " shots.");
                disableAllButtons(); // Disable all buttons at game over
            } else {
                switch(result){
                    case SUNK: // Ship is sunk
                        messageLabel.setForeground(cellColorHit); 
                        messageLabel.setText("You sunk a ship!");
                        break;
                    case ERROR: // Error on the shot made
                        messageLabel.setForeground(cellColorHit); 
                        messageLabel.setText("ERROR");
                    default: // Display shot result
                        messageLabel.setForeground(fontColor); 
                        messageLabel.setText("Shot " + result);
                        break;
                }
            } 
        } 
        
        if (model.isGameOver()) { 
            messageLabel.setForeground(winColor); 
            messageLabel.setText("You won! All ships sunk in " + model.getShotsFiredCount() + " shots.");
            disableAllButtons(); // Disable all buttons at game over
        }
    }
    
    // Creates visual elements in the titel panels
    private void createTitlePanel(){
        titlePanel = new JPanel(); // Panel
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center align, some spacing
        // Text Styling
        titlePanel.setBackground(backgroundColor);  
        titleLabel = new JLabel("BATTLESHIPS");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(fontColor);  
        titlePanel.add(titleLabel); // Add element
    }
    
    // Creates visual elements in the stat panel
    private void createStatsPanel() {
        statsPanel = new JPanel(); // Panel
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Center align, some spacing
        // Text Styling
        statsPanel.setBackground(backgroundColor); 
        shotsFiredLabel.setFont(labelFont);
        shotsFiredLabel.setForeground(fontColor);
        sunkShipsLabel.setFont(labelFont);
        sunkShipsLabel.setForeground(fontColor); 
        statsPanel.add(shotsFiredLabel); // Add element
        statsPanel.add(sunkShipsLabel); // Add element
    }
    
    // Creates visual elements in the 
    private void createStatusPanel() {
        statusPanel = new JPanel(); // Panel
        statusPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center align, some spacing
        // Text Styling
        statusPanel.setBackground(backgroundColor);
        statusPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        messageLabel = new JLabel("Welcome to Battleship!");
        messageLabel.setFont(labelFont);
        messageLabel.setForeground(fontColor);
        messageLabel.setBackground(backgroundColor);
        messageLabel.setOpaque(true);
        statusPanel.add(messageLabel); // Add element
    }
    
    // Creates the board for the game
    private void createBoardPanel() {
        int gridSize = model.board.gridSize;
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(gridSize + 1, gridSize + 2)); // +1 for labels
        boardPanel.setPreferredSize(PANEL_SIZE); 
        boardPanel.setBackground(backgroundColor);

        gridButtons = new JButton[gridSize][gridSize];

        // Empty top-left corner
        boardPanel.add(new JLabel("")); 
        // Column labels (A, B, C...)
        for (int i = 0; i < gridSize; i++) {
            JLabel colLabel = new JLabel(String.valueOf((char) ('A' + i))); // Set Column label
            colLabel.setHorizontalAlignment(SwingConstants.CENTER);
            colLabel.setFont(labelFont); // Set font
            colLabel.setForeground(fontColor); // Set font color 
            colLabel.setBackground(backgroundColor);
            colLabel.setOpaque(true);
            boardPanel.add(colLabel);
        }
        boardPanel.add(new JLabel("")); 
        
        // Loop through each position in board
        for (int row = 0; row < gridSize; row++) {  
            JLabel rowLabel = new JLabel(String.valueOf(row + 1)); // Row labels (1, 2, 3...)
            rowLabel.setHorizontalAlignment(SwingConstants.CENTER);
            rowLabel.setFont(labelFont); // Set font
            rowLabel.setForeground(fontColor); // Set font color
            rowLabel.setBackground(backgroundColor);
            rowLabel.setOpaque(true);
            boardPanel.add(rowLabel);
            for (int col = 0; col < gridSize; col++) {
                JButton button = new JButton(); // Create position button
                gridButtons[row][col] = button; // Add button to grid
                final int finalRow = row;
                final int finalCol = col;
                button.addActionListener(new ActionListener() { // Onclick action
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String target = convertToBattleshipCoordinate(finalRow, finalCol);
                        controller.handleShot(target); // Shot to position
                        button.setEnabled(false); // Disables button while game handles shot
                    }
                });
                boardPanel.add(button); // Add button to board
            }
            // Add righ margin to balance labels
            JLabel marginLabel = new JLabel("");
            marginLabel.setHorizontalAlignment(SwingConstants.CENTER);
            marginLabel.setFont(labelFont); // Set font
            marginLabel.setForeground(fontColor); // Set font color
            marginLabel.setBackground(backgroundColor);
            marginLabel.setOpaque(true);
            boardPanel.add(marginLabel);
        }
    }

    // Diabsles all buttons on the grid
    private void disableAllButtons() {
        int gridSize = model.board.gridSize;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                gridButtons[row][col].setEnabled(false);
            }
        }
    }
    
    // Changes button position to game input coordinates (0,0) -> 'A1'
    private String convertToBattleshipCoordinate(int row, int col) {
        char colChar = (char) ('A' + col);
        return colChar + String.valueOf(row + 1);
    }
    
    // Get Frame of the game
    public JFrame getFrame() {
        return frame;
    }
}