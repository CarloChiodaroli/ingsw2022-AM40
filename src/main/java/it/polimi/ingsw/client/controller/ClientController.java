package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.model.ClientPlayState;
import it.polimi.ingsw.client.network.Client;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.commons.observer.Observer;
import it.polimi.ingsw.commons.observer.ViewObserver;
import it.polimi.ingsw.commons.view.View;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientController implements ViewObserver, Observer {

    private final View view;

    private Client client;
    private String nickname;
    private final ExecutorService taskQueue;
    public static final int UNDO_TIME = 5000;
    private final ClientPlayState playState;

    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
        this.playState = new ClientPlayState();
    }

    @Override
    public void onUpdateServerInfo(Map<String, String> serverInfo) {
        try {
            client = new SocketClient(serverInfo.get("address"), Integer.parseInt(serverInfo.get("port")));
            client.addObserver(this);
            client.readMessage(); // Starts an asynchronous reading from the server.
            client.enablePinger(true);
            taskQueue.execute(view::askNickname);
        } catch (IOException e) {
            taskQueue.execute(() -> view.showLoginResult(false, false, this.nickname));
        }
    }

    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginMessage(this.nickname));
    }

    @Override
    public void onUpdatePlayersNumber(int playersNumber) {
        client.sendMessage(new LobbyMessage(this.nickname, playersNumber));
    }


    @Override
    public void onDisconnection() {
        client.disconnect();
    }

    @Override
    public void update(Message message) {
        try {
            switch (message.getMessageType()) {
                case GENERIC:
                    taskQueue.execute(() -> view.showGenericMessage(((GenericMessage) message).getMessage()));
                    break;
                case ERROR:
                    ErrorMessage em = (ErrorMessage) message;
                    view.showError(em.getError());
                    break;
                case LOGIN:
                    LoginMessage loginMessage = (LoginMessage) message;
                    taskQueue.execute(() -> view.showLoginResult(loginMessage.isNicknameAccepted(), loginMessage.isConnectionSuccessful(), this.nickname));
                    break;
                case LOBBY:
                    LobbyMessage lm = (LobbyMessage) message;
                    this.manageLobby(lm);
                    break;
                default: // Should never reach this condition
                    client.sendMessage(new ErrorMessage(nickname, "Wrong message received"));
                    Client.LOGGER.info(() -> "received wrong message from server");
                    break;
            }
        } catch (ClassCastException e) {
            // Should never reach this condition
            client.sendMessage(new ErrorMessage(nickname, "Wrong message received"));
            Client.LOGGER.info(() -> "received wrong message from server");
        }

    }

    private void manageLobby(LobbyMessage message) {
        if (playState.getMainPlayer() == null) { // First message
            playState.setMainPlayer(message.getMainPlayerName());
            if (playState.getMainPlayer().equals(this.nickname)) { // First player to connect to the game
                taskQueue.execute(view::askPlayersNumber);
            }
            return;
        }
        if (playState.getPlayerNames().isEmpty()) { //
            playState.setPlayerNames(message.getNicknameList());
            return;
        }
        List<String> players = message.getLobbyPlayers();
        if (players.isEmpty()) {
            if (message.getDisconnection().equals(this.nickname)) {
                client.disconnect();
                taskQueue.execute(() -> view.showDisconnectionMessage(message.getDisconnection(), "Server has disconnected you"));
            } else {
                taskQueue.execute(() -> view.showOtherDisconnectionMessage(message.getDisconnection(), "Disconnection of "));
            }
        } else {
            playState.setPlayerNames(players);
        }
    }

    public static boolean isValidIpAddress(String ip) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(regex);
    }

    public static boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
