package it.polimi.ingsw.server.controller.outer;

import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.commons.observer.Observer;
import it.polimi.ingsw.commons.view.View;
import it.polimi.ingsw.server.controller.inner.InputController;
import it.polimi.ingsw.server.controller.inner.TurnController;
import it.polimi.ingsw.server.network.Server;
import it.polimi.ingsw.server.utils.StorageData;
import it.polimi.ingsw.server.view.VirtualView;

import java.lang.reflect.InvocationTargetException;
import java.util.*;


public class GameManager implements Observer {

    private transient Map<String, VirtualView> virtualViewMap;
    private PlayState playState;
    private TurnController turnController;
    private String mainPlayer;
    private int maxPlayers;
    private List<String> playerNames;
    private PlayMessagesReader playMessagesReader = null;
    private static final String STR_INVALID_STATE = "Invalid game state!";
    public static final String SAVED_GAME_FILE = "match.log";


    public GameManager() {
        this.playerNames = new ArrayList<>();
        initGameController();
    }

    public void initGameController() {
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        setPlayState(PlayState.PRE_INIT);
    }

    // switch to be removed soon
    public void onMessageReceived(Message receivedMessage) {

        VirtualView virtualView = virtualViewMap.get(receivedMessage.getSenderName());
        switch (playState) {
            case PRE_INIT:
                preInitState(receivedMessage); // only used by main player
                break;
            case IN_GAME:
                inGameState(receivedMessage);
                break;
            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    private void preInitState(Message receivedMessage) {
        if (receivedMessage.getMessageType() == MessageType.LOBBY) {
            LobbyMessage message = (LobbyMessage) receivedMessage;
            if (message.chosenStudentNumber() == 2 || message.chosenStudentNumber() == 3) {
                this.maxPlayers = message.chosenStudentNumber();
                new LobbyMessage("Server", this.playerNames);
                this.playMessagesReader.setNumOfPlayers(maxPlayers);
                broadcastGenericMessage("Waiting for other Players . . .");
            } else {
                Server.LOGGER.warning("Client has chosen a wrong number of players");
                virtualViewMap.get(message.getSenderName()).showError("Wrong number of players chosen, can be 2 or 3");
            }
        } else {
            Server.LOGGER.warning("Wrong message received from client.");
            virtualViewMap.get(receivedMessage.getSenderName()).showError("Wrong Message to be sent now");
        }
    }

    private void inGameState(Message receivedMessage) {
        PlayMessage message = (PlayMessage) receivedMessage;
        try {
            message.executeMessage(playMessagesReader);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            virtualViewMap.get(receivedMessage.getSenderName()).showError("Error while running message, please retry");
        }
    }

    private void setPlayState(PlayState playState) {
        this.playState = playState;
    }

    public void endGame() {
        StorageData storageData = new StorageData();
        storageData.delete();
        initGameController();
        Server.LOGGER.info("Server ready for a new Game");
    }

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
        } else {
            virtualView.showLoginResult(true, false);
        }
    }


    private void initGame() {
        setPlayState(PlayState.IN_GAME);
        playMessagesReader.startGame();
        //broadcastGenericMessage("All Players are connected. Main player : " + turnController.getActivePlayer() + "\n" + "Good Luck!!!");
        //VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
    }

    public void addVirtualView(String nickname, VirtualView virtualView) {
        virtualViewMap.put(nickname, virtualView);
    }

    public Map<String, VirtualView> getVirtualViewMap() {
        return virtualViewMap;
    }

    public void removeVirtualView(String nickname, boolean notifyEnabled) {
        VirtualView vv = virtualViewMap.remove(nickname);
        playMessagesReader.deletePlayer(nickname);
    }

    public void broadcastGenericMessage(String messageToNotify, String excludeNickname) {
        virtualViewMap.entrySet().stream()
                .filter(entry -> !excludeNickname.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(vv -> vv.showGenericMessage(messageToNotify));
    }

    public void broadcastGenericMessage(String messageToNotify) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showGenericMessage(messageToNotify);
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

    public boolean checkLoginNickname(String nickname, View view) {
        return InputController.checkLoginNickname(nickname, view, virtualViewMap.keySet());
    }


    public boolean isGameStarted() {
        return this.playState != PlayState.PRE_INIT;
    }


    public TurnController getTurnController() {
        return turnController;
    }

    public void update(Message message) {
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
        switch (message.getMessageType()) {
            case ERROR:
                ErrorMessage errMsg = (ErrorMessage) message;
                Server.LOGGER.warning(errMsg.getError());
                break;
            default:
                Server.LOGGER.warning("Invalid effect request!");
                break;
        }
    }

    public List<String> getPlayerNames() {
        return virtualViewMap.keySet().stream().toList();
    }

}
