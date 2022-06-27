package it.polimi.ingsw.server.controller.outer;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.server.controller.inner.*;
import it.polimi.ingsw.server.enums.CardCharacterizations;
import it.polimi.ingsw.server.model.GameModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Of the controller this is the middle part. This class is run by the {@link it.polimi.ingsw.commons.message.play.PlayMessage#executeMessage(PlayMessageReader) executeMessage}
 * method of messages of the play type.
 * This class manages all strictly game related communications between server and client.
 * Received a command, runs it, and if there are errors sends them, else sends singular status messages
 * which represent state changes of the model subsequent the received command.
 */
public class PlayMessagesReader implements PlayMessageReader {

    private final InboundController inbound;
    private final OutboundController outbound;
    private final InputController inputController;
    private final TurnController turnController;
    private final GameManager gameManager;

    private int numOfPlayers;
    private final String mainPlayer;
    private final List<String> playerNames;
    private boolean expertVariant;
    private final String server;
    private boolean stop;

    /**
     * Constructor
     */
    public PlayMessagesReader(String mainPlayer, GameManager gameManager) {
        this.mainPlayer = mainPlayer;
        this.playerNames = new ArrayList<>();
        this.playerNames.add(mainPlayer);
        this.turnController = new TurnController(this);
        this.inputController = new InputController(this);
        GameModel model = new GameModel();
        this.inbound = new InboundController(model, inputController);
        this.outbound = new OutboundController(model, inputController);
        this.server = "server";
        this.gameManager = gameManager;
    }

    /**
     * If number of players is valid, add it to the reader
     *
     * @param numOfPlayers number of players
     */
    public void setNumOfPlayers(int numOfPlayers) {
        if (numOfPlayers != 2 && numOfPlayers != 3) throw new IllegalArgumentException();
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Check the state isn't initial and add a player to the controller and the reader
     *
     * @param playerName player
     */
    public void addPlayer(String playerName) {
        inputController.controlGameState(GameState.INITIAL);
        inputController.addPlayer(playerName);
        this.playerNames.add(playerName);
    }

    /**
     * Check the state isn't initial and remove a player to the controller and the reader
     *
     * @param playerName player
     */
    public void deletePlayer(String playerName) {
        inputController.controlGameState(GameState.INITIAL);
        inputController.removePlayer(playerName);
        playerNames.remove(playerName);
    }

    /**
     * Get number of current players
     *
     * @return number of players
     */
    public int getNumCurrPlayers() {
        return playerNames.size();
    }

    /**
     * Save a list of messages with the status of a player
     *
     * @param player player
     */
    private void sendCompleteStatus(String player) {
        List<Message> commonAnswers = new ArrayList<>();
        List<String> islandIds = outbound.getAllIslandIds();
        List<String> cloudIds = outbound.getAllCloudIds();
        commonAnswers.add(PlayMessagesFabric.statusIslandIds(server, islandIds));
        for (String islandId : islandIds) {
            commonAnswers.add(PlayMessagesFabric.statusStudent(server, islandId, outbound.getStudentInPlace(mainPlayer, islandId)));
        }
        commonAnswers.add(PlayMessagesFabric.statusMotherNature(server, outbound.actualMotherNaturePosition()));
        commonAnswers.add(PlayMessagesFabric.statusCloudIds(server, cloudIds));
        for (String id : cloudIds) {
            commonAnswers.add(PlayMessagesFabric.statusStudent(server, id, outbound.getStudentInPlace(mainPlayer, id)));
        }
        for (String name : playerNames) {
            commonAnswers.add(PlayMessagesFabric.statusTower(server, name, outbound.getPlayerTowerColor(name)));
        }
        if (expertVariant) {
            Map<Characters, Integer> prices = outbound.getCharacterCardPrices();
            inputController.setCharacters(prices);
            Map<String, Integer> pricesString = new HashMap<>();
            prices.forEach((k, v) -> pricesString.put(k.toString(), v));
            commonAnswers.add(PlayMessagesFabric.statusCharacterCard(server, pricesString));
            commonAnswers.add(PlayMessagesFabric.statusPlayerMoney(server, outbound.getPlayerMoney()));
            prices.entrySet().stream()
                    .filter(e -> CardCharacterizations.particular(e.getKey()).getOrDefault("Memory", -1) > 0)
                    .forEach(e -> {
                        Map<TeacherColor, Integer> toAdd = outbound.getStudentInPlace(mainPlayer, e.getKey().toString());
                        if (!toAdd.isEmpty())
                            commonAnswers.add(PlayMessagesFabric.statusStudent(server, e.getKey(), toAdd));
                    });
            commonAnswers.add(PlayMessagesFabric.statusNoEntry(server, outbound.getIslandsWithNoEntry()));
        }
        commonAnswers.forEach(x -> gameManager.sendMessage(player, x));
    }

    /**
     * Get answer's messages to a player
     *
     * @param receiverName player
     */
    private void sendAllPrivate(String receiverName) {
        List<Message> answers = new ArrayList<>(playerDashboard(receiverName));
        answers.add(PlayMessagesFabric.statusPlanning(server, turnController.getActivePlayer()));
        answers.forEach(answer -> gameManager.sendMessage(receiverName, answer));
    }

    /**
     * Send all messages
     *
     * @param playerName player
     */
    public void sendCompleteGameStatus(String playerName) {
        sendCompleteStatus(playerName);
        sendAllPrivate(playerName);
        //playerNames.forEach(this::sendAllPrivate);
    }

    /**
     * Send action or pianification phase informations
     */
    public void sendStatus() {
        if (turnController.getActualState().equalsIgnoreCase("action"))
            playerNames.forEach(x -> gameManager.sendMessage(x, PlayMessagesFabric.statusAction(server, turnController.getActivePlayer())));
        else if (turnController.getActualState().equalsIgnoreCase("planning"))
            playerNames.forEach(x -> gameManager.sendMessage(x, PlayMessagesFabric.statusPlanning(server, turnController.getActivePlayer())));
    }

    /**
     * Save a list of dashboard's info
     *
     * @param player player
     * @return list of messages with dashborad's info
     */
    private List<Message> playerDashboard(String player) {
        List<Message> answers = new ArrayList<>();
        answers.add(PlayMessagesFabric.statusStudent(server, "Entrance", outbound.getStudentInPlace(player, "Entrance")));
        answers.add(PlayMessagesFabric.statusStudent(server, "Room", outbound.getStudentInPlace(player, "Room")));
        answers.add(PlayMessagesFabric.statusTeacher(server, player, outbound.getTeacherInPlace(player)));
        return answers;
    }

    /**
     * When the game is started, send all informations for each player
     */
    public void startGame() {
        try {
            inbound.startGame(playerNames);
            turnController.startPlay(new ArrayList<>(playerNames));
        } catch (Exception e) {
            errorInExecution(e.getMessage());
            return;
        }
        playerNames.forEach(this::sendCompleteStatus);
        playerNames.forEach(this::sendAllPrivate);
    }

    /**
     * Get the game start for turn controller
     *
     * @return true if game is started
     */
    public boolean isGameStarted() {
        return turnController.isGameStarted();
    }

    /**
     * Switch to expert mode
     */
    public void switchExpertVariant() {
        expertVariant = inbound.switchExpertVariant();
    }

    /**
     * Skip a player allowed by turn controller
     *
     * @param playerName player to skip
     */
    public void stopPlayer(String playerName) {
        turnController.skipPlayer(playerName);
        inbound.skipPlayer(playerName);
    }

    /**
     * Don't skip a player allowed by turn controller
     *
     * @param playerName player to not skip
     */
    public void unStopPlayer(String playerName) {
        turnController.unSkipPlayer(playerName);
        inbound.unSkipPlayer(playerName);
    }

    /**
     * Save messages for play assistant card
     *
     * @param player player
     * @param weight card weight
     */
    @Override
    public void playAssistantCard(String player, Integer weight) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.playAssistantCard(player, weight);
        } catch (Exception e) {
            errorInExecution(player, e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusAssistantCard(server, player, weight));
        if (turnController.nextTurn()) {
            answers.add(PlayMessagesFabric.statusAction(server, turnController.getActivePlayer()));
        } else {
            answers.add(PlayMessagesFabric.statusPlanning(server, turnController.getActivePlayer()));
        }
        answers.forEach(gameManager::broadcastMessage);
        if (outbound.endGame()) {
            sendEndGame(outbound.winner());
        }
    }

    /**
     * Save messages for move students
     *
     * @param player player
     * @param color student color
     * @param fromId source place
     * @param toId destination place
     */
    @Override
    public void moveStudent(String player, TeacherColor color, String fromId, String toId) {
        List<Message> answers = new ArrayList<>();
        List<Message> broadcastAnswers = new ArrayList<>();
        try {
            inbound.moveStudent(player, color, fromId, toId);
        } catch (Exception e) {
            errorInExecution(player, e.getMessage());
            return;
        }
        if (fromId.equals("Entrance") || fromId.equals("Room"))
            answers.add(PlayMessagesFabric.statusStudent(server, fromId, outbound.getStudentInPlace(player, fromId)));
        else if (fromId.equals("Card")) {
            broadcastAnswers.add(PlayMessagesFabric.statusStudent(server,
                    outbound.getActualCharacterCard().toString(),
                    outbound.getStudentInPlace(player, fromId)));
        } else
            broadcastAnswers.add(PlayMessagesFabric.statusStudent(server, fromId, outbound.getStudentInPlace(player, fromId)));
        if (toId.equals("Entrance") || toId.equals("Room"))
            answers.add(PlayMessagesFabric.statusStudent(server, toId, outbound.getStudentInPlace(player, toId)));
        else if (toId.equals("Card")) {
            broadcastAnswers.add(PlayMessagesFabric.statusStudent(server,
                    outbound.getActualCharacterCard().toString(),
                    outbound.getStudentInPlace(player, toId)));
        } else
            broadcastAnswers.add(PlayMessagesFabric.statusStudent(server, toId, outbound.getStudentInPlace(player, toId)));
        answers.forEach(answer -> gameManager.sendMessage(player, answer)); // send specific
        playerNames.forEach(name -> gameManager.sendMessage(name, PlayMessagesFabric.statusTeacher(server, name, outbound.getTeacherInPlace(name))));
        if (expertVariant) {
            broadcastAnswers.add(PlayMessagesFabric.statusPlayerMoney(server, outbound.getPlayerMoney()));
        }
        broadcastAnswers.add(PlayMessagesFabric.statusAction(server, turnController.getActivePlayer()));
        broadcastAnswers.forEach(gameManager::broadcastMessage); // send broadcast
        if (outbound.endGame()) {
            sendEndGame(outbound.winner());
        }
    }

    /**
     * Save messages for switch students
     *
     * @param player player
     * @param fromColor student color in entrance
     * @param toColor student olor in other place
     * @param placeId other place of the switch
     */
    @Override
    public void moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        List<Message> answers = new ArrayList<>();
        List<Message> broadcastAnswers = new ArrayList<>();
        try {
            inbound.moveStudent(player, fromColor, toColor, placeId);
        } catch (Exception e) {
            errorInExecution(player, e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusStudent(server, "Entrance", outbound.getStudentInPlace(player, "Entrance")));
        if (placeId.equals("Room"))
            answers.add(PlayMessagesFabric.statusStudent(server, placeId, outbound.getStudentInPlace(player, placeId)));
        else if (placeId.equals("Card")) {
            broadcastAnswers.add(PlayMessagesFabric.statusStudent(server,
                    outbound.getActualCharacterCard().toString(),
                    outbound.getStudentInPlace(player, placeId)));
        } else
            broadcastAnswers.add(PlayMessagesFabric.statusStudent(server, placeId, outbound.getStudentInPlace(player, placeId)));
        broadcastAnswers.add(PlayMessagesFabric.statusAction(server, turnController.getActivePlayer()));
        answers.forEach(answer -> gameManager.sendMessage(player, answer)); // send specific
        playerNames.forEach(name -> gameManager.sendMessage(server, PlayMessagesFabric.statusTeacher(server, name, outbound.getTeacherInPlace(name))));
        broadcastAnswers.forEach(gameManager::broadcastMessage); // send broadcast
        if (outbound.endGame()) {
            sendEndGame(outbound.winner());
        }
    }

    /**
     * Save messages for move mother nature
     *
     * @param player player
     * @param hops number of steps
     */
    @Override
    public void moveMotherNature(String player, Integer hops) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.moveMotherNature(player, hops);
        } catch (Exception e) {
            errorInExecution(player, e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusMotherNature(server, outbound.actualMotherNaturePosition()));
        answers.add(PlayMessagesFabric.statusAction(server, turnController.getActivePlayer()));
        answers.forEach(gameManager::broadcastMessage);
    }

    /**
     * Save messages for calculate influence
     *
     * @param player player name
     */
    @Override
    public void calcInfluence(String player) {
        List<Message> answers = new ArrayList<>();
        // command to change model
        try {
            inbound.calcInfluence(player);
        } catch (Exception e) {
            errorInExecution(player, e.getMessage());
            return;
        }
        // command to read model
        List<String> islandIds = outbound.getAllIslandIds();
        Map<String, TowerColor> dominia = new HashMap<>();
        islandIds.forEach(id ->
                outbound.getTowerInPlace(id).ifPresent(tower -> dominia.put(id, tower)));
        // building answer list
        answers.add(PlayMessagesFabric.statusIslandIds(server, islandIds));
        answers.add(PlayMessagesFabric.statusTower(server, dominia));
        answers.add(PlayMessagesFabric.statusStudent(
                server,
                outbound.actualMotherNaturePosition(),
                outbound.getStudentInPlace(player, outbound.actualMotherNaturePosition())));
        String savedIslandId = turnController.getSavedIsland();
        if (expertVariant && savedIslandId != null)
            answers.add(PlayMessagesFabric.statusStudent(server, savedIslandId, outbound.getStudentInPlace(player, savedIslandId)));
        answers.add(PlayMessagesFabric.statusMotherNature(server, outbound.actualMotherNaturePosition()));
        answers.add(PlayMessagesFabric.statusNoEntry(server, outbound.getIslandsWithNoEntry()));
        answers.add(PlayMessagesFabric.statusAction(server, turnController.getActivePlayer()));
        // sending answers
        answers.forEach(gameManager::broadcastMessage);
        // sending endgame if reached
        if (outbound.endGame()) {
            sendEndGame(outbound.winner());
        }
    }

    /**
     * Save messages for choose a cloud
     *
     * @param player player name
     * @param id cloud id
     */
    @Override
    public void chooseCloud(String player, String id) {
        List<Message> answers = new ArrayList<>();
        List<Message> broadcastAnswers = new ArrayList<>();
        try {
            inbound.chooseCloud(player, id);
        } catch (Exception e) {
            errorInExecution(player, e.getMessage());
            return;
        }
        answers.add(PlayMessagesFabric.statusStudent(server, "Entrance", outbound.getStudentInPlace(player, "Entrance")));
        List<String> cloudIds = outbound.getAllCloudIds();
        broadcastAnswers.add(PlayMessagesFabric.statusCloudIds(server, cloudIds));
        for (String cId : cloudIds) {
            broadcastAnswers.add(PlayMessagesFabric.statusStudent(server, cId, outbound.getStudentInPlace(mainPlayer, cId)));
        }
        if (turnController.nextTurn()) {
            broadcastAnswers.add(PlayMessagesFabric.statusPlanning(server, getActualPlayer()));
        } else {
            broadcastAnswers.add(PlayMessagesFabric.statusAction(server, turnController.getActivePlayer()));
        }
        answers.forEach(answer -> gameManager.sendMessage(player, answer)); // send specific
        broadcastAnswers.forEach(gameManager::broadcastMessage); // send broadcast
        if (outbound.endGame()) {
            sendEndGame(outbound.winner());
        }

    }

    /**
     * Save messages for play a character card
     *
     * @param player player name
     * @param character character chose
     */
    @Override
    public void playCharacterCard(String player, Characters character) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.playCharacterCard(player, character);
            turnController.setActualCharacter(character);
        } catch (Exception e) {
            errorInExecution(player, e.getMessage());
            return;
        }
        corePlayCharacterCard(answers);
    }

    /**
     * Save messages for play a character card that needs an island
     *
     * @param player player name
     * @param character character chose
     * @param id island id
     */
    @Override
    public void playCharacterCard(String player, Characters character, String id) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.playCharacterCard(player, character, id);
            turnController.setActualCharacter(character);
        } catch (Exception e) {
            errorInExecution(player, e.getMessage());
            return;
        }
        turnController.saveIsland(id);
        corePlayCharacterCard(answers);
    }

    /**
     * Save messages for play a character card that needs a color
     *
     * @param player player name
     * @param character character chose
     * @param color chosen color
     */
    @Override
    public void playCharacterCard(String player, Characters character, TeacherColor color) {
        List<Message> answers = new ArrayList<>();
        try {
            inbound.playCharacterCard(player, character, color);
            turnController.setActualCharacter(character);
        } catch (Exception e) {
            errorInExecution(player, e.getMessage());
            return;
        }
        corePlayCharacterCard(answers);
    }

    /**
     * Manages messages when a character card is played
     *
     * @param answers list of answer messages
     */
    private void corePlayCharacterCard(List<Message> answers) {
        Characters actual = turnController.getActualCharacter().get();
        answers.add(PlayMessagesFabric.statusCharacterCard(server, outbound.getActualCharacterCard()));
        Map<Characters, Integer> prices = outbound.getCharacterCardPrices();
        inputController.setCharacters(prices);
        Map<String, Integer> pricesString = new HashMap<>();
        prices.forEach((k, v) -> pricesString.put(k.toString(), v));
        answers.add(PlayMessagesFabric.statusCharacterCard(server, pricesString));
        answers.add(PlayMessagesFabric.statusPlayerMoney(server, outbound.getPlayerMoney()));
        answers.add(PlayMessagesFabric.statusNoEntry(server, outbound.getIslandsWithNoEntry()));
        String actualIsland = turnController.getSavedIsland();
        if (actualIsland != null)
            answers.add(PlayMessagesFabric.statusStudent(server, actualIsland, outbound.getStudentInPlace(getActualPlayer(), actualIsland)));
        playerNames.forEach(player -> {
            List<Message> particular = new ArrayList<>(answers);
            if (inputController.characterEffectsAllPlayers(actual)) answers.addAll(playerDashboard(player));
            else if (player.equals(getActualPlayer()) && inputController.characterEffectsPlayer(actual))
                particular.addAll(playerDashboard(player));
            particular.add(PlayMessagesFabric.statusAction(server, turnController.getActivePlayer()));
            particular.forEach(answer -> gameManager.sendMessage(player, answer));
        });
        if (outbound.endGame()) {
            sendEndGame(outbound.winner());
        }
    }

    /**
     * Send error message
     */
    @Override
    public void statusCharacterCard(String sender, Characters character) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusAssistantCard(String sender, String player, Integer weight) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusEndGame(String sender, String winner) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity) {
        errorIllegalMessage();
    }

    @Override
    public void statusTeacher(String sender, String id, List<TeacherColor> which) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusTower(String sender, Map<String, TowerColor> conquests) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusTower(String sender, String player, TowerColor color) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusIslandIds(String sender, List<String> ids) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusCloudIds(String sender, List<String> ids) {
        errorIllegalMessage();
    }

    @Override
    public void statusMotherNature(String sender, String islandId) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusAction(String sender, String actualPlayer) {
        errorIllegalMessage();
    }

    @Override
    public void statusPlanning(String sender, String actualPlayer) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusCharacterCard(String sender, Map<String, Integer> money) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusPlayerMoney(String sender, Map<String, Integer> money) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusStudent(String sender, Characters character, Map<TeacherColor, Integer> quantity) {
        errorIllegalMessage();
    }

    /**
     * Send error message
     */
    @Override
    public void statusNoEntry(String sender, List<String> islandIds) {
        errorIllegalMessage();
    }

    /**
     * Send in broadcast who wins the game
     *
     * @param winner name of winner
     */
    private void sendEndGame(String winner) {
        gameManager.broadcastMessage(PlayMessagesFabric.statusEndGame(server, winner));
    }

    /**
     * Send error message in broadcast
     */
    private void errorIllegalMessage() {
        Message error = new ErrorMessage(server, server + " should not receive this message");
        gameManager.broadcastMessage(error);
    }

    /**
     * Send error message in broadcast
     */
    private void errorInExecution(String error) {
        Message horror = new ErrorMessage(server, error);
        gameManager.broadcastMessage(horror);
    }

    /**
     * Send error message in broadcast
     */
    private void errorInExecution(String playerName, String error) {
        Message horror = new ErrorMessage(server, error);
        gameManager.sendMessage(playerName, horror);
    }

    /**
     * Getter
     */
    public TurnController getTurnController() {
        return turnController;
    }

    /**
     * Getter
     */
    public InboundController getInbound() {
        return inbound;
    }

    /**
     * Getter
     */
    public OutboundController getOutbound() {
        return outbound;
    }

    /**
     * Getter
     */
    public GameState getState() {
        return turnController.getState();
    }

    /**
     * Getter
     */
    public boolean isCharacterActive() {
        return turnController.isCharacterActive();
    }

    /**
     * Getter
     */
    public List<String> getPlayersInOrder() {
        return outbound.getPlayersInOrder();
    }

    /**
     * Getter
     */
    public String getActualPlayer() {
        return turnController.getActivePlayer();
    }

    /**
     * Getter
     */
    public boolean isExpertVariant() {
        return expertVariant;
    }

    /**
     * Getter
     */
    public List<String> getPlayerNames() {
        return new ArrayList<>(playerNames);
    }

    /**
     * Getter
     */
    public String getMainPlayer() {
        return mainPlayer;
    }

    /**
     * Getter
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * Send a game stop message
     */
    public void stop() {
        stop = true;
        gameManager.broadcastMessage(new GenericMessage("Game is stopped"));
    }

    /**
     * Send a game resumed  message
     */
    public void unStop() {
        gameManager.broadcastMessage(new GenericMessage("Game has resumed"));
        stop = false;
    }

    /**
     * Getter
     */
    public boolean isStopped() {
        return stop;
    }
}
