package it.polimi.ingsw.server.controller.inner;

import it.polimi.ingsw.server.controller.outer.PlayMessagesReader;
import it.polimi.ingsw.server.model.enums.Characters;

import java.util.ArrayList;
import java.util.List;

public class TurnController {

    private final PlayMessagesReader reader;

    private List<String> nicknameQueue;
    private List<String> players;
    private String activePlayer;
    private Characters actualCharacter;
    private GameState state;

    public TurnController(PlayMessagesReader reader) {
        this.nicknameQueue = new ArrayList<>(reader.getPlayerNames());
        //this.activePlayer = nicknameQueue.get(0); // set first active player
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
        setActivePlayer();
    }

    public boolean isGameStarted(){
        return state != GameState.INITIAL;
    }

    public boolean nextTurn() {
        if (state.equals(GameState.PLANNING)) {
            int i = players.indexOf(activePlayer) + 1;
            if (i >= players.size()) {
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
                state = GameState.next(state);
                activePlayer = players.get(0);
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

    public void setActivePlayer(){
        if(activePlayer == null) activePlayer = players.get(0);
        else {
            int index = players.indexOf(activePlayer);
            if(index == players.size()) index = 0;
            activePlayer = players.get(index);
        }
    }

    public boolean isCharacterActive(){
        return actualCharacter != null;
    }

    /* To see better later
    public void next() {
        int currentActive = nicknameQueue.indexOf(activePlayer);
        if (currentActive + 1 < game.getNumCurrentPlayers()) {
            currentActive = currentActive + 1;
        } else {
            currentActive = 0;
        }
        activePlayer = nicknameQueue.get(currentActive);
    }*/

    /*
    public void newTurn() {
        turnControllerNotify("Turn of " + activePlayer, activePlayer);
        VirtualView vv = virtualViewMap.get(getActivePlayer());
        vv.showGenericMessage("It's your turn!");
        StorageData storageData = new StorageData();
        storageData.store(gameController);
    }

    public void turnControllerNotify(String messageToNotify, String excludeNickname) {
        virtualViewMap.entrySet().stream()
                .filter(entry -> !excludeNickname.equals(entry.getKey()))
                .map(Map.Entry::getValue)
                .forEach(vv -> vv.showGenericMessage(messageToNotify));
    }
    */

    public List<String> getNicknameQueue() {
        return nicknameQueue;
    }
}
