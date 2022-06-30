package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.controller.inner.InboundController;
import it.polimi.ingsw.server.controller.inner.OutboundController;
import it.polimi.ingsw.server.controller.inner.TurnController;
import it.polimi.ingsw.server.controller.outer.GameManager;
import it.polimi.ingsw.server.controller.outer.PlayMessagesReader;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.server.model.GameModelException;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.server.enums.ActionPhaseStateType;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.enums.CharactersLookup;
import it.polimi.ingsw.server.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Cloud;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.model.table.MotherNature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private InboundController inbound;
    private OutboundController outbound;
    private TurnController turn;
    private PlayMessagesReader reader;
    private GameModel model;
    private Game game;
    private Player aldo;
    private Player giovanni;
    private final static String aldoName = "Aldo";
    private final static String giovanniName = "Giovanni";
    private final String testIslandId = "i_1";

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

    /**
     * Test init a game in normal game mode
     */
    @Nested
    @DisplayName("Non Expert Variant Tests")
    class NonExpertTests{

        @BeforeEach
        public void initTest() {

            GameManager gameManager = new GameManager();
            reader = new PlayMessagesReader(aldoName, gameManager);
            reader.setNumOfPlayers(2);

            reader.addPlayer(giovanniName);

            reader.startGame();

            inbound = reader.getInbound();
            outbound = reader.getOutbound();
            model = inbound.getModel();
            game = model.getGame();
            turn = reader.getTurnController();

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
        }

        /**
         * Test play assistant card
         */
        @Test
        public void playAssistantCardTest() {

            String illegalName = "Sergio";
            String testIslandId = "i_1";

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

            assertDoesNotThrow(() -> outbound.getAllIslandIds());

            List<String> gotten = outbound.getAllIslandIds();

            assertEquals(expected, gotten);

            // Game alteration for test purposes

            aldo = game.getPlayers().get(0);
            giovanni = game.getPlayers().get(1);
            StudentsManager testIsland = game.getStudentsManagerById(testIslandId).orElseThrow();

            assertDoesNotThrow(() -> inbound.playAssistantCard(aldoName, 3));
            turn.nextTurn();
            assertThrowsIllegalArgumentException(() -> inbound.playAssistantCard(illegalName, 4));
            assertDoesNotThrow(() -> inbound.playAssistantCard(giovanni.getName(), 5));
            turn.nextTurn();

            aldo.getEntrance().addStudent(TeacherColor.BLUE);
            aldo.getEntrance().addStudent(TeacherColor.BLUE);
            aldo.getEntrance().addStudent(TeacherColor.BLUE);

            giovanni.getEntrance().addStudent(TeacherColor.PINK);

            assertDoesNotThrow(() -> inbound.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", "Room"));
        }

        /**
         * Test move student from the entrance
         */
        @Test
        public void moveStudentTest() {

            reader.playAssistantCard(aldoName, 1);
            reader.playAssistantCard(giovanniName, 5);

            aldo.getEntrance().addStudent(TeacherColor.BLUE);
            aldo.getEntrance().addStudent(TeacherColor.BLUE);
            aldo.getEntrance().addStudent(TeacherColor.BLUE);

            Island testIsland = game.getTable().getIslandById(testIslandId).get();

            assertDoesNotThrow(() -> inbound.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", testIslandId));
            assertEquals(2, aldo.getEntrance().howManyStudents(TeacherColor.BLUE));
            assertEquals(1, testIsland.howManyStudents(TeacherColor.BLUE));


            assertDoesNotThrow(() -> inbound.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", "Room"));
            assertEquals(1, aldo.getEntrance().howManyStudents(TeacherColor.BLUE));
            assertEquals(1, aldo.getRoomTable().howManyStudents(TeacherColor.BLUE));

            assertThrowsIllegalArgumentException(() -> inbound.moveStudent("Armando", TeacherColor.BLUE, "Entrance", "Room"));
            assertThrowsIllegalArgumentException(() -> inbound.moveStudent(giovanniName, TeacherColor.BLUE, "Entrance", "Room"));
        }

        /**
         * Test move mother nature
         */
        @Test
        public void moveMotherNatureTest(){

            reader.playAssistantCard(aldoName, 1);
            reader.playAssistantCard(giovanniName, 5);

            game.getActionPhase().setStudentMoves(3);
            Island island = game.getTable().getIslandById(testIslandId).get();
            MotherNature.getMotherNature().setPosition(island);

            String arrivePlace = inbound.moveMotherNature(aldoName, 1);
            assertEquals("i_2", arrivePlace);
            assertEquals(arrivePlace, outbound.actualMotherNaturePosition());
            assertThrowsGameModelException(() -> inbound.moveMotherNature(aldoName, 1));
            assertThrowsIllegalArgumentException(() -> inbound.moveMotherNature(giovanniName, 1));
        }

        /**
         * Test calculate influence
         */
        @Test
        public void calcInfluenceTest(){

            assertThrowsIllegalStateException(() -> inbound.calcInfluence(aldoName));

            reader.playAssistantCard(aldoName, 1);
            reader.playAssistantCard(giovanniName, 5);

            assertThrowsGameModelException(() -> inbound.calcInfluence(aldoName));

            aldo.getEntrance().addStudent(TeacherColor.BLUE);

            giovanni.getEntrance().addStudent(TeacherColor.PINK);

            aldo.getRoomTable().addStudent(TeacherColor.BLUE);
            aldo.addTeacher(TeacherColor.BLUE);

            Island testIsland = game.getTable().getIslandById(testIslandId).get();

            MotherNature.getMotherNature().setPosition(testIsland);
            testIsland.addStudent(TeacherColor.BLUE);

            game.getActionPhase().setCalculatedInfluence(false);
            game.getActionPhase().setMovedMotherNature(true);
            game.getActionPhase().setActualState(ActionPhaseStateType.INFLUENCE.getOrderPlace());

            inbound.calcInfluence(aldoName);

            assertEquals(aldo.getTowerColor(), testIsland.getTowerColor().orElseThrow());
            assertThrowsIllegalArgumentException(() -> inbound.calcInfluence(giovanniName));
        }

        /**
         * Test choose cloud
         */
        @Test
        public void chooseCloudTest(){

            assertThrowsIllegalStateException(() -> inbound.chooseCloud(aldoName, "c_1"));

            reader.playAssistantCard(aldoName, 1);
            reader.playAssistantCard(giovanniName, 5);

            Cloud testCloud = game.getTable().getCloudById("c_1").orElseThrow();

            game.getActionPhase().setCalculatedInfluence(true);
            game.getActionPhase().setChosenCloud(false);
            game.getActionPhase().setActualState(ActionPhaseStateType.CLOUD.getOrderPlace());

            Map<TeacherColor, Integer> cloudContent = new HashMap<>();

            for(TeacherColor color: TeacherColor.values()){
                cloudContent.put(color, testCloud.howManyStudents(color));
            }

            inbound.chooseCloud(aldoName, "c_1");

            assertEquals(cloudContent, outbound.getStudentInPlace(aldoName, "Entrance"));
        }

        @Test
        public void playerFlowTest(){

            assertThrowsIllegalStateException(() -> inbound.calcInfluence(aldoName));

            reader.playAssistantCard(aldoName, 1);
            reader.playAssistantCard(giovanniName, 5);

            game.getActionPhase().setCalculatedInfluence(true);
            game.getActionPhase().setChosenCloud(false);

            assertDoesNotThrow(() -> inbound.chooseCloud(aldoName, "c_1"));
            turn.nextTurn();
        }
    }

    @Nested
    @DisplayName("Expert Variant Tests")
    class ExpertTests{

        /**
         * Test init a game in expert mode
         */
        @BeforeEach
        public void initTest() {

            GameManager gameManager = new GameManager();
            reader = new PlayMessagesReader(aldoName, gameManager);
            reader.setNumOfPlayers(2);

            reader.addPlayer(giovanniName);
            reader.switchExpertVariant();

            reader.startGame();

            inbound = reader.getInbound();
            outbound = reader.getOutbound();
            model = inbound.getModel();
            game = model.getGame();
            turn = reader.getTurnController();

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

            reader.playAssistantCard(aldoName, 1);
            reader.playAssistantCard(giovanniName, 5);
        }

        /**
         * Test move student with character cards
         */
        @Test
        public void expertStudentMovementTest(){



            game.getActionPhase().getCharacterCards().putIfAbsent(Characters.MINSTREL, CharacterCardFabric.createCard(Characters.MINSTREL, game.getActionPhase()));

            aldo.giveMoney(3);
            inbound.playCharacterCard(aldoName, Characters.MINSTREL);

            assertEquals(Characters.MINSTREL, game.getActionPhase().getActualCharacter().get());

            aldo.getEntrance().addStudent(TeacherColor.BLUE);

            aldo.getRoomTable().addStudent(TeacherColor.PINK);

            inbound.moveStudent(aldoName, TeacherColor.BLUE, TeacherColor.PINK, "Room");

            assertEquals(1, aldo.getEntrance().howManyStudents(TeacherColor.PINK));
            assertEquals(0, aldo.getEntrance().howManyStudents(TeacherColor.BLUE));
            assertEquals(1, aldo.getRoomTable().howManyStudents(TeacherColor.BLUE));
            assertEquals(0, aldo.getRoomTable().howManyStudents(TeacherColor.PINK));
        }

        /**
         * Test play character card
         */
        @Test
        public void playCharacterCardTest(){
            game.getActionPhase().getCharacterCards().putIfAbsent(Characters.FRIAR,CharacterCardFabric.createCard(Characters.FRIAR, game.getActionPhase()));

            inbound.getModel().getGame().getActionPhase().setActualState(CharactersLookup.getType(Characters.FRIAR).getOrderPlace());
            aldo.giveMoney(3);
            inbound.playCharacterCard(aldoName, Characters.FRIAR);

            assertEquals(Characters.FRIAR, game.getActionPhase().getActualCharacter().get());
        }

        /**
         * Test play character card who required a color
         */
        @Test
        public void playCharacterCardTeacherColorTest(){
            game.getActionPhase().getCharacterCards().putIfAbsent(Characters.SORCERER, CharacterCardFabric.createCard(Characters.SORCERER, game.getActionPhase()));

            inbound.getModel().getGame().getActionPhase().setActualState(CharactersLookup.getType(Characters.SORCERER).getOrderPlace());
            aldo.giveMoney(3);
            inbound.playCharacterCard(aldoName, Characters.SORCERER, TeacherColor.PINK);

            assertEquals(Characters.SORCERER, game.getActionPhase().getActualCharacter().get());
        }

        /**
         * Test play character card who required an island
         */
        @Test
        public void playCharacterCardIslandTest(){
            game.getActionPhase().getCharacterCards().putIfAbsent(Characters.CRIER,CharacterCardFabric.createCard(Characters.CRIER, game.getActionPhase()));

            inbound.getModel().getGame().getActionPhase().setActualState(CharactersLookup.getType(Characters.CRIER).getOrderPlace());
            aldo.giveMoney(3);
            inbound.playCharacterCard(aldoName, Characters.CRIER, testIslandId);

            assertEquals(Characters.CRIER, game.getActionPhase().getActualCharacter().get());
        }
    }

    @Nested
    @DisplayName("Before round Tests")
    class BeforeRoundTests{
        /**
         * Test can't add 4 players
         */
        @Test
        public void playerAdditionTest() {
            String carloName = "Carlo";
            String enricoName = "Enrico";
            String removedName = "Giovanni";
            String illegalName = "Baldassarre";

            GameManager gameManager = new GameManager();
            reader = new PlayMessagesReader(carloName, gameManager);

            reader.setNumOfPlayers(3);

            assertThrows(InvalidParameterException.class, () -> reader.addPlayer(carloName));
            reader.addPlayer(enricoName);
            reader.addPlayer(removedName);
            reader.deletePlayer(removedName);
            reader.startGame();

            inbound = reader.getInbound();
            outbound = reader.getOutbound();
            model = inbound.getModel();
            game = model.getGame();
            turn = reader.getTurnController();

            aldo = game.getPlayers().get(0);
            giovanni = game.getPlayers().get(1);


            assertThrowsNoSNoSuchElementException(() -> game.getPlayers().stream().filter(player -> player.getName().equals(removedName)).findAny().orElseThrow());
            assertThrowsNoSNoSuchElementException(() -> game.getPlayers().stream().filter(player -> player.getName().equals(illegalName)).findAny().orElseThrow());

            assertEquals(carloName, game.getPlayers().stream().filter(player -> player.getName().equals(carloName)).findAny().get().getName());
            assertEquals(enricoName, game.getPlayers().stream().filter(player -> player.getName().equals(enricoName)).findAny().get().getName());
        }

        /**
         * Test set expert game mode
         */
        @Test
        public void setExpertVariantTest() {

            GameManager gameManager = new GameManager();
            reader = new PlayMessagesReader(aldoName, gameManager);
            reader.setNumOfPlayers(2);

            reader.addPlayer(giovanniName);
            reader.switchExpertVariant();

            reader.startGame();

            inbound = reader.getInbound();
            model = inbound.getModel();
            game = model.getGame();

            assertTrue(game.isExpertVariant());


            gameManager = new GameManager();
            reader = new PlayMessagesReader(aldoName, gameManager);
            reader.setNumOfPlayers(2);

            reader.addPlayer(giovanniName);

            reader.startGame();

            inbound = reader.getInbound();
            model = inbound.getModel();
            game = model.getGame();

            assertFalse(game.isExpertVariant());
        }

        /**
         * Test game in 3 players mode
         */
        @Test
        public void threePlayerGameTest() {

            String aldoName = "Aldo";
            String giovanniName = "Giovanni";
            String giacomoName = "Giacomo";

            GameManager gameManager = new GameManager();
            reader = new PlayMessagesReader(aldoName, gameManager);
            reader.setNumOfPlayers(3);

            reader.addPlayer(giovanniName);
            reader.addPlayer(giacomoName);

            reader.startGame();

            inbound = reader.getInbound();
            model = inbound.getModel();
            game = model.getGame();

            assertTrue(game.isThreePlayerGame());


            gameManager = new GameManager();
            reader = new PlayMessagesReader(aldoName, gameManager);
            reader.setNumOfPlayers(3);

            reader.addPlayer(giovanniName);

            reader.startGame();

            inbound = reader.getInbound();
            model = inbound.getModel();
            game = model.getGame();

            assertFalse(game.isThreePlayerGame());
        }
    }

}