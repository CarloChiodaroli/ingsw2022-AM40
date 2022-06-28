package it.polimi.ingsw.client.model;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.enums.Wizard;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class which saves the state of the game in client.
 * Server sends status messages to the client which, via PlayMessageController, change this class.
 * Then the state can be read from this class to update the view.
 */
public class PlayState {

    // Lobby State
    private Wizard wizard;
    private List<Wizard> availableWizards;
    private String mainPlayer;
    private String myName;

    // Game State
    private String actualPlayer;
    private boolean actionPhase;
    private Characters actualCharacterCard;
    private Map<String, Map<TeacherColor, Integer>> studentsInPlace;
    private Map<String, TowerColor> conquests;
    private Map<String, Integer> islandSize;
    private List<TeacherColor> teachers;
    private String motherNaturePosition;
    private Map<String, Integer> activeAssistantCards;
    private List<Integer> assistantCards;
    private Map<String, TowerColor> playersTowerColors;
    private String winner;
    private int numPlayers;

    // Expert State
    private boolean expert;
    private Map<Characters, Integer> characterCosts;
    private Map<String, Integer> playerMoney;
    private Characters actualCard;
    private Integer myMoney;
    private List<String> noEntryIslands;


    public PlayState() {
        this.mainPlayer = null;
        this.studentsInPlace = new ConcurrentHashMap<>();
        this.conquests = new HashMap<>();
        this.characterCosts = new HashMap<>();
        this.teachers = new ArrayList<>();
        assistantCards = new ArrayList<>();
        this.assistantCards.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        this.activeAssistantCards = new HashMap<>();
        this.playersTowerColors = new HashMap<>();
        actionPhase = false;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public int numInitialTowers() {
        if (numPlayers == 2)
            return 8;
        else
            return 6;
    }

    public int numStudEntrance() {
        if (numPlayers == 2)
            return 7;
        else
            return 9;
    }

    public int numTowers(String player) {
        if (numPlayers == 2) {
            return 8 - getConquests(player);
        } else {
            return 6 - getConquests(player);
        }
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    public boolean isExpert() {
        return expert;
    }

    public void setStudentsInAPlace(String placeId, Map<TeacherColor, Integer> studentsMap) {
        if (studentsInPlace.containsKey(placeId)) {
            studentsInPlace.replace(placeId, studentsMap);
        } else {
            studentsInPlace.put(placeId, studentsMap);
        }
    }

    public Map<String, Map<TeacherColor, Integer>> getStudentsInPlaces() {
        return new ConcurrentHashMap<>(studentsInPlace);
    }

    public void setConquests(Map<String, TowerColor> conquests) {
        this.conquests = conquests;
    }

    public void setIslandIds(List<String> islandIds) {
        islandSize = new HashMap<>();
        islandIds.stream()
                .filter(this::isIslandId)         // control if all are island ids
                .peek(id -> islandSize.put(id, (int) id.chars().filter(x -> x == '_').count()))
                .filter(id -> !studentsInPlace.containsKey(id))  // add new ids
                .forEach(id -> studentsInPlace.put(id, new HashMap<>()));
        studentsInPlace.keySet().stream()         // remove useless ids
                .filter(this::isIslandId)
                .filter(id -> !islandIds.contains(id))
                .forEach(studentsInPlace::remove);
    }

    public void setCloudIds(List<String> cloudIds) {
        cloudIds.stream()
                .filter(this::isCloudId)            // control if all are cloud ids
                .filter(id -> !studentsInPlace.containsKey(id))  // add new ids
                .forEach(id -> studentsInPlace.put(id, new HashMap<>()));
        studentsInPlace.keySet().stream()           // remove useless ids
                .filter(this::isCloudId)
                .filter(id -> !cloudIds.contains(id))
                .forEach(studentsInPlace::remove);
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public int getConquests(String player) {
        return getConquest(playersTowerColors.get(player));
    }

    public List<String> getPlaceIds() {
        return studentsInPlace.keySet().stream().toList();
    }

    public void setMotherNature(String islandId) {
        if (!isIslandId(islandId)) return;
        motherNaturePosition = islandId;
    }

    public String getMotherNature() {
        return motherNaturePosition;
    }

    public Optional<String> getConquest(String id) {
        if (conquests.containsKey(id))
            return Optional.of(conquests.get(id).toString());
        else return Optional.empty();
    }

    public int getConquest(TowerColor color) {
        return conquests.entrySet().stream()
                .filter(x -> x.getValue().equals(color))
                .map(Map.Entry::getKey)
                .map(this::getSize)
                .map(Integer::parseInt)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public List<TeacherColor> getTeachers() {
        return new ArrayList<>(teachers);
    }

    public String getSize(String id) {
        return islandSize.get(id).toString();
    }

    public List<Integer> getAssistantCards() {
        return new ArrayList<>(assistantCards);
    }

    public void useAssistantCard(Integer weight) {
        assistantCards.remove(weight);
    }

    public void setActionPhase(String actualPlayer) {
        actionPhase = true;
        this.actualPlayer = actualPlayer;
    }

    public void setPlanningPhase(String actualPlayer) {
        if (actionPhase) {
            activeAssistantCards = new HashMap<>();
        }
        actionPhase = false;
        this.actualPlayer = actualPlayer;
    }

    public void setActualCharacterCard(Characters characterCard) {
        actualCharacterCard = characterCard;
    }

    public String getActualPlayer() {
        return actualPlayer;
    }

    public void setTeachers(List<TeacherColor> teachers) {
        this.teachers = teachers;
    }

    public void setActiveAssistantCard(String player, Integer weight) {
        if (player.equals(myName)) {
            useAssistantCard(weight);
        }
        this.activeAssistantCards.put(player, weight);
    }

    public Map<String, Integer> getActiveAssistantCards() {
        return new HashMap<>(activeAssistantCards);
    }

    private boolean isIslandId(String id) {
        return id.matches("^i_[0-9_]*");
    }

    private boolean isCloudId(String id) {
        return id.matches("^c_[0-9_]*");
    }

    public List<String> getPlayerNames() {
        return playersTowerColors.keySet().stream().toList();
    }

    public boolean isActionPhase() {
        return actionPhase;
    }

    public void setStatusTower(String playerName, TowerColor color) {
        playersTowerColors.putIfAbsent(playerName, color);
    }

    public Map<String, TowerColor> getPlayersTowerColors() {
        return new HashMap<>(playersTowerColors);
    }

    public TowerColor getMyTowerColor() {
        return getPlayersTowerColors().get(myName);
    }

    public void setWizard(Wizard wizardChoose) {
        wizard = wizardChoose;
    }

    public Optional<Wizard> getWizard() {
        return Optional.ofNullable(wizard);
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }

    public void setAvailableWizards(List<Wizard> availableWizards) {
        this.availableWizards = availableWizards;
    }

    public List<Wizard> getAvailableWizards() {
        return availableWizards;
    }

    public boolean isMainPlayer() {
        return mainPlayer.equals(myName);
    }

    public void setMainPlayer(String mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    public void setCharacterCosts(Map<Characters, Integer> characterCosts) {
        characterCosts.entrySet().stream()
                .forEach(entry -> this.characterCosts.put(entry.getKey(), entry.getValue()));
    }

    public void setActualCard(Characters actualCard) {
        this.actualCard = actualCard;
    }

    private void setMyMoney(Integer myMoney) {
        this.myMoney = myMoney;
    }

    public void setPlayerMoney(Map<String, Integer> money) {
        this.playerMoney = new HashMap<>();
        money.entrySet().stream()
                .forEach(entry -> playerMoney.put(entry.getKey(), entry.getValue()));
        setMyMoney(money.get(myName));
    }

    public boolean myTurn() {
        return myName.equals(actualPlayer);
    }

    public int getNumOfIslands() {
        return islandSize.size();
    }

    // Expert

    public Integer getMyMoney() {
        return myMoney;
    }

    public Map<Characters, Integer> getCharacterCosts() {
        return new HashMap<>(characterCosts);
    }

    public List<Characters> getAvailableCharacters() {
        return characterCosts.keySet().stream().toList();
    }

    public List<String> getNoEntryIslands() {
        return noEntryIslands;
    }

    public void setNoEntryIslands(List<String> noEntryIslands) {

        this.noEntryIslands = noEntryIslands;
    }

    public int getAvailableNoEntry(){
        return noEntryIslands.size() - noEntryIslands.stream().filter(x -> !x.equals("")).toList().size();
    }

    public int numMyTowers() {
        return numTowers(myName);
    }

    public int getNumOfStudentsInCloud() {
        return numPlayers + 1;
    }
}
