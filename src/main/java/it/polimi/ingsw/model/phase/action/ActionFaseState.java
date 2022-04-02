package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

abstract class ActionFaseState{

    private final ActionFase actionFase;

    protected ActionFaseState(ActionFase actionFase){
        this.actionFase = actionFase;
    }

    protected void handle(TeacherColor color, StudentsManager from, StudentsManager to) {    };

    protected void handle(Player player, int steps, int maxSteps) {    };

    protected void handle(Player player, Island island) {    };

    protected void handle(Player player, StudentsManager cloud) {    }

    protected void handle() {    }

    protected ActionFase getActionFase(){
        return actionFase;
    }
}
