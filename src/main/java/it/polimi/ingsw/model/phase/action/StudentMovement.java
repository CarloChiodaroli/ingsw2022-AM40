package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.player.Player;

import java.util.Set;

class StudentMovement extends ActionFaseState{

    StudentMovement(ActionFase actionFase){
        super(actionFase);
    }

    @Override
    protected void handle(TeacherColor color, StudentsManager from, StudentsManager to) {
        if(from.removeStudent(color)) to.addStudent(color);
    }
}
