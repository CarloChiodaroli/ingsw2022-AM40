package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.phase.action.ActionFase;
import it.polimi.ingsw.model.phase.action.ActionFaseState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActionFaseStateTest {
    @Test
    public void actionFaseStateTest(){
        Game game = new Game();
        ActionFase actionfase = new ActionFase(game);
        ActionFaseStateIstance actionfasestate = new ActionFaseStateIstance(actionfase);
        assertEquals(actionfase, actionfasestate.getActionFase());
    }


}

class ActionFaseStateIstance extends ActionFaseState {
    ActionFaseStateIstance(ActionFase actionfase){
        super(actionfase);
    }
}
