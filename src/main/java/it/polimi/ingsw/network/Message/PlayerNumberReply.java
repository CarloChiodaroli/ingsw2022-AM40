package it.polimi.ingsw.network.Message;

/**
 * Message used to send to the server the number of players picked by the client.
 */
public class PlayerNumberReply extends Message {

    private final int playerNumber;

    public PlayerNumberReply(String nickname, int playerNumber) {
        super(nickname, MessageType.PLAYERNUMBER_REPLY);
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    @Override
    public String toString() {
        return "PlayerNumberReply{" +
                "nickname=" + getPlayerName() +
                ", playerNumber=" + playerNumber +
                '}';
    }
}