package it.polimi.ingsw.commons.message;

/**
 * Exception thrown by those classes that manage messages and cannot run what they received.
 */
public class IllegalMessageException extends RuntimeException {

    public IllegalMessageException() {
        super();
    }

    public IllegalMessageException(String message) {
        super(message);
    }
}
