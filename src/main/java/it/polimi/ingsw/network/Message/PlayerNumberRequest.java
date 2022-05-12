package it.polimi.ingsw.network.Message;


/**
 * Message used to ask the client the maximum number of players of the game.
 */
public class PlayerNumberRequest extends Message {

    public PlayerNumberRequest() {
        super("server", MessageType.PLAYERNUMBER_REQUEST);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}



