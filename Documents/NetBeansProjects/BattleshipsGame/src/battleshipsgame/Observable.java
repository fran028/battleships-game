/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package battleshipsgame;
import java.util.Vector; 
/**
 *
 * @author franc
 */

// Class responsible to manage observers implemented in the game. 
// It maintains a list of observers and notifies them of changes.
public class Observable {
    private boolean changed = false; // To indicate if the object has changed state
    private Vector<Observer> obs; // List of observers
    
    // Initialize the list of observers
    public Observable() {
        obs = new Vector<>();
    }
    
    // Adds an observer to the list of observers. 
    public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) { // Prevents adding duplicate observers.
            obs.addElement(o);
        }
    }
    
    // Removes an observer from the list.
    public synchronized void deleteObserver(Observer o) {
        obs.removeElement(o);
    } 
    
    // Notifies all observers of a change in the Observable object's state.
    public void notifyObservers(Object arg) { 
        Observer[] arrLocal;

        synchronized (this) { 
            if (!changed)
                return;
            arrLocal = new Observer[obs.size()];
            obs.copyInto(arrLocal);
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            arrLocal[i].update(this, arg);
    }
    
    // Overload of notifyObservers() without an argument.
    public void notifyObservers() {
        notifyObservers(null);
    }
    
    // Marks this Observable object as having been changed.
    protected synchronized void setChanged() {
        changed = true;
    }
    
    // Clears the 'changed' flag, indicating that the object is no longer considered changed.
    protected synchronized void clearChanged() {
        changed = false;
    }
    
    // Returns true if this object has changed since the last call to notifyObservers().
    public synchronized boolean hasChanged() {
        return changed;
    }
    
    // Returns the number of observers of this Observable object.
    public synchronized int countObservers() {
        return obs.size();
    }
}
