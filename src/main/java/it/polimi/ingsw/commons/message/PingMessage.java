package it.polimi.ingsw.commons.message;

/**
 * Message used to keep the connection alive.
 */
public class PingMessage extends Message {

    public PingMessage(String sender) {
        super(sender, MessageType.PING);
    }
}
