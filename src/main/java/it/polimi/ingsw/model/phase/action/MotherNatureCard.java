package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.util.Map;

class MotherNatureCard extends CharacterCard{

    private MotherNatureState decorated;

    MotherNatureCard(Character character, ActionFase actionFase, Map<String, Integer> args){
        super(args, character, actionFase);
    }

    @Override
    public void handle(Player player, int steps, int maxSteps) {
        decorated.handle(player, steps, maxSteps+2);
    }

    public void activator(MotherNatureState decorated, Player player){
        this.decorated = decorated;
        super.activator(player);
    }

    public void activator(MotherNatureState decorated, Player player, TeacherColor color){    }

    public void activator(MotherNatureState decorated, Player player, Island island){    }
}
