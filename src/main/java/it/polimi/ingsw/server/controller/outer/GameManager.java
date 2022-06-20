package it.polimi.ingsw.server.controller.outer;

import it.polimi.ingsw.commons.enums.Wizard;
import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.commons.message.play.PlayMessage;
import it.polimi.ingsw.server.controller.inner.InputController;
import it.polimi.ingsw.server.network.Server;
import it.polimi.ingsw.server.utils.StorageData;
import it.polimi.ingsw.server.view.VirtualView;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;


public class GameManager implements LobbyMessageReader {

    private transient Map<String, VirtualView> virtualViewMap;
    private String mainPlayer;
    private int maxPlayers;
    private List<String> playerNames;
    private PlayMessagesReader playMessagesReader = null;
    private static final String STR_INVALID_STATE = "Invalid game state!";
    public static final String SAVED_GAME_FILE = "match.log";
    private static final int DEFAULT_MAX_PLAYERS = 99;
    private final Map<String, Wizard> assignedWizards;
    private String server = "server";


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
        if (!playerNames.contains(receivedMessage.getSenderName())) {
            sendMessage(receivedMessage.getSenderName(), new ErrorMessage(server, "You have not logged in"));
        }
        if (playMessagesReader != null && !playMessagesReader.isGameStarted()) {
            try {
                preInitState(receivedMessage);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                e.getCause().printStackTrace();
                sendError(receivedMessage.getSenderName(), "Server Could not understand received lobby message");
            }
            return;
        }
        if (playMessagesReader != null && playMessagesReader.isGameStarted() && (receivedMessage.getMessageType().equals(MessageType.PLAY) || receivedMessage.getMessageType().equals(MessageType.EXPERT))) {
            inGameState(receivedMessage);
            return;
        }
        Server.LOGGER.warning(STR_INVALID_STATE);
    }

    // LOBBY message part

    private void preInitState(Message receivedMessage) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (receivedMessage.getMessageType() == MessageType.LOBBY) {
            LobbyMessage message = (LobbyMessage) receivedMessage;
            LobbyMessageReader.class.getMethod(message.getCommand(), LobbyMessage.class).invoke(this, message);
        }
    }

    @Override
    public void startGame(LobbyMessage message) {
        if (!message.getSenderName().equals(mainPlayer)) {
            sendError(message.getSenderName(), "You can't start the game");
            return;
        }
        if (playerNames.size() != maxPlayers) {
            sendError(message.getSenderName(), "There are not enough players");
            return;
        }
        if (assignedWizards.entrySet().size() != maxPlayers) {
            sendError(message.getSenderName(), "Not all players have chosen their wizards");
            return;
        }
        if (virtualViewMap.size() == maxPlayers && !playMessagesReader.isGameStarted())
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
        if (assignedWizards.entrySet().stream().noneMatch(x -> x.getValue().equals(message.getWizard()))) {
            if (!assignedWizards.containsKey(message.getSenderName())) {
                assignedWizards.put(message.getSenderName(), message.getWizard());
            } else {
                assignedWizards.replace(message.getSenderName(), message.getWizard());
            }
            sendMessage(message.getSenderName(), new LobbyMessage(server, "wizard", true));
        } else {
            sendMessage(message.getSenderName(), new LobbyMessage(server, "wizard", false));
        }
        broadcastMessage(new LobbyMessage(server, "wizardList", availableWizards()), message.getSenderName());
    }

    @Override
    public void wizardList(LobbyMessage message) {
        // should not receive this
    }

    @Override
    public void numOfPlayers(LobbyMessage message) {
        if (maxPlayers != DEFAULT_MAX_PLAYERS) {
            sendError(message.getSenderName(), "Max number of players has been already set and it is: " + maxPlayers);
            return;
        }
        if (!message.getSenderName().equals(mainPlayer)) {
            sendError(message.getSenderName(), "You can't set the max number of players");
            return;
        }
        maxPlayers = message.getMaxPlayers();
        broadcastMessage(new LobbyMessage(server, "numOfPlayers", maxPlayers));
    }

    @Override
    public void disconnection(LobbyMessage message) {
        // should not receive this
    }

    @Override
    public void expert(LobbyMessage message) {
        if (playMessagesReader == null || playMessagesReader.isGameStarted() || !message.getSenderName().equals(mainPlayer)) {
            sendError(message.getSenderName(), "Player can't set nor unset expert variant");
            return;
        }
        if (message.isExpert() != playMessagesReader.isExpertVariant()) {
            playMessagesReader.switchExpertVariant();
        }
        broadcastMessage(new LobbyMessage(server, "expert", playMessagesReader.isExpertVariant()));
    }

    // LOBBY message Utility

    private void initGame() {
        playMessagesReader.startGame();
    }

    // PLAY message part

    private void inGameState(Message receivedMessage) {
        if(playMessagesReader.isStopped())
            sendMessage(receivedMessage.getSenderName(), new ErrorMessage(server, "Game is stopped, server won't run this command"));
        PlayMessage message = (PlayMessage) receivedMessage;
        try {
            message.executeMessage(playMessagesReader);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            sendError(message.getSenderName(), "Error while running message, please retry");
        }
    }

    // LOGIN message part

    public void loginHandler(String nickname, VirtualView virtualView) {
        if (playMessagesReader == null || !playMessagesReader.isGameStarted()) { // game not started
            if (virtualViewMap.isEmpty()) { // First player logged. Ask number of players.
                addVirtualView(nickname, virtualView);
                this.playerNames.add(nickname);
                this.mainPlayer = nickname;
                this.playMessagesReader = new PlayMessagesReader(mainPlayer, this);
                virtualView.showLoginResult(true, true);
                sendMessage(nickname, new LobbyMessage(server, "wizardList", availableWizards()));
                virtualView.sendMainPlayer(mainPlayer);
            } else if (virtualViewMap.size() < maxPlayers) {
                addVirtualView(nickname, virtualView);
                this.playerNames.add(nickname);
                this.playMessagesReader.addPlayer(nickname);
                virtualView.showLoginResult(true, true);
                sendMessage(nickname, new LobbyMessage(server, "wizardList", availableWizards()));
                virtualView.sendMainPlayer(mainPlayer);
                if (maxPlayers != DEFAULT_MAX_PLAYERS)
                    sendMessage(nickname, new LobbyMessage(server, "numOfPlayers", maxPlayers));
                sendMessage(nickname, new LobbyMessage(server, "expert", playMessagesReader.isExpertVariant()));
            } else {
                virtualView.showLoginResult(true, false);
            }
        } else {
            if(playMessagesReader.getPlayerNames().contains(nickname)){
                if(virtualViewMap.containsKey(nickname)){
                    virtualView.showLoginResult(true, false);
                } else {
                    addVirtualView(nickname, virtualView);
                    this.playerNames.add(nickname);
                    virtualView.showLoginResult(true, true);
                    sendMessage(nickname, new LobbyMessage(server, assignedWizards.get(nickname)));
                    playMessagesReader.sendCompleteGameStatus(nickname);
                    if(virtualViewMap.keySet().size() == playMessagesReader.getPlayerNames().size()){
                        playMessagesReader.unStop();
                    }
                }
            } else {
                virtualView.showLoginResult(true, false);
            }
        }
    }

    public void addVirtualView(String nickname, VirtualView virtualView) {
        virtualViewMap.put(nickname, virtualView);
    }

    private List<Wizard> availableWizards() {
        return Arrays.stream(Wizard.values())
                .filter(x -> !assignedWizards.values().stream()
                        .toList()
                        .contains(x))
                .collect(Collectors.toList());
    }

    // Resilience

    public void endGame() {
        StorageData storageData = new StorageData();
        storageData.delete();
        initGameController();
        Server.LOGGER.info("Server ready for a new Game");
    }

    // outbound communication utilities

    public Map<String, VirtualView> getVirtualViewMap() {
        return virtualViewMap;
    }

    public void removeVirtualView(String nickname, boolean notifyEnabled) {
        virtualViewMap.remove(nickname);
        if (playMessagesReader.isGameStarted()) {
            playMessagesReader.stopPlayer(nickname);
        } else {
            playMessagesReader.deletePlayer(nickname);
            assignedWizards.remove(nickname);
        }
        virtualViewMap.values().forEach(x -> x.showDisconnectionMessage(nickname, "Disconnected from the server - waiting to reconnect"));
        if(virtualViewMap.size() == 1) playMessagesReader.stop();
    }

    public void sendMessage(String playerName, Message message) {
        virtualViewMap.entrySet().stream()
                .filter(x -> x.getKey().equals(playerName))
                .findAny()
                .map(Map.Entry::getValue)
                .ifPresent(x -> x.sendMessage(message));
    }

    public void sendError(String playerName, String message) {
        virtualViewMap.entrySet().stream()
                .filter(x -> x.getKey().equals(playerName))
                .findFirst()
                .map(Map.Entry::getValue)
                .ifPresent(x -> x.showError(message));
    }

    public void broadcastMessage(Message message) {
        virtualViewMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .forEach(x -> x.sendMessage(message));
    }

    public void broadcastMessage(Message message, String exceptPlayer) {
        virtualViewMap.entrySet().stream()
                .filter(entry -> !entry.getKey().equals(exceptPlayer))
                .map(Map.Entry::getValue)
                .forEach(x -> x.sendMessage(message));
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
