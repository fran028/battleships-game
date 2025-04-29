/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package battleshipsgame;

/**
 *
 * @author franc
 */
// List of types of shot result
public enum ShotResult {
    HIT, // Shot hits ship "h" -> "h"
    MISS, // Shot hits water "w" -> "m"
    SUNK, // Shot sunks Ship 
    ERROR // Not a valid shot
}
