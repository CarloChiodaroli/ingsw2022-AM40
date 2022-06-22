package it.polimi.ingsw.server.model.phase.action.states.cards;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.server.model.phase.action.ActionFaseState;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.server.model.phase.action.states.MotherNatureState;
import it.polimi.ingsw.server.model.player.Player;

import java.security.InvalidParameterException;
import java.util.Map;

/**
 * 2nd type of Character card, this card decors the action phase's {@link MotherNatureState} State, modelling all mother nature related character card behaviours.
 */
public class MotherNatureCard extends CharacterCard {

    private MotherNatureState decorated;

    public MotherNatureCard(Characters characters, ActionPhase actionPhase, Map<String, Integer> args) {
        super(args, characters, actionPhase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Player player, int steps, int maxSteps) {
        decorated.handle(player, steps, maxSteps + 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activator(ActionFaseState decorated, Player player) throws InvalidParameterException {
        playerPays(player);
        this.decorated = (MotherNatureState) decorated;
        super.activator(player);
    }
}
