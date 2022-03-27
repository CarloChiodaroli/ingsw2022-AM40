package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    }


}
