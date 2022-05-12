package it.polimi.ingsw.manuel.network.message;

/**
 * Message used to keep the connection alive.
 */
public class PingMessage extends Message {

    private static final long serialVersionUID = -7019523659587734169L;

    public PingMessage() {
        super(null, MessageType.PING);
    }
}
