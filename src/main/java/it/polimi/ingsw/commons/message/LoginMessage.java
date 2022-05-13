package it.polimi.ingsw.commons.message;

public class LoginMessage extends Message{

    private final boolean nicknameAccepted;
    private final boolean connectionSuccessful;
    private final boolean request;

    /**
     * Used to send Requests
     * @param sender the name of the sender of the request, and the name of the request
     */
    public LoginMessage(String sender){
        super(sender, MessageType.LOGIN);
        nicknameAccepted = false;
        connectionSuccessful = false;
        request = true;
        super.message();
    }

    /**
     * Used to send Replies
     * @param sender the name of the sender, (always should be a server)
     * @param accepted true if the requested name is accepted, else false
     * @param connected true if the connection has been correctly established, else false
     */
    public LoginMessage(String sender, boolean accepted, boolean connected){
        super(sender, MessageType.LOGIN);
        nicknameAccepted = accepted;
        connectionSuccessful = connected;
        request = false;
        super.message();
    }

    /**
     * Used to read the Reply to Login message
     * @return true if everything is ok, else false
     */
    public boolean readReply(){
        super.controlWritten();
        if(request) throw new IllegalStateException("This is a Login reply");
        return nicknameAccepted && connectionSuccessful;
    }

    /**
     * Used to read the Request to login
     * @return the name of the requester
     */
    public String readRequest(){
        super.controlWritten();
        if(!request) throw new IllegalStateException("This is a Login request");
        return getSenderName();
    }

    /**
     * getter of internal parameter ConnectionSuccessful
     * @return the parameter
     */
    public boolean isConnectionSuccessful() {
        return connectionSuccessful;
    }

    /**
     * getter of internal parameter NicknameAccepted
     * @return the parameter
     */
    public boolean isNicknameAccepted() {
        return nicknameAccepted;
    }

    /**
     * getter of internal parameter Request for testing purposes
     * @return the parameter
     */
    public boolean isRequest() {
        return request;
    }

    @Override
    public String toString() {
        return "LoginMessage{" +
                "nicknameAccepted=" + nicknameAccepted +
                ", connectionSuccessful=" + connectionSuccessful +
                ", request=" + request +
                '}';
    }
}
