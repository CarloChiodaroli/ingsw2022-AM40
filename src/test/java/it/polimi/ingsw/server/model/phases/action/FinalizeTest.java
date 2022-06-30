package it.polimi.ingsw.server.model.phases.action;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.states.Finalize;
import it.polimi.ingsw.server.model.phase.action.states.StudentMovement;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class test the {@link Finalize} methods
 */
public class FinalizeTest {
    /**
     * test finalize with 2 players
     */
    @Test
    public void finalizeTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionPhase());
        StudentsManager entrance0 = game.getPlayers().get(0).getEntrance();
        StudentsManager entrance1 = game.getPlayers().get(1).getEntrance();
        Finalize finalize = new Finalize(game.getActionPhase());

        for(int i = 0; i < 3; i++){
            int j = (int) (Math.random()*5);
            boolean find = false;
            while(!find) {
                if (j == 0 && entrance0.howManyStudents(TeacherColor.YELLOW) > 0) {
                    studentMovement.handle(TeacherColor.YELLOW, Optional.of(entrance0),
                            Optional.of(game.getPlayers().get(0).getRoomTable()));
                    find = true;
                }
                if (j == 0 && entrance0.howManyStudents(TeacherColor.YELLOW) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 1 && entrance0.howManyStudents(TeacherColor.PINK) > 0) {
                    studentMovement.handle(TeacherColor.PINK, Optional.of(entrance0),
                            Optional.of(game.getPlayers().get(0).getRoomTable()));
                    find = true;
                }
                if (j == 1 && entrance0.howManyStudents(TeacherColor.PINK) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 2 && entrance0.howManyStudents(TeacherColor.RED) > 0) {
                    studentMovement.handle(TeacherColor.RED, Optional.of(entrance0),
                            Optional.of(game.getPlayers().get(0).getRoomTable()));
                    find = true;
                }
                if (j == 2 && entrance0.howManyStudents(TeacherColor.RED) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 3 && entrance0.howManyStudents(TeacherColor.GREEN) > 0) {
                    studentMovement.handle(TeacherColor.GREEN, Optional.of(entrance0),
                            Optional.of(game.getPlayers().get(0).getRoomTable()));
                    find = true;
                }
                if (j == 3 && entrance0.howManyStudents(TeacherColor.GREEN) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 4 && entrance0.howManyStudents(TeacherColor.BLUE) > 0) {
                    studentMovement.handle(TeacherColor.BLUE, Optional.of(entrance0),
                            Optional.of(game.getPlayers().get(0).getRoomTable()));
                    find = true;
                }
                if (j == 4 && entrance0.howManyStudents(TeacherColor.BLUE) == 0 && !find)
                    j = (int) (Math.random()*5);
            }
        }
        assertEquals(4, game.getPlayers().get(0).getEntrance().howManyTotStudents());
        assertEquals(3, game.getTable().getCloudList().get(0).howManyTotStudents());
        finalize.handle(game.getPlayers().get(0), game.getTable().getCloudList().get(0));
        assertEquals(7, game.getPlayers().get(0).getEntrance().howManyTotStudents());

        for(int i = 0; i < 3; i++){
            int j = (int) (Math.random()*5);
            boolean find = false;
            while(!find) {
                if (j == 0 && entrance1.howManyStudents(TeacherColor.YELLOW) > 0) {
                    studentMovement.handle(TeacherColor.YELLOW, Optional.of(entrance1),
                            Optional.of(game.getPlayers().get(1).getRoomTable()));
                    find = true;
                }
                if (j == 0 && entrance1.howManyStudents(TeacherColor.YELLOW) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 1 && entrance1.howManyStudents(TeacherColor.PINK) > 0) {
                    studentMovement.handle(TeacherColor.PINK, Optional.of(entrance1),
                            Optional.of(game.getPlayers().get(1).getRoomTable()));
                    find = true;
                }
                if (j == 1 && entrance1.howManyStudents(TeacherColor.PINK) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 2 && entrance1.howManyStudents(TeacherColor.RED) > 0) {
                    studentMovement.handle(TeacherColor.RED, Optional.of(entrance1),
                            Optional.of(game.getPlayers().get(1).getRoomTable()));
                    find = true;
                }
                if (j == 2 && entrance1.howManyStudents(TeacherColor.RED) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 3 && entrance1.howManyStudents(TeacherColor.GREEN) > 0) {
                    studentMovement.handle(TeacherColor.GREEN, Optional.of(entrance1),
                            Optional.of(game.getPlayers().get(1).getRoomTable()));
                    find = true;
                }
                if (j == 3 && entrance1.howManyStudents(TeacherColor.GREEN) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 4 && entrance1.howManyStudents(TeacherColor.BLUE) > 0) {
                    studentMovement.handle(TeacherColor.BLUE, Optional.of(entrance1),
                            Optional.of(game.getPlayers().get(1).getRoomTable()));
                    find = true;
                }
                if (j == 4 && entrance1.howManyStudents(TeacherColor.BLUE) == 0 && !find)
                    j = (int) (Math.random()*5);
            }
        }
        assertEquals(4, game.getPlayers().get(1).getEntrance().howManyTotStudents());
        assertEquals(3, game.getTable().getCloudList().get(0).howManyTotStudents());
        finalize.handle(game.getPlayers().get(1), game.getTable().getCloudList().get(0));
        assertEquals(7, game.getPlayers().get(1).getEntrance().howManyTotStudents());

    }

    /**
     * test finalize with 3 players
     */
    @Test
    public void finalize3PlayersTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.addPlayer("Barbara");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionPhase());
        StudentsManager entrance2 = game.getPlayers().get(2).getEntrance();
        Finalize finalize = new Finalize(game.getActionPhase());

        for(int i = 0; i < 4; i++){
            int j = (int) (Math.random()*5);
            boolean find = false;
            while(!find) {
                if (j == 0 && entrance2.howManyStudents(TeacherColor.YELLOW) > 0) {
                    studentMovement.handle(TeacherColor.YELLOW, Optional.of(entrance2),
                            Optional.of(game.getPlayers().get(2).getRoomTable()));
                    find = true;
                }
                if (j == 0 && entrance2.howManyStudents(TeacherColor.YELLOW) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 1 && entrance2.howManyStudents(TeacherColor.PINK) > 0) {
                    studentMovement.handle(TeacherColor.PINK, Optional.of(entrance2),
                            Optional.of(game.getPlayers().get(2).getRoomTable()));
                    find = true;
                }
                if (j == 1 && entrance2.howManyStudents(TeacherColor.PINK) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 2 && entrance2.howManyStudents(TeacherColor.RED) > 0) {
                    studentMovement.handle(TeacherColor.RED, Optional.of(entrance2),
                            Optional.of(game.getPlayers().get(2).getRoomTable()));
                    find = true;
                }
                if (j == 2 && entrance2.howManyStudents(TeacherColor.RED) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 3 && entrance2.howManyStudents(TeacherColor.GREEN) > 0) {
                    studentMovement.handle(TeacherColor.GREEN, Optional.of(entrance2),
                            Optional.of(game.getPlayers().get(2).getRoomTable()));
                    find = true;
                }
                if (j == 3 && entrance2.howManyStudents(TeacherColor.GREEN) == 0 && !find)
                    j = (int) (Math.random()*5);
                if (j == 4 && entrance2.howManyStudents(TeacherColor.BLUE) > 0) {
                    studentMovement.handle(TeacherColor.BLUE, Optional.of(entrance2),
                            Optional.of(game.getPlayers().get(2).getRoomTable()));
                    find = true;
                }
                if (j == 4 && entrance2.howManyStudents(TeacherColor.BLUE) == 0 && !find)
                    j = (int) (Math.random()*5);
            }
        }
        assertEquals(5, game.getPlayers().get(2).getEntrance().howManyTotStudents());
        assertEquals(4, game.getTable().getCloudList().get(2).howManyTotStudents());
        finalize.handle(game.getPlayers().get(2), game.getTable().getCloudList().get(2));
        assertEquals(9, game.getPlayers().get(2).getEntrance().howManyTotStudents());
    }

}