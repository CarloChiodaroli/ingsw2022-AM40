package it.polimi.ingsw.commons.message;

/**
 * Class implements all messages used in the login phase of the communication between client and server,
 * that that is from establishing connection and sending the name.
 */
public class LoginMessage extends Message {

    private final boolean connectionCompleted;
    private final boolean connectionStarted;
    private final boolean request;

    /**
     * Used to send Requests
     *
     * @param sender the name of the sender of the request, and the name of the request
     */
    public LoginMessage(String sender) {
        super(sender, MessageType.LOGIN);
        connectionCompleted = false;
        connectionStarted = false;
        request = true;
        super.message();
    }

    /**
     * Used to send Replies
     *
     * @param sender    the name of the sender, (always should be a server)
     * @param accepted  true if the requested name is accepted, else false
     * @param connected true if the connection has been correctly established, else false
     */
    public LoginMessage(String sender, boolean accepted, boolean connected) {
        super(sender, MessageType.LOGIN);
        connectionCompleted = accepted;
        connectionStarted = connected;
        request = false;
        super.message();
    }

    /**
     * Used to read the Reply to Login message
     *
     * @return true if everything is ok, else false
     */
    public boolean readReply() {
        controlWritten();
        if (request) throw new IllegalStateException("This is a Login reply");
        return connectionCompleted && connectionStarted;
    }

    /**
     * Used to read the Request to login
     *
     * @return the name of the requester
     */
    public String readRequest() {
        controlWritten();
        if (!request) throw new IllegalStateException("This is a Login request");
        return getSenderName();
    }

    /**
     * Getter of internal parameter ConnectionSuccessful
     *
     * @return the parameter
     */
    public boolean isConnectionStarted() {
        return connectionStarted;
    }

    /**
     * Getter of internal parameter NicknameAccepted
     *
     * @return the parameter
     */
    public boolean isConnectionCompleted() {
        return connectionCompleted;
    }

    /**
     * Getter of internal parameter Request for testing purposes
     *
     * @return the parameter
     */
    public boolean isRequest() {
        return request;
    }

    @Override
    public String toString() {
        return "LoginMessage{" +
                "nicknameAccepted=" + connectionCompleted +
                ", connectionSuccessful=" + connectionStarted +
                ", request=" + request +
                '}';
    }
}
