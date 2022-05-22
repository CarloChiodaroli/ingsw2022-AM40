package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.IllegalMessageException;
import it.polimi.ingsw.commons.message.MessageReader;
import it.polimi.ingsw.commons.message.PlayMessage;
import it.polimi.ingsw.commons.message.PlayMessagesFabric;
import it.polimi.ingsw.commons.view.View;
import it.polimi.ingsw.server.model.enums.Characters;

import java.util.List;
import java.util.Map;

/**
 * This class is run by the executeMessage method of the received PlayMessage to change the PlayState class,
 * and by the view when she needs to send PlayMessages to the server.
 */
public class ClientStateController implements MessageReader {

    private List<String> playerNames;
    private String mainPlayer;
    private final ClientController controller;
    private final View view;
    private final PlayState state;

    public ClientStateController(ClientController controller, View view) {
        this.controller = controller;
        this.view = view;
        this.state = new PlayState();
    }

    public void setMainPlayer(String mainPlayer) {
        this.mainPlayer = mainPlayer;
        // Update view of the change
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
        // Update view of the change
    }

    public String getMainPlayer() {
        return mainPlayer;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    // playerCommands to send to the server
    @Override
    public void playAssistantCard(String player, Integer weight) {
        controlLegal(player);
        PlayMessage message = PlayMessagesFabric.playAssistantCard(player, weight);
        controller.sendMessage(message);
    }

    @Override
    public void moveStudent(String player, TeacherColor color, String fromId, String toId) {
        controlLegal(player);
        PlayMessage message = PlayMessagesFabric.moveStudent(player, color, fromId, toId);
        controller.sendMessage(message);
    }

    @Override
    public void moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        controlLegal(player);
        PlayMessage message = PlayMessagesFabric.moveStudent(player, fromColor, toColor, placeId);
        controller.sendMessage(message);
    }

    @Override
    public void moveMotherNature(String player, Integer hops) {
        controlLegal(player);
        PlayMessage message = PlayMessagesFabric.moveMotherNature(player, hops);
        controller.sendMessage(message);
    }

    @Override
    public void calcInfluence(String player) {
        controlLegal(player);
        PlayMessage message = PlayMessagesFabric.calcInfluence(player);
        controller.sendMessage(message);
    }

    @Override
    public void chooseCloud(String player, String id) {
        controlLegal(player);
        PlayMessage message = PlayMessagesFabric.chooseCloud(player, id);
        controller.sendMessage(message);
    }

    @Override
    public void playCharacterCard(String player, Characters character) {
        controlLegal(player);
        PlayMessage message = PlayMessagesFabric.playCharacterCard(player, character);
        controller.sendMessage(message);
    }

    @Override
    public void playCharacterCard(String player, Characters character, String id) {
        controlLegal(player);
        PlayMessage message = PlayMessagesFabric.playCharacterCard(player, character, id);
        controller.sendMessage(message);
    }

    @Override
    public void playCharacterCard(String player, Characters character, TeacherColor color) {
        // is sent by this player?
        controlLegal(player);
        // build message
        PlayMessage message = PlayMessagesFabric.playCharacterCard(player, character, color);
        // send message
        controller.sendMessage(message);
    }

    // Game state changes to store in client
    @Override
    public void statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity) {
        // is sent by the server?
        controlServer(sender);
        // update Play state
        state.setStudentsInAPlace(id, quantity);
        // update view.
        // view.something(with, attributes)
    }

    @Override
    public void statusTeacher(String sender, String id, List<TeacherColor> which) {
        controlServer(sender);
        state.setTeachers(which);
        // update view
    }

    @Override
    public void statusTower(String sender, Map<String, TowerColor> conquests) {
        controlServer(sender);
        state.setConquests(conquests); // to change the message
        // update view
    }

    @Override
    public void statusIslandIds(String sender, List<String> ids) {
        controlServer(sender);
        state.setIslandIds(ids);
        // update view
    }

    @Override
    public void statusCloudIds(String sender, List<String> ids) {

    }

    @Override
    public void statusMotherNature(String sender, String islandId) {
        controlServer(sender);
        state.setMotherNature(islandId);
        // update view
    }

    @Override
    public void statusAction(String sender, String actualPlayer) {
        controlServer(sender);
        state.setActionPhase(actualPlayer);
        // update view
    }

    @Override
    public void statusPlanning(String sender, String actualPlayer) {
        controlServer(sender);
        state.setPlanningPhase(actualPlayer);
        // update view
    }

    @Override
    public void statusCharacterCard(String sender, Characters character) {
        controlServer(sender);
        state.setActualCharacterCard(character);
        // update view
    }

    @Override
    public void statusAssistantCard(String sender, String player, Integer weight) {
        controlServer(sender);
        state.setActiveAssistantCard(player, weight);
        // update view
    }

    private void controlLegal(String player) {
        if (player.equals("server")) throw new IllegalMessageException("Illegal sender");
    }

    private void controlServer(String sender) {
        if (!sender.equals("server")) throw new IllegalMessageException("Got state update not from server");
    }
}

