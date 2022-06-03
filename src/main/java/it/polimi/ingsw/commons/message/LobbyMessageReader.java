package it.polimi.ingsw.commons.message;

public interface LobbyMessageReader {

    void startGame(LobbyMessage message);

    void lobbyPlayers(LobbyMessage message);

    void mainPlayer(LobbyMessage message);

    void wizard(LobbyMessage message);

    void numOfPlayers(LobbyMessage message);

    void disconnection(LobbyMessage message);

    //void expert(LobbyMessage message);
}
