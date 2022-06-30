package it.polimi.ingsw.client.observer;

import it.polimi.ingsw.commons.enums.Wizard;

import java.util.Map;

public interface ViewObserver {

    /**
     * Create a new connection to the server with the updated info
     *
     * @param serverInfo a map of server address and server port
     */
    void onUpdateServerInfo(Map<String, String> serverInfo);

    /**
     * Sends a message to the server with the updated nickname
     *
     * @param nickname nickname to be sent
     */
    void onUpdateNickname(String nickname);

    /**
     * Sends a message to the server with the player number chosen by the user
     *
     * @param playersNumber number of players
     */
    void onUpdatePlayersNumber(int playersNumber);

    /**
     * Handles a disconnection wanted by the user
     */
    void onDisconnection();

    /**
     * Sends a message to the server with the wizard chosen by the user
     *
     * @param wizard chosen wizard
     */
    void onUpdateWizard(Wizard wizard);

    /**
     * Sends a message to the server with the game is started
     */
    void onUpdateStart();

    /**
     * Sends a message to the server with the game mode
     *
     * @param choice true if is expert mode
     */
    void onUpdateExpert(boolean choice);
}
