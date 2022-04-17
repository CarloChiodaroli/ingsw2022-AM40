package it.polimi.ingsw.controller;

enum GameState {
    PREPARATION, PIANIFICATION, ACTION;

    static GameState next(GameState state) {
        if (state.equals(PREPARATION) || state.equals(ACTION)) {
            return PIANIFICATION;
        } else {
            return ACTION;
        }
    }

}