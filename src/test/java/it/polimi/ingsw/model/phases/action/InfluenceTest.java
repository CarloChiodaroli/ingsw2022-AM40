package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.phase.action.Influence;
import it.polimi.ingsw.model.phase.action.StudentMovement;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InfluenceTest {

    @Test
    public void influenceTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());
        TowerColor towerColor0, towerColor1;

        towerColor0 = game.getPlayers().get(0).getTowerColor();
        towerColor1 = game.getPlayers().get(1).getTowerColor();

        int max = 0;
        TeacherColor firstMaxColor = TeacherColor.PINK;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                firstMaxColor = color;
            }
        }

        studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(firstMaxColor)));
        assertTrue(game.getPlayers().get(0).getRoomTable(firstMaxColor).hasTeacher(), "" + max);
        game.getPlayers().get(0).getEntrance().addStudent(firstMaxColor);

        max = 0;
        TeacherColor secondMaxColor = TeacherColor.RED;
        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(1).getEntrance().howManyStudents(color) > max && color != firstMaxColor) {
                max = game.getPlayers().get(1).getEntrance().howManyStudents(color);
                secondMaxColor = color;
            }
        }

        for (int i = 0; i < game.getPlayers().get(0).getEntrance().howManyStudents(firstMaxColor); i++) {
            studentMovement.handle(firstMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(7)));
        }

        Influence influence = new Influence(game.getActionFase());
        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(7));

        assertTrue(game.getTable().getIslandList().get(7).hasTowers());
        assertEquals(Optional.of(towerColor0), game.getTable().getIslandList().get(7).getTowerColor());

        for (int i = 0; i < game.getPlayers().get(1).getEntrance().howManyStudents(secondMaxColor); i++) {
            studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(secondMaxColor)));
        }

        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(7));
        assertTrue(game.getTable().getIslandList().get(7).hasTowers());
        if(game.getTable().getIslandList().get(7).howManyStudents(firstMaxColor) + 1 > game.getTable().getIslandList().get(7).howManyStudents(secondMaxColor))
            assertEquals(Optional.of(towerColor0), game.getTable().getIslandList().get(7).getTowerColor());
        else assertEquals(Optional.of(towerColor1), game.getTable().getIslandList().get(7).getTowerColor());
    }

    @Test
    public void littleTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());
        TowerColor towerColor0;

        towerColor0 = game.getPlayers().get(0).getTowerColor();
        int max = 0;
        TeacherColor maxColor = TeacherColor.PINK;

        for (TeacherColor color : TeacherColor.values()) {
            if (game.getPlayers().get(0).getEntrance().howManyStudents(color) > max) {
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                maxColor = color;
            }
        }

        studentMovement.handle(maxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(maxColor)));
        assertTrue(game.getPlayers().get(0).getRoomTable(maxColor).hasTeacher());
        game.getPlayers().get(0).getEntrance().addStudent(maxColor);

        studentMovement.handle(maxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getTable().getIslandList().get(7)));

        Influence influence = new Influence(game.getActionFase());
        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(7));
        assertTrue(game.getTable().getIslandList().get(7).hasTowers());
        assertEquals(Optional.of(towerColor0), game.getTable().getIslandList().get(7).getTowerColor());

    }

}
