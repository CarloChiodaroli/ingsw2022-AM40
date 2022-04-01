package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.player.Player;

class Finalize extends ActionFaseState{

    public Finalize(ActionFase actionFase){
        super(actionFase);
    }

    @Override
    public void handle(Player player, StudentsManager cloud) {
        StudentsManager destination = player.getEntrance();
        for(int i = 0; i < cloud.howManyStudents(); i++){
            cloud.getStudent().ifPresent(destination::addStudent);
        }
    }
}
