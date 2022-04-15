package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.phase.action.Characters;
import it.polimi.ingsw.model.phase.action.Influence;
import it.polimi.ingsw.model.phase.action.InfluenceCard;
import it.polimi.ingsw.model.phase.action.StudentMovement;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
                Characters.getCharacterization(sorceress));
        Influence influence = new Influence(game.getActionFase());

        TeacherColor firstMaxColor = TeacherColor.PINK;
        int max = 0;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                firstMaxColor = color;
            }
        }

        studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(firstMaxColor)));
        assertTrue(game.getPlayers().get(0).getRoomTable(firstMaxColor).hasTeacher());
        for(int i = 0; i < 2; i++) {
            game.getPlayers().get(0).getEntrance().addStudent(firstMaxColor);
            studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getPlayers().get(0).getRoomTable(firstMaxColor)));
        }
        for(int i = 0; i < 3; i++) {
            game.getPlayers().get(0).getEntrance().addStudent(firstMaxColor);
            studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(4)));
        }

        max = 0;
        TeacherColor secondMaxColor = TeacherColor.RED;
        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(1).getEntrance().howManyStudents(color) > max && color != firstMaxColor) {
                max = game.getPlayers().get(1).getEntrance().howManyStudents(color);
                secondMaxColor = color;
            }
        }
        studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                Optional.of(game.getPlayers().get(1).getRoomTable(secondMaxColor)));
        assertTrue(game.getPlayers().get(1).getRoomTable(secondMaxColor).hasTeacher());

        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(4));
        assertTrue(game.getTable().getIslandList().get(4).hasTowers());
        assertEquals(game.getPlayers().get(0).getTowerColor(), game.getTable().getIslandList().get(4).getTowerColor().get());

        sorceressCard.activator(influence, game.getPlayers().get(0), game.getTable().getIslandList().get(4));
        sorceressCard.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(4));

        for(int i = 0; i < 6; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(4)));
        }
        influence.handle(game.getPlayers().get(1), game.getTable().getIslandList().get(4));
        assertTrue(game.getTable().getIslandList().get(4).hasTowers());
        assertEquals(game.getPlayers().get(0).getTowerColor(),
                game.getTable().getIslandList().get(4).getTowerColor().get());
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
                Characters.getCharacterization(sorcerer));
        Influence influence = new Influence(game.getActionFase());

        TeacherColor firstMaxColor = TeacherColor.PINK;
        int max = 0;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                firstMaxColor = color;
            }
        }

        studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(firstMaxColor)));
        assertTrue(game.getPlayers().get(0).getRoomTable(firstMaxColor).hasTeacher());
        for(int i = 0; i < 5; i++) {
            game.getPlayers().get(0).getEntrance().addStudent(firstMaxColor);
            studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getPlayers().get(0).getRoomTable(firstMaxColor)));
        }
        for(int i = 0; i < 3; i++) {
            game.getPlayers().get(0).getEntrance().addStudent(firstMaxColor);
            studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(4)));
        }

        max = 0;
        TeacherColor secondMaxColor = TeacherColor.RED;
        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(1).getEntrance().howManyStudents(color) > max && color != firstMaxColor) {
                max = game.getPlayers().get(1).getEntrance().howManyStudents(color);
                secondMaxColor = color;
            }
        }
        studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                Optional.of(game.getPlayers().get(1).getRoomTable(secondMaxColor)));
        assertTrue(game.getPlayers().get(1).getRoomTable(secondMaxColor).hasTeacher());

        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(4));
        assertTrue(game.getTable().getIslandList().get(4).hasTowers());
        assertEquals(game.getPlayers().get(0).getTowerColor(), game.getTable().getIslandList().get(4).getTowerColor().get());
        for(int i = 0; i < 6; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(4)));
        }
        influence.handle(game.getPlayers().get(1), game.getTable().getIslandList().get(4));
        assertTrue(game.getTable().getIslandList().get(4).hasTowers());
        assertEquals(game.getPlayers().get(1).getTowerColor(), game.getTable().getIslandList().get(4).getTowerColor().get());

        sorcererCard.activator(influence, game.getPlayers().get(0), secondMaxColor);
        sorcererCard.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(4));
        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(4));
        assertTrue(game.getTable().getIslandList().get(4).hasTowers());
        assertEquals(game.getPlayers().get(0).getTowerColor(), game.getTable().getIslandList().get(4).getTowerColor().get());

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
                Characters.getCharacterization(centaur));
        Influence influence = new Influence(game.getActionFase());

        TeacherColor firstMaxColor = TeacherColor.PINK;
        int max = 0;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                firstMaxColor = color;
            }
        }

        studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(firstMaxColor)));
        assertTrue(game.getPlayers().get(0).getRoomTable(firstMaxColor).hasTeacher());
        if(game.getTable().getIslandList().get(4).howManyStudents(firstMaxColor) == 0) {
            for(int i = 0; i < 3; i++) {
                game.getPlayers().get(0).getEntrance().addStudent(firstMaxColor);
                studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                        Optional.of(game.getTable().getIslandList().get(4)));
            }
        }
        else{
            for(int i = 0; i < 2; i++) {
                game.getPlayers().get(0).getEntrance().addStudent(firstMaxColor);
                studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                        Optional.of(game.getTable().getIslandList().get(4)));
            }
        }


        max = 0;
        TeacherColor secondMaxColor = TeacherColor.RED;
        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(1).getEntrance().howManyStudents(color) > max && color != firstMaxColor) {
                max = game.getPlayers().get(1).getEntrance().howManyStudents(color);
                secondMaxColor = color;
            }
        }
        studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                Optional.of(game.getPlayers().get(1).getRoomTable(secondMaxColor)));
        assertTrue(game.getPlayers().get(1).getRoomTable(secondMaxColor).hasTeacher());

        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(4));
        assertTrue(game.getTable().getIslandList().get(4).hasTowers());
        assertEquals(game.getPlayers().get(0).getTowerColor(), game.getTable().getIslandList().get(4).getTowerColor().get());

        for(int i = 0; i < 5; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(secondMaxColor)));
        }

        if(game.getTable().getIslandList().get(4).howManyStudents(secondMaxColor) == 0) {
            for (int i = 0; i < 4; i++) {
                game.getPlayers().get(1).getEntrance().addStudent(secondMaxColor);
                studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                        Optional.of(game.getTable().getIslandList().get(4)));
            }
        }
        else{
            for (int i = 0; i < 3; i++) {
                game.getPlayers().get(1).getEntrance().addStudent(secondMaxColor);
                studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                        Optional.of(game.getTable().getIslandList().get(4)));
            }
        }
        influence.handle(game.getPlayers().get(1), game.getTable().getIslandList().get(4));
        assertEquals(game.getPlayers().get(0).getTowerColor(), game.getTable().getIslandList().get(4).getTowerColor().get());

        centaurCard.activator(influence, game.getPlayers().get(1), game.getPlayers().get(0).getTowerColor());
        centaurCard.handle(game.getPlayers().get(1), game.getTable().getIslandList().get(4));
        influence.handle(game.getPlayers().get(1), game.getTable().getIslandList().get(4));
        assertEquals(game.getPlayers().get(1).getTowerColor(), game.getTable().getIslandList().get(4).getTowerColor().get());
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
                Characters.getCharacterization(knight));
        Influence influence = new Influence(game.getActionFase());

        TeacherColor firstMaxColor = TeacherColor.PINK;
        int max = 0;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                firstMaxColor = color;
            }
        }

        studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(firstMaxColor)));
        assertTrue(game.getPlayers().get(0).getRoomTable(firstMaxColor).hasTeacher());
        if (game.getTable().getIslandList().get(4).howManyStudents(firstMaxColor) == 0) {
            for (int i = 0; i < 3; i++) {
                game.getPlayers().get(0).getEntrance().addStudent(firstMaxColor);
                studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                        Optional.of(game.getTable().getIslandList().get(4)));
            }
        } else {
            for (int i = 0; i < 2; i++) {
                game.getPlayers().get(0).getEntrance().addStudent(firstMaxColor);
                studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                        Optional.of(game.getTable().getIslandList().get(4)));
            }
        }


        max = 0;
        TeacherColor secondMaxColor = TeacherColor.RED;
        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(1).getEntrance().howManyStudents(color) > max && color != firstMaxColor) {
                max = game.getPlayers().get(1).getEntrance().howManyStudents(color);
                secondMaxColor = color;
            }
        }
        studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                Optional.of(game.getPlayers().get(1).getRoomTable(secondMaxColor)));
        assertTrue(game.getPlayers().get(1).getRoomTable(secondMaxColor).hasTeacher());

        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(4));
        assertTrue(game.getTable().getIslandList().get(4).hasTowers());
        assertEquals(game.getPlayers().get(0).getTowerColor(), game.getTable().getIslandList().get(4).getTowerColor().get());

        for (int i = 0; i < 2; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(secondMaxColor)));
        }

        if (game.getTable().getIslandList().get(4).howManyStudents(secondMaxColor) == 0) {
            for (int i = 0; i < 3; i++) {
                game.getPlayers().get(1).getEntrance().addStudent(secondMaxColor);
                studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                        Optional.of(game.getTable().getIslandList().get(4)));
            }
        } else {
            for (int i = 0; i < 2; i++) {
                game.getPlayers().get(1).getEntrance().addStudent(secondMaxColor);
                studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                        Optional.of(game.getTable().getIslandList().get(4)));
            }
        }
        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(4));
        assertTrue(game.getTable().getIslandList().get(4).hasTowers());
        assertEquals(game.getPlayers().get(0).getTowerColor(), game.getTable().getIslandList().get(4).getTowerColor().get());

        knightCard.activator(influence, game.getPlayers().get(1));
        knightCard.handle(game.getPlayers().get(1), game.getTable().getIslandList().get(4));
        influence.handle(game.getPlayers().get(1), game.getTable().getIslandList().get(4));
        assertTrue(game.getTable().getIslandList().get(4).hasTowers());
        assertEquals(game.getPlayers().get(1).getTowerColor(), game.getTable().getIslandList().get(4).getTowerColor().get());

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
                Characters.getCharacterization(crier));
        Influence influence = new Influence(game.getActionFase());


    }
}