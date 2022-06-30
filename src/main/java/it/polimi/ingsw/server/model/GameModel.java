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

    /**
     * Constructor
     */
    public GameModel() {
        this.game = new Game();
    }

    /**
     * Add player to the game
     *
     * @param name of player
     */
    public void addPlayer(String name) {
        game.addPlayer(name);
    }

    /**
     * If there's not an exception, start the game
     */
    public void startGame() {
        try {
            game.gameStarter();
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        }
    }

    /**
     * Change the game mode
     */
    public void switchExpertVariant() {
        game.switchExpertVariant();
    }

    // Player moves

    /**
     * Get a player by name
     *
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
     * Message player not found
     *
     * @param playerName the player sought
     * @return string told player not found
     */
    private static String playerNotFoundMessage(String playerName) {
        return "No " + playerName + " player found";
    }

    /**
     * Play assistant card in pianification phase
     *
     * @param playerName the player who is playing the card
     * @param cardWeight the card played
     * @throws GameModelException     error in model
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
     * The student movement of a specific color from a place to another
     *
     * @param playerName    the player who is moving the student
     * @param color         color of the student
     * @param sourceId      origin of the student
     * @param destinationId destination of the student
     * @throws GameModelException     error in model
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
     * The exchange of two students between the entrance and another place
     *
     * @param playerName      the player who is moving the student
     * @param entranceStudent color of the student in the entrance
     * @param otherStudent    color of the student in destination
     * @param destinationId   destination place
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
     * Move mother nature
     *
     * @param playerName the player who is moving mother nature
     * @param steps      number of steps
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
     * Calculate influence
     *
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
     * Choose the cloud
     *
     * @param playerName the player who is choosing the cloud
     * @param cloudId    id of the chosen cloud
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
     * For play a character card in expert variant
     *
     * @param playerName the player who is playing the card
     * @param character  chosen card
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
     * For play a character card in expert variant that requires a color
     *
     * @param playerName the player who is playing the card
     * @param character  chosen card
     * @param color      chosen color
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
     * For play a character card in expert variant that requires an island
     *
     * @param playerName the player who is playing the card
     * @param character  chosen card
     * @param islandId   chosen island
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
     * Get for each color the number of students there are in a specific island
     *
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
     * Get the color of the tower, if any, in a specific island
     *
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
     * Get for each color the number of students there are in a specific cloud
     *
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
     * For a character card show how many students there are in for each color
     *
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
     * Get id of the islands
     *
     * @return list of cloud's id
     */
    public List<String> getCloudIds() {
        return game.getTable().getCloudList().stream()
                .map(Cloud::getId)
                .collect(Collectors.toList());
    }

    /**
     * Get the number of coins there are on the table
     *
     * @return number of coins on the table
     */
    public Integer coinsOnTheTable() {
        return game.getTable().getNumCoin();
    }

    /**
     * Get the character card can be activated
     *
     * @return list of character card can be activated
     */
    public List<Characters> getEnabledCharacterCards() {
        return Arrays.stream(Characters.values())
                .filter(x -> game.getActionPhase().canBeActivated(x))
                .collect(Collectors.toList());
    }

    /**
     * Get the number of players mode of the game
     *
     * @return true if the game is 3 players mode
     */
    public boolean isThreePlayerGame() {
        return game.isThreePlayerGame();
    }

    /**
     * Get the game mode
     *
     * @return true if the game is in expert mode
     */
    public boolean isExpertVariant() {
        return game.isExpertVariant();
    }

    /**
     * Get the position of mother nature
     *
     * @return the id of the island containing mother nature,
     */
    public String getMotherNaturePosition() {
        return MotherNature.getMotherNature().getPosition()
                .map(Island::getId)
                .orElse("NoIsland");
    }

    /**
     * Get the islands without prohibition card for expert variant
     *
     * @return the id of the islands without prohibition cards
     */
    public List<String> getIslandsWithNoEntry() {
        int availableTiles = game.getActionPhase().getCharacterCards().values().stream()
                .filter(x -> x.getCharacterization("NoEntrySetter") > 0)
                .map(x -> x.getCharacterization("Memory"))
                .peek(System.out::println)
                .reduce(Integer::max)
                .orElse(0);
        if (availableTiles == 0) {
            return new ArrayList<>();
        }
        List<String> result = new ArrayList<>(availableTiles);
        for (int i = 0; i < availableTiles; i++) {
            result.add(i, "");
        }
        List<String> islands = game.getTable().getIslandList().stream()
                .filter(Island::hasNoEntryTile)
                .map(Island::getId)
                .toList();
        for (int i = 0; i < islands.size(); i++) {
            result.remove(i);
            result.add(i, islands.get(i));
        }
        return result;
    }

    // Getter of the player state

    /**
     * Get the number of coins a player has
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
     * Get for each color the number of student a player has in his entrance
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
     * Get for each color the number of student a player has in his rooms
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
     * Get the colors of the teachers a player has
     *
     * @param playerName name of the player
     * @return a list showing for each color if the player owns the teacher
     */
    public List<TeacherColor> getTeachersInRoom(String playerName) {
        Player player = getPlayer(playerName);
        return player.getTeachers();
    }

    /**
     * Get id of the islands
     *
     * @return list of island's id
     */
    public List<String> getIslandIds() {
        return game.getTable().getIslandList().stream()
                .map(Island::getId)
                .collect(Collectors.toList());
    }

    /**
     * Get for each islands shows how many islands it corresponds to
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
     * Get the order of play for pianification phase
     *
     * @return a list of players in order for the pianification phase
     */
    public List<String> getPlayersInOrder() {
        return game.getPianificationFase().getPlayersInOrder().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    /**
     * Get for each color the actual number of students in activated character cards
     *
     * @return if a character card with students is active, it returns the number of students for each color
     */
    public Map<TeacherColor, Integer> getActualCardMemory() {
        return getCardMemory(game.getActionPhase().getActualCharacter()
                .orElseThrow(() -> new IllegalStateException("No card has been activated")));
    }

    /**
     * Get for each color the actual number of students in character cards
     *
     * @param character chosen card
     * @retur a map with the number of students for each color on the card
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
     * Get for each active character card the cost
     *
     * @return cost of active character cards
     */
    public Map<Characters, Integer> getActiveCharactersCosts() {
        return game.getActiveCharactersCosts();
    }

    /**
     * Get if the game is finish
     *
     * @return true if the game is finish
     */
    public boolean isGameEnded() {
        return game.isGameEnded();
    }

    /**
     * Get the winner of the game
     *
     * @return name of the winner
     */
    public String getWinner() {
        return game.getEndPlayer();
    }

    /**
     * Get game
     *
     * @return game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Get the color of the towers of a specific player
     *
     * @param playerName chosen player
     * @return color of the player's towers
     */
    public TowerColor getPlayerTowerColor(String playerName) {
        return getPlayer(playerName).getTowerColor();
    }

    /**
     * Get for each player the number of coins he has
     *
     * @return a map in which for each player the corresponding number of coins he owns
     */
    public Map<String, Integer> getPlayerMoney() {
        Map<String, Integer> playerMoney = new HashMap<>();
        game.getPlayers().forEach(x -> playerMoney.put(x.getName(), x.getMoney()));
        return playerMoney;
    }

    /**
     * Get the actual character card
     *
     * @return if present, the current character cards
     */
    public Characters getActualCharacter() {
        return game.getActionPhase().getActualCharacter().orElseThrow(() -> new IllegalStateException("There is no actual character"));
    }

    /**
     * Skip a player
     *
     * @param name name of the player to skip
     */
    public void skipPlayer(String name) {
        game.skipPlayer(getPlayer(name));
    }

    /**
     * Don't skip a player
     *
     * @param name name of the player to not skip
     */
    public void unSkipPlayer(String name) {
        game.unSkipPlayer(getPlayer(name));
    }

    /**
     * Getter of a player's personal deck of assistant cards
     *
     * @param name the name of the interesting player
     * @return a list of the values of the possessed cards
     */
    public List<String> getRemainingAssistants(String name) {
        Player player = getPlayer(name);
        return player.getPersonalDeck().stream().map(x -> "" + x.getWeight()).toList();
    }
}
