package it.polimi.ingsw.server.model;


import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.player.AssistantCard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Cloud;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.model.table.MotherNature;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class manages all interactions between model and controller and handles model exceptions.
 * It's the intended gate for the model package, all model communication shall pass through this class.
 */
public class GameModel {

    private final Game game;

    public GameModel() {
        this.game = new Game();
    }

    /**
     * add player to the game
     * @param name of player
     */
    public void addPlayer(String name) {
        game.addPlayer(name);
    }

    /**
     * if there's not an exception, start the game
     */
    public void startGame() {
        try {
            game.gameStarter();
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        }
    }

    /**
     * choose the game mode
     */
    public void switchExpertVariant() {
        game.switchExpertVariant();
    }

    // Player moves

    /**
     * to get a specific player
     * @param playerName name of the player
     * @return the player sought
     * @throws NoSuchElementException there is no player with the name you are looking for
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
     * message player not found
     * @param playerName the player sought
     * @return string told player not found
     */
    private static String playerNotFoundMessage(String playerName) {
        return "No " + playerName + " player found";
    }

    /**
     * for play assistant card
     * @param playerName the player who is playing the card
     * @param cardWeight the card played
     * @throws GameModelException error in model
     * @throws NoSuchElementException there is no more cards available
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
     * move students
     * @param playerName the player who is moving the student
     * @param color color of the student
     * @param sourceId origin of the student
     * @param destinationId destination of the student
     * @throws GameModelException error in model
     * @throws NoSuchElementException there is no more students
     */
    public void moveStudent(String playerName, TeacherColor color, String sourceId, String destinationId)
            throws GameModelException, NoSuchElementException {
        Player player = getPlayer(playerName);
        try {
            player.moveStudent(color, sourceId, destinationId);
        } catch (IllegalStateException | InvalidParameterException e) {
            throw new GameModelException(e.getMessage());
        }
    }

    /**
     * similar to the previous one useful for the exchange of students of the character cards
     */
    public void moveStudent(String playerName, TeacherColor entranceStudent, TeacherColor otherStudent, String destinationId) {
        Player player = getPlayer(playerName);
        try {
            player.moveStudent(entranceStudent, otherStudent, destinationId);
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        } catch (InvalidParameterException e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    /**
     * move mother nature
     * @param playerName the player who is moving mother nature
     * @param steps number of steps
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
     * calculate influence
     * @param playerName the player who is calling the calculate
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
     * choose the cloud
     * @param playerName the player who is choosing the cloud
     * @param cloudId id of the chosen cloud
     */
    public void chooseCloud(String playerName, String cloudId) {
        Player player = getPlayer(playerName);
        try {
            player.chooseCloud(cloudId);
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        } catch (InvalidParameterException | NoSuchElementException e) {
            throw new InvalidParameterException(e.getMessage());
        }
    }

    /**
     * for play a card in expert variant
     * @param playerName the player who is playing the card
     * @param character choosen card
     */
    public void playCharacterCard(String playerName, Characters character) {
        Player player = getPlayer(playerName);
        try {
            player.playCharacterCard(character);
        } catch (InvalidParameterException | NoSuchElementException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        }
    }

    /**
     * similar to the previous used for character cards that require the choice of a color
     */
    public void playCharacterCard(String playerName, Characters character, TeacherColor color) {
        Player player = getPlayer(playerName);
        try {
            player.playCharacterCard(character, color);
        } catch (InvalidParameterException | NoSuchElementException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        }
    }

    /**
     * similar to the previous used for character cards that require the choice of an island
     */
    public void playCharacterCard(String playerName, Characters character, String islandId) {
        Player player = getPlayer(playerName);
        try {
            player.playCharacterCard(character, game.getTable().getIslandById(islandId).orElseThrow());
        } catch (InvalidParameterException | NoSuchElementException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        }
    }

    // Getter of the model state

    /**
     * @param islandId chosen island, if the id does not exist, it returns an error
     * @return a map showing the number of students for each color in the chosen island
     */
    public Map<TeacherColor, Integer> getStudentsInIsland(String islandId) {
        Island island;
        try {
            island = game.getTable().getIslandById(islandId).orElseThrow();
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
     * @param islandId chosen island, if the id does not exist, it returns an error
     * @return if present, the color of the tower on the chosen island
     */
    public Optional<TowerColor> getTowerInIsland(String islandId) {
        Island island;
        try {
            island = game.getTable().getIslandById(islandId).orElseThrow();
        } catch (NoSuchElementException e) {
            System.err.println("No island with " + islandId + " found");
            return Optional.empty();
        }
        return island.getTowerColor();
    }

    /**
     * @param cloudId chosen cloud, if the id does not exist, it returns an error
     * @return a map showing the number of students for each color in the chosen cloud
     */
    public Map<TeacherColor, Integer> getStudentsInCloud(String cloudId) {
        Cloud cloud;
        try {
            cloud = game.getTable().getCloudById(cloudId).orElseThrow();
        } catch (NoSuchElementException e) {
            System.err.println("No cloud with " + cloudId + " found");
            return new HashMap<>();
        }
        Map<TeacherColor, Integer> studentContent = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            studentContent.put(color, cloud.howManyStudents(color));
        }
        return studentContent;
    }

    /**
     * @param characters chosen character card
     * @return a map showing the number of students for each color in the chosen card
     */
    public Map<TeacherColor, Integer> getStudentsInCard(Characters characters) {
        Optional<StudentsManager> result = game.getActionPhase().getCardMemory(characters);
        if (result.isPresent()) {
            return result.get().getMap();
        } else {
            return new HashMap<>();
        }
    }

    /**
     *
     * @return list of cloud's id
     */
    public List<String> getCloudIds() {
        return game.getTable().getCloudList().stream()
                .map(Cloud::getId)
                .collect(Collectors.toList());
    }

    /**
     *
     * @return number of coins on the table
     */
    public Integer coinsOnTheTable() {
        return game.getTable().getNumCoin();
    }

    /**
     *
     * @return list of character card can be activated
     */
    public List<Characters> getEnabledCharacterCards() {
        return Arrays.stream(Characters.values())
                .filter(x -> game.getActionPhase().canBeActivated(x))
                .collect(Collectors.toList());
    }

    /**
     *
     * @return true if the game is 3 players mode
     */
    public boolean isThreePlayerGame() {
        return game.isThreePlayerGame();
    }

    /**
     *
     * @return true if the game is in expert mode
     */
    public boolean isExpertVariant() {
        return game.isExpertVariant();
    }

    /**
     *
     * @return the id of the island containing mother nature,
     */
    public String getMotherNaturePosition() {
        return MotherNature.getMotherNature().getPosition()
                .map(Island::getId)
                .orElse("NoIsland");
    }

    /**
     *
     * @return the id of the islands without prohibition cards
     */
    public List<String> getIslandsWithNoEntry() {
        return game.getTable().getIslandList().stream()
                .filter(Island::hasNoEntryTile)
                .map(Island::getId)
                .toList();
    }

    // Getter of the player state

    /**
     *
     * @param playerName name of the player
     * @return number of coins the player has, if the player does not exist, it returns -1
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
     *
     * @param playerName name of the player
     * @return a map showing the number of students for each color in the entrance of the player
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
     *
     * @param playerName name of the player
     * @return a map showing the number of students for each color in the rooms of the player
     */
    public Map<TeacherColor, Integer> getStudentsInRoom(String playerName) {
        Player player = getPlayer(playerName);

        Map<TeacherColor, Integer> studentContent = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            studentContent.put(color, player.getRoomTable().howManyStudents(color));
        }
        return studentContent;
    }

    /**
     *
     * @param playerName name of the player
     * @return a list showing for each color if the player owns the teacher
     */
    public List<TeacherColor> getTeachersInRoom(String playerName) {
        Player player = getPlayer(playerName);
        return player.getTeachers();
    }

    /**
     *
     * @return list of island's id
     */
    public List<String> getIslandIds() {
        return game.getTable().getIslandList().stream()
                .map(Island::getId)
                .collect(Collectors.toList());
    }

    /**
     *
     * @return a map showing how many towers there are for each island, then the number of corresponding islands
     */
    public Map<String, Integer> getIslandSizes() {
        Map<String, Integer> result = new HashMap<>();
        game.getTable().getIslandList()
                .forEach(island -> result.put(island.getId(), island.getEquivalent()));
        return result;
    }

    /**
     *
     * @return a list of players in order for the pianification phase
     */
    public List<String> getPlayersInOrder() {
        return game.getPianificationFase().getPlayersInOrder().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    /**
     *
     * @return if a character card with students is active, it returns the number of students for each color
     */
    public Map<TeacherColor, Integer> getActualCardMemory() {
        return getCardMemory(game.getActionPhase().getActualCharacter()
                .orElseThrow(() -> new IllegalStateException("No card has been activated")));
    }

    /**
     *
     * @param character chosen card
     * @return
    a map with the number of students for each color on the card
     */
    public Map<TeacherColor, Integer> getCardMemory(Characters character) {
        Map<TeacherColor, Integer> studentContent = new HashMap<>();
        Optional<StudentsManager> tmp = game.getActionPhase().getCardMemory(character);
        for (TeacherColor color : TeacherColor.values()) {
            if (tmp.isEmpty()) {
                studentContent.put(color, 0);
            } else {
                studentContent.put(color, tmp.get().howManyStudents(color));
            }
        }
        return studentContent;
    }

    /**
     *
     * @return cost of active character cards
     */
    public Map<Characters, Integer> getActiveCharactersCosts() {
        return game.getActiveCharactersCosts();
    }

    /**
     *
     * @return true if the game is finish
     */
    public boolean isGameEnded() {
        return game.isGameEnded();
    }

    /**
     *
     * @return name of the winner
     */
    public String getWinner() {
        return game.getEndPlayer();
    }

    public Game getGame() {
        return game;
    }

    /**
     *
     * @param playerName chosen player
     * @return color of the player's towers
     */
    public TowerColor getPlayerTowerColor(String playerName) {
        return getPlayer(playerName).getTowerColor();
    }

    /**
     *
     * @return a map in which for each player the corresponding number of coins he owns
     */
    public Map<String, Integer> getPlayerMoney() {
        Map<String, Integer> playerMoney = new HashMap<>();
        game.getPlayers().forEach(x -> playerMoney.put(x.getName(), x.getMoney()));
        return playerMoney;
    }

    /**
     *
     * @return if present, the current character cards
     */
    public Characters getActualCharacter() {
        return game.getActionPhase().getActualCharacter().orElseThrow(() -> new IllegalStateException("There is no actual character"));
    }

    /**
     *
     * @param name name of the player to skip
     */
    public void skipPlayer(String name) {
        game.skipPlayer(getPlayer(name));
    }

    /**
     *
     * @param name name of the player to not skip
     */
    public void unSkipPlayer(String name) {
        game.unSkipPlayer(getPlayer(name));
    }
}
