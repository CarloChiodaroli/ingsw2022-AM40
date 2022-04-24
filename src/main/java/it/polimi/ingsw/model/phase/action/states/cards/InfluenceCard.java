package it.polimi.ingsw.model.phase.action.states.cards;

import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;
import it.polimi.ingsw.model.phase.action.ActionPhase;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.phase.action.ActionFaseState;
import it.polimi.ingsw.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.model.phase.action.states.Influence;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfluenceCard extends CharacterCard {

    private Influence decorated;
    private int noEntryCounter = 0;

    public InfluenceCard(Characters characters, ActionPhase actionPhase, Map<String, Integer> args) {
        super(args, characters, actionPhase);
        if (super.getCharacterization("Memory") > 0) {
            noEntryCounter = getCharacterization("Memory");
        }
    }

    @Override
    public void handle(Player player, Island island) {
        if (super.getCharacterization("Island") > 0 && super.getCharacterization("NoEntrySetter") == 0) {
            decoratedCaller(player, super.getInterestingIsland());
        }
        if (super.getCharacterization("NoEntrySetter") == 0) decoratedCaller(player, island);
    }

    private void decoratedCaller(Player player, Island island) {
        if (decorated.noEntryTile(island)) return;
        decoratingHandle(player, island);
    }

    private void decoratingHandle(Player player, Island island) {
        List<Player> players = this.getActionFase().getGame().getPlayers();
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

    public void activator(ActionFaseState decorated, Player player) throws InvalidParameterException {
        playerPays(player);
        super.activator(player);
        this.decorated = (Influence) decorated;
    }

    public void activator(ActionFaseState decorated, Player player, TeacherColor color) throws InvalidParameterException {
        playerPays(player);
        super.activator(player, color);
        this.decorated = (Influence) decorated;
    }

    public void activator(ActionFaseState decorated, Player player, Island island) throws InvalidParameterException {
        playerPays(player);
        super.activator(player, island);
        this.decorated = (Influence) decorated;
        if (super.getCharacterization("Memory") > 0) {
            if (!island.hasNoEntryTile()) {
                island.setNoEntry(true);
                noEntryCounter--;
            }
        }
    }

    public void activator(ActionFaseState decorated, Player player, TowerColor color) {
        if (super.activator(player, color)) this.decorated = (Influence) decorated;
    }
}
