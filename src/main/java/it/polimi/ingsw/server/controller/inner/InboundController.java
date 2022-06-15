package it.polimi.ingsw.server.controller.inner;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.commons.enums.Characters;

import java.security.InvalidParameterException;
import java.util.List;

public class InboundController {

    private final GameModel model;
    private final InputController inputController;

    public InboundController(GameModel model, InputController inputController) {
        this.model = model;
        this.inputController = inputController;
    }

    public void startGame(List<String> playerNames) throws IllegalStateException {
        inputController.controlGameState(GameState.INITIAL);
        if (playerNames.size() <= 1) throw new IllegalStateException("Not enough players");
        playerNames.forEach(model::addPlayer);
        model.startGame();
    }

    public void playAssistantCard(String playerName, int cardWeight) {
        inputController.controlGameState(GameState.PLANNING);
        inputController.controlActualPlayer(playerName);
        model.playAssistantCard(playerName, cardWeight);
    }

    public void moveStudent(String playerName, TeacherColor color, String sourceId, String destinationId) {
        inputController.controlGameState(GameState.ACTION);
        inputController.controlActualPlayer(playerName);
        inputController.controlSourceId(sourceId);
        inputController.controlDestinationId(destinationId);
        model.moveStudent(playerName, color, sourceId, destinationId);
    }

    public void moveStudent(String playerName, TeacherColor entranceStudent, TeacherColor otherStudent, String placeId) {
        inputController.controlExpertVariant();
        inputController.controlGameState(GameState.ACTION);
        inputController.controlActualPlayer(playerName);
        model.moveStudent(playerName, entranceStudent, otherStudent, placeId);
    }

    public String moveMotherNature(String playerName, int steps) {
        inputController.controlGameState(GameState.ACTION);
        inputController.controlActualPlayer(playerName);
        model.moveMotherNature(playerName, steps);
        return model.getMotherNaturePosition();
    }

    public void calcInfluence(String playerName) {
        inputController.controlGameState(GameState.ACTION);
        inputController.controlActualPlayer(playerName);
        model.calcInfluence(playerName);
    }

    public void chooseCloud(String playerName, String cloudId) {
        inputController.controlGameState(GameState.ACTION);
        inputController.controlActualPlayer(playerName);
        inputController.controlCloudId(cloudId);
        model.chooseCloud(playerName, cloudId);
    }

    public void playCharacterCard(String playerName, Characters character) {
        inputController.playCharacterCardPermit(playerName);
        model.playCharacterCard(playerName, character);
    }

    public void playCharacterCard(String playerName, Characters character, TeacherColor color) {
        inputController.playCharacterCardPermit(playerName);
        model.playCharacterCard(playerName, character, color);
    }

    public void playCharacterCard(String playerName, Characters character, String islandId) {
        inputController.playCharacterCardPermit(playerName);
        inputController.controlIslandId(islandId);
        model.playCharacterCard(playerName, character, islandId);
    }

    public boolean switchExpertVariant() {
        inputController.controlGameState(GameState.INITIAL);
        model.switchExpertVariant();
        return model.isExpertVariant();
    }

    public GameModel getModel() {
        return model;
    }
}
