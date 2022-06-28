package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.phase.action.states.MotherNatureState;
import it.polimi.ingsw.server.model.phase.action.states.StudentMovement;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link StudentMovement} methods
 */
public class StudentMovementTest {

    /**
     * Test student movement to rooms
     */
    @Test
    public void studentMovementToRoomTableTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionPhase());

        int max = 0;
        TeacherColor maxColor = TeacherColor.PINK;

        for(TeacherColor color: TeacherColor.values()){
            if(game.getPlayers().get(0).getEntrance().howManyStudents(color) > max){
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                maxColor = color;
            }
        }

        studentMovement.handle(maxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable()));
        assertTrue(game.getPlayers().get(0).hasTeacher(maxColor), "" + max);

        max = 0;
        TeacherColor secondMaxColor = TeacherColor.RED;
        for(TeacherColor color: TeacherColor.values()){
            if(game.getPlayers().get(1).getEntrance().howManyStudents(color) > max && color != maxColor){
                max = game.getPlayers().get(1).getEntrance().howManyStudents(color);
                secondMaxColor = color;
            }
        }

        int total1 = game.getPlayers().get(0).getEntrance().howManyStudents(secondMaxColor);
        for (int i = 0; i < total1; i++) {
            studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getPlayers().get(0).getRoomTable()));
        }

        for (int i = 0; i < max; i++) {
            studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable()));
        }

        if(total1 >= max){
            assertTrue(game.getPlayers().get(0).hasTeacher(secondMaxColor), total1 + " " + max);
            assertFalse(game.getPlayers().get(1).hasTeacher(secondMaxColor), total1 + " " + max);
        } else {
            assertTrue(game.getPlayers().get(1).hasTeacher(secondMaxColor), total1 + " " + max);
            assertFalse(game.getPlayers().get(0).hasTeacher(secondMaxColor), total1 + " " + max);
        }

        int delta;
        Player actualPlayer;
        if(total1 >= max){
            actualPlayer = game.getPlayers().get(1);
            delta = total1-max;
        } else  {
            actualPlayer = game.getPlayers().get(0);
            delta = max-total1;
        }

        delta += 2;

        for (int i = 0; i < delta; i++) {
            actualPlayer.getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(actualPlayer.getEntrance()),
                    Optional.of(actualPlayer.getRoomTable()));
        }

        if(total1 >= max){
            assertTrue(game.getPlayers().get(1).hasTeacher(secondMaxColor), total1 + " " + max);
            assertFalse(game.getPlayers().get(0).hasTeacher(secondMaxColor), total1 + " " + max);
        } else {
            assertTrue(game.getPlayers().get(0).hasTeacher(secondMaxColor), total1 + " " + max);
            assertFalse(game.getPlayers().get(1).hasTeacher(secondMaxColor), total1 + " " + max);
        }
    }

    /**
     * Test student movement to islands
     */
    @Test
    public void studentMovementToIslandTest() {
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionPhase());

        int max = 0;
        TeacherColor maxColor = TeacherColor.PINK;

        for(TeacherColor color: TeacherColor.values()){
            if(game.getPlayers().get(0).getEntrance().howManyStudents(color) > max){
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                maxColor = color;
            }
        }

        int oldTot7 = game.getTable().getIslandList().get(7).howManyTotStudents();
        int oldTot4 = game.getTable().getIslandList().get(4).howManyTotStudents();

        int beforeMaxColor7 = game.getTable().getIslandList().get(7).howManyStudents(maxColor);
        int beforeMaxColor4 = game.getTable().getIslandList().get(4).howManyStudents(maxColor);

        studentMovement.handle(maxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getTable().getIslandList().get(4)));

        int howMany = game.getPlayers().get(0).getEntrance().howManyStudents(maxColor);

        for (int i = 0; i < howMany; i++) {
            studentMovement.handle(maxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(7)));
        }

        assertEquals(beforeMaxColor4 + 1, game.getTable().getIslandList().get(4).howManyStudents(maxColor));
        assertEquals(beforeMaxColor7 + howMany, game.getTable().getIslandList().get(7).howManyStudents(maxColor));

        max = 0;
        maxColor = TeacherColor.PINK;

        for(TeacherColor color: TeacherColor.values()){
            if(game.getPlayers().get(1).getEntrance().howManyStudents(color) > max){
                max = game.getPlayers().get(1).getEntrance().howManyStudents(color);
                maxColor = color;
            }
        }

        int old = howMany;
        beforeMaxColor4 = game.getTable().getIslandList().get(4).howManyStudents(maxColor);
        beforeMaxColor7 = game.getTable().getIslandList().get(7).howManyStudents(maxColor);
        howMany = game.getPlayers().get(1).getEntrance().howManyStudents(maxColor);

        for (int i = 0; i < howMany; i++) {
            studentMovement.handle(maxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(7)));
        }

        assertEquals(beforeMaxColor4, game.getTable().getIslandList().get(4).howManyStudents(maxColor));
        assertEquals(beforeMaxColor7 + howMany, game.getTable().getIslandList().get(7).howManyStudents(maxColor));
        assertEquals(oldTot7 + howMany + old, game.getTable().getIslandList().get(7).howManyTotStudents());
        assertEquals(oldTot4 + 1, game.getTable().getIslandList().get(4).howManyTotStudents());
    }
}


