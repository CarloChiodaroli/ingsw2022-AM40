package it.polimi.ingsw.model;

import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelException;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class GameModelTest {

    public static void assertThrowsNoSNoSuchElementException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(NoSuchElementException.class, executable);
    }

    public static void assertThrowsGameModelException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(GameModelException.class, executable);
    }

    @Test
    @DisplayName("'Player not found' exception")
    public void noSuchElementTest() {
        GameModel model = new GameModel();

        model.addPlayer("Aldo");

        model.addPlayer("Giovanni");

        model.startGame();

        assertThrowsNoSNoSuchElementException(() -> model.playAssistantCard("Giacomo", 2));
        assertThrowsNoSNoSuchElementException(() -> model.moveStudent("Giacomo", TeacherColor.PINK, TeacherColor.BLUE));
        assertThrowsNoSNoSuchElementException(() -> model.moveStudent("Giacomo", TeacherColor.PINK, "Pole", "Pole"));
        assertThrowsNoSNoSuchElementException(() -> model.moveMotherNature("Giacomo", 7));
        assertThrowsNoSNoSuchElementException(() -> model.calcInfluence("Giacomo"));
        assertThrowsNoSNoSuchElementException(() -> model.chooseCloud("Giacomo", "C_1"));
        assertThrowsNoSNoSuchElementException(() -> model.playCharacterCard("Giacomo", Characters.SORCERESS));
        assertThrowsNoSNoSuchElementException(() -> model.getStudentsInEntrance("Giacomo"));
        assertThrowsNoSNoSuchElementException(() -> model.getStudentsInRoom("Giacomo"));
    }

    @DisplayName("Player Moves Exception Test")
    @Nested
    class playerInteractionTest {

        @Test
        @DisplayName("Play Assistant Card")
        public void playAssistantCardTest() {
            GameModel model = new GameModel();

            model.addPlayer("Aldo");

            model.addPlayer("Giovanni");

            assertThrowsNoSNoSuchElementException(() -> model.playAssistantCard("Aldo", 5));

            model.startGame();

            assertDoesNotThrow(() -> model.playAssistantCard("Aldo", 5));
            assertThrowsGameModelException(() -> model.playAssistantCard("Aldo", 8));
            assertThrowsGameModelException(() -> model.playAssistantCard("Giovanni", 5));
            assertDoesNotThrow(() -> model.playAssistantCard("Giovanni", 4));
        }

    }

    @Test
    public void expertVariantTest() {
        GameModel model = new GameModel();

        model.addPlayer("Aldo");

        model.addPlayer("Giovanni");

        assertFalse(model.isExpertVariant());
        model.switchExpertVariant();
        assertTrue(model.isExpertVariant());
        model.switchExpertVariant();
        assertFalse((model.isExpertVariant()));
    }

    @Test
    public void threePlayerTest() {
        GameModel model = new GameModel();

        model.addPlayer("Aldo");

        model.addPlayer("Giovanni");

        assertFalse(model.isThreePlayerGame());

        model.addPlayer("Giacomo");

        assertTrue(model.isThreePlayerGame());
    }

    @Test
    public void playAssistantCardTest(){
        GameModel model = new GameModel();

        String aldoName = "Aldo";
        String giovanniName = "Giovanni";
        String testIslandId = "i_1";

        model.addPlayer(aldoName);
        assertThrowsGameModelException(model::startGame);

        model.addPlayer(giovanniName);

        model.startGame();

        Game game = model.getGame();

        // Game alteration for test purposes

        Player aldo = game.getPlayers().get(0);
        Player giovanni = game.getPlayers().get(1);
        StudentsManager testIsland = game.getStudentsManagerById(testIslandId).orElseThrow();

        model.playAssistantCard(aldo.getName(), 3);

        model.getPlayersInOrder();

        assertThrowsGameModelException(() -> model.playAssistantCard(giovanni.getName(), 3));

        model.playAssistantCard(giovanni.getName(), 5);

        List<String> expectedOrder = new ArrayList<>();

        expectedOrder.add(aldoName);
        expectedOrder.add(giovanniName);
        assertEquals(expectedOrder, model.getPlayersInOrder());

        // emptied aldo and giovanni from all students in entrance and students in island
        for(TeacherColor color: TeacherColor.values()){
            for(int i = aldo.getEntrance().howManyStudents(color); i > 0; i--){
                aldo.getEntrance().removeStudent(color);
            }
            for(int j = giovanni.getEntrance().howManyStudents(color); j > 0; j--){
                giovanni.getEntrance().removeStudent(color);
            }
            for(int k = testIsland.howManyStudents(color); k > 0; k--){
                testIsland.removeStudent(color);
            }
        }

        aldo.getEntrance().addStudent(TeacherColor.BLUE);
        aldo.getEntrance().addStudent(TeacherColor.BLUE);
        aldo.getEntrance().addStudent(TeacherColor.BLUE);

        giovanni.getEntrance().addStudent(TeacherColor.PINK);

        assertEquals(3, aldo.getEntrance().howManyStudents(TeacherColor.BLUE));
        assertEquals(1, giovanni.getEntrance().howManyStudents(TeacherColor.PINK));
        assertEquals(0, testIsland.howManyTotStudents());

        model.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", testIslandId);
        model.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", "Room");

        assertEquals(1, aldo.getEntrance().howManyStudents(TeacherColor.BLUE));
        assertEquals(1, testIsland.howManyStudents(TeacherColor.BLUE));
        assertEquals(1, aldo.getRoomTable(TeacherColor.BLUE).howManyStudents(TeacherColor.BLUE));

        assertThrowsGameModelException(() -> model.moveStudent(giovanniName, TeacherColor.PINK, "Entrance", "Room"));
    }

}
