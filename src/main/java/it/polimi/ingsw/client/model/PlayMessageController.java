package it.polimi.ingsw.client.model;

import it.polimi.ingsw.client.controller.ClientController;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.IllegalMessageException;
import it.polimi.ingsw.commons.message.PlayMessageReader;
import it.polimi.ingsw.commons.message.play.*;
import it.polimi.ingsw.commons.message.PlayMessagesFabric;
import it.polimi.ingsw.commons.enums.Characters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is run by the {@link PlayMessage#executeMessage(PlayMessageReader) executeMessage} method of the received PlayMessage to change the {@link PlayState PlayState} class,
 * and by the view when she needs to send ({@link NormalPlayMessage Normal} or {@link ExpertPlayMessage Expert}) {@link PlayMessage PlayMessages} to the server.
 */
public class PlayMessageController implements PlayMessageReader {

    private String mainPlayer;
    private final ClientController controller;
    private final View view;
    private final PlayState state;

    public PlayMessageController(ClientController controller, View view) {
        this.controller = controller;
        this.view = view;
        this.state = new PlayState();
        view.setStatePrinter(this);
    }


    public void setExpert(boolean expert) {
        state.setExpert(expert);
    }

    /**
     * Getter of the playState
     * @return the playState
     */
    public PlayState getState() {
        return state;
    }

    /**
     * Setter of the main player
     * @param mainPlayer the name of the main player
     */
    public void setMainPlayer(String mainPlayer) {
        this.mainPlayer = mainPlayer;
        controller.getTaskQueue().execute(() -> view.showMainPlayerName(mainPlayer));
    }

    /**
     * Getter of the Place Ids
     * @return a list of the Place Ids
     */
    public List<String> getPlaceIds(){
        return state.getPlaceIds();
    }

    /**
     * Getter of the actual player nickname
     * @return the nickname
     */
    public String getNickname() {
        return controller.getNickname();
    }

    /**
     * Getter of the main player
     * @return the name of the main player
     */
    public String getMainPlayer() {
        return mainPlayer;
    }

    /**
     * Getter of the name pf the players
     * @return a list of player names
     */
    public List<String> getPlayerNames() {
        return state.getPlayerNames();
    }

    // playerCommands to send to the server

    /**
     * {@inheritDoc}
     */
    @Override
    public void playAssistantCard(String player, Integer weight) {
        controlLegal(player);
        NormalPlayMessage message = PlayMessagesFabric.playAssistantCard(player, weight);
        controller.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveStudent(String player, TeacherColor color, String fromId, String toId) {
        controlLegal(player);
        NormalPlayMessage message = PlayMessagesFabric.moveStudent(player, color, fromId, toId);
        controller.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveMotherNature(String player, Integer hops) {
        controlLegal(player);
        NormalPlayMessage message = PlayMessagesFabric.moveMotherNature(player, hops);
        controller.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void calcInfluence(String player) {
        controlLegal(player);
        NormalPlayMessage message = PlayMessagesFabric.calcInfluence(player);
        controller.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void chooseCloud(String player, String id) {
        controlLegal(player);
        NormalPlayMessage message = PlayMessagesFabric.chooseCloud(player, id);
        controller.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    // Game state changes to store in client - Second layer
    @Override
    public synchronized void statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity) {
        // is sent by the server?
        controlServer(sender);
        // update Play state
        state.setStudentsInAPlace(id, quantity);
        // update view.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusTeacher(String sender, String id, List<TeacherColor> which) {
        controlServer(sender);
        state.setTeachers(which);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusTower(String sender, Map<String, TowerColor> conquests) {
        controlServer(sender);
        state.setConquests(conquests); // to change the message
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusTower(String sender, String player, TowerColor color) {
        controlServer(sender);
        state.setStatusTower(player, color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusIslandIds(String sender, List<String> ids) {
        controlServer(sender);
        state.setIslandIds(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusCloudIds(String sender, List<String> ids) {
        controlServer(sender);
        state.setCloudIds(ids);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusMotherNature(String sender, String islandId) {
        controlServer(sender);
        state.setMotherNature(islandId);
    }

    /**
     * Receives the status of the round setting it as "Action phase of actual player".
     * Is one of the two status messages which updates the view (with the other being statusPlanning)
     *
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusAction(String sender, String actualPlayer) {
        controlServer(sender);
        state.setActionPhase(actualPlayer);
        controller.getTaskQueue().execute(view::update);
    }

    /**
     * Receives the status of the round setting it as "Planning phase of actual player".
     * Is one of the two status messages which updates the view (with the other being statusAction)
     *
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusPlanning(String sender, String actualPlayer) {
        controlServer(sender);
        state.setPlanningPhase(actualPlayer);
        controller.getTaskQueue().execute(view::update);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusAssistantCard(String sender, String player, Integer weight) {
        controlServer(sender);
        state.setActiveAssistantCard(player, weight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusEndGame(String sender, String winner) {
        controlServer(sender);
        state.setWinner(winner);
        controller.getTaskQueue().execute(view::showEndGame);
    }

    // Expert

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        controlLegal(player);
        ExpertPlayMessage message = PlayMessagesFabric.moveStudent(player, fromColor, toColor, placeId);
        controller.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playCharacterCard(String player, Characters character) {
        controlLegal(player);
        ExpertPlayMessage message = PlayMessagesFabric.playCharacterCard(player, character);
        controller.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playCharacterCard(String player, Characters character, String id) {
        controlLegal(player);
        ExpertPlayMessage message = PlayMessagesFabric.playCharacterCard(player, character, id);
        controller.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playCharacterCard(String player, Characters character, TeacherColor color) {
        // is sent by this player?
        controlLegal(player);
        // build message
        ExpertPlayMessage message = PlayMessagesFabric.playCharacterCard(player, character, color);
        // send message
        controller.sendMessage(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusCharacterCard(String sender, Map<String, Integer> money) {
        controlServer(sender);
        Map<Characters, Integer> result = new HashMap<>();
        money.forEach((k, v) -> result.put(Characters.valueOf(k), v));
        state.setCharacterCosts(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusPlayerMoney(String sender, Map<String, Integer> money) {
        controlServer(sender);
        state.setPlayerMoney(money);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusStudent(String sender, Characters character, Map<TeacherColor, Integer> quantity) {
        controlServer(sender);
        state.setStudentsInAPlace(character.toString(), quantity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void statusCharacterCard(String sender, Characters character) {
        controlServer(sender);
        state.setActualCharacterCard(character);
    }

    /**
     * Controls if the sender name is legal
     * @param player the sender name that's not server
     */
    private void controlLegal(String player) {
        if (player.equals("server")) throw new IllegalMessageException("Illegal sender");
    }

    /**
     * Controls if the sender name is server's name
     * @param sender the sender name
     */
    private void controlServer(String sender) {
        if (!sender.equals("server")) throw new IllegalMessageException("Got state update not from server");
    }
}

