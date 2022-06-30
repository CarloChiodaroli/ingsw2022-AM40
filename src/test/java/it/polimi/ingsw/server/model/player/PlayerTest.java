package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.player.AssistantCard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.school.SchoolDashboard;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class test the {@link Player} methods
 */
public class PlayerTest {

    /**
     * Test play assistant card
     */
    @Test
    public void assistantCardTest() {
        Game game = new Game();
        game.addPlayer("Aldo");
        game.addPlayer("Giovanni");
        game.gameStarter();

        Player aldo = game.getPlayers().get(0);
        Player giovanni = game.getPlayers().get(1);

        assertEquals(10, aldo.getPersonalDeck().size());
        assertTrue(aldo.getPersonalDeck().contains(new AssistantCard(4)));
        aldo.playAssistantCard(new AssistantCard(4));
        assertEquals(9, aldo.getPersonalDeck().size());
        assertFalse(aldo.getPersonalDeck().contains(new AssistantCard(4)));

        assertEquals(10, giovanni.getPersonalDeck().size());
        assertTrue(giovanni.getPersonalDeck().contains(new AssistantCard(4)));
        assertThrows(IllegalArgumentException.class, () -> giovanni.playAssistantCard(new AssistantCard((4))));
        assertEquals(10, giovanni.getPersonalDeck().size());
        assertTrue(giovanni.getPersonalDeck().contains(new AssistantCard(4)));
        giovanni.playAssistantCard(new AssistantCard((5)));
        assertFalse(giovanni.getPersonalDeck().contains(new AssistantCard(5)));
        giovanni.giveAssistantCard(new AssistantCard(5));
        assertTrue(giovanni.getPersonalDeck().contains(new AssistantCard(5)));

    }

    /**
     * Test ids in player table
     */
    @Test
    public void miscellaneousTest() {
        Game game = new Game();
        game.addPlayer("Aldo");
        game.addPlayer("Giovanni");
        game.gameStarter();

        Player aldo = game.getPlayers().get(0);
        Player giovanni = game.getPlayers().get(1);

        assertEquals("Aldo", aldo.getName());

        assertThrows(InvalidParameterException.class, () -> aldo.getTower(10));

        Optional<StudentsManager> expected = Optional.of(aldo.getEntrance());
        assertEquals(expected, aldo.getStudentsManagerById("Entrance"));

        expected = Optional.of(aldo.getRoomTable());
        assertEquals(expected, aldo.getStudentsManagerById("Room"));

        expected = game.getStudentsManagerById("I_1");
        assertEquals(expected, aldo.getStudentsManagerById("I_1"));

    }

}
