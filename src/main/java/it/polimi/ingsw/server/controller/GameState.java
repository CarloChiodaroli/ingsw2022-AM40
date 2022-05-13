package it.polimi.ingsw.server.controller;

/**
 * Used only in GameManager to keep track of the general state of the game
 */
public enum GameState {
    INITIAL, PLANNING, ACTION;

    static GameState next(GameState state) {
        if (state.equals(INITIAL) || state.equals(ACTION)) {
            return PLANNING;
        } else {
            return ACTION;
        }
    }

}