package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.enums.Characters;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Game Controller which controls which commands are sent to the game mode
 * Making commands and getting state changes from the model itself
 * <br>Manages:<br>
 * &emsp Game initial player registration<br>
 * &emsp PlayState: Initial, Pianification or Action<br>
 * &emsp Player order: if player send moves in the right order<br>
 * &emsp Player moves: runs commands and gets game state changes after each one<br>
 */
public class GameController {

    private final GameModel model;
    private final List<String> playerNames;
    private boolean expertVariant;
    private GameState gameState;
    private final TurnController turnController;

    public GameController() {
        this.model = new GameModel();
        this.playerNames = new ArrayList<>();
        this.expertVariant = model.isExpertVariant();
        this.turnController = new TurnController(this);
        turnController.setInitialState();
    }

    public TurnController getTurnController(){
        return turnController;
    }

    public GameState getState(){
        return turnController.getState();
    }

    public boolean isCharacterActive(){
        return turnController.isCharacterActive();
    }

    public void addPlayer(String name) throws InvalidParameterException, IllegalStateException {
        InputController.controlGameState(this, GameState.INITIAL);
        InputController.addPlayer(this, name);
        this.playerNames.add(name);
    }

    public void deletePlayer(String name) throws InvalidParameterException {
        InputController.controlGameState(this, GameState.INITIAL);
        InputController.removePlayer(this, name);
        this.playerNames.remove(name);
    }

    public void switchExpertVariant() {
        InputController.controlGameState(this, GameState.INITIAL);
        model.switchExpertVariant();
        this.expertVariant = model.isExpertVariant();
    }

    public void startGame() throws IllegalStateException {
        InputController.controlGameState(this, GameState.INITIAL);
        if (playerNames.size() <= 1) throw new IllegalStateException("Not enough players");
        playerNames.forEach(model::addPlayer);
        model.startGame();
        turnController.startPlay(new ArrayList<>(playerNames));
    }

    public String actualMotherNaturePosition() {
        InputController.excludeGameState(this, GameState.INITIAL);
        return model.getMotherNaturePosition();
    }

    public List<String> getAllIslandIds() {
        InputController.excludeGameState(this, GameState.INITIAL);
        return model.getAllIslandIds();
    }

    private void nextTurn() {
        turnController.nextTurn();
    }

    public List<String> getPlayersInOrder(){
        return model.getPlayersInOrder();
    }

    public String getActualPlayer() {
        return turnController.getActivePlayer();
    }

    public boolean isExpertVariant() {
        return expertVariant;
    }

    // Player Moves

    public int playAssistantCard(String playerName, int cardWeight) {
        InputController.controlGameState(this, GameState.PLANNING);
        InputController.controlActualPlayer(this, playerName);
        model.playAssistantCard(playerName, cardWeight);
        nextTurn();
        return cardWeight;
    }

    private Map<TeacherColor, Integer> getStudentContainerStateFromId(String playerName, String Id) {
        InputController.controlActualPlayer(this, playerName);
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

    public void moveStudent(String playerName, TeacherColor color, String sourceId, String destinationId) {
        InputController.controlGameState(this, GameState.ACTION);
        InputController.controlActualPlayer(this, playerName);
        model.moveStudent(playerName, color, sourceId, destinationId);
    }

    public void moveStudent(String playerName, TeacherColor entranceStudent, TeacherColor otherStudent, String placeId) {
        InputController.controlExpertVariant(this);
        InputController.controlGameState(this, GameState.ACTION);
        InputController.controlActualPlayer(this, playerName);
        model.moveStudent(playerName, entranceStudent, otherStudent);
    }

    public String moveMotherNature(String playerName, int steps) {
        InputController.controlGameState(this, GameState.ACTION);
        InputController.controlActualPlayer(this, playerName);
        model.moveMotherNature(playerName, steps);
        return model.getMotherNaturePosition();
    }

    public void calcInfluence(String playerName) {
        InputController.controlGameState(this, GameState.ACTION);
        InputController.controlActualPlayer(this, playerName);
        model.calcInfluence(playerName);
    }

    public Map<TeacherColor, Integer> chooseCloud(String playerName, String cloudId) {
        InputController.controlGameState(this, GameState.ACTION);
        InputController.controlActualPlayer(this, playerName);
        if (!cloudId.contains("c_")) throw new InvalidParameterException("Gotten Id is not a cloud Id");
        model.chooseCloud(playerName, cloudId);
        nextTurn();
        return model.getStudentsInEntrance(playerName);
    }

    public void playCharacterCard(String playerName, Characters character) {
        InputController.playCharacterCardPermit(this, playerName);
        model.playCharacterCard(playerName, character);
    }

    public void playCharacterCard(String playerName, Characters character, TeacherColor color) {
        InputController.playCharacterCardPermit(this, playerName);
        model.playCharacterCard(playerName, character, color);
    }

    public void playCharacterCard(String playerName, Characters character, String islandId) {
        InputController.playCharacterCardPermit(this, playerName);
        model.playCharacterCard(playerName, character, islandId);
    }

    public Map<TeacherColor, Integer> getStudentInPlace(String playerName, String placeId) {
        if (getAllIslandIds().contains(placeId)) return model.getStudentsInIsland(placeId);
        if (placeId.equals("Entrance")) return model.getStudentsInEntrance(playerName);
        if (placeId.equals("Room")) return model.getStudentsInRoom(playerName);
        else return new HashMap<>();
    }

    public Optional<TowerColor> getTowerInPlace(String placeId) {
        if (!getAllIslandIds().contains(placeId)) return Optional.empty();
        return model.getTowerInIsland(placeId);
    }

    public List<TeacherColor> getTeacherInPlace(String playerName, String placeId) {
        if (!placeId.equals("Room")) return new ArrayList<>();
        return model.getTeachersInRoom(playerName);
    }

    public List<String> getPlayerNames() {
        return new ArrayList<>(playerNames);
    }

    public int getPlayerTowers(String playerName) {
        return 0;
    }

    public GameModel getModel() {
        return model;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isStartGame() {
        return getModel().getGame().isStartGame();
    }

    public boolean checkLoginUser(String username) {
        return playerNames.contains(username);
    }
}
