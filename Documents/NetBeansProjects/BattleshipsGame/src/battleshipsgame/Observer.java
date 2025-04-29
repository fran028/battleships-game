/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package battleshipsgame;

/**
 *
 * @author franc
 */
// Class that notifies other classes of changes in the Observable
public interface Observer {
    void update(Observable o, Object arg);
}
