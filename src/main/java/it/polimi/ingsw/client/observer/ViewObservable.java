package it.polimi.ingsw.client.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Custom observable class that can be observed by implementing the {@link ViewObserver} interface and registering as listener.
 */
public abstract class ViewObservable {

    protected final List<ViewObserver> observers = new ArrayList<>();

    /**
     * Add an observer
     *
     * @param obs observer to be added
     */
    public void addObserver(ViewObserver obs) {
        observers.add(obs);
    }

    /**
     * Add a list of observer
     *
     * @param observerList list of observer to be added
     */
    public void addAllObservers(List<ViewObserver> observerList) {
        observers.addAll(observerList);
    }

    /**
     * Remove an observer
     *
     * @param obs observer to be removed
     */
    public void removeObserver(ViewObserver obs) {
        observers.remove(obs);
    }

    /**
     * Remove a list of observer
     *
     * @param observerList list of observer to be removed
     */
    public void removeAllObservers(List<ViewObserver> observerList) {
        observers.removeAll(observerList);
    }

    /**
     * Notifies all the current observers through the lambda argument
     *
     * @param lambda lambda to be called on the observers
     */
    protected void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver observer : observers) {
            lambda.accept(observer);
        }
    }
}
