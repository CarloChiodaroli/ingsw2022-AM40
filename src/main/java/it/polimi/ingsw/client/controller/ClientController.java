package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.commons.observer.Observer;
import it.polimi.ingsw.commons.observer.ViewObserver;
import it.polimi.ingsw.client.network.Client;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.commons.view.View;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientController implements ViewObserver, Observer {

    private final View view;

    private Client client;
    private String nickname;
    private final ExecutorService taskQueue;
    public static final int UNDO_TIME = 5000;

    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
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
        client.sendMessage(new LoginRequest(this.nickname));
    }

    @Override
    public void onUpdatePlayersNumber(int playersNumber) {
        client.sendMessage(new PlayerNumberReply(this.nickname, playersNumber));
    }


    @Override
    public void onDisconnection() {
        client.disconnect();
    }

    @Override
    public void update(Message message) {
        switch (message.getMessageType()) {
            case GENERIC_MESSAGE:
                taskQueue.execute(() -> view.showGenericMessage(((GenericMessage) message).getMessage()));
                break;
            case DISCONNECTION:
                DisconnectionMessage dm = (DisconnectionMessage) message;
                client.disconnect();
                view.showDisconnectionMessage(dm.getNicknameDisconnected(), dm.getMessageStr());
                break;
            case ERROR:
                ErrorMessage em = (ErrorMessage) message;
                view.showErrorAndExit(em.getError());
                break;
            case LOGIN_REPLY:
                LoginReply loginReply = (LoginReply) message;
                taskQueue.execute(() -> view.showLoginResult(loginReply.isNicknameAccepted(), loginReply.isConnectionSuccessful(), this.nickname));
                break;
            case PLAYERNUMBER_REQUEST:
                taskQueue.execute(view::askPlayersNumber);
                break;
            case LOBBY:
                LobbyMessage lobbyMessage = (LobbyMessage) message;
                taskQueue.execute(() -> view.showLobby(lobbyMessage.getNicknameList(), lobbyMessage.getMaxPlayers()));
                break;
            default: // Should never reach this condition
                break;
        }
    }

    //STACK OVERFLOW
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
