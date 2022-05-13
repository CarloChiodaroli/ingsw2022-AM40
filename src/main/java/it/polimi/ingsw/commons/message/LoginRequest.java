package it.polimi.ingsw.commons.message;

/**
 * Message used by the client to request a login to the server.
 */
@Deprecated
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
