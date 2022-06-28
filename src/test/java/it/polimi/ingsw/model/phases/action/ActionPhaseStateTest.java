package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.phase.PlanningPhase;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.ActionFaseState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link it.polimi.ingsw.server.enums.ActionPhaseStateType} methods
 */
public class ActionPhaseStateTest {
    /**
     * Test creation action phase
     */
    @Test
    public void actionFaseStateTest(){
        Game game = new Game();
        ActionPhase actionfase = new ActionPhase(game);
        ActionFaseStateInstance actionFaseState = new ActionFaseStateInstance(actionfase);
        assertEquals(actionfase, actionFaseState.getActionFase());
    }


}

class ActionFaseStateInstance extends ActionFaseState {
    ActionFaseStateInstance(ActionPhase actionfase){
        super(actionfase);
    }
}
