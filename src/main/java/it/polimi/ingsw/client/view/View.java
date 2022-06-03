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

    void setStatePrinter(PlayMessageController playMessageController);

    void askNickname();

    /**
     * When a client Finds out to be a main player client, will show to the user this screen "asking to set the game".
     * That is to say ask the number of players and the variant.
     */
    void askPlaySettings();

    /**
     * Client shows this to all users to set customizations
     */
    void askPlayCustomization();

    @Deprecated
    /*
    There are two types of players: main Player and normal Player
    this "Ask players numers" will be incorporated in "ask play settings"
     */
    void askPlayersNumber();

    void showMainPlayerName(String mainPlayerName);

    @Deprecated
    void showActualState();

    void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname);

    void showGenericMessage(String genericMessage);

    void showDisconnectionMessage(String nicknameDisconnected, String text);

    void showOtherDisconnectionMessage(String nicknameDisconnected, String text);

    void showErrorAndExit(String error);

    void showLobby(List<String> nicknameList, int numPlayers);

    void showError(String errorMessage);

    void showWizard();

    void update();

}
