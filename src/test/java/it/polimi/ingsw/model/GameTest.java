package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    public void playerTest(){
        Game game = new Game();
        playerTest(game);
    }

    public void playerTest(Game game){
        assertTrue(game.addPlayer("Alice"));
        assertTrue(game.addPlayer("Bob"));
        assertTrue(game.addPlayer("Karl"));
        assertFalse(game.addPlayer("Dan"));
    }

    @Test
    public void expertVariantSwitchTest(){
        Game game = new Game();
        assertFalse(game.isExpertVariant());
        game.switchExpertVariant();
        assertTrue(game.isExpertVariant());
        game.switchExpertVariant();
        assertFalse(game.isExpertVariant());
    }

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

        assertEquals(aldo, game.searchPlayerWithMostTower());

        giovanni.getTower(3);

        aldo.getRoomTable(TeacherColor.BLUE).setTeacherPresence(true);

        assertEquals(aldo, game.searchPlayerWithMostTower());
    }

}
