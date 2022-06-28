package it.polimi.ingsw.server.controller.inner;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.server.controller.outer.PlayMessagesReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class keeps track of the turns of the players
 */
public class TurnController {

    private final PlayMessagesReader reader;

    private List<String> nicknameQueue;
    private List<String> players;
    private String activePlayer;
    private Characters actualCharacter;
    private GameState state;
    private int startingPlayer;
    private int messages;
    private String savedIsland;
    private List<String> playersToSkip;

    /**
     * Constructor
     */
    public TurnController(PlayMessagesReader reader) {
        this.nicknameQueue = new ArrayList<>(reader.getPlayerNames());
        this.reader = reader;
        setInitialState();
        playersToSkip = new ArrayList<>();
    }

    /**
     * Set the initial state
     */
    public void setInitialState() {
        this.state = GameState.INITIAL;
    }

    /**
     * Get the game state
     *
     * @return game state
     */
    public GameState getState() {
        return state;
    }

    /**
     * Set the start of the game and pianification phase
     *
     * @param players list of players
     */
    public void startPlay(List<String> players) {
        state = GameState.PLANNING;
        this.players = players;
        startingPlayer = 0;
        if (activePlayer == null) activePlayer = players.get(startingPlayer);
        else {
            int index = players.indexOf(activePlayer);
            if (index == players.size()) index = 0;
            activePlayer = players.get(index);
        }
    }

    /**
     * Return if the game is started
     *
     * @return true if game is started
     */
    public boolean isGameStarted() {
        return state != GameState.INITIAL;
    }

    /**
     * Set the next player in pianification or action phase
     *
     * @return true if the player is not skipped
     */
    public boolean nextTurn() {
        if (state.equals(GameState.PLANNING)) {
            int i = players.indexOf(activePlayer) + 1;
            if (i >= players.size()) {
                state = GameState.next(state);
                nicknameQueue = reader.getPlayersInOrder();
                if (nicknameQueue.isEmpty()) return false;
                activePlayer = nicknameQueue.get(0);
                if(playersToSkip.contains(activePlayer)) nextTurn();
                return true;
            } else {
                activePlayer = players.get(i);
                if(playersToSkip.contains(activePlayer)) nextTurn();
                return false;
            }
        } else if (state.equals(GameState.ACTION)) {
            actualCharacter = null;
            nicknameQueue = reader.getPlayersInOrder();
            int i = nicknameQueue.indexOf(activePlayer) + 1;
            if (i >= nicknameQueue.size()) {
                String player = players.get(0);
                players.remove(player);
                players.add(player);
                playersToSkip = new ArrayList<>(playersToSkip);
                state = GameState.next(state);
                activePlayer = players.get(0);
                if(playersToSkip.contains(activePlayer)) nextTurn();
                return true;
            } else {
                activePlayer = nicknameQueue.get(i);
                if(playersToSkip.contains(activePlayer)) nextTurn();
                return false;
            }
        }
        return false;
    }

    /**
     * Get the actual player
     *
     * @return actual player
     */
    public String getActivePlayer() {
        return activePlayer;
    }

    /**
     * Get the actual state
     *
     * @return actual state
     */
    public String getActualState() {
        return state.toString();
    }

    /**
     * Return if the character is active
     *
     * @return true if the character is active
     */
    public boolean isCharacterActive() {
        return actualCharacter != null;
    }

    /**
     * If any, return active character
     *
     * @return actual character
     */
    public Optional<Characters> getActualCharacter() {
        return Optional.ofNullable(actualCharacter);
    }

    /**
     * Set a character
     *
     * @param character chosen character
     */
    public void setActualCharacter(Characters character) {
        actualCharacter = character;
    }

    /**
     * Save island's id
     *
     * @param islandId island's id
     */
    public void saveIsland(String islandId) {
        savedIsland = islandId;
    }

    /**
     * Get saved island's id
     *
     * @return id of saved island
     */
    public String getSavedIsland() {
        String result = savedIsland;
        savedIsland = null;
        return result;
    }

    /**
     * Skip a player
     *
     * @param playerName player skip
     */
    public void skipPlayer(String playerName) {
        if (getActivePlayer().equals(playerName)) nextTurn();
        playersToSkip.add(playerName);
        players.remove(playerName);
    }

    /**
     * Don't skip a player
     *
     * @param playerName player don't skip
     */
    public void unSkipPlayer(String playerName) {
        players.add(playerName);
    }

    /**
     * If the actual player is to be skipped, change turn
     */
    private void controlAndSkip() {
        if (playersToSkip.contains(activePlayer)) {
            nextTurn();
        }
    }
}
