package it.polimi.ingsw.server.model;


import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.server.model.player.AssistantCard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Cloud;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.model.table.MotherNature;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class manages all interactions between model and controller and handles model exceptions
 */
// Removed Javadoc to make this class less overwhelming from 400+ lines to 300 lines
public class GameModel {

    private final Game game;

    public GameModel() {
        this.game = new Game();
    }

    public void addPlayer(String name) {
        game.addPlayer(name);
    }

    public void startGame() {
        try {
            game.gameStarter();
        } catch (IllegalStateException e) {
            throw new GameModelException(e.getMessage());
        }
    }

    public void switchExpertVariant() {
        game.switchExpertVariant();
    }

    // Player moves

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

    private static String playerNotFoundMessage(String playerName) {
        return "No " + playerName + " player found";
    }

    public void playAssistantCard(String playerName, int cardWeight)
            throws GameModelException, NoSuchElementException {
        Player player = getPlayer(playerName);
        try {
            player.playAssistantCard(new AssistantCard(cardWeight));
        } catch (IllegalStateException | InvalidParameterException e) {
            throw new GameModelException(e.getMessage());
        }
    }

    public void moveStudent(String playerName, TeacherColor color, String sourceId, String destinationId)
            throws GameModelException, NoSuchElementException {
        Player player = getPlayer(playerName);
        try {
            player.moveStudent(color, sourceId, destinationId);
        } catch (IllegalStateException | InvalidParameterException e) {
            throw new GameModelException(e.getMessage());
        }
    }

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

    public Map<TeacherColor, Integer> getStudentsInCloud(String cloudId) {
        Cloud cloud;
        try {
            cloud = game.getTable().getCloudById(cloudId).orElseThrow();
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



    public List<String> getCloudIds() {
        return game.getTable().getCloudList().stream()
                .map(Cloud::getId)
                .collect(Collectors.toList());
    }

    public Integer coinsOnTheTable() {
        return game.getTable().getNumCoin();
    }

    public List<Characters> getEnabledCharacterCards() {
        return Arrays.stream(Characters.values())
                .filter(x -> game.getActionPhase().canBeActivated(x))
                .collect(Collectors.toList());
    }


    public boolean isThreePlayerGame() {
        return game.isThreePlayerGame();
    }

    public boolean isExpertVariant() {
        return game.isExpertVariant();
    }

    public String getMotherNaturePosition() {
        return MotherNature.getMotherNature().getPosition()
                .map(Island::getId)
                .orElse("NoIsland");
    }

    // Getter of the player state
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

    public Map<TeacherColor, Integer> getStudentsInEntrance(String playerName) {
        Player player = getPlayer(playerName);

        Map<TeacherColor, Integer> studentContent = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            studentContent.put(color, player.getEntrance().howManyStudents(color));
        }
        return studentContent;
    }

    public Map<TeacherColor, Integer> getStudentsInRoom(String playerName) {
        Player player = getPlayer(playerName);

        Map<TeacherColor, Integer> studentContent = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            studentContent.put(color, player.getRoomTable().howManyStudents(color));
        }
        return studentContent;
    }

    public List<TeacherColor> getTeachersInRoom(String playerName) {
        Player player = getPlayer(playerName);
        return player.getTeachers();
    }

    public List<String> getIslandIds() {
        return game.getTable().getIslandList().stream()
                .map(Island::getId)
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getIslandSizes(){
        Map<String, Integer> result = new HashMap<>();
        game.getTable().getIslandList()
                .forEach(island -> result.put(island.getId(), island.getEquivalent()));
        return result;
    }

    public List<String> getPlayersInOrder() {
        return game.getPianificationFase().getPlayersInOrder().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    public Map<TeacherColor, Integer> getActualCardMemory() {
        return getCardMemory(game.getActionPhase().getActualCharacter()
                .orElseThrow(() -> new IllegalStateException("No card has been activated")));
    }

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

    public boolean isGameEnded() {
        return game.isGameEnded();
    }

    public String getWinner(){
        return game.getEndPlayer();
    }

    public Game getGame() {
        return game;
    }

    public TowerColor getPlayerTowerColor(String playerName){
        return getPlayer(playerName).getTowerColor();
    }
}
