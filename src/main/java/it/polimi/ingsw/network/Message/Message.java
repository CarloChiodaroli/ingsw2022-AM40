package it.polimi.ingsw.network.Message;

import java.io.Serializable;

public abstract class Message implements Serializable {

    private final String playername;
    private final MessageType messageType;
    private boolean written;

    Message(String playername, MessageType messageType) {
        this.playername = playername;
        this.messageType = messageType;
        written = false;
    }

    public String getPlayerName() {
        return playername;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    protected void message(){
        written = true;
    }

    protected void controlWritten(){
        if(!written) throw new IllegalStateException("Message not written");
    }

    protected void controlNotWritten(){
        if(written) throw new IllegalStateException("Message already written");
    }

    @Override
    public String toString() {
        return "Message{" +
                "playername=" + playername +
                ", messageType=" + messageType +
                '}';
    }
}
