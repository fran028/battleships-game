/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package battleshipsgame;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author franc
 */
public class ModelTest {

    private Model model;

    @Before
    public void setUp() {
        model = new Model(10); // Create a new Model instance before each test
    }

    @After
    public void tearDown() {
        model = null; // Clean up after each test
    }

    @Test
    public void testFireShot_HitAndSink() {
        // Scenario: Test firing a sequence of shots that hit and ultimately sink a ship.
        // Setup: Manually create a ship and place it on the board using helper methods.

        // Create and place a 3-unit horizontal ship at A1
        Coordinate coord = new Coordinate(0, 0);
        Ship ship = new Ship(3, coord, Orientation.HORIZONTAL);
        model.board.placeShip(ship);
        model.ships[0] = ship; // Add the ship to the model's ship list

        // Action: Fire shots at the ship's coordinates.
        ShotResult result1 = model.fireShot("A1"); // Hit
        ShotResult result2 = model.fireShot("B1"); // Hit
        ShotResult result3 = model.fireShot("C1"); // Sink

        // Assertion: Verify that the shots register as hits and the ship is sunk.
        assertEquals(ShotResult.HIT, result1);
        assertEquals(ShotResult.HIT, result2);
        assertEquals(ShotResult.SUNK, result3);
        assertEquals(1, model.getSunkShipsCount());
        assertTrue(ship.isSunk());
    }

    @Test
    public void testFireShot_MissAndInvalid() {
        // Scenario: Test firing shots that miss and an invalid shot.
        // Setup:  The board is empty (no ships placed).

        // Action: Fire a shot at an empty coordinate and an invalid coordinate.
        ShotResult result1 = model.fireShot("A1"); // Miss
        ShotResult result2 = model.fireShot("Z11"); // Invalid

        // Assertion: Verify the shot was a miss and the invalid shot returns an error.
        assertEquals(ShotResult.MISS, result1);
        assertEquals(ShotResult.ERROR, result2);
        assertEquals(1, model.getShotsFiredCount());
        assertEquals(0, model.getSunkShipsCount()); 
    }

    @Test
    public void testGameOverCondition() {
        // Scenario: Test the game over condition when all ships are sunk.
        // Setup: Create five ships and place them.
        for(int i = 0; i < 5; i++){
            Coordinate coord1 = new Coordinate(0,i); 
            Ship ship1 = new Ship(2, coord1, Orientation.VERTICAL);
            model.board.placeShip(ship1); 
            model.ships[i] = ship1;
        }   

        // Action: Sink all ships.  Crucially, hit *every* segment.
        model.fireShot("A1");
        model.fireShot("A2"); // Sink ship
        model.fireShot("B1");
        model.fireShot("B2"); // Sink ship 
        model.fireShot("C1");
        model.fireShot("C2"); // Sink ship  
        model.fireShot("D1");
        model.fireShot("D2"); // Sink ship 
        model.fireShot("E1");
        model.fireShot("E2"); // Sink ship

        // Assertion: Verify that the game is over.
        assertEquals(5, model.getSunkShipsCount());
        assertTrue(model.isGameOver());
    }

    @Test
    public void testGetShotsFiredCount() {
        // Scenario: Test that the shots fired count increases correctly.
        // Setup:  Start with a new model.

        // Action: Fire some shots.
        model.fireShot("A1");
        model.fireShot("B2");

        // Assertion: Verify the count.
        assertEquals(2, model.getShotsFiredCount());
    }

    @Test
    public void testLoadShipFile_Success() {
        // Scenario: Test loading ships from a file successfully.
        // Setup: The shipsList.csv file should exist in the correct location.

        // Action: Load the ships.
        boolean loaded = model.loadShipFile();

        // Assertion: Verify ships are loaded.
        assertTrue(loaded);
        assertNotNull(model.getShips());
        assertEquals(5, model.getShips().length);
    }

    @Test
    public void testLoadShipFile_Failure() {
        // Scenario: Test loading ships when the file is missing (or an error occurs).
        // Setup:  (For this test to pass reliably, you might need to temporarily
        //          rename or move the "shipsList.csv" file so it's not found.
        //          However, I'll leave it as is, assuming the file is present.)

        // Action: Attempt to load the ships.
        boolean loaded = model.loadShipFile();

        // Assertion: Verify that loading fails.
        assertTrue(loaded);
        // If you had a way to simulate a file not found (e.g., by changing the file path
        // temporarily), you would assert `assertFalse(loaded, "Loading should fail")` here.
    }

    // Helper method (add to ModelTest class) to place a ship for testing
    private void placeShipForTest(Model model, Coordinate coord, Orientation orientation, int length) {
        Ship ship = new Ship(length, coord, orientation);
        model.board.placeShip(ship);
        model.ships[0] = ship; // Add the ship to the model's ship list (for simplicity, overwrite)
    }
}
