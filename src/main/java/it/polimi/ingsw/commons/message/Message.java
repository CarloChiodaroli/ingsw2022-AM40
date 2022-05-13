package it.polimi.ingsw.commons.message;

public class Message {

    private final String senderName;
    private final MessageType messageType;
    private boolean written;

    Message(String sender, MessageType messageType) {
        this.senderName = sender;
        this.messageType = messageType;
        written = false;
    }

    public String getSenderName() {
        return senderName;
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
                "senderName=" + senderName +
                ", messageType=" + messageType +
                '}';
    }
}
