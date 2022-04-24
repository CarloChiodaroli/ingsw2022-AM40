package it.polimi.ingsw.model.phase.action.states;

import it.polimi.ingsw.model.phase.action.ActionFase;
import it.polimi.ingsw.model.phase.action.ActionFaseState;
import it.polimi.ingsw.model.table.Island;

import java.util.List;

public class MergeIsland extends ActionFaseState {

    public MergeIsland(ActionFase actionFase){
        super(actionFase);
    }

    @Override
    public void handle() {
        Island mount, valley;
        List<Island> islands = super.getActionFase().getGame().getTable().getIslandList();
        for(int i = 0; i < islands.size(); i++){
            mount = islands.get(i);
            if(i + 1 == islands.size()) valley = islands.get(0);
            else valley = islands.get(i + 1);
            if(mount.hasTowers() && valley.hasTowers()){
                if(mount.getTowerColor().equals(valley.getTowerColor())){
                    super.getActionFase().getGame().getTable().mergeIsland(mount, valley);
                    this.handle();
                    return;
                }
            }
        }
    }
}
