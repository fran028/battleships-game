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
public class View implements Observer {

    private static final Dimension PANEL_SIZE = new Dimension(400, 400);
    private Model model;
    private Controller controller;
    private JFrame frame;
    private JPanel boardPanel; // Panel for the buttons
    private JButton[][] gridButtons;
    private JPanel titlePanel; // New panel for status/message label
    private JLabel titleLabel;
    private JLabel messageLabel; // Label for messages
    private JPanel statusPanel; // New panel for status/message label
    
    private Color cellColorWater = Color.decode("#4D79AB");
    private Color cellColorHit = Color.decode("#AA644D");
    private Color cellColorMiss = Color.decode("#F5E9BF"); 
    private Color cellColorShip = Color.decode("#969696");  
    private Color winColor = Color.decode("#788374");  
    private Color backgroundColor = Color.decode("#2C2C3F"); // Default background color
    private Color fontColor = Color.decode("#D5E8FF");
    private Font labelFont = new Font("Arial", Font.BOLD, 14); 
    
    private JLabel shotsFiredLabel = new JLabel("Shots Fired: 0");
    private JLabel sunkShipsLabel = new JLabel("Ships Sunk: 0"); 
    
    // New Panel for Shots/Sunk Stats
    private JPanel statsPanel;

    public View(Model model, Controller controller) {
        this.model = model;
        this.model.addObserver(this);
        this.controller = controller;
        createControls();
        controller.setView(this);
        update(model, null);
    }

    private void createControls() {
        frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(backgroundColor);   
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(backgroundColor);
        
        createTitlePanel(); 
        topPanel.add(titlePanel, BorderLayout.PAGE_START); // Add stats panel below title 
        
        // Stats Panel (Shots Fired, Ships Sunk)
        createStatsPanel();
        topPanel.add(statsPanel, BorderLayout.CENTER); // Add stats panel below title 
        
        contentPane.add(topPanel, BorderLayout.PAGE_START);
        
       
        // Status Panel (Messages)
        createStatusPanel();
        contentPane.add(statusPanel, BorderLayout.SOUTH); 

         // Board Panel (Grid of Buttons) - CREATE THIS FIRST
        createBoardPanel();
        contentPane.add(boardPanel, BorderLayout.CENTER); 

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    

    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            updateBoardDisplay(arg); // Pass the arg to updateBoardDisplay
            updateStatsPanel();
        }
    }
    
    private void updateStatsPanel() {
        shotsFiredLabel.setText("Shots Fired: " + model.getShotsFiredCount());
        sunkShipsLabel.setText("Ships Sunk: " + model.getSunkShipsCount());
    }

    private void updateBoardDisplay(Object arg) {
        int gridSize = this.model.board.gridSize;
        String[][] grid = model.board.grid;
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
                        button.setEnabled(false);
                        break;
                    case "h": // Hit
                        button.setText("");
                        button.setBackground(cellColorHit); // Red for hit
                        button.setEnabled(false);
                        break;
                }
            }
        }

        if (arg instanceof ShotResult) { // Check if there's a ShotResult
            ShotResult result = (ShotResult) arg;
            if (model.isGameOver()) {
                messageLabel.setForeground(fontColor); 
                messageLabel.setText("You won! All ships sunk in " + model.getShotsFiredCount() + " shots.");
                disableAllButtons(); // Disable all buttons at game over
            } else if (result == ShotResult.SUNK) {
                messageLabel.setText("You sunk a ship!");
            }  else {
                messageLabel.setText("Shot " + result);
            }
        } else if (model.isGameOver()) {
            messageLabel.setText("You won! All ships sunk in " + model.getShotsFiredCount() + " shots.");
            disableAllButtons(); // Disable all buttons at game over
        }
    }
    
    private void createTitlePanel(){
        titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center align, some spacing
        titlePanel.setBackground(backgroundColor);  
        titleLabel = new JLabel("BATTLESHIPS");
        titleLabel.setFont(labelFont);
        titleLabel.setForeground(fontColor);  
        titlePanel.add(titleLabel); 
    }
    
    private void createStatsPanel() {
        statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Center align, some spacing
        statsPanel.setBackground(backgroundColor); 
        shotsFiredLabel.setFont(labelFont);
        shotsFiredLabel.setForeground(fontColor);
        sunkShipsLabel.setFont(labelFont);
        sunkShipsLabel.setForeground(fontColor); 
        statsPanel.add(shotsFiredLabel);
        statsPanel.add(sunkShipsLabel);
    }
    
    private void createStatusPanel() {
        statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        statusPanel.setBackground(backgroundColor);
        statusPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        messageLabel = new JLabel("Welcome to Battleship!");
        messageLabel.setFont(labelFont);
        messageLabel.setForeground(fontColor);
        messageLabel.setBackground(backgroundColor);
        messageLabel.setOpaque(true);
        statusPanel.add(messageLabel);
    }
    
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
            JLabel colLabel = new JLabel(String.valueOf((char) ('A' + i)));
            colLabel.setHorizontalAlignment(SwingConstants.CENTER);
            colLabel.setFont(labelFont); // Set font
            colLabel.setForeground(fontColor); // Set font color 
            colLabel.setBackground(backgroundColor);
            colLabel.setOpaque(true);
            boardPanel.add(colLabel);
        }
        boardPanel.add(new JLabel("")); 

        for (int row = 0; row < gridSize; row++) {
            // Row labels (1, 2, 3...)
            JLabel rowLabel = new JLabel(String.valueOf(row + 1));
            rowLabel.setHorizontalAlignment(SwingConstants.CENTER);
            rowLabel.setFont(labelFont); // Set font
            rowLabel.setForeground(fontColor); // Set font color
            rowLabel.setBackground(backgroundColor);
            rowLabel.setOpaque(true);
            boardPanel.add(rowLabel);
            for (int col = 0; col < gridSize; col++) {
                JButton button = new JButton();
                gridButtons[row][col] = button;
                final int finalRow = row;
                final int finalCol = col;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String target = convertToBattleshipCoordinate(finalRow, finalCol);
                        controller.handleShot(target);
                        button.setEnabled(false);
                    }
                });
                boardPanel.add(button);
            }
            JLabel marginLabel = new JLabel("");
            marginLabel.setHorizontalAlignment(SwingConstants.CENTER);
            marginLabel.setFont(labelFont); // Set font
            marginLabel.setForeground(fontColor); // Set font color
            marginLabel.setBackground(backgroundColor);
            marginLabel.setOpaque(true);
            boardPanel.add(marginLabel);
        }
    }

    private void disableAllButtons() {
        int gridSize = model.board.gridSize;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                gridButtons[row][col].setEnabled(false);
            }
        }
    }

    private String convertToBattleshipCoordinate(int row, int col) {
        char colChar = (char) ('A' + col);
        return colChar + String.valueOf(row + 1);
    }

    public JFrame getFrame() {
        return frame;
    }
}