package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.enums.Characters;

import java.util.ArrayList;
import java.util.List;

public class TurnController {

    private final GameController gameController;

    private List<String> nicknameQueue;
    private List<String> players;
    private String activePlayer;
    private Characters actualCharacter;
    private GameState state;

    public TurnController(GameController gameController) {
        this.nicknameQueue = new ArrayList<>(gameController.getPlayerNames());
        this.activePlayer = nicknameQueue.get(0); // set first active player
        this.gameController = gameController;
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

    public void nextTurn() {
        if (state.equals(GameState.PLANNING)) {
            int i = players.indexOf(activePlayer) + 1;
            if (i >= players.size()) {
                state = GameState.next(state);
                nicknameQueue = gameController.getPlayersInOrder();
                activePlayer = nicknameQueue.get(0);
            } else {
                activePlayer = players.get(i);
            }
        } else if (state.equals(GameState.ACTION)) {
            actualCharacter = null;
            int i = nicknameQueue.indexOf(activePlayer) + 1;
            if (i >= nicknameQueue.size()) {
                state = GameState.next(state);
                activePlayer = players.get(0);
            } else {
                activePlayer = nicknameQueue.get(i);
            }
        }
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
