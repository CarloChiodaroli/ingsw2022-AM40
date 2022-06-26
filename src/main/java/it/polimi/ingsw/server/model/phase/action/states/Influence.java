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

    /**
     * Constructor
     */
    public Influence(ActionPhase actionPhase) {
        super(actionPhase);
    }

    /**
     * If there isn't a prohibition card, calculate teh influence
     *
     * @param player player who calls the calculation
     * @param island computation island
     */
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

    /**
     * Calls the computation of points for each color
     *
     * @param players players
     * @param influences influence for each player
     * @param island computation island
     */
    public void teacherColorPointAssigner(List<Player> players, Map<Player, Integer> influences, Island island) {
        Arrays.stream(TeacherColor.values()).sequential().forEach(color -> singularTeacherColorPointAssigner(players, influences, island, color));
    }

    /**
     * Calculate the points for a color
     *
     * @param players players
     * @param influences influence for each player
     * @param island computation island
     * @param color current color
     */
    public void singularTeacherColorPointAssigner(List<Player> players, Map<Player, Integer> influences, Island island, TeacherColor color) {
        players.stream()
                .filter(x -> x.getTeachers().contains(color))
                .findAny()
                .ifPresent(x -> influences.computeIfPresent(x, (k, v) -> v + island.howManyStudents(color)));
    }

    /**
     * Calculate towers points
     *
     * @param players players
     * @param influences influence for each player
     * @param island computation island
     */
    public void towerColorPointAssigner(List<Player> players, Map<Player, Integer> influences, Island island) {
        if (island.getTowerColor().isPresent())
            island.getTowerColor().flatMap(x -> players.stream()
                    .filter(y -> y.getTowerColor().equals(x))
                    .findAny()).ifPresent(y -> influences.computeIfPresent(y, (k, v) -> v + island.howManyEquivalents()));
    }

    /**
     * Get the winner of the influence calculate
     *
     * @param players players
     * @param influences influence for each player
     * @return players who win
     */
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

    /**
     * Set the influence and if is necessary switch possession between old and new owner
     *
     * @param players players
     * @param island computation island
     * @param maxInfluencePlayer the new owner
     */
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

    /**
     * Switch the possession of the island from a player to another
     *
     * @param island computation island
     * @param outgoing the old owner
     * @param ingoing the new owner
     */
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

    /**
     * Check if there's a prohibition card and eventually remove it
     *
     * @param island computation island
     * @return true if there's the card
     */
    public boolean noEntryTile(Island island) {
        if (!super.getActionFase().getGame().isExpertVariant()) return false;
        if (island.hasNoEntryTile()) {
            island.removeNoEntryTile();
            super.getActionFase().giveNoEntryTileBack();
            return true;
        } else return false;
    }
}
