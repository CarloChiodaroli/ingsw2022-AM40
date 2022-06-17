package it.polimi.ingsw.server.controller.inner;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.commons.enums.Characters;

import java.util.*;

public class OutboundController {

    private final GameModel model;
    private final InputController inputController;

    public OutboundController(GameModel model, InputController controller){
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
        else if (Arrays.stream(Characters.values()).toList().contains(Characters.valueOf(placeId))) return model.getStudentsInCard(Characters.valueOf(placeId));
        else if (placeId.equals("Card")) return model.getActualCardMemory();
        else return new HashMap<>();
    }

    public List<String> getPlayersInOrder(){
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

    public Map<Characters, Integer> getCharacterCardPrices(){
        inputController.controlExpertVariant();
        return model.getActiveCharactersCosts();
    }

    public Characters getActualCharacterCard(){
        return Characters.FRIAR;
    }

    public TowerColor getPlayerTowerColor(String playerName){
        return model.getPlayerTowerColor(playerName);
    }

    public boolean endGame(){
        return model.isGameEnded();
    }

    public String winner(){
        return model.getWinner();
    }

    public Map<String, Integer> getPlayerMoney(){
        inputController.excludeGameState(GameState.INITIAL);
        return model.getPlayerMoney();
    }
}
