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
    private JLabel messageLabel; // Label for messages
    private JPanel statusPanel; // New panel for status/message label
    
    private Color cellColorWater = Color.BLUE;
    private Color cellColorHit = Color.RED;
    private Color cellColorMiss = Color.YELLOW; 
    private Color cellColorShip = Color.GREEN; 

    public View(Model model, Controller controller) {
        this.model = model;
        this.model.addObserver(this);
        this.controller = controller;
        createControls();
        controller.SetView(this);
        update(model, null);
    }

    private void createControls() {
        frame = new JFrame("Battleship");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Board Panel (Grid of Buttons) - CREATE THIS FIRST
        createBoardPanel();
        contentPane.add(boardPanel, BorderLayout.CENTER); 
        
        // Title
        JLabel titleLabel = new JLabel("Battleship", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        contentPane.add(titleLabel, BorderLayout.NORTH);

        // Status Panel (to hold messageLabel)
        statusPanel = new JPanel();
        statusPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Use FlowLayout for centering
        messageLabel = new JLabel("Welcome to Battleship!");
        statusPanel.add(messageLabel);
        contentPane.add(statusPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void createBoardPanel() {
        int gridSize = model.board.gridSize;
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(gridSize + 1, gridSize + 1)); // +1 for labels
        boardPanel.setPreferredSize(PANEL_SIZE);

        gridButtons = new JButton[gridSize][gridSize];

        // Empty top-left corner
        boardPanel.add(new JLabel(""));
        System.out.println("SwingConstants.CENTER: " + SwingConstants.CENTER);
        // Column labels (A, B, C...)
        for (int i = 0; i < gridSize; i++) {
            JLabel colLabel = new JLabel(String.valueOf((char) ('A' + i)));
            colLabel.setHorizontalAlignment(SwingConstants.CENTER);
            boardPanel.add(colLabel);
        }

        for (int row = 0; row < gridSize; row++) {
            // Row labels (1, 2, 3...)
            JLabel rowLabel = new JLabel(String.valueOf(row + 1));
            rowLabel.setHorizontalAlignment(SwingConstants.CENTER);
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
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == model) {
            updateBoardDisplay(arg); // Pass the arg to updateBoardDisplay
        }
    }

    private void updateBoardDisplay(Object arg) {
        int gridSize = model.board.gridSize;
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
                        if (model.isGameOver()) {
                            button.setText("S"); // Show ships after game over 
                            button.setBackground(cellColorShip);
                        } else {
                            button.setText(""); // Hide ships during game
                            button.setEnabled(true);
                            
                            button.setBackground(cellColorWater);
                        }
                        break;
                    case "m": // Miss
                        button.setText("M");
                        button.setBackground(cellColorMiss); // Yellow for miss
                        button.setEnabled(false);
                        break;
                    case "h": // Hit
                        button.setText("H");
                        button.setBackground(cellColorHit); // Red for hit
                        button.setEnabled(false);
                        break;
                }
            }
        }

        if (arg instanceof ShotResult) { // Check if there's a ShotResult
            ShotResult result = (ShotResult) arg;
            if (result == ShotResult.SUNK) {
                messageLabel.setText("You sunk a ship!");
            } else if (model.isGameOver()) {
                messageLabel.setText("You won! All ships sunk in " + model.getShotsFiredCount() + " shots.");
                disableAllButtons(); // Disable all buttons at game over
            } else {
                messageLabel.setText("Shot fired. Result: " + result);
            }
        } else if (model.isGameOver()) {
            messageLabel.setText("You won! All ships sunk in " + model.getShotsFiredCount() + " shots.");
            disableAllButtons(); // Disable all buttons at game over
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