package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.phase.action.ActionPhase;
import it.polimi.ingsw.model.phase.action.ActionFaseState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActionPhaseStateTest {
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
