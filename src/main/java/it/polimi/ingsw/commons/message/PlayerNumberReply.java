package it.polimi.ingsw.commons.message;

/**
 * Message used to send to the server the number of players picked by the client.
 */
public class PlayerNumberReply extends Message {

    private final int playerNumber;

    public PlayerNumberReply(String senderName, int playerNumber) {
        super(senderName, MessageType.PLAYERNUMBER_REPLY);
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    @Override
    public String toString() {
        return "PlayerNumberReply{" +
                "senderName=" + getSenderName() +
                ", playerNumber=" + playerNumber +
                '}';
    }
}
