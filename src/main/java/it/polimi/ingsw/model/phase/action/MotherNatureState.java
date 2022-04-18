package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.model.table.MotherNature;

import java.security.InvalidParameterException;
import java.util.List;

public class MotherNatureState extends ActionFaseState{

    public MotherNatureState(ActionFase actionFase){
        super(actionFase);
    }

    @Override
    public void handle(Player player, int steps, int maxSteps) throws InvalidParameterException{
        if(steps > maxSteps || steps == 0)
            throw new InvalidParameterException("Illegal number of steps");
        List<Island> islands = super.getActionFase().getGame().getTable().getIslandList();
        int actualIndex = islands.indexOf(MotherNature.getMotherNature().getPosition().orElseThrow()) + steps;
        if(actualIndex >= islands.size()) actualIndex -= islands.size();
        MotherNature.getMotherNature().setPosition(islands.get(actualIndex));
    }
}
