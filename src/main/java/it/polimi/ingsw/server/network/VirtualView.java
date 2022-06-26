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

    /**
     * Constructor
     */
    public VirtualView(ClientHandler clientHandler, String server) {
        this.clientHandler = clientHandler;
        this.server = server;
    }

    /**
     * Get ClientHandler
     *
     * @return Clienthandler
     */
    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    /**
     * Send a message with the main player
     *
     * @param mainPlayerName main player
     */
    public void sendMainPlayer(String mainPlayerName) {
        sendMessage(new LobbyMessage(server, "mainPlayer", mainPlayerName));
    }

    /**
     * ClientHandler send message
     *
     * @param message message to send
     */
    public void sendMessage(Message message) {
        clientHandler.sendMessage(message);
    }

    /**
     * Send a message with the available wizards
     *
     * @param availableWizards available wizards
     */
    public void sendAvailableWizards(List<Wizard> availableWizards) {
        sendMessage(new LobbyMessage(server, "wizardList", availableWizards));
    }

    /**
     * Send a message of error
     *
     * @param errorMessage error message
     */
    public void showError(String errorMessage) {
        sendMessage(new ErrorMessage(server, errorMessage));
    }

    /**
     * Send a message with the results of login
     *
     * @param connectionCompleted true if connection is complete
     * @param connectionStarted true if connection is start
     */
    public void showLoginResult(boolean connectionCompleted, boolean connectionStarted) {
        sendMessage(new LoginMessage(server, connectionCompleted, connectionStarted));
    }

    /**
     * Send a generic message
     *
     * @param genericMessage generic message
     */
    public void showGenericMessage(String genericMessage) {
        sendMessage(new GenericMessage(genericMessage));
    }

    /**
     * Send disconnection message
     *
     * @param nicknameDisconnected player disconnected
     * @param text
     */
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        sendMessage(new LobbyMessage(server, "disconnection", nicknameDisconnected));
    }

    /**
     * Send message with number of players
     *
     * @param numOfPlayers number of players
     */
    public void showNumOfPlayers(int numOfPlayers) {
        sendMessage(new LobbyMessage(server, "numOfPlayers", numOfPlayers));
    }

    /**
     * Send message with game mode
     *
     * @param isExpert true if game mode is expert
     */
    public void showExpertVariant(boolean isExpert) {
        sendMessage(new LobbyMessage(server, "expert", isExpert));
    }

    /**
     * Send a message with the assigned wizard
     *
     * @param wizard wizard
     */
    public void showAssignedWizard(Wizard wizard) {
        sendMessage(new LobbyMessage(server, wizard));
    }

    /**
     * Send a message with confirm of chosen wizard
     *
     * @param confirmation true if chosen is done
     */
    public void showWizardConfirmation(boolean confirmation) {
        sendMessage(new LobbyMessage(server, "wizard", confirmation));
    }

    /**
     * Send lobby message
     *
     * @param nicknameList list of players name
     * @param numPlayers number fo players
     */
    public void showLobby(List<String> nicknameList, int numPlayers) {
        sendMessage(new LobbyMessage(server, nicknameList, numPlayers));
    }
}
