package it.polimi.ingsw.server.model.phase.action.states;

import it.polimi.ingsw.server.model.phase.action.ActionFaseState;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.table.Island;

import java.util.List;

/**
 * 4th Action Phase State, this class models the merging of the islands. Does not correspond to a command sent by a player,
 * it's automatically called after influence command.
 */
public class MergeIsland extends ActionFaseState {

    /**
     * Constructor
     */
    public MergeIsland(ActionPhase actionPhase) {
        super(actionPhase);
    }

    /**
     * If is necessary merge neighboring islands
     */
    @Override
    public void handle() {
        Island mount, valley;
        List<Island> islands = super.getActionFase().getGame().getTable().getIslandList();
        for (int i = 0; i < islands.size(); i++) {
            mount = islands.get(i);
            if (i + 1 == islands.size()) valley = islands.get(0);
            else valley = islands.get(i + 1);
            if (mount.hasTowers() && valley.hasTowers()) {
                if (mount.getTowerColor().equals(valley.getTowerColor())) {
                    super.getActionFase().getGame().getTable().mergeIsland(mount, valley);
                    this.handle();
                    return;
                }
            }
        }
    }
}
