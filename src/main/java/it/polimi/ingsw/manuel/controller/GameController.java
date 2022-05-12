package it.polimi.ingsw.manuel.controller;

import it.polimi.ingsw.manuel.model.Game;
import it.polimi.ingsw.manuel.model.GameState;
import it.polimi.ingsw.manuel.model.Player;
import it.polimi.ingsw.manuel.network.message.ErrorMessage;
import it.polimi.ingsw.manuel.network.message.Message;
import it.polimi.ingsw.manuel.network.message.MessageType;
import it.polimi.ingsw.manuel.network.message.PlayerNumberReply;
import it.polimi.ingsw.manuel.network.server.Server;
import it.polimi.ingsw.manuel.observer.Observer;
import it.polimi.ingsw.manuel.utils.StorageData;
import it.polimi.ingsw.manuel.view.View;
import it.polimi.ingsw.manuel.view.VirtualView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class GameController implements Observer {

    private Game game;
    private transient Map<String, VirtualView> virtualViewMap;
    private GameState gameState;
    private TurnController turnController;
    private InputController inputController;
    private static final String STR_INVALID_STATE = "Invalid game state!";
    public static final String SAVED_GAME_FILE = "match.log";


    public GameController()
    {
        initGameController();
    }

    public void initGameController()
    {
        this.game = Game.getInstance();
        this.virtualViewMap = Collections.synchronizedMap(new HashMap<>());
        this.inputController = new InputController(virtualViewMap, this);
        setGameState(GameState.LOGIN);
    }

    public void onMessageReceived(Message receivedMessage) {

        VirtualView virtualView = virtualViewMap.get(receivedMessage.getNickname());
        switch (gameState) {
            case LOGIN:
                loginState(receivedMessage);
                break;
            case INIT:
                if (inputController.checkUser(receivedMessage)) {
                    initState(receivedMessage, virtualView);
                }
                break;
            case IN_GAME:
                if (inputController.checkUser(receivedMessage)) {
                    inGameState(receivedMessage);
                }
                break;
            default:
                Server.LOGGER.warning(STR_INVALID_STATE);
                break;
        }
    }

    private void loginState(Message receivedMessage) {
        if (receivedMessage.getMessageType() == MessageType.PLAYERNUMBER_REPLY) {
            if (inputController.verifyReceivedData(receivedMessage)) {
                game.setChosenMaxPlayers(((PlayerNumberReply) receivedMessage).getPlayerNumber());
                broadcastGenericMessage("Waiting for other Players . . .");
            }
        } else {
            Server.LOGGER.warning("Wrong message received from client.");
        }
    }

    private void initState(Message receivedMessage, VirtualView virtualView) {
        switch (receivedMessage.getMessageType()) {
            //INSERIRIRE IN SWITCH LE FASI DI INIZIALIZZAZIONE DEL GIOCO
        }
    }

    private void inGameState(Message receivedMessage) {}

    private void setGameState(GameState gameState) {
        this.gameState = gameState;
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
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true, Game.SERVER_NICKNAME);
            virtualView.askPlayersNumber();
        } else if (virtualViewMap.size() < game.getChosenPlayersNumber()) {
            addVirtualView(nickname, virtualView);
            game.addPlayer(new Player(nickname));
            virtualView.showLoginResult(true, true, Game.SERVER_NICKNAME);
            if (game.getNumCurrentPlayers() == game.getChosenPlayersNumber()) { // If all players logged
                // check saved matches.
                StorageData storageData = new StorageData();
                GameController savedGameController = storageData.restore();
                if (savedGameController != null &&
                        game.getPlayersNicknames().containsAll(savedGameController.getTurnController().getNicknameQueue())) {
                    Server.LOGGER.info("Saved Match restored.");
                    turnController.newTurn();
                } else {
                    initGame();
                }
            }
        } else {
            virtualView.showLoginResult(true, false, Game.SERVER_NICKNAME);
        }
    }


    private void initGame() {
        setGameState(GameState.INIT);
        turnController = new TurnController(virtualViewMap, this);
        broadcastGenericMessage("All Players are connected. Main player : " + turnController.getActivePlayer()+"\n"+"Good Luck!!!");
        VirtualView virtualView = virtualViewMap.get(turnController.getActivePlayer());
    }

    public void addVirtualView(String nickname, VirtualView virtualView) {
        virtualViewMap.put(nickname, virtualView);
        game.addObserver(virtualView);
    }

    public Map<String, VirtualView> getVirtualViewMap() {
        return virtualViewMap;
    }

    public void removeVirtualView(String nickname, boolean notifyEnabled) {
        VirtualView vv = virtualViewMap.remove(nickname);
        game.removeObserver(vv);
        game.removePlayerByNickname(nickname, notifyEnabled);
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

    public void broadcastDisconnectionMessage(String nicknameDisconnected, String text) {
        for (VirtualView vv : virtualViewMap.values()) {
            vv.showDisconnectionMessage(nicknameDisconnected, text);
        }
    }

    public boolean checkLoginNickname(String nickname, View view) {
        return inputController.checkLoginNickname(nickname, view);
    }


    public boolean isGameStarted() {
        return this.gameState != GameState.LOGIN;
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


}
