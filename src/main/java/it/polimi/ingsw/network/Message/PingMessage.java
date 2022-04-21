package it.polimi.ingsw.network.Message;

public class PingMessage extends Message {


    public PingMessage() {
        super(null, MessageType.PING);
    }

}