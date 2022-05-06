package it.polimi.ingsw.network.Message;


/**
 * Message used to confirm or discard a login request of a client.
 */
public class LoginReply extends Message {

    private final boolean playerNameAccepted;
    private final boolean connectionSuccessful;

    public LoginReply(boolean playerNameAccepted, boolean connectionSuccessful) {
        super(null, MessageType.LOGIN_REPLY);
        this.playerNameAccepted = playerNameAccepted;
        this.connectionSuccessful = connectionSuccessful;
    }

    public boolean isPlayerNameAccepted() {
        return playerNameAccepted;
    }

    public boolean isConnectionSuccessful() {
        return connectionSuccessful;
    }

    @Override
    public String toString() {
        return "LoginReply{" +
                "nickname=" + getPlayerName() +
                ", nicknameAccepted=" + playerNameAccepted +
                ", connectionSuccessful=" + connectionSuccessful +
                '}';
    }
}