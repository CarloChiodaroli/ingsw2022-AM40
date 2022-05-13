package it.polimi.ingsw.commons.message;


/**
 * Message used to ask the client the maximum number of players of the game.
 */
@Deprecated
public class PlayerNumberRequest extends Message {

    public PlayerNumberRequest() {
        super("server", MessageType.PLAYER_NUMBER_REQUEST);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}



