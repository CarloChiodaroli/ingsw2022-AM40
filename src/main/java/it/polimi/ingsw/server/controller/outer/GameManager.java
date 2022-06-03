package it.polimi.ingsw.server.controller.outer;

import it.polimi.ingsw.commons.enums.Wizard;
import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.server.controller.inner.InputController;
import it.polimi.ingsw.server.network.Server;
import it.polimi.ingsw.server.utils.StorageData;
import it.polimi.ingsw.server.view.VirtualView;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class GameManager implements LobbyMessageReader{

    private transient Map<String, VirtualView> virtualViewMap;
    private String mainPlayer;
    private int maxPlayers;
    private List<String> playerNames;
    private PlayMessagesReader playMessagesReader = null;
    private static final String STR_INVALID_STATE = "Invalid game state!";
    public static final String SAVED_GAME_FILE = "match.log";
    private static final int DEFAULT_MAX_PLAYERS = 0;
    private final Map<String, Wizard> assignedWizards;


    public GameManager() {
        this.playerNames = new ArrayList<>();
        initGameController();
        this.maxPlayers = DEFAULT_MAX_PLAYERS;
        this.assignedWizards = new HashMap<>();
    }

    public void initGameController() {
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
    }

    public void onMessageReceived(Message receivedMessage) {
        if(!playerNames.contains(receivedMessage.getSenderName())){
            sendMessage(receivedMessage.getSenderName(), new ErrorMessage("server", "You have not logged in"));
        }
        if(playMessagesReader != null && !playMessagesReader.isGameStarted()) {
            try {
                preInitState(receivedMessage);
            } catch (NoSuchMethodException| InvocationTargetException| IllegalAccessException e) {
                sendMessage(receivedMessage.getSenderName(), new ErrorMessage("server", "Server Could not understand received lobby message"));
            }
            return;
        }
        if(playMessagesReader != null && playMessagesReader.isGameStarted() && receivedMessage.getMessageType().equals(MessageType.PLAY)){
            inGameState(receivedMessage);
            return;
        }
        Server.LOGGER.warning(STR_INVALID_STATE);
    }

    // LOBBY message part

    private void preInitState(Message receivedMessage) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(receivedMessage.getMessageType() == MessageType.LOBBY){
            LobbyMessage message = (LobbyMessage) receivedMessage;
            LobbyMessageReader.class.getMethod(message.getCommand(), LobbyMessage.class).invoke(this, message);
        }
        /*

        if (receivedMessage.getMessageType() == MessageType.LOBBY) {
            LobbyMessage message = (LobbyMessage) receivedMessage;
            if (message.studentNumber() == 2 || message.studentNumber() == 3) {
                this.maxPlayers = message.studentNumber();
                new LobbyMessage("Server", this.playerNames, maxPlayers);
                this.playMessagesReader.setNumOfPlayers(maxPlayers);
                broadcastGenericMessage("Waiting for other Players . . .");
            } else {
                Server.LOGGER.warning("Client has chosen a wrong number of players");
                virtualViewMap.get(message.getSenderName()).showError("Wrong number of players chosen, can be 2 or 3");
            }
        } else {
            Server.LOGGER.warning("Wrong message received from client.");
            virtualViewMap.get(receivedMessage.getSenderName()).showError("Wrong Message to be sent now");
        }*/
    }

    @Override
    public void startGame(LobbyMessage message) {
        if(virtualViewMap.size() == maxPlayers)
            initGame();
            /*if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber()) { // If all players logged
                // check saved matches.
                // to redo better later
                /*StorageData storageData = new StorageData();
                GameManager savedGameManager = storageData.restore();
                if (savedGameManager != null &&
                        game.getPlayersNicknames().containsAll(savedGameManager.getTurnController().getNicknameQueue())) {
                    Server.LOGGER.info("Saved Match restored.");
                    turnController.newTurn();
                } else {
                    initGame();
                }
            }*/
    }

    @Override
    public void lobbyPlayers(LobbyMessage message) {
        // should not receive this
    }

    @Override
    public void mainPlayer(LobbyMessage message) {
        // should not receive this
    }

    @Override
    public void wizard(LobbyMessage message) {
        if(assignedWizards.entrySet().stream().noneMatch(x -> x.getValue().equals(message.getWizard()))){
            if(!assignedWizards.containsKey(message.getSenderName())){
                assignedWizards.put(message.getSenderName(), message.getWizard());
            } else {
                assignedWizards.replace(message.getSenderName(), message.getWizard());
            }
            sendMessage(message.getSenderName(), new LobbyMessage("server", "wizard", true));
        } else {
            sendMessage(message.getSenderName(), new LobbyMessage("server", "wizard", false));
        }
    }

    @Override
    public void numOfPlayers(LobbyMessage message) {
        if(this.maxPlayers == DEFAULT_MAX_PLAYERS){
            maxPlayers = message.getMaxPlayers();
        } else {
            sendMessage("server", new ErrorMessage(message.getSenderName(), "Max number of players has been already set"));
        }
    }

    @Override
    public void disconnection(LobbyMessage message) {
        // should not receive this
    }

    // LOBBY message Utility

    private void initGame() {
        playMessagesReader.startGame();
    }

    // PLAY message part

    private void inGameState(Message receivedMessage) {
        PlayMessage message = (PlayMessage) receivedMessage;
        try {
            message.executeMessage(playMessagesReader);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            virtualViewMap.get(receivedMessage.getSenderName()).showError("Error while running message, please retry");
        }
    }

    // LOGIN message part

    public void loginHandler(String nickname, VirtualView virtualView) {
        if (virtualViewMap.isEmpty()) { // First player logged. Ask number of players.
            addVirtualView(nickname, virtualView);
            this.playerNames.add(nickname);
            this.mainPlayer = nickname;
            this.playMessagesReader = new PlayMessagesReader(mainPlayer, this);
            virtualView.showLoginResult(true, true);
            virtualView.sendMainPlayer(mainPlayer);
        } else if (virtualViewMap.size() < maxPlayers) {
            addVirtualView(nickname, virtualView);
            this.playerNames.add(nickname);
            this.playMessagesReader.addPlayer(nickname);
            virtualView.sendMainPlayer(mainPlayer);
            virtualView.showLoginResult(true, true);
        } else {
            virtualView.showLoginResult(true, false);
        }
    }

    public void addVirtualView(String nickname, VirtualView virtualView) {
        virtualViewMap.put(nickname, virtualView);
    }

    // Resilience

    public void endGame() {
        StorageData storageData = new StorageData();
        storageData.delete();
        initGameController();
        Server.LOGGER.info("Server ready for a new Game");
    }

    public Map<String, VirtualView> getVirtualViewMap() {
        return virtualViewMap;
    }

    public void removeVirtualView(String nickname, boolean notifyEnabled) {
        VirtualView vv = virtualViewMap.remove(nickname);
        if(playMessagesReader.isGameStarted()){
            playMessagesReader.stopPlayer(nickname);
        } else {
            playMessagesReader.deletePlayer(nickname);
        }
    }

    public void sendMessage(String playerName, Message message){
        if(virtualViewMap.containsKey(playerName)) virtualViewMap.get(playerName).sendMessage(message);
    }

    public void broadcastMessage(Message message){
        for(VirtualView vv: virtualViewMap.values()){
            vv.sendMessage(message);
        }
    }

    public void broadcastDisconnectionMessage(String nicknameDisconnected, String text) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showDisconnectionMessage(nicknameDisconnected, text);
        }
    }

    public boolean checkLoginNickname(String nickname, VirtualView view) {
        return InputController.checkLoginNickname(nickname, view, virtualViewMap.keySet());
    }

    public boolean isGameStarted() {
        return playMessagesReader != null && playMessagesReader.isGameStarted();
    }

    public List<String> getPlayerNames() {
        return virtualViewMap.keySet().stream().toList();
    }

    // To be deleted candidates

    @Deprecated
    public void broadcastGenericMessage(String messageToNotify, String excludeNickname) {
        virtualViewMap.entrySet().stream()
                .filter(entry -> !excludeNickname.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(vv -> vv.showGenericMessage(messageToNotify));
    }

    @Deprecated
    public void broadcastGenericMessage(String messageToNotify) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showGenericMessage(messageToNotify);
        }
    }


}
