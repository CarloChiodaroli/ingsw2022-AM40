package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.phase.action.states.Influence;
import it.polimi.ingsw.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.model.phase.action.states.cards.InfluenceCard;
import it.polimi.ingsw.model.phase.action.states.StudentMovement;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InfluenceCardTest {

    @Test
    public void influenceCardSorceressTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());
        Characters sorceress = Characters.SORCERESS;
        InfluenceCard sorceressCard = new InfluenceCard(sorceress, game.getActionFase(),
                CharacterCardFabric.getCharacterization(sorceress));
        Influence influence = new Influence(game.getActionFase());

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
                Optional.of(camilla.getRoomTable(firstMaxColor)));
        assertTrue(camilla.getRoomTable(firstMaxColor).hasTeacher());

        for(int i = 0; i < 3; i++) {
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
                Optional.of(anja.getRoomTable(secondMaxColor)));
        assertTrue(anja.getRoomTable(secondMaxColor).hasTeacher());

        for(int i = 0; i < 1; i++) {
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

        for(int i = 0; i < 6; i++) {
            anja.getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                    Optional.of(testIsland));
        }

        sorceressCard.handle(anja, testIsland);
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());
    }

    @Test
    public void influenceCardSorcererTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());
        Characters sorcerer = Characters.SORCERER;
        InfluenceCard sorcererCard = new InfluenceCard(sorcerer, game.getActionFase(),
                CharacterCardFabric.getCharacterization(sorcerer));
        Influence influence = new Influence(game.getActionFase());

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
                Optional.of(camilla.getRoomTable(firstMaxColor)));
        assertTrue(camilla.getRoomTable(firstMaxColor).hasTeacher());

        for(int i = 0; i < 3; i++) {
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
                Optional.of(anja.getRoomTable(secondMaxColor)));
        assertTrue(anja.getRoomTable(secondMaxColor).hasTeacher());

        influence.handle(camilla, testIsland);
        assertTrue(testIsland.hasTowers());
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());
        for(int i = 0; i < 6; i++) {
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

    @Test
    public void influenceCardCentaurTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());
        Characters centaur = Characters.CENTAUR;
        InfluenceCard centaurCard = new InfluenceCard(centaur, game.getActionFase(),
                CharacterCardFabric.getCharacterization(centaur));
        Influence influence = new Influence(game.getActionFase());

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
                Optional.of(camilla.getRoomTable(firstMaxColor)));
        assertTrue(camilla.getRoomTable(firstMaxColor).hasTeacher());
        if(testIsland.howManyStudents(firstMaxColor) == 0) {
            for(int i = 0; i < 3; i++) {
                camilla.getEntrance().addStudent(firstMaxColor);
                studentMovement.handle(firstMaxColor, Optional.of(camilla.getEntrance()),
                        Optional.of(testIsland));
            }
        }
        else{
            for(int i = 0; i < 2; i++) {
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
                Optional.of(anja.getRoomTable(secondMaxColor)));
        assertTrue(anja.getRoomTable(secondMaxColor).hasTeacher());

        influence.handle(camilla, testIsland);
        assertTrue(testIsland.hasTowers());
        assertEquals(camilla.getTowerColor(), testIsland.getTowerColor().get());

        for(int i = 0; i < 5; i++) {
            anja.getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                    Optional.of(anja.getRoomTable(secondMaxColor)));
        }

        if(testIsland.howManyStudents(secondMaxColor) == 0) {
            for (int i = 0; i < 4; i++) {
                anja.getEntrance().addStudent(secondMaxColor);
                studentMovement.handle(secondMaxColor, Optional.of(anja.getEntrance()),
                        Optional.of(testIsland));
            }
        }
        else{
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

        centaurCard.activator(influence, anja, camilla.getTowerColor());
        centaurCard.handle(anja, testIsland);
        assertEquals(anja.getTowerColor(), testIsland.getTowerColor().get());
    }

    @Test
    public void influenceCardKnightTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());
        Characters knight = Characters.KNIGHT;
        InfluenceCard knightCard = new InfluenceCard(knight, game.getActionFase(),
                CharacterCardFabric.getCharacterization(knight));
        Influence influence = new Influence(game.getActionFase());

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
                Optional.of(camilla.getRoomTable(firstMaxColor)));
        assertTrue(camilla.getRoomTable(firstMaxColor).hasTeacher());

        int qnt = 3;

        if(testIsland.howManyStudents(firstMaxColor) > 0) qnt -= testIsland.howManyStudents(firstMaxColor);

        for(int i = 0; i < qnt; i++) {
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
                Optional.of(anja.getRoomTable(secondMaxColor)));
        assertTrue(anja.getRoomTable(secondMaxColor).hasTeacher());

        qnt = 3;

        if(testIsland.howManyStudents(secondMaxColor) > 0) qnt -= testIsland.howManyStudents(secondMaxColor);

        for(int i = 0; i < qnt; i++) {
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

    @Test
    public void influenceCardCrierTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());
        Characters crier = Characters.CRIER;
        InfluenceCard crierCard = new InfluenceCard(crier, game.getActionFase(),
                CharacterCardFabric.getCharacterization(crier));
        Influence influence = new Influence(game.getActionFase());

        Player camilla = game.getPlayers().get(0);
        Player anja = game.getPlayers().get(1);
        Island testIsland = game.getTable().getIslandList().get(4);


    }
}