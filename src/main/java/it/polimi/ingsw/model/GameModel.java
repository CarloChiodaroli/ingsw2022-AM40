package it.polimi.ingsw.model;


import it.polimi.ingsw.model.phase.action.Characters;
import it.polimi.ingsw.model.player.AssistantCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Cloud;
import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.model.table.MotherNature;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class manages all interactions between model and controller and handles model exceptions
 */
public class GameModel {

    private final Game game;

    public GameModel() {
        this.game = new Game();
    }

    /**
     * Adds a player to the game
     *
     * @param name is the name of the player to add, the name need to be different from each other
     */
    public void addPlayer(String name) {
        game.addPlayer(name);
    }

    /**
     * Starts the game once there are enough players
     */
    public void startGame() {
        game.gameStarter();
    }

    /**
     * Switches the variant of the game between Expert or not Expert variants
     */
    public void switchExpertVariant() {
        game.switchExpertVariant();
    }

    // Player moves

    /**
     * Gets a player from player name, used to get the right player to execute commands from
     *
     * @param playerName the name of the player who sent the command
     * @return the player corresponding to the given name
     * @throws NoSuchElementException if no player with that name was found
     */
    private Player getPlayer(String playerName) throws NoSuchElementException {
        try {
            return game.getPlayers().stream()
                    .filter(player -> player.getName().equals(playerName))
                    .findAny()
                    .orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(playerNotFoundMessage(playerName));
        }
    }

    /**
     * Builds the message to send when a player was not found
     *
     * @param playerName the name of the not existing player
     * @return the message
     */
    private static String playerNotFoundMessage(String playerName) {
        return "No " + playerName + " player found";
    }

    /**
     * Makes a player play an assistant card
     *
     * @param playerName the name of the player who plays the card
     * @param cardWeight the weight of the card the player wants to play
     */
    public void playAssistantCard(String playerName, int cardWeight)
            throws GameModelException, NoSuchElementException {
        Player player = getPlayer(playerName);
        try {
            player.playAssistantCard(new AssistantCard(cardWeight));
        } catch (IllegalStateException | InvalidParameterException e) {
            throw new GameModelException(e.getMessage());
        }
    }

    /**
     * Makes a player move a student
     *
     * @param playerName    the name of the player who makes the move
     * @param color         the color of the student the player wants to move
     * @param sourceId      the id of the source of the movement of the student
     * @param destinationId the id og the destination of the movement of the student
     */
    public void moveStudent(String playerName, TeacherColor color, String sourceId, String destinationId)
            throws GameModelException, NoSuchElementException{
        Player player = getPlayer(playerName);

        try {
            player.moveStudent(color, sourceId, destinationId);
        } catch (IllegalStateException | InvalidParameterException e) {
            throw new GameModelException(e.getMessage());
        }
    }

    /**
     * Makes a player shift two students from two different places
     *
     * @param playerName      the name of the player who makes the move
     * @param entranceStudent the color of the student ho is in the entrance
     * @param otherStudent    the color of the student who is in the room or card
     */
    public void moveStudent(String playerName, TeacherColor entranceStudent, TeacherColor otherStudent) {
        Player player = getPlayer(playerName);
        try {
            player.moveStudent(entranceStudent, otherStudent);
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        } catch (InvalidParameterException e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    /**
     * Moves mother nature
     *
     * @param playerName the name of the player who makes the move
     * @param steps      the number of steps he wants to move her
     */
    public void moveMotherNature(String playerName, int steps) {
        Player player = getPlayer(playerName);
        try {
            player.moveMotherNature(steps);
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        } catch (InvalidParameterException e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    /**
     * Makes the calculation of the influence
     *
     * @param playerName the name of the player who makes the move
     */
    public void calcInfluence(String playerName) {
        Player player = getPlayer(playerName);
        try {
            player.calcInfluence();
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        } catch (InvalidParameterException e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    /**
     * Executes the choice of the cloud by the player at the end of his action phase
     *
     * @param playerName the name of the player who makes the move
     * @param cloudId    the id of the chosen cloud
     */
    public void chooseCloud(String playerName, String cloudId) {
        Player player = getPlayer(playerName);
        try {
            player.chooseCloud(cloudId);
        } catch (IllegalStateException  e) {
            throw new GameModelException(e.getMessage());
        } catch (InvalidParameterException | NoSuchElementException e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    /**
     * Makes the player play and pay a character card
     *
     * @param playerName the name of the player who makes the move
     * @param character  the character of the card the player wants to use
     */
    public void playCharacterCard(String playerName, Characters character) {
        Player player = getPlayer(playerName);
        try {
            player.playCharacterCard(character);
        } catch (InvalidParameterException | NoSuchElementException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (IllegalStateException e){
            throw new GameModelException(e.getMessage());
        }
    }

    public void playCharacterCard(String playerName, Characters character, TeacherColor color) {
        Player player = getPlayer(playerName);
        try {
            player.playCharacterCard(character, color);
        } catch (InvalidParameterException | NoSuchElementException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (IllegalStateException e){
            throw new GameModelException(e.getMessage());
        }
    }

    public void playCharacterCard(String playerName, Characters character, String islandId) {
        Player player = getPlayer(playerName);
        try {
            player.playCharacterCard(character, game.getTable().getIslandById(islandId).orElseThrow());
        } catch (InvalidParameterException | NoSuchElementException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (IllegalStateException e){
            throw new GameModelException(e.getMessage());
        }
    }

    public void playCharacterCard(String playerName, Characters character, TowerColor color) {
        Player player = getPlayer(playerName);
        try {
            player.playCharacterCard(character, color);
        } catch (InvalidParameterException | NoSuchElementException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (IllegalStateException e){
            throw new GameModelException(e.getMessage());
        }
    }

    // Getter of the model state

    /**
     * Gets the detail of the student population on a specific island
     *
     * @param islandId is the island of which we want to know the population
     * @return a map detailing the population of the island
     */
    public Map<TeacherColor, Integer> getStudentsInIsland(String islandId) {
        Island island;
        try {
            island = (Island) game.getTable().getIslandById(islandId).orElseThrow();
        } catch (NoSuchElementException e) {
            System.err.println("No island with " + islandId + " found");
            return new HashMap<>();
        }
        Map<TeacherColor, Integer> studentContent = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            studentContent.put(color, island.howManyStudents(color));
        }
        return studentContent;
    }

    /**
     * Gets the detail of the presence of a tower on a specific island
     *
     * @param islandId is the island of which we want to know the tower
     * @return an Optional containing the color of the tower if present
     */
    public Optional<TowerColor> getTowerInIsland(String islandId) {
        Island island;
        try {
            island = (Island) game.getTable().getIslandById(islandId).orElseThrow();
        } catch (NoSuchElementException e) {
            System.err.println("No island with " + islandId + " found");
            return Optional.empty();
        }
        return island.getTowerColor();
    }

    /**
     * Gets all IDs of all islands present on the table
     *
     * @return a List of all island IDs
     */
    public List<String> getIslandIds() {
        return game.getTable().getIslandList().stream()
                .map(Island::getId)
                .collect(Collectors.toList());
    }

    /**
     * Gets the detail of the student population on a specific Cloud
     *
     * @param cloudId is the cloud of which we want to know the population
     * @return a map detailing the population of the cloud
     */
    public Map<TeacherColor, Integer> getStudentsInCloud(String cloudId) {
        Cloud cloud;
        try {
            cloud = (Cloud) game.getTable().getCloudById(cloudId).orElseThrow();
        } catch (NoSuchElementException e) {
            System.err.println("No island with " + cloudId + " found");
            return new HashMap<>();
        }
        Map<TeacherColor, Integer> studentContent = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            studentContent.put(color, cloud.howManyStudents(color));
        }
        return studentContent;
    }

    /**
     * Gets all IDs of all clouds present on the table
     *
     * @return a List of all cloud IDs
     */
    public List<String> getCloudIds() {
        return game.getTable().getCloudList().stream()
                .map(Cloud::getId)
                .collect(Collectors.toList());
    }

    /**
     * Gets how many coins are on the table
     *
     * @return the number of coins on the table
     */
    public Integer coinsOnTheTable() {
        return game.getTable().getNumCoin();
    }

    /**
     * Gets which cards Character cards are enabled to be chosen in a Action Phase round
     *
     * @return a list of Characters the cards of which are usable
     */
    public List<Characters> getEnabledCharacterCards() {
        return Arrays.stream(Characters.values())
                .filter(x -> game.getActionFase().canBeActivated(x))
                .collect(Collectors.toList());
    }

    /**
     * Gets if the Game is played by three players
     *
     * @return true if it is, false if not
     */
    public boolean isThreePlayerGame() {
        return game.isThreePlayerGame();
    }

    /**
     * Gets if the Game is in expert variant mode
     *
     * @return true if it is, false if not
     */
    public boolean isExpertVariant() {
        return game.isExpertVariant();
    }

    /**
     * Gets the actual position of Mother Nature
     *
     * @return the id the island on which Mother Nature is sitting
     */
    public String getMotherNaturePosition() {
        return MotherNature.getMotherNature().getPosition()
                .map(Island::getId)
                .orElse("NoIsland");
    }

    // Getter of the player state

    /**
     * Gets how many coins does a player have
     *
     * @param playerName the name of the player
     * @return the number of coins the player has
     */
    public Integer coinsOfThePlayer(String playerName) {
        Player player;
        try {
            player = getPlayer(playerName);
        } catch (NoSuchElementException e) {
            System.err.println(playerNotFoundMessage(playerName));
            return -1;
        }
        return player.getMoney();
    }

    /**
     * Gets the detail of the student population in a school entrance
     *
     * @param playerName the name of the player
     * @return a map detailing the population of the entrance
     */
    public Map<TeacherColor, Integer> getStudentsInEntrance(String playerName) {
        Player player = getPlayer(playerName);

        Map<TeacherColor, Integer> studentContent = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            studentContent.put(color, player.getEntrance().howManyStudents(color));
        }
        return studentContent;
    }

    /**
     * Gets the detail of the student population in a schoolroom
     *
     * @param playerName the name of the player
     * @return a map detailing the population of the room
     */
    public Map<TeacherColor, Integer> getStudentsInRoom(String playerName) {
        Player player = getPlayer(playerName);

        Map<TeacherColor, Integer> studentContent = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            studentContent.put(color, player.getRoomTable(color).howManyStudents(color));
        }
        return studentContent;
    }

    public List<String> getAllIslandIds(){
        return game.getTable().getIslandList().stream()
                .map(Island::getId)
                .collect(Collectors.toList());
    }

    public List<String> getPlayersInOrder(){
        return game.getPianificationFase().getPlayersInOrder().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public Map<TeacherColor, Integer> getActualCardMemory(){
        return getCardMemory(game.getActionFase().getActualCharacter()
                .orElseThrow(() -> new IllegalStateException("No card has been activated")));
    }

    public Map<TeacherColor, Integer> getCardMemory(Characters character){
        Map<TeacherColor, Integer> studentContent = new HashMap<>();
        Optional<StudentsManager> tmp = game.getActionFase().getCardMemory(character);
        for (TeacherColor color : TeacherColor.values()) {
            if(tmp.isEmpty()){
                studentContent.put(color, 0);
            } else {
                studentContent.put(color, tmp.get().howManyStudents(color));
            }
        }
        return studentContent;
    }

    public boolean isGameEnded(){
        return game.isGameEnded();
    }
}
