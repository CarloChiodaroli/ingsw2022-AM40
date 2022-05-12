package it.polimi.ingsw.utils;

import it.polimi.ingsw.controller.GameManager;

public class Persistence  {

    private final GameManager gameManager;

    public Persistence(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameManager getGameController() {
        return gameManager;
    }

}