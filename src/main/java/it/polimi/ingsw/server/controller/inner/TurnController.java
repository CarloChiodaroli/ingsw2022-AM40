package it.polimi.ingsw.server.controller.inner;

import it.polimi.ingsw.server.controller.outer.PlayMessagesReader;
import it.polimi.ingsw.commons.enums.Characters;

import java.util.ArrayList;
import java.util.List;

public class TurnController {

    private final PlayMessagesReader reader;

    private List<String> nicknameQueue;
    private List<String> players;
    private String activePlayer;
    private Characters actualCharacter;
    private GameState state;
    private int startingPlayer;
    private int messages;

    public TurnController(PlayMessagesReader reader) {
        this.nicknameQueue = new ArrayList<>(reader.getPlayerNames());
        this.reader = reader;
        setInitialState();
    }

    public void setInitialState(){
        this.state = GameState.INITIAL;
    }

    public GameState getState() {
        return state;
    }

    public void startPlay(List<String> players){
        state = GameState.PLANNING;
        this.players = players;
        startingPlayer = 0;
        if(activePlayer == null) activePlayer = players.get(startingPlayer);
        else {
            int index = players.indexOf(activePlayer);
            if(index == players.size()) index = 0;
            activePlayer = players.get(index);
        }
    }

    public boolean isGameStarted(){
        return state != GameState.INITIAL;
    }

    public boolean nextTurn() {
        if (state.equals(GameState.PLANNING)) {
            messages++;
            int i = players.indexOf(activePlayer) + 1;
            if(i >= players.size()) i = 0;
            if (messages >= players.size()) {
                state = GameState.next(state);
                nicknameQueue = reader.getPlayersInOrder();
                activePlayer = nicknameQueue.get(0);
                return true;
            } else {
                activePlayer = players.get(i);
                return false;
            }
        } else if (state.equals(GameState.ACTION)) {
            actualCharacter = null;
            int i = nicknameQueue.indexOf(activePlayer) + 1;
            if (i >= nicknameQueue.size()) {
                messages = 0;
                startingPlayer++;
                if(startingPlayer >= players.size()) startingPlayer = 0;
                state = GameState.next(state);
                activePlayer = players.get(startingPlayer);
                return true;
            } else {
                activePlayer = nicknameQueue.get(i);
                return false;
            }
        }
        return false;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    public boolean isCharacterActive(){
        return actualCharacter != null;
    }

    public List<String> getNicknameQueue() {
        return nicknameQueue;
    }
}
