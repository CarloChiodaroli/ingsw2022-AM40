package it.polimi.ingsw.commons.message;

/**
 * Message to notify an error to the user.
 */
public class ErrorMessage extends Message {

    private final String error;

    /**
     * Constructor
     */
    public ErrorMessage(String nickname, String error) {
        super(nickname, MessageType.ERROR);
        this.error = error;
        super.message();
    }

    /**
     * Getter
     */
    public String getError() {
        controlWritten();
        return error;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "senderName=" + getSenderName() +
                ", error=" + error +
                '}';
    }
}
