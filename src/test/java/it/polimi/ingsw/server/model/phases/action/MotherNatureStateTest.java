package it.polimi.ingsw.server.model.phases.action;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.phase.action.states.MergeIsland;
import it.polimi.ingsw.server.model.phase.action.states.MotherNatureState;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.model.table.MotherNature;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the {@link MotherNatureState} methods
 */
public class MotherNatureStateTest {
    /**
     * Test mother nature position with 2 players
     */
    @Test
    public void motherNatureStateTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        MotherNatureState motherNatureState = new MotherNatureState(game.getActionPhase());
        Optional<Island> initialpos = MotherNature.getMotherNature().getPosition();
        Island inpos = initialpos.get();
        int firstposition = game.getTable().getIslandList().indexOf(inpos);

        motherNatureState.handle(game.getPlayers().get(0), 5, 5);
        Optional<Island> secondpos = MotherNature.getMotherNature().getPosition();
        Island secpos = secondpos.get();
        int secondposition = game.getTable().getIslandList().indexOf(secpos);
        if (firstposition + 5 > 11)
            assertEquals(firstposition - 12 + 5, secondposition);
        else
            assertEquals(firstposition + 5, secondposition);
        firstposition = secondposition;

        assertThrows(InvalidParameterException.class ,() -> motherNatureState.handle(game.getPlayers().get(1), 7, 5));
        secondpos = MotherNature.getMotherNature().getPosition();
        secpos = secondpos.get();
        secondposition = game.getTable().getIslandList().indexOf(secpos);
        assertEquals(firstposition, secondposition);

        assertThrows(InvalidParameterException.class ,() -> motherNatureState.handle(game.getPlayers().get(1), 0, 5));
        secondpos = MotherNature.getMotherNature().getPosition();
        secpos = secondpos.get();
        secondposition = game.getTable().getIslandList().indexOf(secpos);
        assertEquals(firstposition, secondposition);

        motherNatureState.handle(game.getPlayers().get(1), 2, 4);
        secondpos = MotherNature.getMotherNature().getPosition();
        secpos = secondpos.get();
        secondposition = game.getTable().getIslandList().indexOf(secpos);
        if (firstposition + 2 > 11)
            assertEquals(firstposition - 12 + 2, secondposition);
        else
            assertEquals(firstposition + 2, secondposition);

    }

    /**
     * Test mother nature position with 3 players
     */
    @Test
    public void motherNatureState3PlayersTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.addPlayer("Barbara");
        game.gameStarter();
        MotherNatureState motherNatureState = new MotherNatureState(game.getActionPhase());
        Optional<Island> initialpos = MotherNature.getMotherNature().getPosition();
        Island inpos = initialpos.get();
        int firstposition = game.getTable().getIslandList().indexOf(inpos);

        motherNatureState.handle(game.getPlayers().get(2), 5, 5);
        Optional<Island> secondpos = MotherNature.getMotherNature().getPosition();
        Island secpos = secondpos.get();
        int secondposition = game.getTable().getIslandList().indexOf(secpos);
        if (firstposition + 5 > 11)
            assertEquals(firstposition - 12 + 5, secondposition);
        else
            assertEquals(firstposition + 5, secondposition);
        firstposition = secondposition;

        assertThrows(InvalidParameterException.class ,() -> motherNatureState.handle(game.getPlayers().get(2), 7, 5));
        secondpos = MotherNature.getMotherNature().getPosition();
        secpos = secondpos.get();
        secondposition = game.getTable().getIslandList().indexOf(secpos);
        assertEquals(firstposition, secondposition);

        assertThrows(InvalidParameterException.class ,() -> motherNatureState.handle(game.getPlayers().get(2), 0, 5));
        secondpos = MotherNature.getMotherNature().getPosition();
        secpos = secondpos.get();
        secondposition = game.getTable().getIslandList().indexOf(secpos);
        assertEquals(firstposition, secondposition);

        motherNatureState.handle(game.getPlayers().get(2), 2, 4);
        secondpos = MotherNature.getMotherNature().getPosition();
        secpos = secondpos.get();
        secondposition = game.getTable().getIslandList().indexOf(secpos);
        if (firstposition + 2 > 11)
            assertEquals(firstposition - 12 + 2, secondposition);
        else
            assertEquals(firstposition + 2, secondposition);
    }
}
