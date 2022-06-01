package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.model.PlayMessageController;
import it.polimi.ingsw.client.network.Client;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.commons.observer.Observer;
import it.polimi.ingsw.commons.observer.ViewObserver;
import it.polimi.ingsw.client.view.View;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientController implements ViewObserver, Observer {

    private final View view;

    private Client client;
    private String nickname;
    private final ExecutorService taskQueue;
    private final PlayMessageController playMessageReader;

    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
        this.playMessageReader = new PlayMessageController(this, this.view);
    }

    public ExecutorService getTaskQueue() {
        return taskQueue;
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
                    notCriticalError(em.getError());
                    break;
                case LOGIN:
                    LoginMessage loginMessage = (LoginMessage) message;
                    taskQueue.execute(() -> view.showLoginResult(loginMessage.isNicknameAccepted(), loginMessage.isConnectionSuccessful(), this.nickname));
                    break;
                case LOBBY:
                    LobbyMessage lm = (LobbyMessage) message;
                    this.manageLobby(lm);
                    break;
                case PLAY:
                    PlayMessage pm = (PlayMessage) message;
                    taskQueue.execute(() -> {
                        try {
                            pm.executeMessage(playMessageReader);
                        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                            notCriticalError("Error while managing message from server: " + e.getCause().getMessage());
                        }
                    });
                    break;
                default: // Should never reach this condition
                    // client.sendMessage(new ErrorMessage(nickname, "Wrong message received"));
                    notCriticalError("Received wrong message from server");
                    break;
            }
        } catch (ClassCastException e) {
            // Should never reach this condition
            notCriticalError("Completely wrong message received from server");
        }
    }

    public String getNickname() {
        return nickname;
    }

    private void manageLobby(LobbyMessage message) {
        if (playMessageReader.getMainPlayer() == null) { // First message
            playMessageReader.setMainPlayer(message.getMainPlayerName());
            if (playMessageReader.getMainPlayer().equals(this.nickname)) { // First player to connect to the game
                taskQueue.execute(view::askPlayersNumber);
            }
            return;
        }
        if (playMessageReader.getPlayerNames().isEmpty()) {
            playMessageReader.setPlayerNames(message.getNicknameList());
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
            playMessageReader.setPlayerNames(players);
        }
    }

    public void sendMessage(Message message){
        client.sendMessage(message);
    }

    private void notCriticalError(String error){
        taskQueue.execute(() -> view.showError(error));
        Client.LOGGER.severe(error);
    }

    public void criticalError(String error){
        notCriticalError("Severe " + error);
        killMe(1);
    }

    private void killMe(int status){
        System.exit(status);
    }

    /**
     * Validates the given IPv4 address by using a regex.
     *
     * @param ip the string of the ip address to be validated
     * @return {@code true} if the ip is valid, {@code false} otherwise.
     */
    public static boolean isValidIpAddress(String ip) {
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        return ip.matches(regex);
    }

    /**
     * Checks if the given port string is in the range of allowed ports.
     *
     * @param portStr the ports to be checked.
     * @return {@code true} if the port is valid, {@code false} otherwise.
     */
    public static boolean isValidPort(String portStr) {
        try {
            int port = Integer.parseInt(portStr);
            return port >= 1 && port <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
