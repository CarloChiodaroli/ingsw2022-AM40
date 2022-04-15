package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.phase.action.Characters;
import it.polimi.ingsw.model.phase.action.StudentMovement;
import it.polimi.ingsw.model.phase.action.StudentMovementCard;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void priceTest() {
        Map<String, Integer> custom = createBaseCharacterization();

        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();

        custom.replace("Price", 1);

        StudentMovement phase = new StudentMovement(game.getActionFase());

        StudentMovementCard card = new StudentMovementCard(Characters.HOST, game.getActionFase(), custom);

        assertEquals(1, card.getPrice());

        card.activator(phase, game.getPlayers().get(0));

        assertEquals(2, card.getPrice());
    }

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

        StudentMovement phase = new StudentMovement(game.getActionFase());

        StudentMovementCard card = new StudentMovementCard(Characters.HOST, game.getActionFase(), custom);

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
                Optional.of(game.getPlayers().get(0).getRoomTable(maxColor1)));

        assertNotEquals(maxColor1, maxColor2);

        assertEqualsWithDelta(beforeMaxColor1 - 1, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor1), "1");
        assertEqualsWithDelta(beforeMaxColor2, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor2), "2");

        card.handle(game.getPlayers().get(0), maxColor2, maxColor1);

        assertEqualsWithDelta(beforeMaxColor1 - 1, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor1), "1");
        assertEqualsWithDelta(beforeMaxColor2, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor2), "2");

        card.activator(phase, game.getPlayers().get(0));

        assertEqualsWithDelta(0, game.getPlayers().get(0).getMoney(), "5");

        card.handle(game.getPlayers().get(0), maxColor2, maxColor1);

        assertEqualsWithDelta(beforeMaxColor1, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor1), "3");
        assertEqualsWithDelta(beforeMaxColor2 - 1, game.getPlayers().get(0).getEntrance().howManyStudents(maxColor2), "4");

    }

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

        StudentMovement phase = new StudentMovement(game.getActionFase());

        StudentMovementCard card = new StudentMovementCard(Characters.HOST, game.getActionFase(), custom);

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

        card.handle(game.getPlayers().get(0), maxColor1, maxColor2);

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

        StudentMovement phase = new StudentMovement(game.getActionFase());

        StudentMovementCard card = new StudentMovementCard(Characters.HOST, game.getActionFase(), custom);

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
                    phase.handle(color, Optional.of(player.getEntrance()), Optional.of(player.getRoomTable(color)));
                }
            }
        }

        int playerBeforeMaxColor1 = players.get(0).getRoomTable(maxColor1).howManyTotStudents();
        int playerBeforeMaxColor2 = players.get(1).getRoomTable(maxColor1).howManyTotStudents();

        card.activator(phase, game.getPlayers().get(0), maxColor1);

        assertEqualsWithDelta(0, game.getPlayers().get(0).getMoney(), "1");

        assertTrue(players.get(0).getRoomTable(maxColor1).howManyStudents(maxColor1) <= playerBeforeMaxColor1);
        assertTrue(players.get(1).getRoomTable(maxColor1).howManyStudents(maxColor1) <= playerBeforeMaxColor2);

        if (players.get(0).getRoomTable(maxColor1).howManyStudents(maxColor1) > 0) {
            assertEqualsWithDelta(playerBeforeMaxColor1 - 3, players.get(0).getRoomTable(maxColor1).howManyStudents(maxColor1), "2");
        } else {
            assertEqualsWithDelta(0, players.get(0).getRoomTable(maxColor1).howManyStudents(maxColor1), "3");
        }

        if (players.get(1).getRoomTable(maxColor1).howManyStudents(maxColor1) > 0) {
            assertEqualsWithDelta(playerBeforeMaxColor2 - 3, players.get(1).getRoomTable(maxColor1).howManyStudents(maxColor1), "4");
        } else {
            assertEqualsWithDelta(0, players.get(1).getRoomTable(maxColor1).howManyStudents(maxColor1), "5");
        }
    }

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

        StudentMovement phase = new StudentMovement(game.getActionFase());

        StudentMovementCard card = new StudentMovementCard(Characters.HOST, game.getActionFase(), custom);

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
                    phase.handle(color, Optional.of(player.getEntrance()), Optional.of(player.getRoomTable(color)));
                }
            }
        }

        int oldTableContent1 = players.get(0).getRoomTable(maxColor).howManyStudentsColor();
        int oldTableContent2 = players.get(1).getRoomTable(maxColor).howManyStudentsColor();

        if (oldTableContent1 >= oldTableContent2) {
            assertTrue(players.get(0).getRoomTable(maxColor).hasTeacher());
            assertFalse(players.get(1).getRoomTable(maxColor).hasTeacher());
        } else {
            assertFalse(players.get(0).getRoomTable(maxColor).hasTeacher());
            assertTrue(players.get(1).getRoomTable(maxColor).hasTeacher());
        }

        if (oldTableContent1 == oldTableContent2) {
            players.get(1).getRoomTable(maxColor).removeStudent(maxColor);
            oldTableContent2--;
        }
        if (oldTableContent1 < oldTableContent2) {
            card.activator(phase, players.get(0));
            for (int i = 0; i < (oldTableContent2 - oldTableContent1); i++) {
                players.get(0).getEntrance().addStudent(maxColor);
                card.handle(maxColor, Optional.of(players.get(0).getEntrance()), Optional.of(players.get(0).getRoomTable(maxColor)));
            }

            int tableContent1 = players.get(0).getRoomTable(maxColor).howManyStudentsColor();
            int tableContent2 = players.get(1).getRoomTable(maxColor).howManyStudentsColor();

            // Not assert equals... left and right values can be wrong... I can't know now what value to expect
            assertTrue(tableContent2 == tableContent1, "" + tableContent2 + " != " + tableContent1);

            assertFalse(players.get(1).getRoomTable(maxColor).hasTeacher());
            assertTrue(players.get(0).getRoomTable(maxColor).hasTeacher());
        } else {
            card.activator(phase, players.get(1));
            for (int i = 0; i < (oldTableContent1 - oldTableContent2); i++) {
                players.get(1).getEntrance().addStudent(maxColor);
                card.handle(maxColor, Optional.of(players.get(1).getEntrance()), Optional.of(players.get(1).getRoomTable(maxColor)));
            }

            int tableContent1 = players.get(0).getRoomTable(maxColor).howManyStudentsColor();
            int tableContent2 = players.get(1).getRoomTable(maxColor).howManyStudentsColor();

            // Not assert equals... left and right values can be wrong... I can't know now what value to expect
            assertTrue(tableContent2 == tableContent1, "" + tableContent2 + " != " + tableContent1);

            assertFalse(players.get(0).getRoomTable(maxColor).hasTeacher());
            assertTrue(players.get(1).getRoomTable(maxColor).hasTeacher());
        }
    }
}