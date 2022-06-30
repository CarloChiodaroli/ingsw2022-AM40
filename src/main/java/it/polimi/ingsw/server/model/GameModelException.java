package it.polimi.ingsw.server.model;

/**
 * every kind of exception gets catched {@link GameModel} and forwarded like this exception.
 */
public class GameModelException extends RuntimeException {

    public GameModelException(String message) {
        super(message);
    }
}
