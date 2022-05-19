package it.polimi.ingsw.commons.message;

public class Message {

    private final String senderName;
    private final MessageType messageType;
    private boolean written;

    /**
     * Constructor
     * @param sender the name of who sends the message
     * @param messageType The type of the message
     */
    Message(String sender, MessageType messageType) {
        this.senderName = sender;
        this.messageType = messageType;
        written = false;
    }

    /**
     * Getter of the name of the sender
     * @return the name
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * Getter of the type of the message
     * @return the type
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Needed to set the message as written
     */
    protected void message(){
        written = true;
    }

    /**
     * Controls if the message has been written
     * @throws IllegalStateException if it's not written
     */
    protected void controlWritten() throws IllegalStateException{
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
