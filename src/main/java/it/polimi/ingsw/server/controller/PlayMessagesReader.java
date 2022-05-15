package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.MessageReader;
import it.polimi.ingsw.commons.message.PlayMessage;
import it.polimi.ingsw.commons.message.PlayMessagesFabric;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.enums.Characters;

import java.util.*;

/**
 * Executes PlayMessages and answers with other PlayMessages
 */
public class PlayMessagesReader implements MessageReader {

    private int numOfPlayers;
    private final String mainPlayer;
    private final InboundController inbound;
    private final OutboundController outbound;
    private final InputController inputController;

    private final List<String> playerNames;
    private boolean expertVariant;
    private GameState gameState;
    private final TurnController turnController;
    private final String server;


    public PlayMessagesReader(String mainPlayer) {
        this.mainPlayer = mainPlayer;
        this.playerNames = new ArrayList<>();
        this.turnController = new TurnController(this);
        this.inputController = new InputController(this);
        GameModel model = new GameModel();
        this.inbound = new InboundController(model, inputController);
        this.outbound = new OutboundController(model, inputController);
        this.server = "server";
    }

    public void setNumOfPlayers(int numOfPlayers){
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

    public void startGame(){
        inbound.startGame(playerNames);
        turnController.startPlay(new ArrayList<>(playerNames));
    }

    public void switchExpertVariant() {
        expertVariant = inbound.switchExpertVariant();
    }

    @Override
    public void playAssistantCard(String player, int weight) {
        List<PlayMessage> answers = new ArrayList<>();
        inbound.playAssistantCard(player, weight);
        turnController.nextTurn();
    }

    @Override
    public void moveStudent(String player, TeacherColor color, String fromId, String toId) {
        List<PlayMessage> answers = new ArrayList<>();
        inbound.moveStudent(player, color, fromId, toId);
        outbound.getStudentInPlace(player, fromId);
        outbound.getStudentInPlace(player, toId);
    }

    @Override
    public void moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        List<PlayMessage> answers = new ArrayList<>();
        inbound.moveStudent(player, fromColor, toColor, placeId);
        outbound.getStudentInPlace(player, "Entrance");
        outbound.getStudentInPlace(player, placeId);
    }

    @Override
    public void moveMotherNature(String player, Integer hops) {
        List<PlayMessage> answers = new ArrayList<>();
        inbound.moveMotherNature(player, hops);
        answers.add(PlayMessagesFabric.statusMotherNature(server, outbound.actualMotherNaturePosition()));
    }

    @Override
    public void calcInfluence(String player) {
        List<PlayMessage> answers = new ArrayList<>();
        inbound.calcInfluence(player);
        List<String> islandIds = outbound.getAllIslandIds();
        Map<String, Optional<TowerColor>> domina = new HashMap<>();
        for(String id: islandIds){
            domina.put(id, outbound.getTowerInPlace(id));
        }
        answers.add(PlayMessagesFabric.statusIslandIds(server, islandIds));
        answers.add(PlayMessagesFabric.statusTower(server, domina));
        answers.add(PlayMessagesFabric.statusStudent(
                server,
                outbound.actualMotherNaturePosition(),
                outbound.getStudentInPlace(player, outbound.actualMotherNaturePosition())));
    }

    @Override
    public void chooseCloud(String player, String id) {
        List<PlayMessage> answers = new ArrayList<>();
        inbound.chooseCloud(player, id);
        answers.add(PlayMessagesFabric.statusStudent(server, "Entrance", outbound.getStudentInPlace(player, "Entrance")));
        if(turnController.nextTurn()){

        } else {
            answers.add(PlayMessagesFabric.statusAction(server, turnController.getActivePlayer()));
        }
    }

    @Override
    public void playCharacterCard(String player, Characters character) {
        List<PlayMessage> answers = new ArrayList<>();
        inbound.playCharacterCard(player, character);
        outbound.getActualCharacterCard();
    }

    @Override
    public void playCharacterCard(String player, Characters character, String id) {
        List<PlayMessage> answers = new ArrayList<>();
        inbound.playCharacterCard(player, character, id);
        outbound.getActualCharacterCard();
    }

    @Override
    public void playCharacterCard(String player, Characters character, TeacherColor color) {
        List<PlayMessage> answers = new ArrayList<>();
        inbound.playCharacterCard(player, character, color);
        outbound.getActualCharacterCard();
    }

    @Override
    public void statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity) {
        // ERROR
    }

    @Override
    public void statusTeacher(String sender, String id, List<TeacherColor> which) {
        // ERROR
    }

    @Override
    public void statusTower(String sender, Map<String, Optional<TowerColor>> conquests) {
        // ERROR
    }

    @Override
    public void statusIslandIds(String sender, List<String> ids) {
        // ERROR
    }

    @Override
    public void statusMotherNature(String sender, String islandId) {
        // ERROR
    }

    @Override
    public void statusAction(String sender, String actualPlayer) {
        // ERROR
    }

    @Override
    public void statusPlanning(String sender, String actualPlayer) {
        // ERROR
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

    public List<String> getPlayersInOrder(){
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
