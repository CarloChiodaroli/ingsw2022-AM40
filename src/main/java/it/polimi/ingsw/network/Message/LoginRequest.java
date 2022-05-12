package it.polimi.ingsw.network.Message;

/**
 * Message used by the client to request a login to the server.
 */
public class LoginRequest extends Message {

    public LoginRequest(String nickname) {
        super(nickname, MessageType.LOGIN_REQUEST);
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "senderName=" + getSenderName() +
                '}';
    }
}
