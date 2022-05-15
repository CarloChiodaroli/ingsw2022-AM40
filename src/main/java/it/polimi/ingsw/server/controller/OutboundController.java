package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.enums.Characters;

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
        return model.getAllIslandIds();
    }

    public Map<TeacherColor, Integer> getStudentInPlace(String playerName, String placeId) {
        if (getAllIslandIds().contains(placeId)) return model.getStudentsInIsland(placeId);
        if (placeId.equals("Entrance")) return model.getStudentsInEntrance(playerName);
        if (placeId.equals("Room")) return model.getStudentsInRoom(playerName);
        else return new HashMap<>();
    }

    public List<String> getPlayersInOrder(){
        return model.getPlayersInOrder();
    }

    public Optional<TowerColor> getTowerInPlace(String placeId) {
        if (!getAllIslandIds().contains(placeId)) return Optional.empty();
        return model.getTowerInIsland(placeId);
    }

    public List<TeacherColor> getTeacherInPlace(String playerName, String placeId) {
        if (!placeId.equals("Room")) return new ArrayList<>();
        return model.getTeachersInRoom(playerName);
    }

    public String actualMotherNaturePosition() {
        inputController.excludeGameState(GameState.INITIAL);
        return model.getMotherNaturePosition();
    }

    public Characters getActualCharacterCard(){
        return Characters.FRIAR;
    }
}
