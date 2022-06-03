package it.polimi.ingsw.client.observer;

import it.polimi.ingsw.commons.message.Message;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    public void removeObserver(Observer obs) {
        observers.remove(obs);
    }

    protected void notifyObserver(Message message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
