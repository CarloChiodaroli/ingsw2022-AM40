package it.polimi.ingsw.server.view;

import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.commons.observer.Observer;
import it.polimi.ingsw.commons.view.View;
import it.polimi.ingsw.server.network.ClientHandler;

import java.util.List;

/**
 * For the controller there is a one-to-one correspondence between client and virtual view.
 * The Game Manager thinks that all client are local, so calls this class thinking that this is the view of a singular client.
 * But what this class really does is building messages to send to the client via Socket Client Handler
 * (here known as her interface Client Handler) effectively hiding the outgoing network implementation from the manager.
 */

public class VirtualView implements View, Observer {

    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    // not so good string
    @Override
    public void askNickname() {
        clientHandler.sendMessage(new LoginMessage("Server", false, true));
    }

    public void sendMainPlayer(String mainPlayerName) {
        clientHandler.sendMessage(new LobbyMessage("Server", mainPlayerName, false));
    }

    @Override
    public void askPlayersNumber() {

    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {

    }

    @Override
    public void showError(String errorMessage) {
        clientHandler.sendMessage(new ErrorMessage("Server", errorMessage));
    }

    // not so good string
    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful) {
        clientHandler.sendMessage(new LoginMessage("Server", nicknameAccepted, connectionSuccessful));
    }

    @Override
    public void showGenericMessage(String genericMessage) {
        clientHandler.sendMessage(new GenericMessage(genericMessage));
    }

    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        clientHandler.sendMessage(new LobbyMessage("Server", nicknameDisconnected, true));
    }

    @Override
    public void showOtherDisconnectionMessage(String nicknameDisconnected, String text) {
        clientHandler.sendMessage(new LobbyMessage("Server", nicknameDisconnected, true));
    }

    @Override
    public void showErrorAndExit(String error) {
        clientHandler.sendMessage(new ErrorMessage("server", error));
    }

    @Override
    public void showLobby(List<String> nicknameList, int numPlayers) {
        clientHandler.sendMessage(new LobbyMessage("Server", nicknameList));
    }

    @Override
    public void update(Message message) {
        clientHandler.sendMessage(message);
    }
}
