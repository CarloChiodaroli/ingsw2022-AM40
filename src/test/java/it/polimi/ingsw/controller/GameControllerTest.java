package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameModelException;
import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameControllerTest {

    public static void assertThrowsNoSNoSuchElementException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(NoSuchElementException.class, executable);
    }

    public static void assertThrowsGameModelException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(GameModelException.class, executable);
    }

    @Test
    public void playAssistantCardTest(){
        GameController controller = new GameController();

        String aldoName = "Aldo";
        String giovanniName = "Giovanni";
        String illegalName = "Sergio";
        String testIslandId = "i_1";

        controller.addPlayer(aldoName);

        controller.addPlayer(giovanniName);

        List<String> expected = new ArrayList<>();

        expected.add("i_1");
        expected.add("i_2");
        expected.add("i_3");
        expected.add("i_4");
        expected.add("i_5");
        expected.add("i_6");
        expected.add("i_7");
        expected.add("i_8");
        expected.add("i_9");
        expected.add("i_10");
        expected.add("i_11");
        expected.add("i_12");

        assertThrows(IllegalStateException.class, () -> controller.getAllIslandIds());

        List<String> gotten = controller.startGame();

        assertEquals(expected, gotten);

        Game game = controller.getModel().getGame();

        // Game alteration for test purposes

        Player aldo = game.getPlayers().get(0);
        Player giovanni = game.getPlayers().get(1);
        StudentsManager testIsland = game.getStudentsManagerById(testIslandId).orElseThrow();

        assertEquals(3, controller.playAssistantCard(aldoName, 3));

        assertEquals(5, controller.playAssistantCard(giovanni.getName(), 5));

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

        Map<String, Integer> expectedFromMove = new HashMap<>();
        expectedFromMove.put("Entrance", -1);
        expectedFromMove.put(testIslandId, 1);

        assertEquals(expectedFromMove, controller.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", testIslandId));

        expectedFromMove = new HashMap<>();
        expectedFromMove.put("Entrance", -1);
        expectedFromMove.put("Room", 1);
        assertEquals(expectedFromMove, controller.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", "Room"));

        assertThrows(InvalidParameterException.class, () -> controller.moveStudent(giovanniName, TeacherColor.PINK, "Entrance", "Room"));
        assertThrows(InvalidParameterException.class, () -> controller.moveStudent(illegalName, TeacherColor.PINK, "Entrance", "Room"));
    }
}
