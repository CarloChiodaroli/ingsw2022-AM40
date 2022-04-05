package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InfluenceCard extends CharacterCard{

    private Influence decorated;
    private int noEntryCounter = 0;

    InfluenceCard(Characters characters, ActionFase actionFase, Map<String, Integer> args){
        super(args, characters, actionFase);
        if(super.getCharacterization("Memory") > 0){
            noEntryCounter = getCharacterization("Memory");
        }
    }

    @Override
    public void handle(Player player, Island island) {
        if(super.getCharacterization("Island") > 1){
            decoratedCaller(player, super.getInterestingIsland());
        }
        decoratedCaller(player, island);
    }

    private void decoratedCaller(Player player, Island island){
        if(super.getCharacterization("Memory") > 0){
            if(island.hasNoEntryTile()){
                island.setNoEntry(false);
                noEntryCounter++;
            } else {
                decoratingHandle(player, island);
            }
        }
    }

    private void decoratingHandle(Player player, Island island){
        List<Player> players = this.getActionFase().getGame().getPlayers();
        Map<Player, Integer> influences = new HashMap<>();
        players.forEach(x -> influences.put(x, 0));

        if(super.getCharacterization("Teacher") > 0){
            for(TeacherColor color: TeacherColor.values()){
                if(!color.equals(super.getInterestingTeacherColor())){
                    decorated.singularTeacherColorPointAssigner(players, influences, island, color);
                }
            }
        } else decorated.teacherColorPointAssigner(players, influences, island);
        if(super.getCharacterization("Tower") == 0) decorated.towerColorPointAssigner(players, influences, island);
        if(super.getCharacterization("Player") > 0)
            influences.computeIfPresent(player, (k, val) -> val += super.getCharacterization("Player"));
        decorated.influenceSetter(players, island, decorated.winnerFinder(players, influences));
    }

    public void activator(Influence decorated, Player player){
        if(super.activator(player)) this.decorated = decorated;
    }

    public void activator(Influence decorated, Player player, TeacherColor color){
        if(super.activator(player, color)) this.decorated = decorated;
    }

    public void activator(Influence decorated, Player player,  Island island){
        if(super.activator(player, island)) this.decorated = decorated;
        if(super.getCharacterization("Memory") > 0){
            if(!island.hasNoEntryTile()){
                island.setNoEntry(true);
                noEntryCounter --;
            }
        }
    }

    public void activator(Influence decorated, Player player, TowerColor color){
        if(super.activator(player, color)) this.decorated = decorated;
    }
}
