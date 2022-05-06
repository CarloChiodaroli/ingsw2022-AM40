package it.polimi.ingsw.network.message;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Message.Message;
import it.polimi.ingsw.network.Message.MessageType;

/**
 * Message used to confirm or discard a login request of a client.
 */
public class LoginReply extends Message {

    private static final long serialVersionUID = -1423312065079102467L;
    private final boolean playerNameAccepted;
    private final boolean connectionSuccessful;

    public LoginReply(boolean nicknameAccepted, boolean connectionSuccessful) {
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
                ", nicknameAccepted=" + nicknameAccepted +
                ", connectionSuccessful=" + connectionSuccessful +
                '}';
    }
}