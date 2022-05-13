package it.polimi.ingsw.commons.message;



public class GenericMessage extends Message {

    private final String message;


    public GenericMessage(String message) {
        super("server", MessageType.GENERIC);
        this.message = message;
    }


    public String getMessage() {
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
