package it.polimi.ingsw.view.cli;

public interface View {

    void askPlayerName();

    void showLoginResult(boolean playerNameAccepted, boolean connectionSuccessful, String playerName);

    void showDisconnectionMessage(String playerName, String text);
}
