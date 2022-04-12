package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.phase.action.StudentMovement;
import it.polimi.ingsw.model.phase.action.Influence;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
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
        TowerColor towerColor0, towerColor1;

        towerColor0 = game.getPlayers().get(0).getTowerColor();
        towerColor1 = game.getPlayers().get(1).getTowerColor();
        int max = 0;
        TeacherColor maxColor = TeacherColor.PINK;

        for(TeacherColor color: TeacherColor.values()){
            if(game.getPlayers().get(0).getEntrance().howManyStudents(color) > max){
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                maxColor = color;
            }
        }

        studentMovement.handle(maxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(maxColor)));
        //assertTrue(game.getPlayers().get(0).getRoomTable(maxColor).hasTeacher(), "" + max);
        game.getPlayers().get(0).getEntrance().addStudent(maxColor);

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
                    Optional.of(game.getPlayers().get(0).getRoomTable(secondMaxColor)));
        }

        for (int i = 0; i < max; i++) {
            studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(secondMaxColor)));
        }

        studentMovement.handle(maxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getTable().getIslandList().get(7)));
        Influence influence = new Influence(game.getActionFase());
        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(7));
        assertTrue(game.getTable().getIslandList().get(7).hasTowers());
        assertEquals(Optional.of(towerColor0), game.getTable().getIslandList().get(7).getTowerColor());

        while(!(game.getPlayers().get(1).getRoomTable(secondMaxColor).hasTeacher())){
            game.getPlayers().get(1).getEntrance().addStudent(secondMaxColor);
            studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getPlayers().get(1).getRoomTable(secondMaxColor)));
        }

        while(game.getPlayers().get(1).getEntrance().howManyStudents(secondMaxColor) < game.getTable().getIslandList().get(7).
                howManyStudents(maxColor) + 2 /*+ 2 = 1 torre + 1 studente in più che fa vincere l'influenza*/){
            game.getPlayers().get(1).getEntrance().addStudent(secondMaxColor);
        }
        for(int i = 0; i < game.getPlayers().get(1).getEntrance().howManyStudents(secondMaxColor); i++){
            studentMovement.handle(secondMaxColor, Optional.of(game.getPlayers().get(1).getEntrance()),
                    Optional.of(game.getTable().getIslandList().get(7)));
        }
        influence.handle(game.getPlayers().get(1), game.getTable().getIslandList().get(7));
        assertTrue(game.getTable().getIslandList().get(7).hasTowers());
        assertEquals(Optional.of(towerColor1), game.getTable().getIslandList().get(7).getTowerColor());


    }

    @Test
    public void littleTest(){
        Game game = new Game();
        game.addPlayer("Camilla");
        game.addPlayer("Anja");
        game.gameStarter();
        StudentMovement studentMovement = new StudentMovement(game.getActionFase());
        Player hasTeacher;
        TowerColor towerColor0;

        towerColor0 = game.getPlayers().get(0).getTowerColor();
        int max = 0;
        TeacherColor maxColor = TeacherColor.PINK;

        for(TeacherColor color: TeacherColor.values()){
            if(game.getPlayers().get(0).getEntrance().howManyStudents(color) > max){
                max = game.getPlayers().get(0).getEntrance().howManyStudents(color);
                maxColor = color;
            }
        }

        studentMovement.handle(maxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getPlayers().get(0).getRoomTable(maxColor)));
        //assertTrue(game.getPlayers().get(0).getRoomTable(maxColor).hasTeacher());
        game.getPlayers().get(0).getEntrance().addStudent(maxColor);

        studentMovement.handle(maxColor, Optional.of(game.getPlayers().get(0).getEntrance()),
                Optional.of(game.getTable().getIslandList().get(7)));

        Influence influence = new Influence(game.getActionFase());
        influence.handle(game.getPlayers().get(0), game.getTable().getIslandList().get(7));
        assertTrue(game.getTable().getIslandList().get(7).hasTowers());
        assertEquals(Optional.of(towerColor0), game.getTable().getIslandList().get(7).getTowerColor());

    }

}
