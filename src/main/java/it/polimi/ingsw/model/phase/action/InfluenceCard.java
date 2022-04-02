package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.util.Map;

class InfluenceCard extends CharacterCard{

    private Influence decorated;

    InfluenceCard(Character character, ActionFase actionFase, Map<String, Integer> args){
        super(args, character, actionFase);
    }

    @Override
    public void handle(Player player, Island island) {

    }

    public void activator(Influence decorated, Player player){
        this.decorated = decorated;
        super.activator(player);
    }

    public void activator(Influence decorated, Player player, TeacherColor color){
        this.decorated = decorated;
        super.activator(player, color);
    }

    public void activator(Influence decorated, Player player,  Island island){
        this.decorated = decorated;
        super.activator(player, island);
    }
}
