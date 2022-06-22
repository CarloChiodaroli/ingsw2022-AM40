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
import it.polimi.ingsw.commons.message.play.ExpertPlayMessage;
import it.polimi.ingsw.commons.message.play.NormalPlayMessage;
import it.polimi.ingsw.commons.message.play.PlayMessage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ClientController implements ViewObserver, Observer, LobbyMessageReader {

    private final View view;

    private Client client;
    private String nickname;
    private boolean playing;
    private final ExecutorService taskQueue;
    private final PlayMessageController playMessageReader;
    private final PlayState state;

    public ClientController(View view) {
        this.view = view;
        taskQueue = Executors.newSingleThreadExecutor();
        this.playMessageReader = new PlayMessageController(this, this.view);
        this.state = playMessageReader.getState();
        playing = false;
    }

    public ExecutorService getTaskQueue() {
        return taskQueue;
    }

    // Communication Client - Server
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
        if(!haveNickname()) return;
        client.sendMessage(new LobbyMessage(this.nickname, "numOfPlayers", playersNumber));
    }

    public void onUpdateWizard(Wizard wizard) {
        if(!haveNickname()) return;
        if(state.getWizard().isPresent() && state.getWizard().get().equals(wizard)){
            taskQueue.execute(view::showWizard);
            return;
        }
        if(!state.getAvailableWizards().contains(wizard)){
            showNotCriticalError("Your chosen wizard is not available");
            return;
        }
        state.setWizard(wizard);
        sendMessage(new LobbyMessage(nickname, wizard));
    }

    public void onUpdateExpert(boolean how) {
        if(!haveNickname()) return;
        sendMessage(new LobbyMessage(nickname, "expert", how));
    }

    @Override
    public void onDisconnection() {
        client.disconnect();
    }

    @Override
    public void onUpdateStart() {
        if(!haveNickname()) return;
        if(startedPlaying()) return;
        sendMessage(new LobbyMessage(nickname, "startGame"));
    }

    private boolean haveNickname(){
        if(nickname == null){
            showNotCriticalError("It's too early to use this command");
            return false;
        }
        return true;
    }

    private boolean startedPlaying(){
        if(playing){
            showNotCriticalError("Game has already started");
            return true;
        }
        return false;
    }

    // Communication Server - Client

    // First level
    @Override
    public void update(Message message){
        try{
            this.getClass().getMethod(message.getMessageType().toString().toLowerCase(), Message.class).invoke(this, message);
        } catch (InvocationTargetException| IllegalAccessException | NoSuchMethodException e) {
            notCriticalError("Received illegal message type");
        }
    }

    public void login(Message message){
        LoginMessage loginMessage = (LoginMessage) message;
        taskQueue.execute(() -> view.showLoginResult(loginMessage.isNicknameAccepted(), loginMessage.isConnectionSuccessful(), this.nickname));
    }

    public void lobby(Message message){
        LobbyMessage lm = (LobbyMessage) message;
        try {
            LobbyMessageReader.class.getMethod(lm.getCommand(), LobbyMessage.class).invoke(this, lm);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            notCriticalError("Error while managing message from server: " + e.getCause().getMessage());
        }
    }

    public void expert(Message message){
        ExpertPlayMessage pm = (ExpertPlayMessage) message;
        taskQueue.execute(() -> {
            try {
                pm.executeMessage(playMessageReader);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                notCriticalError("Error while managing message from server: " + e.getMessage());
            }
        });
    }

    public void play(Message message){
        NormalPlayMessage pm = (NormalPlayMessage) message;
        taskQueue.execute(() -> {
            try {
                pm.executeMessage(playMessageReader);
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                notCriticalError("Error while managing message from server: " + e.getMessage());
            }
        });
    }

    public void generic(Message message){
        taskQueue.execute(() -> view.showGenericMessage(((GenericMessage) message).getMessage()));
    }

    public void error(Message message){
        ErrorMessage em = (ErrorMessage) message;
        taskQueue.execute(() -> view.showError(em.getError()));
    }

    // Second layer - Lobby Messages

    @Override
    public void expert(LobbyMessage message) {
        playMessageReader.setExpert(message.isExpert());
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
        state.setMainPlayer(message.getMainPlayerName());
        state.setMyName(nickname);
        playMessageReader.setMainPlayer(message.getMainPlayerName());
        if (playMessageReader.getMainPlayer().equals(this.nickname)) { // First player to connect to the game
            taskQueue.execute(view::askPlaySettings);
        } else {
            taskQueue.execute(view::askPlayCustomization);
        }
    }

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

    @Override
    public void wizardList(LobbyMessage message) {
        state.setAvailableWizards(message.getAvailableWizards());
        if(state.getWizard().isEmpty()){
            taskQueue.execute(view::showAvailableWizards);
        }
    }

    @Override
    public void numOfPlayers(LobbyMessage message) {
        state.setNumPlayers(message.getMaxPlayers());
        taskQueue.execute(() -> view.showChosenNumOfPlayers(message.getMaxPlayers()));
    }

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

    public String getNickname() {
        return nickname;
    }

    public void sendMessage(Message message) {
        client.sendMessage(message);
    }

    private void notCriticalError(String error) {
        showNotCriticalError(error);
        Client.LOGGER.severe(error);
    }

    private void showNotCriticalError(String error){
        taskQueue.execute(() -> view.showError(error));
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
