import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.GameModelException;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.Characters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

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

}
