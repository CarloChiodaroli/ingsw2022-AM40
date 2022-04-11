package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.phase.action.StudentMovement;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StudentMovementTest {
    @Test
    public void studentMovementToRoomTableTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());

        game.getPlayers().get(0).getEntrance().addStudent(TeacherColor.PINK);
        studentMovement.handle(TeacherColor.PINK, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(TeacherColor.PINK)));
        assertTrue(game.getPlayers().get(0).getRoomTable(TeacherColor.PINK).hasTeacher());

        for (int i = 0; i < 5; i++) {
            game.getPlayers().get(0).getEntrance().addStudent(TeacherColor.RED);
            studentMovement.handle(TeacherColor.RED, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getPlayers().get(0).getRoomTable(TeacherColor.RED)));
        }
        for (int i = 0; i < 4; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.RED);
            studentMovement.handle(TeacherColor.RED, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(TeacherColor.RED)));
        }
        assertTrue(game.getPlayers().get(0).getRoomTable(TeacherColor.RED).hasTeacher());
        assertFalse(game.getPlayers().get(1).getRoomTable(TeacherColor.RED).hasTeacher());
        for (int i = 0; i < 2; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.RED);
            studentMovement.handle(TeacherColor.RED, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(TeacherColor.RED)));
        }
        assertTrue(game.getPlayers().get(1).getRoomTable(TeacherColor.RED).hasTeacher());
        assertFalse(game.getPlayers().get(0).getRoomTable(TeacherColor.RED).hasTeacher());
    }

    @RepeatedTest(30)
    public void studentMovementToIslandTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());

        for (int i = 0; i < 5; i++) {
            game.getPlayers().get(0).getEntrance().addStudent(TeacherColor.GREEN);
        }

        for (int i = 0; i < 4; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.BLUE);
        }

        int oldTot7 = game.getTable().getIslandList().get(7).howManyTotStudents();
        int oldTot4 = game.getTable().getIslandList().get(4).howManyTotStudents();

        int beforeGreen7 = game.getTable().getIslandList().get(7).howManyStudents(TeacherColor.GREEN);
        int beforeGreen4 = game.getTable().getIslandList().get(4).howManyStudents(TeacherColor.GREEN);

        studentMovement.handle(TeacherColor.GREEN, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getTable().getIslandList().get(4)));

        for (int i = 0; i < 2; i++) {
            studentMovement.handle(TeacherColor.GREEN, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(7)));
        }

        assertEquals(beforeGreen4 + 1, game.getTable().getIslandList().get(4).howManyStudents(TeacherColor.GREEN));
        assertEquals(beforeGreen7 + 2, game.getTable().getIslandList().get(7).howManyStudents(TeacherColor.GREEN));

        int beforeBlue4 = game.getTable().getIslandList().get(4).howManyStudents(TeacherColor.BLUE);
        int beforeBlue7 = game.getTable().getIslandList().get(7).howManyStudents(TeacherColor.BLUE);

        for (int i = 0; i < 2; i++) {
            studentMovement.handle(TeacherColor.BLUE, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(7)));
        }

        assertEquals(beforeBlue4, game.getTable().getIslandList().get(4).howManyStudents(TeacherColor.BLUE));
        assertEquals(beforeBlue7 + 2, game.getTable().getIslandList().get(7).howManyStudents(TeacherColor.BLUE));
        assertEquals(oldTot7 + 4, game.getTable().getIslandList().get(7).howManyTotStudents());
        assertEquals(oldTot4 + 1, game.getTable().getIslandList().get(4).howManyTotStudents());
    }
}


