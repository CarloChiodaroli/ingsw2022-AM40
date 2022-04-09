package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.phase.action.StudentMovement;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class StudentMovementTest {
    @Test
    public void studentMovementTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());

        game.getPlayers().get(0).getEntrance().addStudent(TeacherColor.PINK);
        studentMovement.handle(TeacherColor.PINK, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(TeacherColor.PINK)));
        assertTrue(game.getPlayers().get(0).getRoomTable(TeacherColor.PINK).hasTeacher());

        for(int i = 0; i < 5; i++){
            game.getPlayers().get(0).getEntrance().addStudent(TeacherColor.RED);
            studentMovement.handle(TeacherColor.RED, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getPlayers().get(0).getRoomTable(TeacherColor.RED)));
        }
        for(int i = 0; i < 4; i++){
            game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.RED);
            studentMovement.handle(TeacherColor.RED, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(TeacherColor.RED)));
        }
        assertTrue(game.getPlayers().get(0).getRoomTable(TeacherColor.RED).hasTeacher());
        assertFalse(game.getPlayers().get(1).getRoomTable(TeacherColor.RED).hasTeacher());
        for(int i = 0; i < 2; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.RED);
            studentMovement.handle(TeacherColor.RED, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(TeacherColor.RED)));
        }
        assertTrue(game.getPlayers().get(1).getRoomTable(TeacherColor.RED).hasTeacher());
        assertFalse(game.getPlayers().get(0).getRoomTable(TeacherColor.RED).hasTeacher());
    }
}


