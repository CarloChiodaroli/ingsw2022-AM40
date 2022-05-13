package it.polimi.ingsw.server.model.phase.action.states;

import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.ActionFaseState;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.model.table.MotherNature;

import java.security.InvalidParameterException;
import java.util.List;

public class MotherNatureState extends ActionFaseState {

    public MotherNatureState(ActionPhase actionPhase) {
        super(actionPhase);
    }

    @Override
    public void handle(Player player, int steps, int maxSteps) throws InvalidParameterException {
        if (steps > maxSteps || steps == 0)
            throw new InvalidParameterException("Illegal number of steps");
        List<Island> islands = super.getActionFase().getGame().getTable().getIslandList();
        int actualIndex = islands.indexOf(MotherNature.getMotherNature().getPosition().orElseThrow()) + steps;
        if (actualIndex >= islands.size()) actualIndex -= islands.size();
        MotherNature.getMotherNature().setPosition(islands.get(actualIndex));
    }
}
