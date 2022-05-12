package it.polimi.ingsw.network.Message;

import java.io.Serializable;

public abstract class Message {

    private final String playerName;
    private final MessageType messageType;
    private boolean written;

    Message(String playerName, MessageType messageType) {
        this.playerName = playerName;
        this.messageType = messageType;
        written = false;
    }

    public String getPlayerName() {
        return playerName;
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

    @Override
    public String toString() {
        return "Message{" +
                "playerName=" + playerName +
                ", messageType=" + messageType +
                '}';
    }
}
