package it.polimi.ingsw.client.view;


import it.polimi.ingsw.client.model.PlayMessageController;
import it.polimi.ingsw.client.observer.ViewObserver;
import it.polimi.ingsw.commons.enums.Wizard;

import java.util.List;
import java.util.function.Consumer;

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
     * @param playMessageController
     */
    void setStatePrinter(PlayMessageController playMessageController);

    /**
     * After socket connection the user is asked to chose his username to complete the in game registration.
     */
    void askNickname();

    /**
     * When a client Finds out to be a main player client, will show to the user this screen "asking to set the game".
     * That is to say ask the number of players and the variant.
     */
    // Incorporates old ask player number
    void askPlaySettings();

    /**
     * Client shows this to all users to set customizations.
     */
    void askPlayCustomization();

    /**
     * Used to show the main player to the user.
     * @param mainPlayerName the name of the main player.
     */
    void showMainPlayerName(String mainPlayerName);

    /**
     * Used to show the result of the login to the player, shown after asking gor the username.
     * The Confirmation message contains the three arguments:
     * @param nicknameAccepted if the nickname was accepted;
     * @param connectionSuccessful it fhe connection was successfully established;
     * @param nickname the accepted nickname.
     */
    void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname);

    /**
     * One kind of messages that can be sent by the server are of the GENERIC type; this method shows their's content.
     * @param genericMessage message's content;
     */
    void showGenericMessage(String genericMessage);

    /**
     * Shows to the user if the client has been disconnected from the server.
     * @param nicknameDisconnected
     * @param text the message of the disconnection.
     */
    void showDisconnectionMessage(String nicknameDisconnected, String text);

    /**
     * Shows the disconnection of an other player.
     * @param nicknameDisconnected the nickname who disconnected;
     * @param text the message if the disconnection.
     */
    void showOtherDisconnectionMessage(String nicknameDisconnected, String text);

    /**
     *
     * @param error
     */
    void showErrorAndExit(String error);

    /**
     * Called every time a player joins, shows the complete lobby iof players.
     * @param nicknameList the list of players;
     * @param numPlayers the max number of players who should be connected.
     */
    void showLobby(List<String> nicknameList, int numPlayers);

    /**
     * Shows the content of the error message sent by the server.
     * @param errorMessage the content of the ERROR type message.
     */
    void showError(String errorMessage);

    /**
     * Shows if the player's preference on the wizard's choice was accepted or discarded.
     */
    void showWizard();

    /**
     * Shows which wizards are available to be chosen.
     */
    void showAvailableWizards();

    /**
     * Shows to the user how many players will this game gave, as for what the main player has chosen.
     * @param maxPlayers the max number of players.
     */
    void showChosenNumOfPlayers(int maxPlayers);

    /**
     * Shows if the play is in the expert variant or in the normal one.
     * @param expertStatus true if the play is in expert variant, false else.
     */
    void showExpert(boolean expertStatus);

    /**
     * Shows the content of the end game play message received from the server.
     */
    void showEndGame(String winnerName);

    /**
     * After having changed the game status in the Play message class, this method is called to print the actual state of the game.
     */
    void update();
}
