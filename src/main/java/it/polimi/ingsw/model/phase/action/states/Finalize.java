package it.polimi.ingsw.model.phase.action.states;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.phase.action.ActionFase;
import it.polimi.ingsw.model.phase.action.ActionFaseState;
import it.polimi.ingsw.model.player.Player;

public class Finalize extends ActionFaseState {

    public Finalize(ActionFase actionFase) {
        super(actionFase);
    }

    @Override
    public void handle(Player player, StudentsManager cloud) {
        StudentsManager destination = player.getEntrance();
        int tmp = cloud.howManyTotStudents();
        for (int i = 0; i < tmp; i++) {
            cloud.getStudent().ifPresent(destination::addStudent);
        }
    }
}
