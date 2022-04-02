package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.util.*;

class Influence extends ActionFaseState{

    public Influence(ActionFase actionFase){
        super(actionFase);
    }

    @Override
    public void handle(Player player, Island island) {
        if(noEntryTile(island)) return;
        // Initialization
        List<Player> players = this.getActionFase().getGame().getPlayers();
        Map<Player, Integer> influences = new HashMap<>();
        players.forEach(x -> influences.put(x, 0));
        // Assign points for teacher color
        Arrays.stream(TeacherColor.values()).sequential().forEach(color ->
                players.stream()
                        .filter(x -> x.getTeachers().contains(color))
                        .findAny()
                        .ifPresent(x -> influences.computeIfPresent(x, (k, v) -> v + island.howManyStudents(color))));
        // Assign points for towers
        island.getTowerColor().flatMap(x -> players.stream()
                .filter(y -> y.getTowerColor().equals(x))
                .findAny()).ifPresent(y -> influences.computeIfPresent(y, (k, v) -> v + island.howManyEquivalents()));
        // Get max influence value
        int maxInfluence = players.stream().map(influences::get)
                .mapToInt(influence -> influence).max().getAsInt();  //for how is written there will be always a value
        // Get the player who has that value, ensures that is only one
        Player tmpMaxInfluencePlayer = null;
        for(Player iPlayer: players){
            if(influences.get(iPlayer) == maxInfluence){
                if(tmpMaxInfluencePlayer == null){
                    tmpMaxInfluencePlayer = iPlayer;
                } else {
                    return;
                }
            }
        }
        final Player maxInfluencePlayer = tmpMaxInfluencePlayer;
        // Set the new influence on the island
        if(island.getTowerColor().isPresent())
            island.getTowerColor().ifPresent(x -> possessionSwitcher(
                island,
                players.stream().filter(y -> y.getTowerColor().equals(island.getTowerColor().get())).findFirst(),
                maxInfluencePlayer));
        else{
            possessionSwitcher(island, Optional.empty(), maxInfluencePlayer);
        }
    }

    private void possessionSwitcher(Island island, Optional<Player> outgoing, Player ingoing){
        outgoing.ifPresent(x -> x.pushTower(island.howManyTowers()));
        try {
            island.setInfluence(ingoing.getTower(island.howManyTowers()));
        }
        catch(Exception e){
            System.err.println("End of play case"); //Temporary
        }
    }

    private boolean noEntryTile(Island island){
        if(!super.getActionFase().getGame().isExpertVariant()) return false;
        if(island.hasNoEntryTile()){
            island.removeNoEntryTile();
            return true;
        } else return false;
    }
}
