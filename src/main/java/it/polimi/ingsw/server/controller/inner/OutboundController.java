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

    /**
     * Constructor
     */
    public OutboundController(GameModel model, InputController controller) {
        this.model = model;
        this.inputController = controller;
    }

    /**
     * Check the state isn't initial and get islands list by the model
     *
     * @return list of islands
     */
    public List<String> getAllIslandIds() {
        inputController.excludeGameState(GameState.INITIAL);
        return model.getIslandIds();
    }

    /**
     * Check the state isn't initial and get clouds list by the model
     *
     * @return list of clouds
     */
    public List<String> getAllCloudIds() {
        inputController.excludeGameState(GameState.INITIAL);
        return model.getCloudIds();
    }

    /**
     * Get from the model students in the required place
     *
     * @param playerName player
     * @param placeId required place
     * @return a map with for all color the number of students
     */
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

    /**
     * Get from the model the ored of the players
     *
     * @return a list with the players in the right order
     */
    public List<String> getPlayersInOrder() {
        return model.getPlayersInOrder();
    }

    /**
     * Get, if presents, from the model the color of the tower in the required island
     *
     * @param placeId required island
     * @return if any, the tower color
     */
    public Optional<TowerColor> getTowerInPlace(String placeId) {
        if (!getAllIslandIds().contains(placeId)) return Optional.empty();
        return model.getTowerInIsland(placeId);
    }

    /**
     * Get from the model the teachers of a player
     *
     * @param playerName player
     * @return list of teachers
     */
    public List<TeacherColor> getTeacherInPlace(String playerName) {
        return model.getTeachersInRoom(playerName);
    }

    /**
     * Check the state isn't initial and get the position of mother nature by the model
     *
     * @return the island where is mother nature
     */
    public String actualMotherNaturePosition() {
        inputController.excludeGameState(GameState.INITIAL);
        return model.getMotherNaturePosition();
    }

    /**
     * Check the game is in expert mode and get characters price from model
     *
     * @return a map witch for each character the price
     */
    public Map<Characters, Integer> getCharacterCardPrices() {
        inputController.controlExpertVariant();
        return model.getActiveCharactersCosts();
    }

    /**
     * Get from the model the actual character
     *
     * @return actual character
     */
    public Characters getActualCharacterCard() {
        return model.getActualCharacter();
    }

    /**
     * Get from the model the color of a player's towers
     *
     * @param playerName player
     * @return tower color
     */
    public TowerColor getPlayerTowerColor(String playerName) {
        return model.getPlayerTowerColor(playerName);
    }

    /**
     * Get from the model if the game is finished
     *
     * @return true if the game is finished
     */
    public boolean endGame() {
        return model.isGameEnded();
    }

    /**
     * Get from the model the winner
     *
     * @return winner name
     */
    public String winner() {
        return model.getWinner();
    }

    /**
     * Check the state isn't initial and get the number of a player's coin from the model
     *
     * @return a map with for each player the number of coins
     */
    public Map<String, Integer> getPlayerMoney() {
        inputController.excludeGameState(GameState.INITIAL);
        return model.getPlayerMoney();
    }

    /**
     * Check the state isn't initial and get the islands with prohibition card from the model
     *
     * @return list of islands with prohibition card
     */
    public List<String> getIslandsWithNoEntry() {
        inputController.controlExpertVariant();
        inputController.excludeGameState(GameState.INITIAL);
        return model.getIslandsWithNoEntry();
    }
}
