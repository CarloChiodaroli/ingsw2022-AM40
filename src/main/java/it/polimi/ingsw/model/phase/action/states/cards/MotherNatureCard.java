package it.polimi.ingsw.model.phase.action.states.cards;

import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.phase.action.ActionFase;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.phase.action.ActionFaseState;
import it.polimi.ingsw.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.model.phase.action.states.MotherNatureState;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.security.InvalidParameterException;
import java.util.Map;

public class MotherNatureCard extends CharacterCard {

    private MotherNatureState decorated;

    public MotherNatureCard(Characters characters, ActionFase actionFase, Map<String, Integer> args){
        super(args, characters, actionFase);
    }

    @Override
    public void handle(Player player, int steps, int maxSteps) {
        decorated.handle(player, steps, maxSteps+2);
    }

    public void activator(ActionFaseState decorated, Player player) throws InvalidParameterException {
        playerPays(player);
        this.decorated = (MotherNatureState) decorated;
        super.activator(player);
    }
}
