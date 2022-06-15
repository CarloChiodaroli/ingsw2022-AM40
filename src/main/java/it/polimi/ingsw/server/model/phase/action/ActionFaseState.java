package it.polimi.ingsw.server.model.phase.action;

import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Island;

import java.util.Optional;

public abstract class ActionFaseState {

    private final ActionPhase actionPhase;

    protected ActionFaseState(ActionPhase actionPhase) {
        this.actionPhase = actionPhase;
    }

    protected void handle(TeacherColor color, Optional<StudentsManager> from, Optional<StudentsManager> to) {
    }

    protected void handle(Player player, TeacherColor fromColor, TeacherColor toColor, String place) {
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
