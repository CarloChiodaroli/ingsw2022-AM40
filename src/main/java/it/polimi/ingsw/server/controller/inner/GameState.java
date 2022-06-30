package it.polimi.ingsw.server.controller.inner;

/**
 * Used only in Controller to keep track of the general state of the game.
 */
public enum GameState {
    /**
     * Represents when the play is not started.
     */
    INITIAL,
    /**
     * Represents when the play is in planning phase.
     */
    PLANNING,
    /**
     * Represents when the play is in action phase.
     */
    ACTION;

    /**
     * Getter of the next game phase
     *
     * @param state previous state
     * @return after state
     */
    static GameState next(GameState state) {
        if (state.equals(INITIAL) || state.equals(ACTION)) {
            return PLANNING;
        } else {
            return ACTION;
        }
    }

}