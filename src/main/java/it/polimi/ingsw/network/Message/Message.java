package it.polimi.ingsw.network.Message;

import java.io.Serializable;

public abstract class Message {

    private final String playername;
    private final MessageType messageType;

    Message(String playername, MessageType messageType) {
        this.playername = playername;
        this.messageType = messageType;
    }

    public String getPlayerName() {
        return playername;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "playername=" + playername +
                ", messageType=" + messageType +
                '}';
    }
}
