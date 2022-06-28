package it.polimi.ingsw.model;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the {@link Game} methods
 */
public class GameTest {

    /**
     * Test game
     */
    @Test
    public void playerTest(){
        Game game = new Game();
        playerTest(game);
    }

    /**
     * Test add players
     * @param game game
     */
    public void playerTest(Game game){
        assertTrue(game.addPlayer("Alice"));
        assertTrue(game.addPlayer("Bob"));
        assertTrue(game.addPlayer("Karl"));
        assertFalse(game.addPlayer("Dan"));
    }

    /**
     * Test switch game mode
     */
    @Test
    public void expertVariantSwitchTest(){
        Game game = new Game();
        assertFalse(game.isExpertVariant());
        game.switchExpertVariant();
        assertTrue(game.isExpertVariant());
        game.switchExpertVariant();
        assertFalse(game.isExpertVariant());
    }

    /**
     * Test game start
     */
    @Test
    public void startGameTest(){
        Game game = new Game();
        playerTest(game);
        assertFalse(game.isGameStarted());
        game.gameStarter();
        assertTrue(game.isGameStarted());

        assertEquals(9, game.getPlayers().get(0).getEntrance().howManyTotStudents());
        assertEquals(9, game.getPlayers().get(1).getEntrance().howManyTotStudents());
        assertEquals(9, game.getPlayers().get(2).getEntrance().howManyTotStudents());
    }

    /**
     * Test find the winner
     */
    @Test
    @DisplayName("Finding right winner")
    public void winnerFinderTest(){
        Game game = new Game();
        game.addPlayer("Aldo");
        game.addPlayer("Giovanni");
        game.gameStarter();

        Player aldo = game.getPlayers().get(0);
        Player giovanni = game.getPlayers().get(1);

        aldo.getTower(3);

        assertEquals(aldo.getName(), game.searchPlayerWithMostTower());

        giovanni.getTower(3);

        aldo.addTeacher(TeacherColor.BLUE);

        assertEquals(aldo.getName(), game.searchPlayerWithMostTower());
    }

}
