package it.polimi.ingsw.server.model.phases.action;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.ActionPhaseState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the {@link it.polimi.ingsw.server.enums.ActionPhaseStateType} methods
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
        assertEquals(actionfase, actionFaseState.getActionPhase());
    }


}

class ActionFaseStateInstance extends ActionPhaseState {
    ActionFaseStateInstance(ActionPhase actionfase){
        super(actionfase);
    }
}
