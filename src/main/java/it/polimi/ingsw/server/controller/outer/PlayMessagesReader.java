package it.polimi.ingsw.server.controller.outer;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.ErrorMessage;
import it.polimi.ingsw.commons.message.Message;
import it.polimi.ingsw.commons.message.MessageReader;
import it.polimi.ingsw.commons.message.PlayMessagesFabric;
import it.polimi.ingsw.server.controller.inner.*;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.enums.Characters;
import it.polimi.ingsw.server.view.VirtualView;

import java.util.*;

/**
 * Executes PlayMessages and answers with other PlayMessages
 */
public class PlayMessagesReader implements MessageReader {

    private final InboundController inbound;
    private final OutboundController outbound;
    private final InputController inputController;
    private final TurnController turnController;
    private final GameManager gameManager;

    private int numOfPlayers;
    private final String mainPlayer;
    private final List<String> playerNames;
    private boolean expertVariant;
    private final String server;
    private transient Map<String, VirtualView> virtualViewMap;


    public PlayMessagesReader(String mainPlayer, GameManager gameManager) {
        this.mainPlayer = mainPlayer;
        this.playerNames = new ArrayList<>();
        this.playerNames.add(mainPlayer);
        this.turnController = new TurnController(this);
        this.inputController = new InputController(this);
        GameModel model = new GameModel();
        this.inbound = new InboundController(model, inputController);
        this.outbound = new OutboundController(model, inputController);
        this.server = "server";
        this.gameManager = gameManager;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        if (numOfPlayers != 2 && numOfPlayers != 3) throw new IllegalArgumentException();
        this.numOfPlayers = numOfPlayers;
    }

    public void addPlayer(String playerName) {
        inputController.controlGameState(GameState.INITIAL);
        inputController.addPlayer(playerName);
        this.playerNames.add(playerName);
    }

    public void deletePlayer(String playerName) {
        inputController.controlGameState(GameState.INITIAL);
        inputController.removePlayer(playerName);
        playerNames.remove(playerName);
    }

    public int getNumCurrPlayers() {
        return playerNames.size();
    }

    public void startGame() {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.startGame(playerNames);
            turnController.startPlay(new ArrayList<>(playerNames));
            answers.add(PlayMessagesFabric.statusPlanning(server, turnController.getActivePlayer()));
            List<String> islandIds = outbound.getAllIslandIds();
            answers.add(PlayMessagesFabric.statusIslandIds(server, islandIds));
            for(String islandId: islandIds){
                answers.add(PlayMessagesFabric.statusStudent(server, islandId, outbound.getStudentInPlace(mainPlayer, islandId)));
            }
            answers.add(PlayMessagesFabric.statusMotherNature(server, outbound.actualMotherNaturePosition()));
            for(String name: playerNames){
                answers.add(PlayMessagesFabric.statusStudent(server, "Entrance", outbound.getStudentInPlace(name, "Entrance")));
                answers.add(PlayMessagesFabric.statusStudent(server, "Room", outbound.getStudentInPlace(name, "Room")));
            }
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        answers.forEach(gameManager::broadcastMessage);
    }

    public void switchExpertVariant() {
        expertVariant = inbound.switchExpertVariant();
    }

    @Override
    public void playAssistantCard(String player, Integer weight) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.playAssistantCard(player, weight);
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusAssistantCard(server, player, weight));
        //turnController.nextTurn();
        if (turnController.nextTurn()) {
            answers.add(PlayMessagesFabric.statusAction(server, turnController.getActivePlayer()));
        } else {
            answers.add(PlayMessagesFabric.statusPlanning(server, turnController.getActivePlayer()));
        }
        answers.forEach(gameManager::broadcastMessage);
    }

    @Override
    public void moveStudent(String player, TeacherColor color, String fromId, String toId) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.moveStudent(player, color, fromId, toId);
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusStudent(server, toId, outbound.getStudentInPlace(player, toId)));
        answers.add(PlayMessagesFabric.statusStudent(server, fromId, outbound.getStudentInPlace(player, fromId)));
        answers.add(PlayMessagesFabric.statusTeacher(server, player, outbound.getTeacherInPlace(player)));
        answers.forEach(gameManager::broadcastMessage);
    }

    @Override
    public void moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.moveStudent(player, fromColor, toColor, placeId);
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusStudent(server, "Entrance", outbound.getStudentInPlace(player, "Entrance")));
        answers.add(PlayMessagesFabric.statusStudent(server, placeId, outbound.getStudentInPlace(player, placeId)));
        answers.forEach(gameManager::broadcastMessage);
    }

    @Override
    public void moveMotherNature(String player, Integer hops) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.moveMotherNature(player, hops);
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusMotherNature(server, outbound.actualMotherNaturePosition()));
        answers.forEach(gameManager::broadcastMessage);
    }

    @Override
    public void calcInfluence(String player) {
        List<Message> answers = new ArrayList<>();
        // command to change model
        try {
            inbound.calcInfluence(player);
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        // command to read model
        List<String> islandIds = outbound.getAllIslandIds();
        Map<String, Optional<TowerColor>> domina = new HashMap<>();
        for (String id : islandIds) {
            domina.put(id, outbound.getTowerInPlace(id));
        }
        // building answer list
        answers.add(PlayMessagesFabric.statusIslandIds(server, islandIds));
        answers.add(PlayMessagesFabric.statusTower(server, domina));
        answers.add(PlayMessagesFabric.statusStudent(
                server,
                outbound.actualMotherNaturePosition(),
                outbound.getStudentInPlace(player, outbound.actualMotherNaturePosition())));
        // sending answers
        answers.forEach(gameManager::broadcastMessage);
    }

    @Override
    public void chooseCloud(String player, String id) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.chooseCloud(player, id);
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusStudent(server, "Entrance", outbound.getStudentInPlace(player, "Entrance")));
        if (turnController.nextTurn()) {
            answers.add(PlayMessagesFabric.statusPlanning(server, getActualPlayer()));
            // show new clouds
        } else {
            answers.add(PlayMessagesFabric.statusAction(server, turnController.getActivePlayer()));
        }
        answers.forEach(gameManager::broadcastMessage);
    }

    @Override
    public void playCharacterCard(String player, Characters character) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.playCharacterCard(player, character);
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusCharacterCard(server, outbound.getActualCharacterCard()));
        answers.forEach(gameManager::broadcastMessage);
    }

    @Override
    public void playCharacterCard(String player, Characters character, String id) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.playCharacterCard(player, character, id);
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusCharacterCard(server, outbound.getActualCharacterCard()));
        answers.forEach(gameManager::broadcastMessage);
    }

    @Override
    public void playCharacterCard(String player, Characters character, TeacherColor color) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.playCharacterCard(player, character, color);
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusCharacterCard(server, outbound.getActualCharacterCard()));
        answers.forEach(gameManager::broadcastMessage);
    }

    @Override
    public void statusCharacterCard(String sender, Characters character) {
        errorIllegalMessage();
    }

    @Override
    public void statusAssistantCard(String sender, String player, Integer weight) {
        errorIllegalMessage();
    }

    @Override
    public void statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity) {
        errorIllegalMessage();
    }

    @Override
    public void statusTeacher(String sender, String id, List<TeacherColor> which) {
        errorIllegalMessage();
    }

    @Override
    public void statusTower(String sender, Map<String, Optional<TowerColor>> conquests) {
        errorIllegalMessage();
    }

    @Override
    public void statusIslandIds(String sender, List<String> ids) {
        errorIllegalMessage();
    }

    @Override
    public void statusMotherNature(String sender, String islandId) {
        errorIllegalMessage();
    }

    @Override
    public void statusAction(String sender, String actualPlayer) {
        errorIllegalMessage();
    }

    @Override
    public void statusPlanning(String sender, String actualPlayer) {
        errorIllegalMessage();
    }

    private void errorIllegalMessage() {
        Message error = new ErrorMessage(server, server + " should not receive this message");
        gameManager.broadcastMessage(error);
    }

    private void errorInExecution(String error) {
        Message horror = new ErrorMessage(server, error);
        gameManager.broadcastMessage(horror);
    }

    public TurnController getTurnController() {
        return turnController;
    }

    public InboundController getInbound(){
        return inbound;
    }

    public OutboundController getOutbound(){
        return outbound;
    }

    public GameState getState() {
        return turnController.getState();
    }

    public boolean isCharacterActive() {
        return turnController.isCharacterActive();
    }

    public List<String> getPlayersInOrder() {
        return outbound.getPlayersInOrder();
    }

    public String getActualPlayer() {
        return turnController.getActivePlayer();
    }

    public boolean isExpertVariant() {
        return expertVariant;
    }

    public List<String> getPlayerNames() {
        return new ArrayList<>(playerNames);
    }

    public String getMainPlayer() {
        return mainPlayer;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }
}
