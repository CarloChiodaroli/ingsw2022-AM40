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

    private List<String> playerNames;
    private String mainPlayer;
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
    private boolean editing;

    public PlayState(){
        this.playerNames = new ArrayList<>();
        this.mainPlayer = null;
        this.studentsInPlace = new HashMap<>();
        this.conquests = new HashMap<>();
        this.teachers = new ArrayList<>();
        this.assistantCards = List.of(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
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
                .peek(this::isIslandId)         // control if all are island ids
                .peek(id -> islandSize.put(id, (int) id.chars().filter(x -> x == '_').count()))
                .filter(id -> !studentsInPlace.containsKey(id))  // add new ids
                .forEach(id -> studentsInPlace.put(id, new HashMap<>()));
        System.out.println(islandSize);
        studentsInPlace.keySet().stream()// remove useless ids
                .filter(id -> id.matches("^i_[0-9_]*"))
                .filter(id -> !islandIds.contains(id))
                .forEach(studentsInPlace::remove);
    }

    public void setMotherNature(String islandId){
        isIslandId(islandId);
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
        return (int) conquests.entrySet().stream().filter(x -> x.getValue().equals(color)).count();
    }

    public TowerColor getActualTowerColor(){
        return TowerColor.BLACK;
    }

    public List<TeacherColor> getTeachers(){
        return new ArrayList<>(teachers);
    }

    public String getSize(String id){
        System.out.println(id);
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
        return id.toLowerCase().contains("i_");
    }

    public boolean isActionPhase(){
        return actionPhase;
    }

    @Override
    public String toString() {
        String studentsInPlaceString = studentsInPlace.entrySet().stream()
                .map(x -> "\t" + x.getKey() + ": " + x.getValue().toString() + "\n")
                .reduce((x, y) -> x +y).orElse(studentsInPlace.toString() + "LOL did not work");

        return "PlayState{" +
                "playerNames=" + playerNames + "\n" +
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
