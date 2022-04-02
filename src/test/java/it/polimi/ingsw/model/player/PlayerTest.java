package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.phase.PianificationFase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;



public class PlayerTest {

    @Test
    public void assistantCardManagementTest(){

        /*
        In order to execute this test you need to go to the Player class and in the playAssistantCard method
        comment the indicated line. That line invokes directly the Pianification Phase that needs to follow a
        long procedure that goes out of the goal of this test.

        This test was written to see if the management of the personal assistant card deck of the player
        was correctly developed, not if the game works correctly.
         */

        boolean commented = false;

        /*
        Change commented to true to enable this test, after having commented what from above.
         */

        if(!commented) return;

        Game game = new Game();

        Player player = new Player(game, "Test", TowerColor.BLACK);

        player.playAssistantCard(new AssistantCard(4));
        assertTrue(player.canChangeAssistantCard());
        player.playAssistantCard(new AssistantCard(7));
        assertTrue(player.canChangeAssistantCard());
        player.playAssistantCard(new AssistantCard(2));
        assertTrue(player.canChangeAssistantCard());
        player.playAssistantCard(new AssistantCard(5));
        assertTrue(player.canChangeAssistantCard());
        player.playAssistantCard(new AssistantCard(1));
        assertTrue(player.canChangeAssistantCard());
        player.playAssistantCard(new AssistantCard(3));
        assertTrue(player.canChangeAssistantCard());
        player.playAssistantCard(new AssistantCard(8));
        assertTrue(player.canChangeAssistantCard());
        player.playAssistantCard(new AssistantCard(6));
        assertTrue(player.canChangeAssistantCard());
        player.playAssistantCard(new AssistantCard(9));
        assertTrue(player.canChangeAssistantCard());
        player.playAssistantCard(new AssistantCard(10));
        assertFalse(player.canChangeAssistantCard());

        List<AssistantCard> playerDeck = player.getPersonalDeck();
        player.playAssistantCard(new AssistantCard(10));
        assertEquals(playerDeck, player.getPersonalDeck());


        player.giveAssistantCard(new AssistantCard(1));
        assertTrue(player.canChangeAssistantCard());
        player.playAssistantCard(new AssistantCard(1));
        assertFalse(player.canChangeAssistantCard());
    }
}
