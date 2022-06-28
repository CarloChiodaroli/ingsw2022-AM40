package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.server.model.phase.action.states.StudentMovement;
import it.polimi.ingsw.server.model.phase.action.states.cards.InfluenceCard;
import it.polimi.ingsw.server.model.phase.action.states.cards.StudentMovementCard;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link StudentMovementCard} methods, character cards who modify student movement
 */
public class StudentMovementCardTest {

    private static Map<String, Integer> createBaseCharacterization() {
        Map<String, Integer> result = new HashMap<>();

        result.put("Price", 0); // Card Price
        result.put("Memory", 0); // Card memory size
        result.put("Usages", 0); // How many times the card is used
        result.put("Bidirectional", 0); // Has a bidirectional behaviour
        result.put("TeacherBehaviour", 0); // Changes behaviour of the movement of teachers
        result.put("EffectAllPlayers", 0); // Has effect to all players in this turn
        result.put("NoEntrySetter", 0); // Defines if it needs to set no entry tile
        // On how many things the card works on
        result.put("Island", 0);
        result.put("Player", 0);
        result.put("Room", 0);
        result.put("Tower", 0);
        result.put("Student", 0);
        result.put("Entrance", 0);

        return result;
    }

    /**
     * Test cards price
     */
    @Test
    public void priceTest() {
        Map<String, Integer> custom = createBaseCharacterization();

        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();

        custom.replace("Price", 1);

        StudentMovement phase = new StudentMovement(game.getActionPhase());

        StudentMovementCard card = new StudentMovementCard(Characters.HOST, game.getActionPhase(), custom);

        assertEquals(1, card.getPrice());

        card.activator(phase, game.getPlayers().get(0));

        assertEquals(2, card.getPrice());
    }

    /**
     * Test room attributes
     */
    @RepeatedTest(30)
    public void roomAttributeTest() {
        Map<String, Integer> custom = createBaseCharacterization();

        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();

        custom.replace("Price", 1);
        custom.replace("Usages", 1);
        custom.replace("Room", 1);

        StudentMovement phase = new StudentMovement(game.getActionPhase());

        StudentMovementCard card = new StudentMovementCard(Characters.HOST, game.getActionPhase(), custom);

        assertEquals(7, game.getPlayers().get(0).getEntrance().howManyTotStudents());

        int max = 0;
        TeacherColor maxColor1 = TeacherColor.PINK;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                maxColor1 = color;
            }
        }

        max = 0;
        TeacherColor maxColor2 = TeacherColor.PINK;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max && color != maxColor1) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                maxColor2 = color;
            }
        }

        int beforeMaxColor1 = game.getPlayers().get(0).getEntrance().howManyStudents(maxColor1);
        int beforeMaxColor2 = game.getPlayers().get(0).getEntrance().howManyStudents(maxColor2);

        phase.handle(maxColor1, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable()));

        assertNotEquals(maxColor1, maxColor2);

        assertEqualsWithDelta(beforeMaxColor1 - 1, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor1), "1");
        assertEqualsWithDelta(beforeMaxColor2, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor2), "2");

        card.handle(game.getPlayers().get(0), maxColor2, maxColor1, "Room");

        assertEqualsWithDelta(beforeMaxColor1 - 1, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor1), "1");
        assertEqualsWithDelta(beforeMaxColor2, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor2), "2");

        card.activator(phase, game.getPlayers().get(0));

        assertEqualsWithDelta(0, game.getPlayers().get(0).getMoney(), "5");

        card.handle(game.getPlayers().get(0), maxColor2, maxColor1, "Room");

        assertEqualsWithDelta(beforeMaxColor1, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor1), "3");
        assertEqualsWithDelta(beforeMaxColor2 - 1, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor2), "4");

    }

    /**
     * Test card memory
     */
    @RepeatedTest(30)
    public void memory6AttributeTest() {
        Map<String, Integer> custom = createBaseCharacterization();

        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();

        custom.replace("Price", 1);
        custom.replace("Usages", 1);
        custom.replace("Memory", 6);

        StudentMovement phase = new StudentMovement(game.getActionPhase());

        StudentMovementCard card = new StudentMovementCard(Characters.HOST, game.getActionPhase(), custom);

        assertEquals(7, game.getPlayers().get(0).getEntrance().howManyTotStudents());

        int max = 0;
        TeacherColor maxColor1 = TeacherColor.PINK;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                maxColor1 = color;
            }
        }

        max = 0;
        TeacherColor maxColor2 = TeacherColor.PINK;

        for (TeacherColor color : TeacherColor.values()) {
            if (card.getStudents().howManyStudents(color) > max && color != maxColor1) {
                max = card.getStudents().howManyStudents(color);
                maxColor2 = color;
            }
        }

        int playerBeforeMaxColor1 = game.getPlayers().get(0).getEntrance().howManyStudents(maxColor1);
        int playerBeforeMaxColor2 = game.getPlayers().get(0).getEntrance().howManyStudents(maxColor2);

        int containerBeforeMaxColor1 = card.getStudents().howManyStudents(maxColor1);
        int containerBeforeMaxColor2 = card.getStudents().howManyStudents(maxColor2);

        card.activator(phase, game.getPlayers().get(0));

        assertEqualsWithDelta(0, game.getPlayers().get(0).getMoney(), "5");

        card.handle(game.getPlayers().get(0), maxColor1, maxColor2, "Card");

        assertEqualsWithDelta(playerBeforeMaxColor1 - 1, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor1), "3");
        assertEqualsWithDelta(playerBeforeMaxColor2 + 1, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor2), "4");

        assertEqualsWithDelta(containerBeforeMaxColor2 - 1, card.getStudents().howManyStudents(maxColor2), "6");
        assertEqualsWithDelta(containerBeforeMaxColor1 + 1, card.getStudents().howManyStudents(maxColor1), "7");
    }

    public static void assertEqualsWithDelta(int expected, int actual, String where) {
        assertEquals(expected, actual, where + " delta is: " + (expected - actual));
    }

    @RepeatedTest(30)
    public void friarCardTest() {
        Map<String, Integer> custom = createBaseCharacterization();

        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();

        custom.replace("Price", 1);
        custom.replace("Usages", 0);
        custom.replace("Student", 1);

        StudentMovement phase = new StudentMovement(game.getActionPhase());

        StudentMovementCard card = new StudentMovementCard(Characters.HOST, game.getActionPhase(), custom);

        assertEquals(7, game.getPlayers().get(0).getEntrance().howManyTotStudents());

        List<Player> players = game.getPlayers();

        int max = 0;
        TeacherColor maxColor1 = TeacherColor.PINK;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                maxColor1 = color;
            }
        }

        for (Player player : players) {
            for (TeacherColor color : TeacherColor.values()) {
                for (int i = 0; i < player.getEntrance().howManyStudents(color); i++) {
                    phase.handle(color, Optional.of(player.getEntrance()), Optional.of(player.getRoomTable()));
                }
            }
        }

        int playerBeforeMaxColor1 = players.get(0).getRoomTable().howManyTotStudents();
        int playerBeforeMaxColor2 = players.get(1).getRoomTable().howManyTotStudents();

        card.activator(phase, game.getPlayers().get(0), maxColor1);

        assertEqualsWithDelta(0, game.getPlayers().get(0).getMoney(), "1");

        assertTrue(players.get(0).getRoomTable().howManyStudents(maxColor1) <= playerBeforeMaxColor1);
        assertTrue(players.get(1).getRoomTable().howManyStudents(maxColor1) <= playerBeforeMaxColor2);

        if (players.get(0).getRoomTable().howManyStudents(maxColor1) > 0) {
            assertEqualsWithDelta(playerBeforeMaxColor1 - 3, players.get(0).getRoomTable().howManyStudents(maxColor1), "2");
        } else {
            assertEqualsWithDelta(0, players.get(0).getRoomTable().howManyStudents(maxColor1), "3");
        }

        if (players.get(1).getRoomTable().howManyStudents(maxColor1) > 0) {
            assertEqualsWithDelta(playerBeforeMaxColor2 - 3, players.get(1).getRoomTable().howManyStudents(maxColor1), "4");
        } else {
            assertEqualsWithDelta(0, players.get(1).getRoomTable().howManyStudents(maxColor1), "5");
        }
    }

    /**
     * Test Host card
     */
    @RepeatedTest(10)
    public void teacherBehaviourTest() {
        Map<String, Integer> custom = createBaseCharacterization();

        Game game = new Game();
        game.addPlayer("Aldo");
        game.addPlayer("Giovanni");
        game.gameStarter();

        custom.replace("Price", 1);
        custom.replace("Usages", 3);
        custom.replace("TeacherBehaviour", 1);

        StudentMovement phase = new StudentMovement(game.getActionPhase());

        StudentMovementCard card = new StudentMovementCard(Characters.HOST, game.getActionPhase(), custom);

        assertEquals(7, game.getPlayers().get(0).getEntrance().howManyTotStudents());

        List<Player> players = game.getPlayers();

        int max = 0;
        TeacherColor maxColor = TeacherColor.PINK;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                maxColor = color;
            }
        }

        for (Player player : players) {
            for (TeacherColor color : TeacherColor.values()) {
                for (int i = 0; i < player.getEntrance().howManyStudents(color); i++) {
                    phase.handle(color, Optional.of(player.getEntrance()), Optional.of(player.getRoomTable()));
                }
            }
        }

        int oldTableContent1 = players.get(0).howManyStudentsInRoom(maxColor);
        int oldTableContent2 = players.get(1).howManyStudentsInRoom(maxColor);

        if (oldTableContent1 >= oldTableContent2) {
            assertTrue(players.get(0).hasTeacher(maxColor));
            assertFalse(players.get(1).hasTeacher(maxColor));
        } else {
            assertFalse(players.get(0).hasTeacher(maxColor));
            assertTrue(players.get(1).hasTeacher(maxColor));
        }

        if (oldTableContent1 == oldTableContent2) {
            players.get(1).getRoomTable().removeStudent(maxColor);
            oldTableContent2--;
        }
        if (oldTableContent1 < oldTableContent2) {
            card.activator(phase, players.get(0));
            for (int i = 0; i < (oldTableContent2 - oldTableContent1); i++) {
                players.get(0).getEntrance().addStudent(maxColor);
                card.handle(maxColor, Optional.of(players.get(0).getEntrance()), Optional.of(players.get(0).getRoomTable()));
            }

            int tableContent1 = players.get(0).howManyStudentsInRoom(maxColor);
            int tableContent2 = players.get(1).howManyStudentsInRoom(maxColor);

            // Not assert equals... left and right values can be wrong... I can't know now what value to expect
            assertTrue(tableContent2 == tableContent1, "" + tableContent2 + " != " + tableContent1);

            assertFalse(players.get(1).hasTeacher(maxColor));
            assertTrue(players.get(0).hasTeacher(maxColor));
        } else {
            card.activator(phase, players.get(1));
            for (int i = 0; i < (oldTableContent1 - oldTableContent2); i++) {
                players.get(1).getEntrance().addStudent(maxColor);
                card.handle(maxColor, Optional.of(players.get(1).getEntrance()), Optional.of(players.get(1).getRoomTable()));
            }

            int tableContent1 = players.get(0).howManyStudentsInRoom(maxColor);
            int tableContent2 = players.get(1).howManyStudentsInRoom(maxColor);

            // Not assert equals... left and right values can be wrong... I can't know now what value to expect
            assertTrue(tableContent2 == tableContent1, "" + tableContent2 + " != " + tableContent1);

            assertFalse(players.get(0).hasTeacher(maxColor));
            assertTrue(players.get(1).hasTeacher(maxColor));
        }
    }
}
