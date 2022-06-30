package it.polimi.ingsw.commons.message;

/**
 * All {@link LobbyMessage} readers need to implement this interface.
 * This interface lists all commands sent from server to client and viceversa in the Lobby part of the dialogue.
 */
public interface LobbyMessageReader {

    /**
     * Handles Lobby start game messages
     *
     * @param message the received message to handle
     */
    void startGame(LobbyMessage message);

    /**
     * Handles Lobby players list message
     *
     * @param message the received message to handle
     */
    void lobbyPlayers(LobbyMessage message);

    /**
     * Handles Lobby main player message
     *
     * @param message the received message to handle
     */
    void mainPlayer(LobbyMessage message);

    /**
     * Handles Lobby wizard choice message
     *
     * @param message the received message to handle
     */
    void wizard(LobbyMessage message);

    /**
     * Handles Lobby wizard list message
     *
     * @param message the received message to handle
     */
    void wizardList(LobbyMessage message);

    /**
     * Handles Lobby num of players message
     *
     * @param message the received message to handle
     */
    void numOfPlayers(LobbyMessage message);

    /**
     * Handles Lobby player disconnection message
     *
     * @param message the received message to handle
     */
    void disconnection(LobbyMessage message);

    /**
     * Handles Lobby ruleset selection and notification messages
     *
     * @param message the received message to handle
     */
    void expert(LobbyMessage message);
}
