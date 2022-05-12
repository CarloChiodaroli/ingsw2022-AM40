package it.polimi.ingsw.manuel.controller;

import it.polimi.ingsw.manuel.model.Game;
import it.polimi.ingsw.manuel.utils.StorageData;
import it.polimi.ingsw.manuel.view.VirtualView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TurnController {

    private final Game game;
    private final List<String> nicknameQueue;
    private String activePlayer;

    transient Map<String, VirtualView> virtualViewMap;
    private final GameController gameController;

    public TurnController(Map<String, VirtualView> virtualViewMap, GameController gameController) {
        this.game = Game.getInstance();
        this.nicknameQueue = new ArrayList<>(game.getPlayersNicknames());
        this.activePlayer = nicknameQueue.get(0); // set first active player
        this.virtualViewMap = virtualViewMap;
        this.gameController = gameController;
    }

    public String getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void next() {
        int currentActive = nicknameQueue.indexOf(activePlayer);
        if (currentActive + 1 < game.getNumCurrentPlayers())
        {
            currentActive = currentActive + 1;
        }
        else
        {
            currentActive = 0;
        }
        activePlayer = nicknameQueue.get(currentActive);
    }

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

    public void setVirtualViewMap(Map<String, VirtualView> virtualViewMap) {
        this.virtualViewMap = virtualViewMap;
    }

    public List<String> getNicknameQueue() {
        return nicknameQueue;
    }

}
