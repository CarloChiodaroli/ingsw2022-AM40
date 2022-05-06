package it.polimi.ingsw.network.Message;


/**
 * Message to notify a disconnection to the other players.
 */
public class DisconnectionMessage extends Message {


    private final String playerNameDisconnected;
    private final String messageStr;

    public DisconnectionMessage(String playerNameDisconnected, String messageStr) {
        super("server", MessageType.DISCONNECTION);
        this.playerNameDisconnected = playerNameDisconnected;
        this.messageStr = messageStr;
    }

    public String getPlayerNameDisconnectedDisconnected() {
        return playerNameDisconnected;
    }

    public String getMessageStr() {
        return messageStr;
    }

    @Override
    public String toString() {
        return "DisconnectionMessage{" +
                "nicknameDisconnected='" + playerNameDisconnected + '\'' +
                ", messageStr='" + messageStr + '\'' +
                '}';
    }
}