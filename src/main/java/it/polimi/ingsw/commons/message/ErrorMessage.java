package it.polimi.ingsw.commons.message;

/**
 * Message to notify an error to the user.
 */
public class ErrorMessage extends Message {

    private final String error;

    public ErrorMessage(String nickname, String error) {
        super(nickname, MessageType.ERROR);
        this.error = error;
    }

    public String getError() {
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
