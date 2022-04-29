package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.ActionPhaseStateType;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.phase.action.ActionPhase;
import it.polimi.ingsw.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.model.player.AssistantCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.MotherNature;
import it.polimi.ingsw.model.table.Island;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ActionPhaseTest {

    public static void assertThrowsIllegalStateException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(IllegalStateException.class, executable);
    }

    public static void assertThrowsRunTimeException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(RuntimeException.class, executable);
    }

    public static void assertThrowsNoSuchElementException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(NoSuchElementException.class, executable);
    }

    @Test
    public void startPhaseTest(){
        Game game = new Game();
        ActionPhase actionPhase = new ActionPhase(game);
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        Player player1 = game.getPianificationFase().getActualPlayer();

        actionPhase.startPhase(player1);
        assertEquals(player1, game.getPlayers().get(0));
        assertTrue(actionPhase.isActivated());
    }

    @Test
    public void activateCardTest(){
        Game game = new Game();
        ActionPhase actionPhase;
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.switchExpertVariant();
        game.gameStarter();
        actionPhase = game.getActionFase();

        List<CharacterCard> actualCharacterCards = actionPhase.getCharacterCards();

        actualCharacterCards.add(CharacterCardFabric.createCard(Characters.CENTAUR, actionPhase));

        Player camilla = game.getPlayers().get(0);
        Player anja = game.getPlayers().get(1);

        AssistantCard card = new AssistantCard(4);
        camilla.playAssistantCard(card);

        AssistantCard card2 = new AssistantCard(6);
        anja.playAssistantCard(card2);

        Island testIsland = game.getTable().getIslandList().get(3);

        for(TeacherColor color: TeacherColor.values()){
            for(int i = anja.getEntrance().howManyStudents(color); i > 0; i--){
                anja.getEntrance().removeStudent(color);
            }
            for(int j = camilla.getEntrance().howManyStudents(color); j > 0; j--){
                camilla.getEntrance().removeStudent(color);
            }
            for(int k = testIsland.howManyStudents(color); k > 0; k--){
                testIsland.removeStudent(color);
            }
        }

        camilla.getRoomTable(TeacherColor.BLUE).addStudent(TeacherColor.BLUE);
        camilla.getRoomTable(TeacherColor.BLUE).setTeacherPresence(true);

        anja.getRoomTable(TeacherColor.PINK).addStudent(TeacherColor.PINK);
        anja.getRoomTable(TeacherColor.PINK).setTeacherPresence(true);

        testIsland.addStudent(TeacherColor.BLUE);

        testIsland.addStudent(TeacherColor.PINK);
        testIsland.addStudent(TeacherColor.PINK);

        MotherNature.getMotherNature().setPosition(testIsland);
        actionPhase.setMovedMotherNature(false);
        assertThrowsIllegalStateException(() -> actionPhase.request(camilla, "MotherNature"));

        actionPhase.setMovedMotherNature(true);
        game.getActionFase().setActualState(ActionPhaseStateType.INFLUENCE.getOrderPlace());
        //camilla.calcInfluence();
        actionPhase.request(camilla, "MotherNature");

        MotherNature.getMotherNature().resetPosition();
        assertThrowsRunTimeException(() -> actionPhase.request(camilla, "MotherNature"));
        MotherNature.getMotherNature().setPosition(testIsland);

        actionPhase.setCalculatedInfluence(true);
        assertThrowsIllegalStateException(() -> actionPhase.request(camilla, "MotherNature"));
        assertEquals(anja.getTowerColor(), testIsland.getTowerColor().get());
        actionPhase.setCalculatedInfluence(false);
        actionPhase.setMergedIslands(false);
        actionPhase.setActualState(ActionPhaseStateType.INFLUENCE.getOrderPlace());

        testIsland.addStudent(TeacherColor.BLUE);
        testIsland.addStudent(TeacherColor.BLUE);

        camilla.giveMoney(2);
        actionPhase.activateCard(Characters.CENTAUR, camilla);

        camilla.calcInfluence();
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());
    }


    @Test
    public void activateCardTest2(){
        Game game = new Game();
        ActionPhase actionPhase;
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.switchExpertVariant();
        game.gameStarter();
        actionPhase = game.getActionFase();

        List<CharacterCard> actualCharacterCards = actionPhase.getCharacterCards();

        actualCharacterCards.add(CharacterCardFabric.createCard(Characters.SORCERER, actionPhase));

        Player camilla = game.getPlayers().get(0);
        Player anja = game.getPlayers().get(1);

        AssistantCard card = new AssistantCard(4);
        camilla.playAssistantCard(card);

        AssistantCard card2 = new AssistantCard(6);
        anja.playAssistantCard(card2);

        game.getTable().mergeIsland(game.getTable().getIslandList().get(3), game.getTable().getIslandList().get(4));

        Island testIsland = game.getTable().getIslandList().get(3);

        for(TeacherColor color: TeacherColor.values()){
            for(int i = anja.getEntrance().howManyStudents(color); i > 0; i--){
                anja.getEntrance().removeStudent(color);
            }
            for(int j = camilla.getEntrance().howManyStudents(color); j > 0; j--){
                camilla.getEntrance().removeStudent(color);
            }
            for(int k = testIsland.howManyStudents(color); k > 0; k--){
                testIsland.removeStudent(color);
            }
        }

        camilla.getRoomTable(TeacherColor.BLUE).addStudent(TeacherColor.BLUE);
        camilla.getRoomTable(TeacherColor.BLUE).setTeacherPresence(true);

        anja.getRoomTable(TeacherColor.PINK).addStudent(TeacherColor.PINK);
        anja.getRoomTable(TeacherColor.PINK).setTeacherPresence(true);

        testIsland.addStudent(TeacherColor.BLUE);

        testIsland.addStudent(TeacherColor.PINK);
        testIsland.addStudent(TeacherColor.PINK);

        MotherNature.getMotherNature().setPosition(testIsland);

        actionPhase.setMovedMotherNature(true);
        game.getActionFase().setActualState(ActionPhaseStateType.INFLUENCE.getOrderPlace());

        camilla.calcInfluence();
        assertEquals(anja.getTowerColor(), testIsland.getTowerColor().get());

        testIsland.addStudent(TeacherColor.BLUE);
        testIsland.addStudent(TeacherColor.BLUE);

        actionPhase.setMovedMotherNature(true);
        actionPhase.setCalculatedInfluence(false);
        actionPhase.setMergedIslands(false);
        game.getActionFase().setActualState(ActionPhaseStateType.INFLUENCE.getOrderPlace());

        camilla.calcInfluence();
        assertEquals(anja.getTowerColor(), testIsland.getTowerColor().get());

        actionPhase.setMovedMotherNature(true);
        actionPhase.setCalculatedInfluence(false);
        actionPhase.setMergedIslands(false);
        game.getActionFase().setActualState(ActionPhaseStateType.INFLUENCE.getOrderPlace());

        camilla.giveMoney(2);
        actionPhase.activateCard(Characters.SORCERER, game.getPlayers().get(0), TeacherColor.PINK);

        assertThrowsIllegalStateException(() -> actionPhase.request(camilla, "c_1"));
        camilla.calcInfluence();
        actionPhase.setCalculatedInfluence(true);
        game.getActionFase().setActualState(ActionPhaseStateType.CLOUD.getOrderPlace());
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());

        assertThrowsNoSuchElementException(() -> actionPhase.request(camilla, "casual"));
        assertDoesNotThrow(() -> actionPhase.request(camilla, game.getTable().getCloudList().get(1).getId()));
        assertThrowsIllegalStateException(() -> actionPhase.request(anja, game.getTable().getCloudList().get(0).getId()));
        assertThrowsIllegalStateException(() -> actionPhase.request(camilla, "c_1"));

        assertEquals(3, camilla.getEntrance().howManyTotStudents());



    }

    @Test
    public void activateCardTest3(){
        Game game = new Game();
        ActionPhase actionPhase;
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.switchExpertVariant();
        game.gameStarter();
        actionPhase = game.getActionFase();

        List<CharacterCard> actualCharacterCards = actionPhase.getCharacterCards();

        actualCharacterCards.add(CharacterCardFabric.createCard(Characters.CRIER, actionPhase));

        Player camilla = game.getPlayers().get(0);
        Player anja = game.getPlayers().get(1);

        AssistantCard card = new AssistantCard(4);
        camilla.playAssistantCard(card);

        AssistantCard card2 = new AssistantCard(6);
        anja.playAssistantCard(card2);

        Island testIsland = game.getTable().getIslandList().get(3);
        Island specialTestIsland = game.getTable().getIslandList().get(5);

        for(TeacherColor color: TeacherColor.values()){
            for(int i = anja.getEntrance().howManyStudents(color); i > 0; i--){
                anja.getEntrance().removeStudent(color);
            }
            for(int j = camilla.getEntrance().howManyStudents(color); j > 0; j--){
                camilla.getEntrance().removeStudent(color);
            }
            for(int k = testIsland.howManyStudents(color); k > 0; k--){
                testIsland.removeStudent(color);
            }
            for(int q = specialTestIsland.howManyStudents(color); q > 0; q--){
                specialTestIsland.removeStudent(color);
            }
        }

        camilla.getRoomTable(TeacherColor.BLUE).addStudent(TeacherColor.BLUE);
        camilla.getRoomTable(TeacherColor.BLUE).setTeacherPresence(true);

        anja.getRoomTable(TeacherColor.PINK).addStudent(TeacherColor.PINK);
        anja.getRoomTable(TeacherColor.PINK).setTeacherPresence(true);

        testIsland.addStudent(TeacherColor.BLUE);
        specialTestIsland.addStudent(TeacherColor.BLUE);

        testIsland.addStudent(TeacherColor.PINK);
        testIsland.addStudent(TeacherColor.PINK);

        camilla.giveMoney(2);
        game.getActionFase().setActualState(ActionPhaseStateType.INFLUENCE.getOrderPlace());
        camilla.playCharacterCard(Characters.CRIER, specialTestIsland);
        //actionPhase.activateCard(Characters.CRIER, camilla, specialTestIsland);

        MotherNature.getMotherNature().setPosition(testIsland);

        actionPhase.setMovedMotherNature(true);
        camilla.calcInfluence();
        assertEquals(anja.getTowerColor(), testIsland.getTowerColor().get());
        assertEquals(camilla.getTowerColor(), specialTestIsland.getTowerColor().get());

    }


    @Test
    public void requestTest(){
        Game game = new Game();
        ActionPhase actionPhase;
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.switchExpertVariant();
        game.gameStarter();
        actionPhase = game.getActionFase();

        Player camilla = game.getPlayers().get(0);
        Player anja = game.getPlayers().get(1);

        AssistantCard card = new AssistantCard(4);
        camilla.playAssistantCard(card);

        AssistantCard card2 = new AssistantCard(6);
        anja.playAssistantCard(card2);

    }

    @Test
    public void requestColorTest(){
        Game game = new Game();
        ActionPhase actionPhase = new ActionPhase(game);
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        game.switchExpertVariant();

        TeacherColor color = game.getPlayers().get(0).getEntrance().getStudent().get();
        game.getPlayers().get(0).getEntrance().addStudent(color);

        assertThrowsIllegalStateException(() -> actionPhase.request(color, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(color))));

        actionPhase.startPhase(game.getPlayers().get(0));
        for(int i = 0; i < 3; i++){
            TeacherColor color1 = game.getPlayers().get(0).getEntrance().getStudent().get();
            game.getPlayers().get(0).getEntrance().addStudent(color1);
            actionPhase.request(color1, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getPlayers().get(0).getRoomTable(color1)));
        }
        TeacherColor color2 = game.getPlayers().get(0).getEntrance().getStudent().get();
        game.getPlayers().get(0).getEntrance().addStudent(color2);
        assertThrowsIllegalStateException(() -> actionPhase.request(color2, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(color2))));

    }

    @Test
    public void requestMotherNatureTest(){
        Game game = new Game();
        ActionPhase actionPhase = new ActionPhase(game);
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        actionPhase.startPhase(game.getPlayers().get(0));

        Island inpos = MotherNature.getMotherNature().getPosition().get();
        int firstposition = game.getTable().getIslandList().indexOf(inpos);
        actionPhase.request(game.getPlayers().get(0), 1);
        Island finpos = MotherNature.getMotherNature().getPosition().get();
        int secondposition = game.getTable().getIslandList().indexOf(finpos);
        int position = firstposition + 1;
        if(position == 12) position = 0;
        assertEquals(position, secondposition);

        assertThrowsIllegalStateException(() -> actionPhase.request(game.getPlayers().get(0), 1));


    }

    @Test
    public void requestMergeIslandTest(){
        Game game = new Game();
        ActionPhase actionPhase = new ActionPhase(game);
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        actionPhase.startPhase(game.getPlayers().get(0));

        game.getTable().getIslandList().get(3).setInfluence(game.getPlayers().get(0).getTowerColor());
        game.getTable().getIslandList().get(4).setInfluence(game.getPlayers().get(0).getTowerColor());

        assertThrowsIllegalStateException(() -> actionPhase.request());

        actionPhase.setCalculatedInfluence(true);
        actionPhase.setActualState(ActionPhaseStateType.MERGE.getOrderPlace());
        actionPhase.request();
        assertEquals(11, game.getTable().getIslandList().size());

        assertThrowsIllegalStateException(() -> actionPhase.request());

    }

    @Test
    public void requestSpecialColorTest(){
        Game game = new Game();
        ActionPhase actionPhase = new ActionPhase(game);
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        actionPhase.startPhase(game.getPlayers().get(0));

        assertThrowsIllegalStateException(() -> actionPhase.request(game.getPlayers().get(0), TeacherColor.PINK,
                TeacherColor.RED));
    }

    @Test
    public void requestSpecialColorTest2(){
        Game game = new Game();
        ActionPhase actionPhase;
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.switchExpertVariant();
        game.gameStarter();
        actionPhase = game.getActionFase();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        actionPhase.startPhase(game.getPlayers().get(0));

        List<CharacterCard> actualCharacterCards = actionPhase.getCharacterCards();

        actualCharacterCards.add(CharacterCardFabric.createCard(Characters.JESTER, actionPhase));

        assertTrue(game.isExpertVariant());
        assertThrowsIllegalStateException(() -> actionPhase.request(game.getPlayers().get(0), TeacherColor.PINK,
                TeacherColor.RED));
        assertTrue(actionPhase.canBeActivated(Characters.JESTER));
        actionPhase.activateCard(Characters.JESTER, game.getPlayers().get(0));
        TeacherColor color = game.getPlayers().get(0).getEntrance().getStudent().get();
        game.getPlayers().get(0).getEntrance().addStudent(color);
        actionPhase.request(game.getPlayers().get(0), color, TeacherColor.PINK);
    }

}
