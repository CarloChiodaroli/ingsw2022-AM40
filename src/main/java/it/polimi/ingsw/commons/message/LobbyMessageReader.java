package it.polimi.ingsw.commons.message;

/**
 * All {@link LobbyMessage} readers need to implement this interface.
 * This interface lists all commands sent from server to client and viceversa in the Lobby part of the dialogue.
 */
public interface LobbyMessageReader {

    void startGame(LobbyMessage message);

    void lobbyPlayers(LobbyMessage message);

    void mainPlayer(LobbyMessage message);

    void wizard(LobbyMessage message);

    void wizardList(LobbyMessage message);

    void numOfPlayers(LobbyMessage message);

    void disconnection(LobbyMessage message);

    void expert(LobbyMessage message);
}
