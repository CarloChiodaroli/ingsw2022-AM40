package it.polimi.ingsw.manuel.utils;

import it.polimi.ingsw.manuel.controller.GameController;

public class Persistence  {

    private final GameController gameController;

    public Persistence(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }

}