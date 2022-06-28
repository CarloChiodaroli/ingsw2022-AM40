package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.server.enums.ActionPhaseStateType;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.phase.action.states.Finalize;
import it.polimi.ingsw.server.model.phase.action.states.Influence;
import it.polimi.ingsw.server.model.phase.action.states.StudentMovement;
import it.polimi.ingsw.server.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.server.model.phase.action.states.cards.InfluenceCard;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Island;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link InfluenceCard} methods, character cards who modify influence
 */
public class InfluenceCardTest {

    /**
     * Test Sorceress card
     */
    @Test
    public void influenceCardSorceressTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.switchExpertVariant();
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionPhase());
        Characters sorceress = Characters.SORCERESS;
        InfluenceCard sorceressCard = (InfluenceCard) CharacterCardFabric.createCard(sorceress, game.getActionPhase());
        game.getActionPhase().getCharacterCards().putIfAbsent(sorceress, sorceressCard);
        Influence influence = (Influence) game.getActionPhase().getState(ActionPhaseStateType.INFLUENCE);

        Player camilla = game.getPlayers().get(0);
        Player anja = game.getPlayers().get(1);
        Island testIsland = game.getTable().getIslandList().get(4);

        int max = 0;
        TeacherColor firstMaxColor = TeacherColor.PINK;
        for (TeacherColor color : TeacherColor.values()) {
            if (camilla.getEntrance().howManyStudents(color) > max) {
                max = camilla.getEntrance().howManyStudents(color);
                firstMaxColor = color;
            }
        }

        studentMovement.handle(firstMaxColor, Optional.of(camilla.getEntrance()),
                Optional.of(camilla.getRoomTable()));
        assertTrue(camilla.hasTeacher(firstMaxColor));

        for (int i = 0; i < 3; i++) {
            camilla.getEntrance().addStudent(firstMaxColor);
            studentMovement.handle(firstMaxColor, Optional.of(camilla.getEntrance()), Optional.of(testIsland));
        }

        influence.handle(camilla, testIsland);
        assertTrue(testIsland.hasTowers());
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());

        max = 0;
        TeacherColor secondMaxColor = TeacherColor.RED;
        for (TeacherColor color : TeacherColor.values()) {
            if (anja.getEntrance().howManyStudents(color) > max && color != firstMaxColor) {
                max = anja.getEntrance().howManyStudents(color);
                secondMaxColor = color;
            }
        }
        studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                Optional.of(anja.getRoomTable()));
        assertTrue(anja.hasTeacher(secondMaxColor));

        for (int i = 0; i < 1; i++) {
            anja.getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()), Optional.of(testIsland));
        }

        influence.handle(camilla, testIsland);
        assertTrue(testIsland.hasTowers());
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());

        assertThrows(InvalidParameterException.class, () -> sorceressCard.activator(influence, anja));
        anja.giveMoney(3);

        sorceressCard.activator(influence, anja, testIsland);

        testIsland.setNoEntry(true);

        for (int i = 0; i < 6; i++) {
            anja.getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                    Optional.of(testIsland));
        }

        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());
    }

    /**
     * Test Sorcerer card
     */
    @Test
    public void influenceCardSorcererTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionPhase());
        Characters sorcerer = Characters.SORCERER;
        InfluenceCard sorcererCard = new InfluenceCard(sorcerer, game.getActionPhase(),
                CharacterCardFabric.getCharacterization(sorcerer));
        Influence influence = new Influence(game.getActionPhase());

        Player camilla = game.getPlayers().get(0);
        Player anja = game.getPlayers().get(1);
        Island testIsland = game.getTable().getIslandList().get(4);

        TeacherColor firstMaxColor = TeacherColor.PINK;
        int max = 0;

        for (TeacherColor color : TeacherColor.values()) {
            if (camilla.getEntrance().howManyStudents(color) > max) {
                max = camilla.getEntrance().howManyStudents(color);
                firstMaxColor = color;
            }
        }

        studentMovement.handle(firstMaxColor, Optional.of(camilla.getEntrance()),
                Optional.of(camilla.getRoomTable()));
        assertTrue(camilla.hasTeacher(firstMaxColor));

        for (int i = 0; i < 3; i++) {
            camilla.getEntrance().addStudent(firstMaxColor);
            studentMovement.handle(firstMaxColor, Optional.of(camilla.getEntrance()),
                    Optional.of(testIsland));
        }

        max = 0;
        TeacherColor secondMaxColor = TeacherColor.RED;
        for (TeacherColor color : TeacherColor.values()) {
            if (anja.getEntrance().howManyStudents(color) > max && color != firstMaxColor) {
                max = anja.getEntrance().howManyStudents(color);
                secondMaxColor = color;
            }
        }
        studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                Optional.of(anja.getRoomTable()));
        assertTrue(anja.hasTeacher(secondMaxColor));

        influence.handle(camilla, testIsland);
        assertTrue(testIsland.hasTowers());
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());
        for (int i = 0; i < 6; i++) {
            anja.getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                    Optional.of(testIsland));
        }
        influence.handle(anja, testIsland);
        assertTrue(testIsland.hasTowers());
        assertEquals(anja.getTowerColor(), testIsland.getTowerColor().get());

        assertThrows(InvalidParameterException.class, () -> sorcererCard.activator(influence, camilla));
        camilla.giveMoney(3);

        sorcererCard.activator(influence, camilla, secondMaxColor);
        sorcererCard.handle(camilla, testIsland);
        assertTrue(testIsland.hasTowers());
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());

    }

    /**
     * Test Centaur card
     */
    @Test
    public void influenceCardCentaurTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionPhase());
        Characters centaur = Characters.CENTAUR;
        InfluenceCard centaurCard = new InfluenceCard(centaur, game.getActionPhase(),
                CharacterCardFabric.getCharacterization(centaur));
        Influence influence = new Influence(game.getActionPhase());

        Player camilla = game.getPlayers().get(0);
        Player anja = game.getPlayers().get(1);
        Island testIsland = game.getTable().getIslandList().get(4);

        TeacherColor firstMaxColor = TeacherColor.PINK;
        int max = 0;

        for (TeacherColor color : TeacherColor.values()) {
            if (camilla.getEntrance().howManyStudents(color) > max) {
                max = camilla.getEntrance().howManyStudents(color);
                firstMaxColor = color;
            }
        }

        studentMovement.handle(firstMaxColor, Optional.of(camilla.getEntrance()),
                Optional.of(camilla.getRoomTable()));
        assertTrue(camilla.hasTeacher(firstMaxColor));
        if (testIsland.howManyStudents(firstMaxColor) == 0) {
            for (int i = 0; i < 3; i++) {
                camilla.getEntrance().addStudent(firstMaxColor);
                studentMovement.handle(firstMaxColor, Optional.of(camilla.getEntrance()),
                        Optional.of(testIsland));
            }
        } else {
            for (int i = 0; i < 2; i++) {
                camilla.getEntrance().addStudent(firstMaxColor);
                studentMovement.handle(firstMaxColor, Optional.of(camilla.getEntrance()),
                        Optional.of(testIsland));
            }
        }


        max = 0;
        TeacherColor secondMaxColor = TeacherColor.RED;
        for (TeacherColor color : TeacherColor.values()) {
            if (anja.getEntrance().howManyStudents(color) > max && color != firstMaxColor) {
                max = anja.getEntrance().howManyStudents(color);
                secondMaxColor = color;
            }
        }
        studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                Optional.of(anja.getRoomTable()));
        assertTrue(anja.hasTeacher(secondMaxColor));

        influence.handle(camilla, testIsland);
        assertTrue(testIsland.hasTowers());
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());

        for (int i = 0; i < 5; i++) {
            anja.getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                    Optional.of(anja.getRoomTable()));
        }

        if (testIsland.howManyStudents(secondMaxColor) == 0) {
            for (int i = 0; i < 4; i++) {
                anja.getEntrance().addStudent(secondMaxColor);
                studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                        Optional.of(testIsland));
            }
        } else {
            for (int i = 0; i < 3; i++) {
                anja.getEntrance().addStudent(secondMaxColor);
                studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                        Optional.of(testIsland));
            }
        }
        influence.handle(anja, testIsland);
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());

        assertThrows(InvalidParameterException.class, () -> centaurCard.activator(influence, anja));
        anja.giveMoney(3);

        centaurCard.activator(influence, anja);
        centaurCard.handle(anja, testIsland);
        assertEquals(anja.getTowerColor(), testIsland.getTowerColor().get());
    }

    /**
     * Test Knight card
     */
    @Test
    public void influenceCardKnightTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionPhase());
        Characters knight = Characters.KNIGHT;
        InfluenceCard knightCard = new InfluenceCard(knight, game.getActionPhase(),
                CharacterCardFabric.getCharacterization(knight));
        Influence influence = new Influence(game.getActionPhase());

        Player camilla = game.getPlayers().get(0);
        Player anja = game.getPlayers().get(1);
        Island testIsland = game.getTable().getIslandList().get(4);

        int max = 0;
        TeacherColor firstMaxColor = TeacherColor.PINK;
        for (TeacherColor color : TeacherColor.values()) {
            if (camilla.getEntrance().howManyStudents(color) > max) {
                max = camilla.getEntrance().howManyStudents(color);
                firstMaxColor = color;
            }
        }

        studentMovement.handle(firstMaxColor, Optional.of(camilla.getEntrance()),
                Optional.of(camilla.getRoomTable()));
        assertTrue(camilla.hasTeacher(firstMaxColor));

        int qnt = 3;

        if (testIsland.howManyStudents(firstMaxColor) > 0) qnt -= testIsland.howManyStudents(firstMaxColor);

        for (int i = 0; i < qnt; i++) {
            camilla.getEntrance().addStudent(firstMaxColor);
            studentMovement.handle(firstMaxColor, Optional.of(camilla.getEntrance()), Optional.of(testIsland));
        }

        influence.handle(camilla, testIsland);
        assertTrue(testIsland.hasTowers());
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());

        max = 0;
        TeacherColor secondMaxColor = TeacherColor.RED;
        for (TeacherColor color : TeacherColor.values()) {
            if (anja.getEntrance().howManyStudents(color) > max && color != firstMaxColor) {
                max = anja.getEntrance().howManyStudents(color);
                secondMaxColor = color;
            }
        }
        studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                Optional.of(anja.getRoomTable()));
        assertTrue(anja.hasTeacher(secondMaxColor));

        qnt = 3;

        if (testIsland.howManyStudents(secondMaxColor) > 0) qnt -= testIsland.howManyStudents(secondMaxColor);

        for (int i = 0; i < qnt; i++) {
            anja.getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()), Optional.of(testIsland));
        }

        influence.handle(camilla, testIsland);
        assertTrue(testIsland.hasTowers());
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());

        assertThrows(InvalidParameterException.class, () -> knightCard.activator(influence, anja));
        anja.giveMoney(3);

        knightCard.activator(influence, anja);
        knightCard.handle(anja, testIsland);
        assertEquals(anja.getTowerColor(), testIsland.getTowerColor().get());
    }

    /**
     * Test Crier card
     */
    @Test
    public void influenceCardCrierTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionPhase());
        Characters crier = Characters.CRIER;
        InfluenceCard crierCard = new InfluenceCard(crier, game.getActionPhase(),
                CharacterCardFabric.getCharacterization(crier));
        Influence influence = new Influence(game.getActionPhase());

        Player camilla = game.getPlayers().get(0);
        Player anja = game.getPlayers().get(1);
        Island testIsland = game.getTable().getIslandList().get(4);


    }
}