package it.polimi.ingsw.view;


import java.util.List;

/**
 * Defines a generic view to be implemented by each view type
 */
public interface View {

    void askNickname();

    void askPlayersNumber();

    void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname);

    void showGenericMessage(String genericMessage);

    void showDisconnectionMessage(String nicknameDisconnected, String text);

    void showErrorAndExit(String error);

    void showLobby(List<String> nicknameList, int numPlayers);

}
