package it.polimi.ingsw.view;

import it.polimi.ingsw.manuel.model.Game;
import it.polimi.ingsw.network.Server.ClientHandler;
import it.polimi.ingsw.Observer.Observer;
import it.polimi.ingsw.network.Message.*;


import java.util.List;

/**
 * PER CONTROLLER VI E' CORRISPONDENZA BIUNIVOCA TRA CLIENT E VIRTUALVIEW
 * IL CONTROLLER PENSA CHE TUTTI I CLIENT SIANO IN LOCALE. QUINDI MANDA MESSSAGGI A QUESTA CLASSE PENSANDO SIA LA VIEW DI
 * CIASCUN CLIENT. IN REALTA VIRTUAL VIEW SERVENDOSI DELLA CLASSE SOCKETCLIENTHANLDER(QUI IMPLEMENTATA CON INTERFACCIA
 * CLIENT HANDLER) INVIERA' I MESSAGGI ATTTRAVERSO LA RETE
 * Hides the network implementation from the controller.
 * The controller calls methods from this class as if it was a normal view.
 * Instead, a network protocol is used to communicate with the real view on the client side.
 */

public class VirtualView implements View, Observer {

    private final ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    @Override
    public void askNickname() {
        clientHandler.sendMessage(new LoginReply(false, true));
    }

    @Override
    public void askPlayersNumber() {
        clientHandler.sendMessage(new PlayerNumberRequest());
    }

    @Override
    public void showLoginResult(boolean nicknameAccepted, boolean connectionSuccessful, String nickname) {
        clientHandler.sendMessage(new LoginReply(nicknameAccepted, connectionSuccessful));
    }

    @Override
    public void showGenericMessage(String genericMessage) {
        clientHandler.sendMessage(new GenericMessage(genericMessage));
    }

    @Override
    public void showDisconnectionMessage(String nicknameDisconnected, String text) {
        clientHandler.sendMessage(new DisconnectionMessage(nicknameDisconnected, text));
    }

    @Override
    public void showErrorAndExit(String error) {
        clientHandler.sendMessage(new ErrorMessage(Game.SERVER_NICKNAME, error));
    }

    @Override
    public void showLobby(List<String> nicknameList, int numPlayers) {
        clientHandler.sendMessage(new LobbyMessage(nicknameList, numPlayers));
    }

    @Override
    public void update(Message message) {
        clientHandler.sendMessage(message);
    }
}
