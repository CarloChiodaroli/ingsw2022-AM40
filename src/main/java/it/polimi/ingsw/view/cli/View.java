package it.polimi.ingsw.view.cli;

import java.util.List;

public interface View {

    void askPlayerName();

    void showLoginResult(boolean playerNameAccepted, boolean connectionSuccessful, String playerName);

    void showDisconnectionMessage(String playerName, String text);

    void askFirstPlayer(List<String> nicknameList);

    void showMatchInfo(List<String> players, String activePlayer);


}
