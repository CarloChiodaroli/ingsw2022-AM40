package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TowerColor;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void assistantCardTest(){
        Player player = new Player(new Game(), "Test", TowerColor.BLACK);

        assertEquals(Optional.of(new AssistantCard(4)), player.playAssistantCard(new AssistantCard(4)));
        assertTrue(player.canChangeAssistantCard());
        assertEquals(Optional.of(new AssistantCard(6)), player.playAssistantCard(new AssistantCard(6)));
        assertTrue(player.canChangeAssistantCard());
        assertEquals(Optional.of(new AssistantCard(3)), player.playAssistantCard(new AssistantCard(3)));
        assertTrue(player.canChangeAssistantCard());
        assertEquals(Optional.of(new AssistantCard(1)), player.playAssistantCard(new AssistantCard(1)));
        assertTrue(player.canChangeAssistantCard());
        assertEquals(Optional.of(new AssistantCard(7)), player.playAssistantCard(new AssistantCard(7)));
        assertTrue(player.canChangeAssistantCard());
        assertEquals(Optional.of(new AssistantCard(8)), player.playAssistantCard(new AssistantCard(8)));
        assertTrue(player.canChangeAssistantCard());
        assertEquals(Optional.of(new AssistantCard(2)), player.playAssistantCard(new AssistantCard(2)));
        assertTrue(player.canChangeAssistantCard());
        assertEquals(Optional.of(new AssistantCard(5)), player.playAssistantCard(new AssistantCard(5)));
        assertTrue(player.canChangeAssistantCard());
        assertEquals(Optional.of(new AssistantCard(9)), player.playAssistantCard(new AssistantCard(9)));
        assertTrue(player.canChangeAssistantCard());
        assertEquals(Optional.of(new AssistantCard(10)), player.playAssistantCard(new AssistantCard(10)));
        assertFalse(player.canChangeAssistantCard());

        assertTrue(player.playAssistantCard(new AssistantCard(10)).isEmpty());
        assertTrue(player.playAssistantCard(new AssistantCard(9)).isEmpty());
        assertTrue(player.playAssistantCard(new AssistantCard(8)).isEmpty());
        assertTrue(player.playAssistantCard(new AssistantCard(7)).isEmpty());
        assertTrue(player.playAssistantCard(new AssistantCard(6)).isEmpty());
        assertTrue(player.playAssistantCard(new AssistantCard(5)).isEmpty());
        assertTrue(player.playAssistantCard(new AssistantCard(4)).isEmpty());
        assertTrue(player.playAssistantCard(new AssistantCard(3)).isEmpty());
        assertTrue(player.playAssistantCard(new AssistantCard(2)).isEmpty());
        assertTrue(player.playAssistantCard(new AssistantCard(1)).isEmpty());

        player.giveAssistantCard(new AssistantCard(1));
        assertTrue(player.canChangeAssistantCard());
        assertEquals(Optional.of(new AssistantCard(1)), player.playAssistantCard(new AssistantCard(1)));
        assertFalse(player.canChangeAssistantCard());
    }
}
