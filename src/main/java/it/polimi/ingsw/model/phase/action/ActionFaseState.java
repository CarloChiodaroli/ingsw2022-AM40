package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.util.Optional;

public abstract class ActionFaseState {

    private final ActionPhase actionPhase;

    protected ActionFaseState(ActionPhase actionPhase) {
        this.actionPhase = actionPhase;
    }

    protected void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) {
    }

    protected void handle(Player player, TeacherColor fromColor, TeacherColor toColor) {
    }

    protected void handle(Player player, int steps, int maxSteps) {
    }

    protected void handle(Player player, Island island) {
    }

    protected void handle(Player player, StudentsManager cloud) {
    }

    protected void handle() {
    }

    public ActionPhase getActionFase() {
        return actionPhase;
    }
}
