package it.polimi.ingsw.server.network;

import it.polimi.ingsw.commons.enums.Wizard;
import it.polimi.ingsw.commons.message.*;

import java.util.List;

/**
 * For the Game Manager there is a one-to-one correspondence between client and virtual view.
 * This class supplies the controller an interface to send data to the controller, masking network details.
 * This is a sort of fabric method class that allows the Game manager to send Login, Lobby, Error and Generic messages to the client.
 * For other types of messages managed in {@link it.polimi.ingsw.server.controller.outer.PlayMessagesReader Play Message Reader} class
 * the {@link #sendMessage(Message) sendMessage} method is exposed in order to send them.
 */
public class VirtualView {

    private final ClientHandler clientHandler;
    private final String server;

    public VirtualView(ClientHandler clientHandler, String server) {
        this.clientHandler = clientHandler;
        this.server = server;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public void sendMainPlayer(String mainPlayerName) {
        sendMessage(new LobbyMessage(server, "mainPlayer", mainPlayerName));
    }

    public void sendMessage(Message message) {
        clientHandler.sendMessage(message);
    }

    public void sendAvailableWizards(List<Wizard> availableWizards) {
        sendMessage(new LobbyMessage(server, "wizardList", availableWizards));
    }

    public void showError(String errorMessage) {
        sendMessage(new ErrorMessage(server, errorMessage));
    }

    public void showLoginResult(boolean connectionCompleted, boolean connectionStarted) {
        sendMessage(new LoginMessage(server, connectionCompleted, connectionStarted));
    }

    public void showGenericMessage(String genericMessage) {
        sendMessage(new GenericMessage(genericMessage));
    }

    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        sendMessage(new LobbyMessage(server, "disconnection", nicknameDisconnected));
    }

    public void showNumOfPlayers(int numOfPlayers) {
        sendMessage(new LobbyMessage(server, "numOfPlayers", numOfPlayers));
    }

    public void showExpertVariant(boolean isExpert) {
        sendMessage(new LobbyMessage(server, "expert", isExpert));
    }

    public void showAssignedWizard(Wizard wizard) {
        sendMessage(new LobbyMessage(server, wizard));
    }

    public void showWizardConfirmation(boolean confirmation) {
        sendMessage(new LobbyMessage(server, "wizard", confirmation));
    }

    public void showLobby(List<String> nicknameList, int numPlayers) {
        sendMessage(new LobbyMessage(server, nicknameList, numPlayers));
    }
}
