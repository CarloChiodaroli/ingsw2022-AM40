package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.TowerColor;
import it.polimi.ingsw.model.phase.action.states.MergeIsland;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MergeIslandTest {
    @Test
    public void mergeIsland2PlayersTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        assertEquals(12, game.getTable().getIslandList().size());
        MergeIsland mergeIsland = new MergeIsland(game.getActionFase());

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

    @Test
    public void mergeIsland3PlayersTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.addPlayer("Barbara");
        game.gameStarter();
        assertEquals(12, game.getTable().getIslandList().size());
        MergeIsland mergeIsland = new MergeIsland(game.getActionFase());

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
