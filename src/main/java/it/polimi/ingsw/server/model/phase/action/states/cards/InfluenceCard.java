package it.polimi.ingsw.server.model.phase.action.states.cards;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.ActionPhaseState;
import it.polimi.ingsw.server.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.server.model.phase.action.states.Influence;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Island;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 3rd type of Character card, this card decors the action phase's {@link Influence} State, modelling all influence calc related character card behaviours.
 */
public class InfluenceCard extends CharacterCard {

    private Influence decorated;
    private int noEntryCounter = 0;

    /**
     * Constructor
     */
    public InfluenceCard(Characters characters, ActionPhase actionPhase, Map<String, Integer> args) {
        super(args, characters, actionPhase);
        if (super.getCharacterization("Memory") > 0) {
            noEntryCounter = getCharacterization("Memory");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(Player player, Island island) {
        if (super.getCharacterization("Island") > 0 && super.getCharacterization("NoEntrySetter") == 0) {
            decoratedCaller(player, super.getInterestingIsland());
        }
        decoratedCaller(player, island);
    }

    /**
     * If the island has the prohibition card, return else decorate
     *
     * @param player player play card
     * @param island chosen island
     */
    private void decoratedCaller(Player player, Island island) {
        if (decorated.noEntryTile(island)) return;
        decoratingHandle(player, island);
    }

    /**
     * According to the type of card, different actions are taken to modify the influence
     *
     * @param player player
     * @param island chosen island
     */
    private void decoratingHandle(Player player, Island island) {
        List<Player> players = this.getActionPhase().getGame().getPlayers();
        Map<Player, Integer> influences = new HashMap<>();
        players.forEach(x -> influences.put(x, 0));

        if (super.getCharacterization("Student") > 0) {
            for (TeacherColor color : TeacherColor.values()) {
                if (!color.equals(super.getInterestingTeacherColor())) {
                    decorated.singularTeacherColorPointAssigner(players, influences, island, color);
                }
            }
        } else decorated.teacherColorPointAssigner(players, influences, island);
        if (super.getCharacterization("Tower") == 0) decorated.towerColorPointAssigner(players, influences, island);
        if (super.getCharacterization("Player") > 0)
            influences.computeIfPresent(player, (k, val) -> val += super.getCharacterization("Player"));
        decorated.influenceSetter(players, island, decorated.winnerFinder(players, influences));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activator(ActionPhaseState decorated, Player player) throws InvalidParameterException {
        if (super.getCharacterization("Island") > 0) {
            throw new InvalidParameterException("An island Id is needed to activate this card");
        }
        if (super.getCharacterization("Student") > 0) {
            throw new InvalidParameterException("A teacher color is needed to activate this card");
        }
        playerPays(player);
        super.activator(player);
        this.decorated = (Influence) decorated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activator(ActionPhaseState decorated, Player player, TeacherColor color) throws InvalidParameterException {
        if (super.getCharacterization("Island") > 0) {
            throw new InvalidParameterException("An island Id is needed to activate this card");
        }
        playerPays(player);
        super.activator(player, color);
        this.decorated = (Influence) decorated;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void activator(ActionPhaseState decorated, Player player, Island island) throws InvalidParameterException {
        if (super.getCharacterization("Student") > 0) {
            throw new InvalidParameterException("A teacher color is needed to activate this card");
        }
        playerPays(player);
        super.activator(player, island);
        this.decorated = (Influence) decorated;
        if (super.getCharacterization("NoEntrySetter") > 0) {
            if (!island.hasNoEntryTile()) {
                island.setNoEntry(true);
                noEntryCounter--;
            }
        }
    }

    /**
     * If not all prohibition cards are back, add one
     */
    public void giveNoEntryBack() {
        if (super.getCharacterization("NoEntrySetter") == 0) {
            throw new IllegalStateException("Card does not manage no entry cards");
        }
        if (noEntryCounter == super.getCharacterization("Memory")) {
            throw new IllegalStateException("Card has already got back all no entry tiles");
        }
        noEntryCounter++;
    }
}
