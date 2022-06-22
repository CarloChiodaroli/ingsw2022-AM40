package it.polimi.ingsw.server.controller.outer;

import it.polimi.ingsw.commons.enums.Wizard;
import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.commons.message.play.PlayMessage;
import it.polimi.ingsw.server.controller.inner.InputController;
import it.polimi.ingsw.server.network.Server;
import it.polimi.ingsw.server.utils.StorageData;
import it.polimi.ingsw.server.network.VirtualView;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class manages all client-server communication not directly regarding the game.
 * Of the controller this is the outermost part, here is where all incoming messages of all types are managed.
 * After reading the type, the message will be treated as needed.
 * Login, Lobby, Error and Generic messages are treated by this class.
 * Play and Expert messages are forwarded to {@link PlayMessagesReader} class to be treated as needed by executing them.
 * Also disconnections and reconnections are managed here. A client reconnection will be done when a Login message comes with
 * the same name of a disconnected player.
 */
public class GameManager implements LobbyMessageReader {

    private transient Map<String, VirtualView> virtualViewMap;
    private String mainPlayer;
    private int maxPlayers;
    private List<String> playerNames;
    private PlayMessagesReader playMessagesReader = null;
    private static final String STR_INVALID_STATE = "Invalid game state!";
    public static final String SAVED_GAME_FILE = "match.log";
    private static final int DEFAULT_MAX_PLAYERS = 1;
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
        if (!playerNames.contains(receivedMessage.getSenderName())) {
            virtualViewMap.get(receivedMessage.getSenderName()).showError("You have not logged in");
        }
        if (playMessagesReader != null && !playMessagesReader.isGameStarted()) {
            try {
                preInitState(receivedMessage);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
                e.getCause().printStackTrace();
                virtualViewMap.get(receivedMessage.getSenderName()).showError("Server Could not understand received lobby message");
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
            virtualViewMap.get(message.getSenderName()).showError("You can't start the game");
            return;
        }
        if (playerNames.size() != maxPlayers) {
            virtualViewMap.get(message.getSenderName()).showError("There are not enough players");
            return;
        }
        if (assignedWizards.entrySet().size() != maxPlayers) {
            virtualViewMap.get(message.getSenderName()).showError("Not all players have chosen their wizards");
            return;
        }
        if (virtualViewMap.size() == maxPlayers && !playMessagesReader.isGameStarted())
            initGame();
        else
            virtualViewMap.get(message.getSenderName()).showError("Game already started");

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
            virtualViewMap.get(message.getSenderName()).showWizardConfirmation(true);
        } else {
            virtualViewMap.get(message.getSenderName()).showWizardConfirmation(false);
        }
        virtualViewMap.values().forEach(x -> x.sendAvailableWizards(availableWizards()));
    }

    @Override
    public void wizardList(LobbyMessage message) {
        // should not receive this
    }

    @Override
    public void numOfPlayers(LobbyMessage message) {
        if (maxPlayers != DEFAULT_MAX_PLAYERS) {
            virtualViewMap.get(message.getSenderName()).showError("Max number of players has been already set and it is: " + maxPlayers);
            return;
        }
        if (!message.getSenderName().equals(mainPlayer)) {
            virtualViewMap.get(message.getSenderName()).showError("You can't set the max number of players");
            return;
        }
        maxPlayers = message.getMaxPlayers();
        virtualViewMap.values().forEach(x -> x.showNumOfPlayers(maxPlayers));
    }

    @Override
    public void disconnection(LobbyMessage message) {
        // should not receive this
    }

    @Override
    public void expert(LobbyMessage message) {
        if (playMessagesReader == null || playMessagesReader.isGameStarted() || !message.getSenderName().equals(mainPlayer)) {
            virtualViewMap.get(message.getSenderName()).showError("Player can't set nor unset expert variant");
            return;
        }
        if (message.isExpert() != playMessagesReader.isExpertVariant()) {
            playMessagesReader.switchExpertVariant();
        }
        virtualViewMap.values().forEach(x -> x.showExpertVariant(playMessagesReader.isExpertVariant()));
    }

    // LOBBY message Utility

    private void initGame() {
        playMessagesReader.startGame();
    }

    // PLAY message part

    private void inGameState(Message receivedMessage) {
        if(playMessagesReader.isStopped()){
            virtualViewMap.get(receivedMessage.getSenderName()).showError("Game is stopped, server won't run this command");
            return;
        }
        PlayMessage message = (PlayMessage) receivedMessage;
        try {
            message.executeMessage(playMessagesReader);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            virtualViewMap.get(receivedMessage.getSenderName()).showError("Error while running message, please retry");
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
                virtualView.sendAvailableWizards(availableWizards());
                virtualView.sendMainPlayer(mainPlayer);
                virtualView.showExpertVariant(playMessagesReader.isExpertVariant());
            } else {
                if(this.maxPlayers == DEFAULT_MAX_PLAYERS){
                    virtualView.showError("Main Player needs to choose how many players can Login");
                    virtualView.showLoginResult(false, true);
                    return;
                }
                if (virtualViewMap.size() < maxPlayers) {
                    addVirtualView(nickname, virtualView);
                    this.playerNames.add(nickname);
                    this.playMessagesReader.addPlayer(nickname);
                    virtualView.showLoginResult(true, true);
                    virtualView.sendAvailableWizards(availableWizards());
                    virtualView.sendMainPlayer(mainPlayer);
                    if (maxPlayers != DEFAULT_MAX_PLAYERS)
                        virtualView.showNumOfPlayers(maxPlayers);
                    virtualView.showExpertVariant(playMessagesReader.isExpertVariant());
                } else {
                    virtualView.showLoginResult(true, false);
                }
            }
        } else {
            if(playMessagesReader.getPlayerNames().contains(nickname)){
                if(virtualViewMap.containsKey(nickname)){
                    virtualView.showLoginResult(true, false);
                } else {
                    addVirtualView(nickname, virtualView);
                    this.playerNames.add(nickname);
                    virtualView.showLoginResult(true, true);
                    virtualView.showAssignedWizard(assignedWizards.get(nickname));
                    playMessagesReader.unStopPlayer(nickname);
                    playMessagesReader.sendCompleteGameStatus(nickname);
                    playMessagesReader.sendStatus();
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

    public boolean removeVirtualView(String nickname, boolean notifyEnabled) {
        playerNames.remove(nickname);
        if(!virtualViewMap.containsKey(nickname)) {
            return virtualViewMap.isEmpty();
        }
        if (playMessagesReader.isGameStarted()) {
            playMessagesReader.stopPlayer(nickname);
            virtualViewMap.remove(nickname);
            if(virtualViewMap.size() == 1) playMessagesReader.stop();
            else playMessagesReader.sendStatus();
        } else {
            playMessagesReader.deletePlayer(nickname);
            assignedWizards.remove(nickname);
            virtualViewMap.remove(nickname);
        }
        virtualViewMap.values().forEach(x -> x.showDisconnectionMessage(nickname, "Disconnected from the server"));
        return virtualViewMap.isEmpty();
    }

    public void sendMessage(String playerName, Message message) {
        virtualViewMap.entrySet().stream()
                .filter(x -> x.getKey().equals(playerName))
                .findAny()
                .map(Map.Entry::getValue)
                .ifPresent(x -> x.sendMessage(message));
    }

    public void broadcastMessage(Message message) {
        virtualViewMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .forEach(x -> x.sendMessage(message));
    }

    public boolean checkLoginNickname(String nickname, VirtualView view) {
        if (nickname.isEmpty() || nickname.equalsIgnoreCase("server")) {
            view.showError("Forbidden name");
            view.showLoginResult(false, true);
            return false;
        } else if (virtualViewMap.containsKey(nickname)) {
            view.showError("Nickname already taken");
            view.showLoginResult(false, true);
            return false;
        }
        return true;
    }

    public boolean isGameStarted() {
        return playMessagesReader != null && playMessagesReader.isGameStarted();
    }

    public List<String> getPlayerNames() {
        return virtualViewMap.keySet().stream().toList();
    }
}
