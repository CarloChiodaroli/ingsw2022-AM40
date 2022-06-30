package it.polimi.ingsw.server.model.phases.action;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.phase.action.states.Influence;
import it.polimi.ingsw.server.model.phase.action.states.MergeIsland;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the {@link MergeIsland} methods
 */
public class MergeIslandTest {
    /**
     * Test merge island with 2 players
     */
    @Test
    public void mergeIsland2PlayersTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        assertEquals(12, game.getTable().getIslandList().size());
        MergeIsland mergeIsland = new MergeIsland(game.getActionPhase());

        game.getTable().getIslandList().get(3).setInfluence(TowerColor.WHITE);
        game.getTable().getIslandList().get(4).setInfluence(TowerColor.WHITE);
        mergeIsland.handle();
        assertEquals(11, game.getTable().getIslandList().size());

        game.getTable().getIslandList().get(0).setInfluence(TowerColor.WHITE);
        game.getTable().getIslandList().get(10).setInfluence(TowerColor.WHITE);
        mergeIsland.handle();
        assertEquals(10, game.getTable().getIslandList().size());

        game.getTable().getIslandList().get(2).setInfluence(TowerColor.WHITE);
        game.getTable().getIslandList().get(3).setInfluence(TowerColor.BLACK);
        mergeIsland.handle();
        assertEquals(10, game.getTable().getIslandList().size());

        game.getTable().getIslandList().get(9).setInfluence(TowerColor.BLACK);
        game.getTable().getIslandList().get(0).setInfluence(TowerColor.BLACK);
        mergeIsland.handle();
        assertEquals(9, game.getTable().getIslandList().size());


    }

    /**
     * Test merge island with 3 players
     */
    @Test
    public void mergeIsland3PlayersTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.addPlayer("Barbara");
        game.gameStarter();
        assertEquals(12, game.getTable().getIslandList().size());
        MergeIsland mergeIsland = new MergeIsland(game.getActionPhase());

        game.getTable().getIslandList().get(3).setInfluence(TowerColor.WHITE);
        game.getTable().getIslandList().get(4).setInfluence(TowerColor.WHITE);
        mergeIsland.handle();
        assertEquals(11, game.getTable().getIslandList().size());

        game.getTable().getIslandList().get(0).setInfluence(TowerColor.WHITE);
        game.getTable().getIslandList().get(10).setInfluence(TowerColor.WHITE);
        mergeIsland.handle();
        assertEquals(10, game.getTable().getIslandList().size());

        game.getTable().getIslandList().get(2).setInfluence(TowerColor.WHITE);
        game.getTable().getIslandList().get(3).setInfluence(TowerColor.BLACK);
        mergeIsland.handle();
        assertEquals(10, game.getTable().getIslandList().size());

        game.getTable().getIslandList().get(9).setInfluence(TowerColor.BLACK);
        game.getTable().getIslandList().get(0).setInfluence(TowerColor.BLACK);
        mergeIsland.handle();
        assertEquals(9, game.getTable().getIslandList().size());

        game.getTable().getIslandList().get(3).setInfluence(TowerColor.GREY);
        game.getTable().getIslandList().get(4).setInfluence(TowerColor.GREY);
        mergeIsland.handle();
        assertEquals(8, game.getTable().getIslandList().size());


    }
}