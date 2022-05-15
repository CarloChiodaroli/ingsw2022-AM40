package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.MessageReader;
import it.polimi.ingsw.server.model.enums.Characters;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayMessagesReader implements MessageReader {

    private int numOfPlayers;
    private final String mainPlayer;
    private final GameController controller;

    public PlayMessagesReader(String mainPlayer) {
        this.mainPlayer = mainPlayer;
        this.controller = new GameController();
        this.controller.addPlayer(mainPlayer);
    }

    public void setNumOfPlayers(int numOfPlayers){
        if (numOfPlayers != 2 && numOfPlayers != 3) throw new IllegalArgumentException();
        this.numOfPlayers = numOfPlayers;
    }

    public void addPlayer(String playerName) {
        controller.addPlayer(playerName);
    }

    public void deletePlayer(String playerName) {
        controller.deletePlayer(playerName);
    }

    public int getNumCurrPlayers() {
        return controller.getPlayerNames().size();
    }

    public GameController getController() {
        return controller;
    }

    public void startGame(){
        controller.startGame();
    }

    @Override
    public void moveStudent(String player, TeacherColor color, String fromId, String toId) {

    }

    @Override
    public void moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {

    }

    @Override
    public void moveMotherNature(String player, Integer hops) {

    }

    @Override
    public void chooseCloud(String player, String id) {

    }

    @Override
    public void playCharacterCard(String player, Characters character) {

    }

    @Override
    public void playCharacterCard(String player, Characters character, String id) {

    }

    @Override
    public void playCharacterCard(String player, Characters character, TeacherColor color) {

    }

    @Override
    public void statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity) {

    }

    @Override
    public void statusTeacher(String sender, String id, List<TeacherColor> which) {

    }

    @Override
    public void statusTower(String sender, Map<String, Optional<TowerColor>> conquests) {

    }

    @Override
    public void statusIslandIds(String sender, List<String> ids) {

    }
}
