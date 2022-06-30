package it.polimi.ingsw.client.view;


import it.polimi.ingsw.client.model.PlayMessageController;

import java.util.List;

/**
 * Defines a generic view to be implemented by each view type
 * There are two types of players:
 * the main player, the first to connect to the game, which defines the games settings,
 * and the normal player who defines only his customization
 */
public interface View {

    /**
     * The view to update herself relies on a PlayState class given by the playMessageController, which stores the actual
     * known to the client state of the game.
     *
     * @param playMessageController
     */
    void setStatePrinter(PlayMessageController playMessageController);

    /**
     * After socket connection the user is asked to chose his username to complete the in game registration.
     */
    void askNickname();

    /**
     * When a client Finds out to be a main player client, will show to the user this screen "asking to set the game".
     * That is to say ask the number of players and the mode
     */
    // Incorporates old ask player number
    void askPlaySettings();

    /**
     * Client shows this to all users to set customizations.
     */
    void askPlayCustomization();

    /**
     * Used to show the main player to the user
     *
     * @param mainPlayerName name of the main player
     */
    void showMainPlayerName(String mainPlayerName);

    /**
     * Used to show the result of the login to the player, shown after asking gor the username
     *
     * @param connectionCompleted if the nickname has been completely established
     * @param connectionStarted   true if connection has been successfully started
     * @param nickname            accepted nickname
     */
    void showLoginResult(boolean connectionCompleted, boolean connectionStarted, String nickname);

    /**
     * One kind of messages that can be sent by the server are of the GENERIC type
     *
     * @param genericMessage message's content
     */
    void showGenericMessage(String genericMessage);

    /**
     * Shows to the user if the client has been disconnected from the server
     *
     * @param nicknameDisconnected name disconnected
     * @param text                 message of the disconnection
     */
    void showDisconnectionMessage(String nicknameDisconnected, String text);

    /**
     * Shows the disconnection of another player
     *
     * @param nicknameDisconnected nickname who disconnected
     * @param text                 message of the disconnection
     */
    void showOtherDisconnectionMessage(String nicknameDisconnected, String text);

    /**
     * Error and exit
     *
     * @param error error
     */
    void showErrorAndExit(String error);

    /**
     * Called every time a player joins
     *
     * @param nicknameList list of players
     * @param numPlayers   max number of players who should be connected
     */
    void showLobby(List<String> nicknameList, int numPlayers);

    /**
     * Shows the content of the error message sent by the server
     *
     * @param errorMessage content of the ERROR message
     */
    void showError(String errorMessage);

    /**
     * Shows if the player's preference on the wizard's choice was accepted or discarded
     */
    void showWizard();

    /**
     * Shows which wizards are available to be chosen
     */
    void showAvailableWizards();

    /**
     * Shows to the user how many players will this game gave, as for what the main player has chosen
     *
     * @param maxPlayers max number of players
     */
    void showChosenNumOfPlayers(int maxPlayers);

    /**
     * Shows if the play is in the expert variant or in the normal one
     *
     * @param expertStatus true if the play is in expert variant, false else
     */
    void showExpert(boolean expertStatus);

    /**
     * Shows the content of the end game play message received from the server
     */
    void showEndGame(String winnerName);

    /**
     * After having changed the game status in the Play message class, this method is called to print the actual state of the game
     */
    void update();
}
