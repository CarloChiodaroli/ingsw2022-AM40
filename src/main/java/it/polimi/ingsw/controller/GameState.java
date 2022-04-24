package it.polimi.ingsw.controller;

/**
 * Used only in GameController to keep track of the general state of the game
 */
enum GameState {
    INITIAL, PLANNING, ACTION;

    static GameState next(GameState state) {
        if (state.equals(INITIAL) || state.equals(ACTION)) {
            return PLANNING;
        } else {
            return ACTION;
        }
    }

}