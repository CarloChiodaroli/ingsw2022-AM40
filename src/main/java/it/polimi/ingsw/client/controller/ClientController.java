package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.client.model.PlayMessageController;
import it.polimi.ingsw.client.model.PlayState;
import it.polimi.ingsw.client.network.Client;
import it.polimi.ingsw.client.network.SocketClient;
import it.polimi.ingsw.client.observer.Observer;
import it.polimi.ingsw.client.observer.ViewObserver;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.commons.enums.Wizard;
import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.commons.message.play.ExpertPlayMessage;
import it.polimi.ingsw.commons.message.play.NormalPlayMessage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controls the client reading and managing all not Play messages and forwarding them to the {@link PlayMessageController} class.
 */
public class ClientController implements ViewObserver, Observer, LobbyMessageReader {

    private final View view;

    private Client client;
    private String nickname;
    private boolean playing;
    private final ExecutorService taskQueue;
    private final PlayMessageController playMessageReader;
    private final PlayState state;

    /**
     * Constructor
     */
    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
        this.playMessageReader = new PlayMessageController(this, this.view);
        this.state = playMessageReader.getState();
        playing = false;
    }

    /**
     * Getter
     */
    public ExecutorService getTaskQueue() {
        return taskQueue;
    }

    // Communication Client - Server
    /**
     * Create a new Socket Connection to the server with the updated info
     * An error view is shown if connection is killed
     *
     * @param serverInfo a map of server address and server port
     */
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

    /**
     * Sends a message to the server with the updated nickname
     *
     * @param nickname nickname to be sent
     */
    @Override
    public void onUpdateNickname(String nickname) {
        this.nickname = nickname;
        client.sendMessage(new LoginMessage(this.nickname));
    }

    /**
     * Sends a message to the server with the player number chosen by the main player
     *
     * @param playersNumber number of players
     */
    @Override
    public void onUpdatePlayersNumber(int playersNumber) {
        if (!haveNickname()) return;
        client.sendMessage(new LobbyMessage(this.nickname, "numOfPlayers", playersNumber));
    }

    /**
     * Sends a message to the server with the wizard chosen by the user
     *
     * @param wizard chosen wizard
     */
    public void onUpdateWizard(Wizard wizard) {
        if (!haveNickname()) return;
        if (state.getWizard().isPresent() && state.getWizard().get().equals(wizard)) {
            taskQueue.execute(view::showWizard);
            return;
        }
        if (!state.getAvailableWizards().contains(wizard)) {
            showNotCriticalError("Your chosen wizard is not available");
            return;
        }
        state.setWizard(wizard);
        sendMessage(new LobbyMessage(nickname, wizard));
    }

    /**
     * Sends a message to the server with the game mode
     *
     * @param how true if is expert
     */
    public void onUpdateExpert(boolean how) {
        if (!haveNickname()) return;
        sendMessage(new LobbyMessage(nickname, "expert", how));
    }

    /**
     * Disconnect the client
     */
    @Override
    public void onDisconnection() {
        client.disconnect();
    }

    /**
     * Sends a message to the server the game is started
     */
    @Override
    public void onUpdateStart() {
        if (!haveNickname()) return;
        if (startedPlaying()) return;
        sendMessage(new LobbyMessage(nickname, "startGame"));
    }

    /**
     * Send a message if is possible to have name
     *
     * @return true if isn't too early
     */
    private boolean haveNickname() {
        if (nickname == null) {
            showNotCriticalError("It's too early to use this command");
            return false;
        }
        return true;
    }

    /**
     * Send a message if the game is started
     *
     * @return true if is started
     */
    private boolean startedPlaying() {
        if (playing) {
            showNotCriticalError("Game has already started");
            return true;
        }
        return false;
    }

    // Communication Server - Client

    // First level
    /**
     * Check the type of message received, and invokes the correct handler
     *
     * @param message message received from the server
     */
    @Override
    public void update(Message message) {
        try {
            this.getClass().getMethod(message.getMessageType().toString().toLowerCase(), Message.class).invoke(this, message);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            notCriticalError("Received illegal message type");
        }
    }

    /**
     * Login message handler
     *
     * @param message message received from the server
     */
    public void login(Message message) {
        LoginMessage loginMessage = (LoginMessage) message;
        taskQueue.execute(() -> view.showLoginResult(loginMessage.isConnectionCompleted(), loginMessage.isConnectionStarted(), this.nickname));
        if (loginMessage.isConnectionCompleted() && !loginMessage.isConnectionStarted()) killMe(0);
    }

    /**
     * Lobby message handler
     *
     * @param message message received from the server
     */
    public void lobby(Message message) {
        LobbyMessage lm = (LobbyMessage) message;
        try {
            LobbyMessageReader.class.getMethod(lm.getCommand(), LobbyMessage.class).invoke(this, lm);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            notCriticalError("Error while managing message from server: " + e.getCause().getMessage());
        }
    }

    /**
     * Expert message handler
     *
     * @param message message received from the server
     */
    public void expert(Message message) {
        ExpertPlayMessage pm = (ExpertPlayMessage) message;
        taskQueue.execute(() -> {
            try {
                pm.executeMessage(playMessageReader);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                notCriticalError("Error while managing message from server: " + e.getMessage());
            }
        });
    }

    /**
     * Play message handler
     *
     * @param message message received from the server
     */
    public void play(Message message) {
        NormalPlayMessage pm = (NormalPlayMessage) message;
        taskQueue.execute(() -> {
            try {
                pm.executeMessage(playMessageReader);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                notCriticalError("Error while managing message from server: " + e.getMessage());
            }
        });
    }

    /**
     * Generic message handler
     *
     * @param message message received from the server
     */
    public void generic(Message message) {
        taskQueue.execute(() -> view.showGenericMessage(((GenericMessage) message).getMessage()));
    }

    /**
     * Error message handler
     *
     * @param message message received from the server
     */
    public void error(Message message) {
        ErrorMessage em = (ErrorMessage) message;
        taskQueue.execute(() -> view.showError(em.getError()));
    }

    // Second layer - Lobby Messages
    /**
     * Receives actual game mode.
     *
     * <br> {@inheritDoc}
     */
    @Override
    public void expert(LobbyMessage message) {
        playMessageReader.setExpert(message.isExpert());
        taskQueue.execute(() -> view.showExpert(message.isExpert()));
    }

    /**
     * Sends error on start game request. Receiving this message goes against the communication protocol.
     *
     * <br> {@inheritDoc}
     */
    @Override
    public void startGame(LobbyMessage message) {
        notCriticalError("Received wrong message from server");
    }

    /**
     * Receives actual play player name list.
     *
     * <br> {@inheritDoc}
     */
    @Override
    public void lobbyPlayers(LobbyMessage message) {
        taskQueue.execute(() -> view.showLobby(message.getLobbyPlayers(), message.studentNumber()));
    }

    /**
     * Receives actual play main player name
     *
     * <br> {@inheritDoc}
     */
    @Override
    public void mainPlayer(LobbyMessage message) {
        if (playMessageReader.getMainPlayer() != null) return;
        state.setMainPlayer(message.getMainPlayerName());
        state.setMyName(nickname);
        playMessageReader.setMainPlayer(message.getMainPlayerName());
        if (playMessageReader.getMainPlayer().equals(this.nickname)) { // First player to connect to the game
            taskQueue.execute(view::askPlaySettings);
        } else {
            taskQueue.execute(view::askPlayCustomization);
        }
    }

    /**
     * Receives wizard choice confirmation.
     * while reconnecting receives the old wizard choice and sets it in the client.
     *
     * <br> {@inheritDoc}
     */
    @Override
    public void wizard(LobbyMessage message) {
        if (!message.getAccepted()) {
            state.setWizard(null);
        }
        if (message.getWizard() != null) {
            state.setWizard(message.getWizard());
        }
        taskQueue.execute(view::showWizard);
    }

    /**
     * Receives remaining available wizards.
     *
     * <br> {@inheritDoc}
     */
    @Override
    public void wizardList(LobbyMessage message) {
        state.setAvailableWizards(message.getAvailableWizards());
        if (state.getWizard().isEmpty()) {
            taskQueue.execute(view::showAvailableWizards);
        }
    }

    /**
     * Receives actual play max num of players.
     *
     * <br> {@inheritDoc}
     */
    @Override
    public void numOfPlayers(LobbyMessage message) {
        state.setNumPlayers(message.getMaxPlayers());
        taskQueue.execute(() -> view.showChosenNumOfPlayers(message.getMaxPlayers()));
    }

    /**
     * Receives actual player disconnection messages.
     *
     * <br> {@inheritDoc}
     */
    @Override
    public void disconnection(LobbyMessage message) {
        String outgoingName = message.getDisconnection();
        if (outgoingName.equals(nickname)) {
            taskQueue.execute(() -> view.showDisconnectionMessage(nickname, "Server has disconnected you"));
            killMe(0);
        } else {
            taskQueue.execute(() -> view.showOtherDisconnectionMessage(outgoingName, " has disconnected"));
        }
    }

    // Utility

    /**
     * getter
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Send a message to server
     *
     * @param message message to sent
     */
    public void sendMessage(Message message) {
        client.sendMessage(message);
    }

    /**
     * Shows and logs not critical error
     *
     * @param error error
     */
    private void notCriticalError(String error) {
        showNotCriticalError(error);
        Client.LOGGER.severe(error);
    }

    /**
     * Show not critical error
     *
     * @param error error
     */
    private void showNotCriticalError(String error) {
        taskQueue.execute(() -> view.showError(error));
    }

    /**
     * shows and logs critical errors and closes the client.
     * @param error
     */
    public void criticalError(String error) {
        notCriticalError("Severe " + error);
        killMe(1);
    }

    /**
     * Closes the client.
     *
     * @param status exit
     */
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
