package it.polimi.ingsw.server.model.phase.action;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Island;

import java.util.Optional;

/**
 * Models the abstract and most general {@link ActionPhase} state, keeping track of the "context" class.
 */
public abstract class ActionFaseState {

    private final ActionPhase actionPhase;

    protected ActionFaseState(ActionPhase actionPhase) {
        this.actionPhase = actionPhase;
    }

    protected void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    protected void handle(Player player, TeacherColor fromColor, TeacherColor toColor, String place) {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    protected void handle(Player player, int steps, int maxSteps) {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    protected void handle(Player player, Island island) {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    protected void handle(Player player, StudentsManager cloud) {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    protected void handle() {
        throw new IllegalCallerException("A very critical error occurred in the model, please retry");
    }

    public ActionPhase getActionFase() {
        return actionPhase;
    }
}
