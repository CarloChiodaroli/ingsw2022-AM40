package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameModelException;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.phase.PianificationFase;
import it.polimi.ingsw.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.model.phase.action.states.Influence;
import it.polimi.ingsw.model.phase.action.states.StudentMovement;
import it.polimi.ingsw.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.model.player.AssistantCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.phase.action.ActionFase;
import it.polimi.ingsw.model.table.MotherNature;
import it.polimi.ingsw.model.table.Island;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ActionFaseTest {

    public static void assertThrowsIllegalStateException(org.junit.jupiter.api.function.Executable executable) {
        assertThrows(IllegalStateException.class, executable);
    }

    @Test
    public void startPhaseTest(){
        Game game = new Game();
        ActionFase actionFase = new ActionFase(game);
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        Player player1 = game.getPianificationFase().getActualPlayer();

        actionFase.startPhase(player1);
        assertEquals(player1, game.getPlayers().get(0));
        assertTrue(actionFase.isActivated());
    }

    @Test
    @Disabled("Need to fix")
    public void activateCardTest(){
        Game game = new Game();
        ActionFase actionFase;
        PianificationFase pianificationFase;
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.switchExpertVariant();
        game.gameStarter();
        //game.getPianificationFase().activate();
        actionFase = game.getActionFase();
        pianificationFase = game.getPianificationFase();

        List<CharacterCard> actualCharacterCards = actionFase.getCharacterCards();

        actualCharacterCards.add(CharacterCardFabric.createCard(Characters.SORCERER, actionFase));

        StudentMovement studentMovement = new StudentMovement(game.getActionFase());
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        actionFase.startPhase(game.getPlayers().get(0));

        TeacherColor color = game.getPlayers().get(0).getEntrance().getStudent().get();
        game.getPlayers().get(0).getEntrance().addStudent(TeacherColor.PINK);
        studentMovement.handle(TeacherColor.PINK, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(TeacherColor.PINK)));
        assertTrue(game.getPlayers().get(0).getRoomTable(TeacherColor.PINK).hasTeacher());

        TeacherColor color1 = game.getPlayers().get(1).getEntrance().getStudent().get();
        game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.RED);
        studentMovement.handle(TeacherColor.RED, Optional.of(game.getPlayers().get(1).getEntrance()),
                Optional.of(game.getPlayers().get(1).getRoomTable(TeacherColor.RED)));
        assertTrue(game.getPlayers().get(1).getRoomTable(TeacherColor.RED).hasTeacher());

        TeacherColor color2 = game.getPlayers().get(0).getEntrance().getStudent().get();
        game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.PINK);
        studentMovement.handle(TeacherColor.PINK, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getTable().getIslandList().get(3)));

        for(int i = 0; i < 2; i++){
            TeacherColor color3 = game.getPlayers().get(1).getEntrance().getStudent().get();
            game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.RED);
            studentMovement.handle(TeacherColor.RED, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(3)));
        }

        game.getPlayers().get(0).giveMoney(2);
        actionFase.activateCard(Characters.SORCERER, game.getPlayers().get(0), TeacherColor.RED);
        actionFase.setCalculatedInfluence(true);
        actionFase.request(game.getPlayers().get(0), game.getTable().getIslandList().get(3).getId());
        assertEquals(game.getPlayers().get(0).getTowerColor(), game.getTable().getIslandList().get(3).getTowerColor());


    }


    @Test
    public void requestColorTest(){
        Game game = new Game();
        ActionFase actionFase = new ActionFase(game);
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

        assertThrowsIllegalStateException(() -> actionFase.request(color, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(color))));

        actionFase.startPhase(game.getPlayers().get(0));
        for(int i = 0; i < 3; i++){
            TeacherColor color1 = game.getPlayers().get(0).getEntrance().getStudent().get();
            game.getPlayers().get(0).getEntrance().addStudent(color1);
            actionFase.request(color1, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getPlayers().get(0).getRoomTable(color1)));
        }
        TeacherColor color2 = game.getPlayers().get(0).getEntrance().getStudent().get();
        game.getPlayers().get(0).getEntrance().addStudent(color2);
        assertThrowsIllegalStateException(() -> actionFase.request(color2, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(color2))));

    }

    @Test
    public void requestMotherNatureTest(){
        Game game = new Game();
        ActionFase actionFase = new ActionFase(game);
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        actionFase.startPhase(game.getPlayers().get(0));

        Island inpos = MotherNature.getMotherNature().getPosition().get();
        int firstposition = game.getTable().getIslandList().indexOf(inpos);
        actionFase.request(game.getPlayers().get(0), 1);
        Island finpos = MotherNature.getMotherNature().getPosition().get();
        int secondposition = game.getTable().getIslandList().indexOf(finpos);
        assertEquals(firstposition + 1, secondposition);

        assertThrowsIllegalStateException(() -> actionFase.request(game.getPlayers().get(0), 1));


    }

    @Test
    public void requestMergeIslandTest(){
        Game game = new Game();
        ActionFase actionFase = new ActionFase(game);
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        actionFase.startPhase(game.getPlayers().get(0));

        game.getTable().getIslandList().get(3).setInfluence(game.getPlayers().get(0).getTowerColor());
        game.getTable().getIslandList().get(4).setInfluence(game.getPlayers().get(0).getTowerColor());

        assertThrowsIllegalStateException(() -> actionFase.request());

        actionFase.setCalculatedInfluence(true);
        actionFase.request();
        assertEquals(11, game.getTable().getIslandList().size());

        assertThrowsIllegalStateException(() -> actionFase.request());

    }

    @Test
    public void requestSpecialColorTest(){
        Game game = new Game();
        ActionFase actionFase = new ActionFase(game);
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        actionFase.startPhase(game.getPlayers().get(0));

        assertThrowsIllegalStateException(() -> actionFase.request(game.getPlayers().get(0), TeacherColor.PINK,
                TeacherColor.RED));
    }

    @Test
    public void requestSpecialColorTest2(){
        Game game = new Game();
        ActionFase actionFase;
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.switchExpertVariant();
        game.gameStarter();
        actionFase = game.getActionFase();
        game.getPianificationFase().activate();
        AssistantCard card = new AssistantCard(4);
        AssistantCard card1 = game.getPianificationFase().play(card, game.getPlayers().get(0));
        AssistantCard card2 = new AssistantCard(6);
        AssistantCard card3 = game.getPianificationFase().play(card2, game.getPlayers().get(1));
        actionFase.startPhase(game.getPlayers().get(0));

        List<CharacterCard> actualCharacterCards = actionFase.getCharacterCards();

        actualCharacterCards.add(CharacterCardFabric.createCard(Characters.JESTER, actionFase));

        assertTrue(game.isExpertVariant());
        assertThrowsIllegalStateException(() -> actionFase.request(game.getPlayers().get(0), TeacherColor.PINK,
                TeacherColor.RED));
        actionFase.activateCard(Characters.JESTER, game.getPlayers().get(0));
        TeacherColor color = game.getPlayers().get(0).getEntrance().getStudent().get();
        game.getPlayers().get(0).getEntrance().addStudent(color);
        actionFase.request(game.getPlayers().get(0), color, TeacherColor.PINK);
    }

}
