package it.polimi.ingsw.server.model.phase.action.states;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.phase.action.ActionFaseState;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Island;

import java.util.*;

/**
 * 3rd Action phase state, this class models the game's influence calc.
 */
public class Influence extends ActionFaseState {

    public Influence(ActionPhase actionPhase) {
        super(actionPhase);
    }

    @Override
    public void handle(Player player, Island island) {
        if (noEntryTile(island)) return;
        List<Player> players = this.getActionFase().getGame().getPlayers();
        Map<Player, Integer> influences = new HashMap<>();
        players.forEach(x -> influences.put(x, 0));

        teacherColorPointAssigner(players, influences, island);
        towerColorPointAssigner(players, influences, island);
        influenceSetter(players, island, winnerFinder(players, influences));
    }

    public void teacherColorPointAssigner(List<Player> players, Map<Player, Integer> influences, Island island) {
        Arrays.stream(TeacherColor.values()).sequential().forEach(color -> singularTeacherColorPointAssigner(players, influences, island, color));
    }

    public void singularTeacherColorPointAssigner(List<Player> players, Map<Player, Integer> influences, Island island, TeacherColor color) {
        players.stream()
                .filter(x -> x.getTeachers().contains(color))
                .findAny()
                .ifPresent(x -> influences.computeIfPresent(x, (k, v) -> v + island.howManyStudents(color)));
    }

    public void towerColorPointAssigner(List<Player> players, Map<Player, Integer> influences, Island island) {
        if (island.getTowerColor().isPresent())
            island.getTowerColor().flatMap(x -> players.stream()
                    .filter(y -> y.getTowerColor().equals(x))
                    .findAny()).ifPresent(y -> influences.computeIfPresent(y, (k, v) -> v + island.howManyEquivalents()));
    }

    public Player winnerFinder(List<Player> players, Map<Player, Integer> influences) {
        int maxInfluence = players.stream().map(influences::get)
                .mapToInt(influence -> influence).max().orElseThrow();
        Player tmpMaxInfluencePlayer = null;
        for (Player iPlayer : players) {
            if (influences.get(iPlayer) == maxInfluence) {
                if (tmpMaxInfluencePlayer == null) {
                    tmpMaxInfluencePlayer = iPlayer;
                } else {
                    return null;
                }
            }
        }
        return tmpMaxInfluencePlayer;
    }

    public void influenceSetter(List<Player> players, Island island, Player maxInfluencePlayer) {
        if (maxInfluencePlayer == null) return;
        if (island.getTowerColor().isPresent())
            island.getTowerColor().ifPresent(x -> possessionSwitcher(
                    island,
                    players.stream().filter(y -> y.getTowerColor().equals(island.getTowerColor().get())).findFirst(),
                    maxInfluencePlayer));
        else {
            possessionSwitcher(island, Optional.empty(), maxInfluencePlayer);
        }
    }

    private void possessionSwitcher(Island island, Optional<Player> outgoing, Player ingoing) {
        outgoing.ifPresent(x -> x.pushTower(island.howManyTowers()));
        if (ingoing.getNumberTowersLeft() < island.howManyEquivalents()) {
            island.setInfluence(ingoing.getTower(ingoing.getNumberTowersLeft()));
        } else {
            island.setInfluence(ingoing.getTower(island.howManyEquivalents()));
        }
        if (ingoing.getNumberTowersLeft() <= 0) {
            this.getActionFase().getGame().endGame();
        }
    }

    public boolean noEntryTile(Island island) {
        if (!super.getActionFase().getGame().isExpertVariant()) return false;
        if (island.hasNoEntryTile()) {
            island.removeNoEntryTile();
            super.getActionFase().giveNoEntryTileBack();
            return true;
        } else return false;
    }
}
