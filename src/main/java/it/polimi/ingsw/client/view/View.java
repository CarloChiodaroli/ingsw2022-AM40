package it.polimi.ingsw.client.view;


import it.polimi.ingsw.client.model.PlayMessageController;

import java.util.List;

/**
 * Defines a generic view to be implemented by each view type
 */
public interface View {

    void setStatePrinter(PlayMessageController playMessageController);

    void askNickname();

    void askPlayersNumber();

    void showMainPlayerName(String mainPlayerName);

    void showActualState();

    void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname);

    void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful);

    void showGenericMessage(String genericMessage);

    void showDisconnectionMessage(String nicknameDisconnected, String text);

    void showOtherDisconnectionMessage(String nicknameDisconnected, String text);

    void showErrorAndExit(String error);

    void showLobby(List<String> nicknameList, int numPlayers);

    void showError(String errorMessage);

    void update();

}
