package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameModelException;
import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Cloud;
import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.model.table.MotherNature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    public static void assertThrowsNoSNoSuchElementException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(NoSuchElementException.class, executable);
    }

    public static void assertThrowsGameModelException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(GameModelException.class, executable);
    }

    public static void assertThrowsIllegalArgumentException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(IllegalArgumentException.class, executable);
    }

    public static void assertThrowsIllegalStateException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(IllegalStateException.class, executable);
    }

    private GameController controller;
    private Game game;
    private Player aldo;
    private Player giovanni;
    private final static String aldoName = "Aldo";
    private final static String giovanniName = "Giovanni";
    private final int giovanniAssistantCardValue = 5;
    private final int aldoAssistantCardValue = 3;
    private final String testIslandId = "i_1";

    @BeforeEach
    public void initTest() {
        controller = new GameController();

        controller.addPlayer(aldoName);
        controller.addPlayer(giovanniName);
        controller.switchExpertVariant();
        controller.startGame();
        game = controller.getModel().getGame();

        aldo = game.getPlayers().get(0);
        giovanni = game.getPlayers().get(1);

        for (TeacherColor color : TeacherColor.values()) {
            for (int i = aldo.getEntrance().howManyStudents(color); i > 0; i--) {
                aldo.getEntrance().removeStudent(color);
            }
            for (int j = giovanni.getEntrance().howManyStudents(color); j > 0; j--) {
                giovanni.getEntrance().removeStudent(color);
            }
            for (Island island : game.getTable().getIslandList()) {
                for (int k = island.howManyStudents(color); k > 0; k--) {
                    island.removeStudent(color);
                }
            }
        }

        controller.playAssistantCard(aldoName, aldoAssistantCardValue);
        controller.playAssistantCard(giovanniName, giovanniAssistantCardValue);
    }

    @Test
    public void playAssistantCardTest() {

        controller = new GameController();

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

        game = controller.getModel().getGame();

        // Game alteration for test purposes

        aldo = game.getPlayers().get(0);
        giovanni = game.getPlayers().get(1);
        StudentsManager testIsland = game.getStudentsManagerById(testIslandId).orElseThrow();

        assertEquals(3, controller.playAssistantCard(aldoName, 3));
        assertEquals(5, controller.playAssistantCard(giovanni.getName(), 5));

        // emptied aldo and giovanni from all students in entrance and students in island
        for (TeacherColor color : TeacherColor.values()) {
            for (int i = aldo.getEntrance().howManyStudents(color); i > 0; i--) {
                aldo.getEntrance().removeStudent(color);
            }
            for (int j = giovanni.getEntrance().howManyStudents(color); j > 0; j--) {
                giovanni.getEntrance().removeStudent(color);
            }
            for (int k = testIsland.howManyStudents(color); k > 0; k--) {
                testIsland.removeStudent(color);
            }
        }

        aldo.getEntrance().addStudent(TeacherColor.BLUE);
        aldo.getEntrance().addStudent(TeacherColor.BLUE);
        aldo.getEntrance().addStudent(TeacherColor.BLUE);

        giovanni.getEntrance().addStudent(TeacherColor.PINK);

        assertDoesNotThrow(() -> controller.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", "Room"));
    }

    @Test
    public void playerAdditionTest() {
        controller = new GameController();

        String carloName = "Carlo";
        String enricoName = "Enrico";
        String removedName = "Giovanni";
        String illegalName = "Baldassarre";

        controller.addPlayer(carloName);
        controller.addPlayer(enricoName);
        controller.addPlayer(removedName);
        controller.deletePlayer(removedName);
        controller.startGame();

        assertThrows(IllegalStateException.class, () -> controller.addPlayer(illegalName));

        game = controller.getModel().getGame();

        assertThrowsNoSNoSuchElementException(() -> game.getPlayers().stream().filter(player -> player.getName().equals(removedName)).findAny().orElseThrow());
        assertThrowsNoSNoSuchElementException(() -> game.getPlayers().stream().filter(player -> player.getName().equals(illegalName)).findAny().orElseThrow());

        assertEquals(carloName, game.getPlayers().stream().filter(player -> player.getName().equals(carloName)).findAny().get().getName());
        assertEquals(enricoName, game.getPlayers().stream().filter(player -> player.getName().equals(enricoName)).findAny().get().getName());
    }

    @Test
    public void setExpertVariantTest() {
        GameController controller = new GameController();

        controller.addPlayer(aldoName);
        controller.addPlayer(giovanniName);
        controller.switchExpertVariant();
        controller.startGame();
        game = controller.getModel().getGame();

        assertTrue(game.isExpertVariant());

        controller = new GameController();
        controller.addPlayer(aldoName);
        controller.addPlayer(giovanniName);
        controller.startGame();
        game = controller.getModel().getGame();

        assertFalse(game.isExpertVariant());
    }

    @Test
    public void threePlayerGameTest() {
        controller = new GameController();

        String aldoName = "Aldo";
        String giovanniName = "Giovanni";
        String giacomoName = "Giacomo";

        controller.addPlayer(aldoName);
        controller.addPlayer(giovanniName);
        controller.startGame();
        game = controller.getModel().getGame();

        assertFalse(game.isThreePlayerGame());

        controller = new GameController();
        controller.addPlayer(aldoName);
        controller.addPlayer(giovanniName);
        controller.addPlayer(giacomoName);
        controller.startGame();
        game = controller.getModel().getGame();

        assertTrue(game.isThreePlayerGame());
    }


    @Test
    public void nonExpertMoveStudentTest() {

        aldo.getEntrance().addStudent(TeacherColor.BLUE);
        aldo.getEntrance().addStudent(TeacherColor.BLUE);
        aldo.getEntrance().addStudent(TeacherColor.BLUE);

        Map<String, Integer> expectedFromMove = new HashMap<>();
        expectedFromMove.put("Entrance", -1);
        expectedFromMove.put(testIslandId, 1);

        assertEquals(expectedFromMove, controller.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", testIslandId));

        expectedFromMove = new HashMap<>();
        expectedFromMove.put("Entrance", -1);
        expectedFromMove.put("Room", 1);
        assertEquals(expectedFromMove, controller.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", "Room"));

        assertThrowsIllegalArgumentException(() -> controller.moveStudent("Armando", TeacherColor.BLUE, "Entrance", "Room"));
        assertThrowsIllegalArgumentException(() -> controller.moveStudent(giovanniName, TeacherColor.BLUE, "Entrance", "Room"));
    }

    @Test
    public void expertStudentMovementTest(){
        game.getActionFase().getCharacterCards().add(CharacterCardFabric.createCard(Characters.MINSTREL, game.getActionFase()));

        aldo.giveMoney(3);
        controller.playCharacterCard(aldoName, Characters.MINSTREL);

        assertEquals(Characters.MINSTREL, game.getActionFase().getActualCharacter().get());

        aldo.getEntrance().addStudent(TeacherColor.BLUE);

        aldo.getRoomTable(TeacherColor.PINK).addStudent(TeacherColor.PINK);

        controller.moveStudent(aldoName, TeacherColor.BLUE, TeacherColor.PINK, "Room");

        assertEquals(1, aldo.getEntrance().howManyStudents(TeacherColor.PINK));
        assertEquals(0, aldo.getEntrance().howManyStudents(TeacherColor.BLUE));
        assertEquals(1, aldo.getRoomTable(TeacherColor.BLUE).howManyStudents(TeacherColor.BLUE));
        assertEquals(0, aldo.getRoomTable(TeacherColor.PINK).howManyStudents(TeacherColor.PINK));
    }

    @Test
    public void MoveMotherNatureTest(){
        game.getActionFase().setPossibleStudentMovements(0);
        Island island = game.getTable().getIslandById(testIslandId).get();
        MotherNature.getMotherNature().setPosition(island);

        String arrivePlace = controller.moveMotherNature(aldoName, 1);
        assertEquals("i_2", arrivePlace);
        assertEquals(arrivePlace, controller.actualMotherNaturePosition());
        assertThrowsGameModelException(() -> controller.moveMotherNature(aldoName, 1));
        assertThrowsIllegalArgumentException(() -> controller.moveMotherNature(giovanniName, 1));

        controller.setGameState(GameState.INITIAL);
        assertThrowsIllegalStateException(() -> controller.moveMotherNature(aldoName, 1));

        controller.setGameState(GameState.PLANNING);
        assertThrowsIllegalStateException(() -> controller.moveMotherNature(aldoName, 2));
    }

    @Test
    public void calcInfluenceTest(){
        aldo.getEntrance().addStudent(TeacherColor.BLUE);

        giovanni.getEntrance().addStudent(TeacherColor.PINK);

        aldo.getRoomTable(TeacherColor.BLUE).addStudent(TeacherColor.BLUE);
        aldo.getRoomTable(TeacherColor.BLUE).setTeacherPresence(true);

        Island testIsland = game.getTable().getIslandById(testIslandId).get();

        MotherNature.getMotherNature().setPosition(testIsland);
        testIsland.addStudent(TeacherColor.BLUE);

        game.getActionFase().setCalculatedInfluence(false);
        game.getActionFase().setMovedMotherNature(true);

        controller.calcInfluence(aldoName);

        assertEquals(aldo.getTowerColor(), testIsland.getTowerColor().orElseThrow());
        assertThrowsIllegalArgumentException(() -> controller.calcInfluence(giovanniName));

        controller.setGameState(GameState.INITIAL);
        assertThrowsIllegalStateException(() -> controller.calcInfluence(aldoName));
        controller.setGameState(GameState.INITIAL);
        assertThrowsIllegalStateException(() -> controller.calcInfluence(aldoName));
    }

    @Test
    public void chooseCloudTest(){
        Cloud testCloud = game.getTable().getCloudById("c_1").orElseThrow();

        game.getActionFase().setCalculatedInfluence(true);
        game.getActionFase().setChosenCloud(false);

        Map<TeacherColor, Integer> cloudContent = new HashMap<>();

        for(TeacherColor color: TeacherColor.values()){
            cloudContent.put(color, testCloud.howManyStudents(color));
        }

        Map<TeacherColor, Integer> aldoEntrance = controller.chooseCloud(aldoName, "c_1");

        assertEquals(cloudContent, aldoEntrance);

        giovanni.getEntrance().addStudent(TeacherColor.BLUE);

        controller.moveStudent(giovanniName, TeacherColor.BLUE, "Entrance", "Room");
    }

    @Test
    public void playerFlowTest(){
        game.getActionFase().setCalculatedInfluence(true);
        game.getActionFase().setChosenCloud(false);

        controller.chooseCloud(aldoName, "c_1");

        game.getActionFase().setCalculatedInfluence(true);
        game.getActionFase().setChosenCloud(false);

        controller.chooseCloud(giovanniName, "c_2");

        assertEquals(GameState.PLANNING, controller.getGameState());
    }

    @Test
    public void playCharacterCardTest(){
        game.getActionFase().getCharacterCards().add(CharacterCardFabric.createCard(Characters.FRIAR, game.getActionFase()));

        aldo.giveMoney(3);
        controller.playCharacterCard(aldoName, Characters.FRIAR);

        assertEquals(Characters.FRIAR, game.getActionFase().getActualCharacter().get());
    }

    @Test
    public void playCharacterCardTeacherColorTest(){
        game.getActionFase().getCharacterCards().add(CharacterCardFabric.createCard(Characters.SORCERER, game.getActionFase()));

        aldo.giveMoney(3);
        controller.playCharacterCard(aldoName, Characters.SORCERER, TeacherColor.PINK);

        assertEquals(Characters.SORCERER, game.getActionFase().getActualCharacter().get());
    }

    @Test
    public void playCharacterCardIslandTest(){
        game.getActionFase().getCharacterCards().add(CharacterCardFabric.createCard(Characters.CRIER, game.getActionFase()));

        aldo.giveMoney(3);
        controller.playCharacterCard(aldoName, Characters.CRIER, testIslandId);

        assertEquals(Characters.CRIER, game.getActionFase().getActualCharacter().get());
    }

}
