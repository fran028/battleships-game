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
public class Observable {
    private boolean changed = false;
    private Vector<Observer> obs;
 
    public Observable() {
        obs = new Vector<>();
    }
 
    public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }
 
    public synchronized void deleteObserver(Observer o) {
        obs.removeElement(o);
    } 
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
 
    public void notifyObservers() {
        notifyObservers(null);
    }
 
    protected synchronized void setChanged() {
        changed = true;
    }
 
    protected synchronized void clearChanged() {
        changed = false;
    }
 
    public synchronized boolean hasChanged() {
        return changed;
    }
 
    public synchronized int countObservers() {
        return obs.size();
    }
}
