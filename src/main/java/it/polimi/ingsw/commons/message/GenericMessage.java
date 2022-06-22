package it.polimi.ingsw.commons.message;


/**
 * Simple type of message that sends messages as a String.
 */
public class GenericMessage extends Message {

    private final String message;


    public GenericMessage(String message) {
        super("server", MessageType.GENERIC);
        this.message = message;
        super.message();
    }


    public String getMessage() {
        controlWritten();
        return message;
    }

    @Override
    public String toString() {
        return "GenericMessage{" +
                "senderName=" + getSenderName() +
                ", message=" + message +
                '}';
    }
}
