package it.polimi.ingsw.model.phases;

import it.polimi.ingsw.model.phase.PianificationFase;
import it.polimi.ingsw.model.player.AssistantCard;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PianificationFaseTest {

    @Test
    public void activationTest(){
        Game game = new Game();

        game.addPlayer("Aldo");
        game.addPlayer("Giovanni");

        game.gameStarter();

        PianificationFase phase = game.getPianificationFase();

        assertFalse(phase.isActivated());
        assertFalse(phase.isInOrder());

        phase.activate();

        assertTrue(phase.isActivated());
    }

    @Test
    public void simple2PlayerTest(){
        Game game = new Game();

        game.addPlayer("Aldo");
        game.addPlayer("Giovanni");

        game.gameStarter();

        PianificationFase phase = game.getPianificationFase();

        assertFalse(phase.isActivated());
        assertFalse(phase.isInOrder());
        phase.activate();
        assertTrue(phase.isActivated());
        assertFalse(phase.isInOrder());

        List<Player> players = game.getPlayers();
        players.get(0).playAssistantCard(new AssistantCard(4));
        assertFalse(phase.isInOrder());
        players.get(1).playAssistantCard(new AssistantCard(6));
        assertTrue(phase.isInOrder());

        assertEquals(players, phase.getPlayersInOrder());
        assertEquals(players.get(0), phase.getActualPlayer());
        assertEquals(2, phase.getMotherNatureHops(phase.getActualPlayer()));
        phase.nextPlayer();
        assertEquals(players.get(1), phase.getActualPlayer());
        assertEquals(3, phase.getMotherNatureHops(phase.getActualPlayer()));

        phase.reset();
        assertFalse(phase.isInOrder());
        assertFalse(phase.isActivated());

        phase.activate();
        assertTrue(phase.isActivated());
        assertEquals(new ArrayList<>(), phase.getPlayersInOrder());
        assertNull(phase.getActualPlayer());
    }

    @Test
    public void simple3PlayerTest(){
        Game game = new Game();

        game.addPlayer("Aldo");
        game.addPlayer("Giovanni");
        game.addPlayer("Giacomo");

        game.gameStarter();

        PianificationFase phase = game.getPianificationFase();

        assertFalse(phase.isActivated());
        assertFalse(phase.isInOrder());
        phase.activate();
        assertTrue(phase.isActivated());
        assertFalse(phase.isInOrder());

        List<Player> players = game.getPlayers();
        players.get(0).playAssistantCard(new AssistantCard(4));
        assertFalse(phase.isInOrder());
        players.get(1).playAssistantCard(new AssistantCard(6));
        assertFalse(phase.isInOrder());
        players.get(2).playAssistantCard(new AssistantCard(7));
        assertTrue(phase.isInOrder());

        assertEquals(players, phase.getPlayersInOrder());
        assertEquals(players.get(0), phase.getActualPlayer());
        assertEquals(2, phase.getMotherNatureHops(phase.getActualPlayer()));
        phase.nextPlayer();
        assertEquals(players.get(1), phase.getActualPlayer());
        assertEquals(3, phase.getMotherNatureHops(phase.getActualPlayer()));
        phase.nextPlayer();
        assertEquals(players.get(2), phase.getActualPlayer());
        assertEquals(4, phase.getMotherNatureHops(phase.getActualPlayer()));

        phase.reset();
        assertFalse(phase.isInOrder());
        assertFalse(phase.isActivated());

        phase.activate();
        assertTrue(phase.isActivated());
        assertEquals(new ArrayList<>(), phase.getPlayersInOrder());
        assertNull(phase.getActualPlayer());
    }
}
