package it.polimi.ingsw.commons.message;

/**
 * Message used to keep the connection alive.
 */
public class PingMessage extends Message {

    /**
     * Constructor
     *
     * @param sender sender
     */
    public PingMessage(String sender) {
        super(sender, MessageType.PING);
        super.message();
    }
}
