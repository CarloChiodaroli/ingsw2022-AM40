package it.polimi.ingsw.server.controller.inner;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.GameModel;

import java.util.*;

/**
 * Class used by the Play Message reader to read status informations from the model.
 */
public class OutboundController {

    private final GameModel model;
    private final InputController inputController;

    public OutboundController(GameModel model, InputController controller) {
        this.model = model;
        this.inputController = controller;
    }

    public List<String> getAllIslandIds() {
        inputController.excludeGameState(GameState.INITIAL);
        return model.getIslandIds();
    }

    public List<String> getAllCloudIds() {
        inputController.excludeGameState(GameState.INITIAL);
        return model.getCloudIds();
    }

    public Map<TeacherColor, Integer> getStudentInPlace(String playerName, String placeId) {
        if (getAllIslandIds().contains(placeId)) return model.getStudentsInIsland(placeId);
        else if (getAllCloudIds().contains(placeId)) return model.getStudentsInCloud(placeId);
        else if (placeId.equals("Entrance")) return model.getStudentsInEntrance(playerName);
        else if (placeId.equals("Room")) return model.getStudentsInRoom(playerName);
        else if (placeId.equals("Card")) return model.getActualCardMemory();
        else if (Arrays.stream(Characters.values()).map(Enum::toString).toList().contains(placeId))
            return model.getStudentsInCard(Characters.valueOf(placeId));
        else return new HashMap<>();
    }

    public List<String> getPlayersInOrder() {
        return model.getPlayersInOrder();
    }

    public Optional<TowerColor> getTowerInPlace(String placeId) {
        if (!getAllIslandIds().contains(placeId)) return Optional.empty();
        return model.getTowerInIsland(placeId);
    }

    public List<TeacherColor> getTeacherInPlace(String playerName) {
        return model.getTeachersInRoom(playerName);
    }

    public String actualMotherNaturePosition() {
        inputController.excludeGameState(GameState.INITIAL);
        return model.getMotherNaturePosition();
    }

    public Map<Characters, Integer> getCharacterCardPrices() {
        inputController.controlExpertVariant();
        return model.getActiveCharactersCosts();
    }

    public Characters getActualCharacterCard() {
        return model.getActualCharacter();
    }

    public TowerColor getPlayerTowerColor(String playerName) {
        return model.getPlayerTowerColor(playerName);
    }

    public boolean endGame() {
        return model.isGameEnded();
    }

    public String winner() {
        return model.getWinner();
    }

    public Map<String, Integer> getPlayerMoney() {
        inputController.excludeGameState(GameState.INITIAL);
        return model.getPlayerMoney();
    }

    public List<String> getIslandsWithNoEntry() {
        inputController.excludeGameState(GameState.INITIAL);
        return model.getIslandsWithNoEntry();
    }
}
