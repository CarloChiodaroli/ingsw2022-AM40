package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.TeacherColor;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    private final GameModel model;
    private final List<String> playerNames;
    private List<String> actionPlayerOrder;
    private boolean expertVariant;
    private String actualPlayer;
    private GameState gameState;

    public GameController() {
        this.model = new GameModel();
        this.playerNames = new ArrayList<>();
        this.expertVariant = model.isExpertVariant();
        setInitialState();
    }

    private void setInitialState() {
        this.gameState = GameState.PREPARATION;
    }

    private void controlGameState(GameState required) throws IllegalStateException {
        if (!gameState.equals(required))
            throw new IllegalStateException("Actual state is " + gameState + " when " + required + " is required");
    }

    private void excludeGameState(GameState exclude) throws IllegalStateException {
        if (gameState.equals(exclude))
            throw new IllegalStateException("Actual state is " + gameState + " and it's illegal for this action");
    }

    public void addPlayer(String name) throws InvalidParameterException, IllegalStateException {
        controlGameState(GameState.PREPARATION);
        if (this.playerNames.contains(name)) throw new InvalidParameterException("Player name already present");
        if (this.playerNames.size() >= 3) throw new IllegalStateException("There are already 3 players");
        this.playerNames.add(name);
    }

    public void deletePlayer(String name) throws InvalidParameterException {
        controlGameState(GameState.PREPARATION);
        if (!this.playerNames.contains(name)) throw new InvalidParameterException("Player name not found");
        this.playerNames.remove(name);
    }

    public Map<String, Map<TeacherColor, Integer>> startGame() throws IllegalStateException {
        controlGameState(GameState.PREPARATION);
        if (playerNames.size() <= 1) throw new IllegalStateException("Not enough players");
        playerNames.forEach(model::addPlayer);
        model.startGame();
        gameState = GameState.next(gameState);
        return getAllIslands();
    }

    public String actualMotherNaturePosition() {
        excludeGameState(GameState.PREPARATION);
        return model.getMotherNaturePosition();
    }

    public Map<String, Map<TeacherColor, Integer>> getAllIslands() {
        excludeGameState(GameState.PREPARATION);
        return model.getAllIslands();
    }

    public void switchExpertVariant() {
        controlGameState(GameState.PREPARATION);
        model.switchExpertVariant();
        this.expertVariant = model.isExpertVariant();
    }

    private boolean isThreePlayerGame() {
        return model.isThreePlayerGame();
    }

    private void nextTurn() {
        if (gameState.equals(GameState.PREPARATION)) {
            int i = playerNames.indexOf(actualPlayer) + 1;
            if (i >= playerNames.size()) {
                gameState = GameState.next(gameState);
                actionPlayerOrder = model.getPlayersInOrder();
                actualPlayer = actionPlayerOrder.get(0);
            } else {
                actualPlayer = playerNames.get(i);
            }
        } else if (gameState.equals(GameState.ACTION)) {
            int i = actionPlayerOrder.indexOf(actualPlayer) + 1;
            if (i >= actionPlayerOrder.size()) {
                gameState = GameState.next(gameState);
                actualPlayer = playerNames.get(0);
            } else {
                actualPlayer = actionPlayerOrder.get(i);
            }
        }
    }

    private void controlActualPlayer(String actualPlayer) {
        if (!playerNames.contains(actualPlayer))
            throw new InvalidParameterException("Player is not playing the game");
        if (!actualPlayer.equals(this.actualPlayer))
            throw new InvalidParameterException("Player is not the actual player");
    }

    // Player Moves

    public int playAssistantCard(String playerName, int cardWeight) {
        controlGameState(GameState.PIANIFICATION);
        controlActualPlayer(playerName);
        model.playAssistantCard(playerName, cardWeight);
        nextTurn();
        return cardWeight;
    }

    private Map<TeacherColor, Integer> getStudentContainerStateFromId(String playerName, String Id) {
        controlActualPlayer(playerName);
        switch (Id) {
            case "Entrance" -> {
                return model.getStudentsInEntrance(playerName);
            }
            case "Room" -> {
                return model.getStudentsInRoom(playerName);
            }
            default -> {
                if (Id.contains("i_")) return model.getStudentsInIsland(Id);
                else if (Id.contains("c_")) return model.getStudentsInCloud(Id);
                else throw new InvalidParameterException("Invalid Parameter");
            }
        }
    }

    public Map<String, Integer> moveStudent(String playerName, TeacherColor color, String sourceId, String destinationId) {
        Map<TeacherColor, Integer> beforeDestination;
        Map<TeacherColor, Integer> beforeSource;
        Map<TeacherColor, Integer> afterDestination;
        Map<TeacherColor, Integer> afterSource;
        Map<String, Integer> delta = new HashMap<>();
        controlActualPlayer(playerName);
        if (destinationId.contains("i") || destinationId.equals("Room")) {
            beforeDestination = getStudentContainerStateFromId(playerName, destinationId);
        } else {
            throw new InvalidParameterException("Illegal destination id");
        }
        if (sourceId.equals("Entrance")) {
            beforeSource = getStudentContainerStateFromId(playerName, sourceId);
        } else {
            throw new InvalidParameterException("Illegal source id");
        }
        model.moveStudent(playerName, color, sourceId, destinationId);
        afterDestination = getStudentContainerStateFromId(playerName, destinationId);
        afterSource = getStudentContainerStateFromId(playerName, sourceId);
        for (TeacherColor iColor : TeacherColor.values()) {
            if (!afterDestination.get(iColor).equals(beforeDestination.get(iColor))) {
                delta.put(destinationId, beforeDestination.get(iColor) - afterDestination.get(iColor));
            }
            if (!afterSource.get(iColor).equals(beforeSource.get(iColor))) {
                delta.put(sourceId, beforeSource.get(iColor) - afterSource.get(iColor));
            }
        }
        return delta;
    }

    public void moveMotherNature(String playerName, int steps) {

    }

    public Map<TeacherColor, Integer> chooseCloud(String playerName, String cloudId) {
        controlActualPlayer(playerName);
        if (!cloudId.contains("c_")) throw new InvalidParameterException("Gotten Id is not a cloud Id");
        model.chooseCloud(playerName, cloudId);
        nextTurn();
        return model.getStudentsInEntrance(playerName);
    }


}
