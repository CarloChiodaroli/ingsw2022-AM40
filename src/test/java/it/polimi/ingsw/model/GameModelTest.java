package it.polimi.ingsw.model;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.GameModelException;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.enums.Characters;
import it.polimi.ingsw.server.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.model.table.MotherNature;
import org.junit.jupiter.api.*;

import java.security.InvalidParameterException;
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

    public static void assertInvalidParameterException(org.junit.jupiter.api.function.Executable executable){
        assertThrows(InvalidParameterException.class, executable);
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

    @Test
    public void moveMotherNatureCalcInfluenceChooseCloudTest(){
        GameModel model = new GameModel();

        String aldoName = "Aldo";
        String giovanniName = "Giovanni";

        model.addPlayer(aldoName);
        model.addPlayer(giovanniName);
        model.startGame();

        Game game = model.getGame();
        Optional<Island> initialpos = MotherNature.getMotherNature().getPosition();
        Island inpos = initialpos.get();
        int firstposition = game.getTable().getIslandList().indexOf(inpos);
        int finposition = firstposition + 1;
        if(finposition == 12) finposition = 0;

        Player aldo = game.getPlayers().get(0);
        Player giovanni = game.getPlayers().get(1);
        Island island = game.getTable().getIslandList().get(finposition);
        StudentsManager testIsland = game.getStudentsManagerById(island.getId()).orElseThrow();
        String testIslandId = island.getId();

        model.playAssistantCard(aldo.getName(), 3);
        model.playAssistantCard(giovanni.getName(), 5);

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

        for(int i = 0; i < 3; i++) {
            giovanni.getEntrance().addStudent(TeacherColor.PINK);
        }

        model.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", testIslandId);
        model.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", "Room");

        aldo.enable();
        assertInvalidParameterException(() -> model.moveMotherNature(aldoName, 3));
        model.moveMotherNature(aldoName, 1);
        model.calcInfluence(aldoName);
        assertEquals(testIslandId, model.getMotherNaturePosition());
        assertEquals(aldo.getTowerColor(), island.getTowerColor().get());
        model.chooseCloud(aldoName, game.getTable().getCloudList().get(0).getId());
        aldo.disable();

        MotherNature.getMotherNature().setPosition(inpos);

        giovanni.enable();
        for(int i = 0; i < 2; i++) {
            model.moveStudent(giovanniName, TeacherColor.PINK, "Entrance", testIslandId);
        }
        model.moveStudent(giovanniName, TeacherColor.PINK, "Entrance", "Room");

        assertInvalidParameterException(() -> model.moveMotherNature(giovanniName, 4));
        model.moveMotherNature(giovanniName, 1);
        model.calcInfluence(giovanniName);
        assertEquals(island, MotherNature.getMotherNature().getPosition().get());
        assertEquals(aldo.getTowerColor(), island.getTowerColor().get());
        model.chooseCloud(giovanniName, game.getTable().getCloudList().get(1).getId());
        giovanni.disable();

        assertEquals(3, aldo.getEntrance().howManyTotStudents());
        assertEquals(3, giovanni.getEntrance().howManyTotStudents());

    }

    @Test
    public void playCharacterCardTest(){
        GameModel model = new GameModel();
        String aldoName = "Aldo";
        String giovanniName = "Giovanni";

        model.addPlayer(aldoName);
        model.addPlayer(giovanniName);
        model.switchExpertVariant();
        model.startGame();

        Game game = model.getGame();
        Optional<Island> initialpos = MotherNature.getMotherNature().getPosition();
        Island inpos = initialpos.get();
        int firstposition = game.getTable().getIslandList().indexOf(inpos);
        int finposition = firstposition + 1;
        if(finposition == 12) finposition = 0;

        Player aldo = game.getPlayers().get(0);
        Player giovanni = game.getPlayers().get(1);
        Island island = game.getTable().getIslandList().get(finposition);
        StudentsManager testIsland = game.getStudentsManagerById(island.getId()).orElseThrow();
        String testIslandId = island.getId();

        model.playAssistantCard(aldo.getName(), 3);
        model.playAssistantCard(giovanni.getName(), 5);
        model.getGame().getActionFase().getCharacterCards().putIfAbsent(Characters.CENTAUR,CharacterCardFabric.createCard(Characters.CENTAUR, model.getGame().getActionFase()));

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

        for(int i = 0; i < 3; i++) {
            aldo.getEntrance().addStudent(TeacherColor.BLUE);
        }

        for(int i = 0; i < 3; i++) {
            giovanni.getEntrance().addStudent(TeacherColor.PINK);
        }

        model.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", testIslandId);
        model.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", "Room");
        model.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", "Room");

        aldo.enable();
        assertInvalidParameterException(() -> model.moveMotherNature(aldoName, 3));
        model.moveMotherNature(aldoName, 1);
        model.calcInfluence(aldoName);
        assertEquals(testIslandId, model.getMotherNaturePosition());
        assertEquals(aldo.getTowerColor(), island.getTowerColor().get());
        model.chooseCloud(aldoName, game.getTable().getCloudList().get(0).getId());
        //aldo.disable();

        MotherNature.getMotherNature().setPosition(inpos);

        //giovanni.enable();
        for(int i = 0; i < 2; i++) {
            model.moveStudent(giovanniName, TeacherColor.PINK, "Entrance", testIslandId);
        }
        model.moveStudent(giovanniName, TeacherColor.PINK, "Entrance", "Room");

        assertInvalidParameterException(() -> model.moveMotherNature(giovanniName, 4));
        model.moveMotherNature(giovanniName, 1);
        assertEquals(1, model.coinsOfThePlayer(giovanniName));
        giovanni.giveMoney(-1);
        assertInvalidParameterException(() -> model.playCharacterCard(giovanniName, Characters.CENTAUR));
        giovanni.giveMoney(3);
        model.playCharacterCard(giovanniName, Characters.CENTAUR);

        //model.getGame().getActionFase().setCalculatedInfluence(false);
        //model.getGame().getActionFase().setMovedMotherNature(true);
        //model.getGame().getActionFase().setActualState(ActionPhaseStateType.INFLUENCE.getOrderPlace());

        model.calcInfluence(giovanniName);
        assertEquals(testIslandId, model.getMotherNaturePosition());
        assertEquals(giovanni.getTowerColor(), island.getTowerColor().get());
        model.chooseCloud(giovanniName, game.getTable().getCloudList().get(1).getId());
        giovanni.disable();

        assertEquals(3, aldo.getEntrance().howManyTotStudents());
        assertEquals(3, giovanni.getEntrance().howManyTotStudents());
    }

}
