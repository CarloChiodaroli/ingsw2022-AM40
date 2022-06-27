package it.polimi.ingsw.client.observer;

import it.polimi.ingsw.commons.message.Message;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Add an observer
     *
     * @param obs observer to be added
     */
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    /**
     * Remove an observer
     *
     * @param obs observer to be removed
     */
    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    /**
     * Notifies all the current observers through the update method and passes to them a message
     *
     * @param message message to be passed to the observers
     */
    protected void notifyObserver(Message message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
