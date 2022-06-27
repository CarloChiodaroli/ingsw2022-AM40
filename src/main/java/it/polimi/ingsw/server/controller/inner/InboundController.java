package it.polimi.ingsw.server.controller.inner;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.controller.outer.PlayMessagesReader;
import it.polimi.ingsw.server.model.GameModel;

import java.util.List;

/**
 * Of the controller this class manages model's inbound communications, checking if those commands are runnable or not
 * if not runnable an exception will be raised to be caught by the {@link PlayMessagesReader} class
 */
public class InboundController {

    private final GameModel model;
    private final InputController inputController;

    /**
     * Constructor
     */
    public InboundController(GameModel model, InputController inputController) {
        this.model = model;
        this.inputController = inputController;
    }

    /**
     * Check the game can start and DD PLyers to the model
     *
     * @param playerNames list of name of players
     * @throws IllegalStateException 0 players
     */
    public void startGame(List<String> playerNames) throws IllegalStateException {
        inputController.controlGameState(GameState.INITIAL);
        if (playerNames.size() <= 1) throw new IllegalStateException("Not enough players");
        playerNames.forEach(model::addPlayer);
        model.startGame();
    }

    /**
     * Check the assistant card can be played and send it to the model
     *
     * @param playerName player
     * @param cardWeight weight of played card
     */
    public void playAssistantCard(String playerName, int cardWeight) {
        inputController.controlGameState(GameState.PLANNING);
        inputController.controlActualPlayer(playerName);
        model.playAssistantCard(playerName, cardWeight);
    }

    /**
     * Check is action phase and places for student movement and send info to the model
     *
     * @param playerName player
     * @param color student color
     * @param sourceId source fo movement
     * @param destinationId destination of movement
     */
    public void moveStudent(String playerName, TeacherColor color, String sourceId, String destinationId) {
        inputController.controlGameState(GameState.ACTION);
        inputController.controlActualPlayer(playerName);
        inputController.controlSourceId(sourceId);
        if(sourceId.equals("Card")){
            inputController.controlCardDestinationId(destinationId);
        } else {
            inputController.controlDestinationId(destinationId);
        }
        model.moveStudent(playerName, color, sourceId, destinationId);
    }

    /**
     * Check the player can switch two students and send it to the model
     *
     * @param playerName player
     * @param entranceStudent color of sdtuent in entrance
     * @param otherStudent color of student in other place
     * @param placeId place of the switch with entrance
     */
    public void moveStudent(String playerName, TeacherColor entranceStudent, TeacherColor otherStudent, String placeId) {
        inputController.controlExpertVariant();
        inputController.controlGameState(GameState.ACTION);
        inputController.controlActualPlayer(playerName);
        model.moveStudent(playerName, entranceStudent, otherStudent, placeId);
    }

    /**
     * Check mother nature can be moved and send it to the model
     *
     * @param playerName player
     * @param steps number of steps
     * @return id of mother nature position
     */
    public String moveMotherNature(String playerName, int steps) {
        inputController.controlGameState(GameState.ACTION);
        inputController.controlActualPlayer(playerName);
        model.moveMotherNature(playerName, steps);
        return model.getMotherNaturePosition();
    }

    /**
     * Check influence can be calculated and send it to the model
     *
     * @param playerName name of player
     */
    public void calcInfluence(String playerName) {
        inputController.controlGameState(GameState.ACTION);
        inputController.controlActualPlayer(playerName);
        model.calcInfluence(playerName);
    }

    /**
     * Check you can choose  a cloud and the id and send it to the model
     *
     * @param playerName player
     * @param cloudId id of the cloud
     */
    public void chooseCloud(String playerName, String cloudId) {
        inputController.controlGameState(GameState.ACTION);
        inputController.controlActualPlayer(playerName);
        inputController.controlCloudId(cloudId);
        model.chooseCloud(playerName, cloudId);
    }

    /**
     * Check the character card is permitted and send it to the model
     *
     * @param playerName player
     * @param character chosen character
     */
    public void playCharacterCard(String playerName, Characters character) {
        inputController.playCharacterCardPermit(playerName);
        model.playCharacterCard(playerName, character);
    }

    /**
     * Check the character card is permitted, for character who needs a color, and send it to the model
     *
     * @param playerName player
     * @param character chosen character
     * @param color chosen color
     */
    public void playCharacterCard(String playerName, Characters character, TeacherColor color) {
        inputController.playCharacterCardPermit(playerName);
        model.playCharacterCard(playerName, character, color);
    }

    /**
     * Check the character card is permitted, for character who needs an island, and send it to the model
     *
     * @param playerName player
     * @param character chosen character
     * @param islandId chosen island
     */
    public void playCharacterCard(String playerName, Characters character, String islandId) {
        inputController.playCharacterCardPermit(playerName);
        inputController.controlIslandId(islandId);
        model.playCharacterCard(playerName, character, islandId);
    }

    /**
     * Check the player mode can be switched and send it to the model
     *
     * @return true if game is expert mode
     */
    public boolean switchExpertVariant() {
        inputController.controlGameState(GameState.INITIAL);
        model.switchExpertVariant();
        return model.isExpertVariant();
    }

    /**
     * Check a player can be skipped and send it to the model
     *
     * @param playerName player to skip
     */
    public void skipPlayer(String playerName) {
        model.skipPlayer(playerName);
    }

    /**
     * Check a player don't be skipped and send it to the model
     *
     * @param playerName player to not skip
     */
    public void unSkipPlayer(String playerName) {
        model.unSkipPlayer(playerName);
    }

    /**
     * Get game model
     *
     * @return game model
     */
    public GameModel getModel() {
        return model;
    }
}
