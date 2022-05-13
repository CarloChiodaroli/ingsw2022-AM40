package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.server.controller.GameManager;

public class Persistence  {

    private final GameManager gameManager;

    public Persistence(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameManager getGameController() {
        return gameManager;
    }

}