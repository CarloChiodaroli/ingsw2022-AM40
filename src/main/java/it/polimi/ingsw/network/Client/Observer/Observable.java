package it.polimi.ingsw.network.Client.Observer;

import it.polimi.ingsw.network.Message.Message;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private final List<Observer> observers=new ArrayList<>();

    public void addObserver(Observer observer)
    {
        observers.add(observer);
    }

    public void removeObserver(Observer observer)
    {
        observers.remove(observer);
    }

    public void notifyObserver(Message message)
    {
        for (Observer observer:observers)
        {
            observer.update(message);
        }
    }
}
