package it.polimi.ingsw.client.model;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.enums.Characters;

import java.util.*;

/**
 * Class which saves the state of the game in client.
 * Server sends status messages to the client which, via PlayMessageController, change this class.
 * Then the state can be read from this class to update the view.
 */
public class PlayState {

    //private List<String> playerNames;
    private String mainPlayer;
    private String actualPlayer;
    private String myName;
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

    public PlayState(){
        //this.playerNames = new ArrayList<>();
        this.mainPlayer = null;
        this.studentsInPlace = new HashMap<>();
        this.conquests = new HashMap<>();
        this.teachers = new ArrayList<>();
        this.assistantCards = List.of(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        this.playersTowerColors = new HashMap<>();
    }

    public void setStudentsInAPlace(String placeId, Map<TeacherColor, Integer> studentsMap){
        if(studentsInPlace.containsKey(placeId)){
            studentsInPlace.replace(placeId, studentsMap);
        } else {
            studentsInPlace.put(placeId, studentsMap);
        }
    }

    public Map<String, Map<TeacherColor, Integer>> getStudentsInPlaces(){
        return new HashMap<>(studentsInPlace);
    }

    public void setConquests(Map<String, TowerColor> conquests) {
        this.conquests = conquests;
    }

    public void setIslandIds(List<String> islandIds){
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

    public void setCloudIds(List<String> cloudIds){
        cloudIds.stream()
                .filter(this::isCloudId)         // control if all are cloud ids
                .filter(id -> !studentsInPlace.containsKey(id))  // add new ids
                .forEach(id -> studentsInPlace.put(id, new HashMap<>()));
        studentsInPlace.keySet().stream()        // remove useless ids
                .filter(this::isCloudId)
                .filter(id -> !cloudIds.contains(id))
                .forEach(studentsInPlace::remove);
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public int getMyConquests(){
        return getConquest(playersTowerColors.get(myName));
    }

    public List<String> getPlaceIds(){
        return studentsInPlace.keySet().stream().toList();
    }

    public void setMotherNature(String islandId){
        if(!isIslandId(islandId)) return;
        motherNaturePosition = islandId;
    }

    public String getMotherNature() {
        return motherNaturePosition;
    }

    public Optional<String> getConquest(String id){
        if(conquests.containsKey(id))
            return Optional.of(conquests.get(id).toString());
        else return Optional.empty();
    }

    public int getConquest(TowerColor color){
        return conquests.entrySet().stream()
                .filter(x -> x.getValue().equals(color))
                .map(Map.Entry::getKey)
                .map(this::getSize)
                .map(Integer::parseInt)
                .reduce(Integer::sum)
                .orElse(0);
    }

    public TowerColor getActualTowerColor(){
        return TowerColor.BLACK;
    }

    public List<TeacherColor> getTeachers(){
        return new ArrayList<>(teachers);
    }

    public String getSize(String id){
        return islandSize.get(id).toString();
    }

    public List<Integer> getAssistantCards(){
        return new ArrayList<>(assistantCards);
    }

    public void useAssistantCard(Integer weight){
        assistantCards.remove(weight);
    }

    public void setActionPhase(String actualPlayer) {
        actionPhase = true;
        this.actualPlayer = actualPlayer;
    }

    public void setPlanningPhase(String actualPlayer) {
        if(actionPhase){
            activeAssistantCards = new HashMap<>();
        }
        actionPhase = false;
        this.actualPlayer = actualPlayer;
    }

    public void setActualCharacterCard(Characters characterCard){
        actualCharacterCard = characterCard;
    }

    public String getActualPlayer(){
        return actualPlayer;
    }

    public void setTeachers(List<TeacherColor> teachers){
        this.teachers = teachers;
    }

    public void setActiveAssistantCard(String player, Integer weight){
        this.activeAssistantCards.put(player, weight);
    }

    private boolean isIslandId(String id){
        return id.matches("^i_[0-9_]*");
    }

    private boolean isCloudId(String id){
        return id.matches("^c_[0-9_]*");
    }

    public List<String> getPlayerNames(){
        return playersTowerColors.keySet().stream().toList();
    }

    public boolean isActionPhase(){
        return actionPhase;
    }

    public void setStatusTower(String playerName, TowerColor color){
        playersTowerColors.putIfAbsent(playerName, color);
    }

    public Map<String, TowerColor> getPlayersTowerColors(){
        return new HashMap<>(playersTowerColors);
    }

    @Override
    @Deprecated
    public String toString() {
        String studentsInPlaceString = studentsInPlace.entrySet().stream()
                .map(x -> "\t" + x.getKey() + ": " + x.getValue().toString() + "\n")
                .reduce((x, y) -> x +y).orElse(studentsInPlace.toString() + "LOL did not work");

        return "PlayState{" +
                //"playerNames=" + playerNames + "\n" +
                ", mainPlayer='" + mainPlayer + '\'' + "\n" +
                ", actualPlayer='" + actualPlayer + '\'' + "\n" +
                ", actionPhase=" + actionPhase + "\n" +
                ", actualCharacterCard=" + actualCharacterCard + "\n" +
                ", studentsInPlace=\n" + studentsInPlaceString + "\n" +
                ", conquests=" + conquests + "\n" +
                ", islandSize=" + islandSize + "\n" +
                ", teachers=" + teachers + "\n" +
                ", motherNaturePosition='" + motherNaturePosition + '\'' + "\n" +
                ", activeAssistantCards=" + activeAssistantCards + "\n" +
                '}';
    }
}
