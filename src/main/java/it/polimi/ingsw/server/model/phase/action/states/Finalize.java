package it.polimi.ingsw.server.model.phase.action.states;

import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.ActionPhaseState;
import it.polimi.ingsw.server.model.player.Player;

/**
 * 5th state of Action phase, this class models the choice and use of a cloud.
 */
public class Finalize extends ActionPhaseState {

    /**
     * Constructor
     */
    public Finalize(ActionPhase actionPhase) {
        super(actionPhase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Player player, StudentsManager cloud) {
        StudentsManager destination = player.getEntrance();
        int tmp = cloud.howManyTotStudents();
        for (int i = 0; i < tmp; i++) {
            cloud.getStudent().ifPresent(x -> {
                if (!destination.addStudent(x)) {
                    cloud.addStudent(x);
                    return;
                }
            });
        }
        if (cloud.howManyTotStudents() == 0) {
            getActionPhase().getGame().removeCloud(cloud);
        }
    }
}
