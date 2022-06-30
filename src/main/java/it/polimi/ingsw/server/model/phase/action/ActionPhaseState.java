package it.polimi.ingsw.server.model.phase.action;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Island;

import java.util.Optional;

/**
 * Models the abstract and most general {@link ActionPhase} state, keeping track of the "context" class.
 */
public abstract class ActionPhaseState {

    private final ActionPhase actionPhase;

    /**
     * Constructor
     */
    protected ActionPhaseState(ActionPhase actionPhase) {
        this.actionPhase = actionPhase;
    }

    /**
     * The starting method for moving student from a place to another, controls the presence of
     * the places and students
     *
     * @param color student color
     * @param from  place where begin the movement
     * @param to    place where finish the movement
     */
    protected void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    protected void handle(Player player, TeacherColor fromColor, TeacherColor toColor, String place) {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    /**
     * After check number of steps, move mother nature
     *
     * @param player   player want to move
     * @param steps    steps the player want
     * @param maxSteps maximum number of steps are allowed
     */
    protected void handle(Player player, int steps, int maxSteps) {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    /**
     * If there isn't a prohibition card, calculate teh influence
     *
     * @param player player who calls the calculation
     * @param island computation island
     */
    protected void handle(Player player, Island island) {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    /**
     * Fill entrance with students in cloud
     *
     * @param player player
     * @param cloud  chosen cloud
     */
    protected void handle(Player player, StudentsManager cloud) {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    /**
     * If is necessary merge neighboring islands
     */
    protected void handle() {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    public ActionPhase getActionPhase() {
        return actionPhase;
    }
}
