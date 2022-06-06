package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.model.PlayMessageController;
import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.client.network.Client;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.client.observer.Observer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.client.observer.ViewObserver;
import it.polimi.ingsw.commons.enums.Wizard;
import it.polimi.ingsw.commons.message.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientController implements ViewObserver, Observer, LobbyMessageReader {

    private final View view;

    private Client client;
    private String nickname;
    private final ExecutorService taskQueue;
    private final PlayMessageController playMessageReader;
    private final PlayState state;

    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
        this.playMessageReader = new PlayMessageController(this, this.view);
        this.state = playMessageReader.getState();
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
        client.sendMessage(new LobbyMessage(this.nickname, "numOfPlayers", playersNumber));
    }

    public void onUpdateWizard(Wizard wizard) {
        state.setWizard(wizard);
        sendMessage(new LobbyMessage(nickname, wizard));
    }

    public void onUpdateStartGame() {
        sendMessage(new LobbyMessage(nickname, "startGame"));
    }

    public void onUpdateExpert(boolean how) {
        sendMessage(new LobbyMessage(nickname, "expert", how));
    }

    @Override
    public void onDisconnection() {
        client.disconnect();
    }

    @Override
    public void onUpdateStart() {
        sendMessage(new LobbyMessage(nickname, "startGame"));
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
                    taskQueue.execute(() -> view.showError(em.getError()));
                    break;
                case LOGIN:
                    LoginMessage loginMessage = (LoginMessage) message;
                    taskQueue.execute(() -> view.showLoginResult(loginMessage.isNicknameAccepted(), loginMessage.isConnectionSuccessful(), this.nickname));
                    break;
                case LOBBY:
                    LobbyMessage lm = (LobbyMessage) message;
                    try {
                        this.manageLobby(lm);
                    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                        notCriticalError("Error while managing message from server: " + e.getMessage());
                    }
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

    private void manageLobby(LobbyMessage message) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        LobbyMessageReader.class.getMethod(message.getCommand(), LobbyMessage.class).invoke(this, message);
    }

    @Override
    public void expert(LobbyMessage message) {
        taskQueue.execute(() -> view.showExpert(message.isExpert()));
    }

    @Override
    public void startGame(LobbyMessage message) {
        notCriticalError("Received wrong message from server");
    }

    @Override
    public void lobbyPlayers(LobbyMessage message) {
        taskQueue.execute(() -> view.showLobby(message.getLobbyPlayers(), message.studentNumber()));
    }

    @Override
    public void mainPlayer(LobbyMessage message) {
        if (playMessageReader.getMainPlayer() != null) return;
        playMessageReader.setMainPlayer(message.getMainPlayerName());
        if (playMessageReader.getMainPlayer().equals(this.nickname)) { // First player to connect to the game
            taskQueue.execute(view::askPlaySettings);
        } else {
            //taskQueue.execute(() -> view.askPlayCustomization(message.getAvailableWizards()));
        }
    }

    @Override
    public void wizard(LobbyMessage message) {
        if (!message.getAccepted()) {
            state.setWizard(null);
        }
        taskQueue.execute(view::showWizard);
    }

    @Override
    public void numOfPlayers(LobbyMessage message) {
        taskQueue.execute(() -> view.showChosenNumOfPlayers(message.getMaxPlayers()));
    }

    @Override
    public void disconnection(LobbyMessage message) {
        String outgoingName = message.getDisconnection();
        if (outgoingName.equals(nickname)) {
            taskQueue.execute(() -> view.showDisconnectionMessage(nickname, "Server has disconnected you"));
            killMe(0);
        } else {
            taskQueue.execute(() -> view.showOtherDisconnectionMessage(outgoingName, "Has disconnected"));
        }
    }

    public void sendMessage(Message message) {
        client.sendMessage(message);
    }

    private void notCriticalError(String error) {
        taskQueue.execute(() -> view.showError(error));
        Client.LOGGER.severe(error);
    }

    public void criticalError(String error) {
        notCriticalError("Severe " + error);
        killMe(1);
    }

    private void killMe(int status) {
        Client.LOGGER.info("Closing client");
        client.disconnect();
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
