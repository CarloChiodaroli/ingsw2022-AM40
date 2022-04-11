package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.phase.action.StudentMovement;
import it.polimi.ingsw.model.phase.action.Influence;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InfluenceTest {
    @Test
    public void influenceTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());

        for(int i = 0; i < 4; i++){
            game.getPlayers().get(0).getEntrance().addStudent(TeacherColor.GREEN);
            studentMovement.handle(TeacherColor.GREEN, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getPlayers().get(0).getRoomTable(TeacherColor.GREEN)));
        }
        for(int i = 0; i < 2; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.GREEN);
            studentMovement.handle(TeacherColor.GREEN, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(TeacherColor.GREEN)));
        }
        for(int i = 0; i < 1; i++){
            game.getPlayers().get(0).getEntrance().addStudent(TeacherColor.BLUE);
            studentMovement.handle(TeacherColor.BLUE, Optional.of(game.getPlayers().get(0).getEntrance()),
                    Optional.of(game.getPlayers().get(0).getRoomTable(TeacherColor.BLUE)));
        }
        for(int i = 0; i < 4; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.BLUE);
            studentMovement.handle(TeacherColor.BLUE, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(TeacherColor.BLUE)));
        }
        //player 1 has Green teacher and player 2 has Blue teacher

        for(int i = 0; i < 5; i++){
            game.getPlayers().get(0).getEntrance().addStudent(TeacherColor.GREEN);
        }
        for(int i = 0; i < 4; i++) {
            game.getPlayers().get(1).getEntrance().addStudent(TeacherColor.BLUE);
        }
        studentMovement.handle(TeacherColor.GREEN, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getTable().getIslandList().get(4)));

        Influence influence = new Influence(game.getActionFase());


    }

}
