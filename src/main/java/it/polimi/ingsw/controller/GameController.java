package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Game Controller which controls which commands are sent to the game mode
 * Making commands and getting state changes from the model itself
 * <br>Manages:<br>
 * &emsp Game initial player registration<br>
 * &emsp GameState: Initial, Pianification or Action<br>
 * &emsp Player order: if player send moves in the right order<br>
 * &emsp Player moves: runs commands and gets game state changes after each one<br>
 */
public class GameController {

    private final GameModel model;
    private final List<String> playerNames;
    private List<String> actionPlayerOrder;
    private boolean expertVariant;
    private String actualPlayer;
    private Characters actualCharacter;
    private GameState gameState;

    public GameController() {
        this.model = new GameModel();
        this.playerNames = new ArrayList<>();
        this.expertVariant = model.isExpertVariant();
        setInitialState();
    }

    private void setInitialState() {
        this.gameState = GameState.INITIAL;
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
        controlGameState(GameState.INITIAL);
        if (this.playerNames.contains(name)) throw new InvalidParameterException("Player name already present");
        if (this.playerNames.size() >= 3) throw new IllegalStateException("There are already 3 players");
        this.playerNames.add(name);
    }

    public void deletePlayer(String name) throws InvalidParameterException {
        controlGameState(GameState.INITIAL);
        if (!this.playerNames.contains(name)) throw new InvalidParameterException("Player name not found");
        this.playerNames.remove(name);
    }

    public List<String> startGame() throws IllegalStateException {
        controlGameState(GameState.INITIAL);
        if (playerNames.size() <= 1) throw new IllegalStateException("Not enough players");
        playerNames.forEach(model::addPlayer);
        model.startGame();
        gameState = GameState.next(gameState);
        actualPlayer = playerNames.get(0);
        return getAllIslandIds();
    }

    public String actualMotherNaturePosition() {
        excludeGameState(GameState.INITIAL);
        return model.getMotherNaturePosition();
    }

    public List<String> getAllIslandIds() {
        excludeGameState(GameState.INITIAL);
        return model.getAllIslandIds();
    }

    public void switchExpertVariant() {
        controlGameState(GameState.INITIAL);
        model.switchExpertVariant();
        this.expertVariant = model.isExpertVariant();
    }

    private void nextTurn() {
        if (gameState.equals(GameState.PLANNING)) {
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

    private void controlActualPlayer(String actualPlayer) throws InvalidParameterException {
        if (!playerNames.contains(actualPlayer))
            throw new InvalidParameterException("Player is not playing the game");
        if (!actualPlayer.equals(this.actualPlayer))
            throw new InvalidParameterException("Player is not the actual player");
    }

    private void controlExpertVariant() throws IllegalStateException {
        if (!expertVariant) throw new IllegalStateException("Game is not in Expert variant");
    }

    // Player Moves

    public int playAssistantCard(String playerName, int cardWeight) {
        controlGameState(GameState.PLANNING);
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
        controlGameState(GameState.ACTION);
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
                delta.put(destinationId, afterDestination.get(iColor) - beforeDestination.get(iColor));
            }
            if (!afterSource.get(iColor).equals(beforeSource.get(iColor))) {
                delta.put(sourceId, afterSource.get(iColor) - beforeSource.get(iColor));
            }
        }
        return delta;
    }

    public void moveStudent(String playerName, TeacherColor entranceStudent, TeacherColor otherStudent, String placeId) {
        controlExpertVariant();
        controlGameState(GameState.ACTION);
        controlActualPlayer(playerName);
        model.moveStudent(playerName, entranceStudent, otherStudent);
    }

    public String moveMotherNature(String playerName, int steps) {
        controlGameState(GameState.ACTION);
        controlActualPlayer(playerName);
        model.moveMotherNature(playerName, steps);
        return model.getMotherNaturePosition();
    }

    public void calcInfluence(String playerName) {
        controlGameState(GameState.ACTION);
        controlActualPlayer(playerName);
        model.calcInfluence(playerName);
    }

    public Map<TeacherColor, Integer> chooseCloud(String playerName, String cloudId) {
        controlGameState(GameState.ACTION);
        controlActualPlayer(playerName);
        if (!cloudId.contains("c_")) throw new InvalidParameterException("Gotten Id is not a cloud Id");
        model.chooseCloud(playerName, cloudId);
        nextTurn();
        return model.getStudentsInEntrance(playerName);
    }

    private void playCharacterCardPermit(String playerName) {
        controlExpertVariant();
        controlGameState(GameState.ACTION);
        controlActualPlayer(playerName);
    }

    public void playCharacterCard(String playerName, Characters character) {
        playCharacterCardPermit(playerName);
        model.playCharacterCard(playerName, character);
    }

    public void playCharacterCard(String playerName, Characters character, TeacherColor color) {
        playCharacterCardPermit(playerName);
        model.playCharacterCard(playerName, character, color);
    }

    public void playCharacterCard(String playerName, Characters character, String islandId) {
        playCharacterCardPermit(playerName);
        model.playCharacterCard(playerName, character, islandId);
    }

    public Map<TeacherColor, Integer> getStudentInPlace(String playerName, String placeId){
        if(getAllIslandIds().contains(placeId)) return model.getStudentsInIsland(placeId);
        if(placeId.equals("Entrance")) return model.getStudentsInEntrance(playerName);
        if(placeId.equals("Room")) return model.getStudentsInRoom(playerName);
        else return new HashMap<>();
    }

    public Optional<TowerColor> getTowerInPlace(String placeId){
        if(!getAllIslandIds().contains(placeId)) return Optional.empty();
        return model.getTowerInIsland(placeId);
    }

    public List<TeacherColor> getTeacherInPlace(String playerName, String placeId){
        if(!placeId.equals("Room")) return new ArrayList<>();
        return model.getTeachersInRoom(playerName);
    }

    public String getMotherNaturePosition(){
        return model.getMotherNaturePosition();
    }

    public List<String> getPlayerNames(){
        return playerNames;
    }

    public int getPlayerTowers(String playerName){
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

    public boolean isStartGame()
    {
        return getModel().getGame().isStartGame();
    }

    public boolean checkLoginUser(String username)
    {
        return playerNames.contains(username);
    }
}
