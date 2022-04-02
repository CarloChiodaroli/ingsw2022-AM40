package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.util.HashMap;
import java.util.Map;

abstract class CharacterCard extends ActionFaseState {

    private final int price;
    private boolean usedOneTime;
    private final Character character;
    private boolean using;
    private TeacherColor interestingColor;
    private Island interestingIsland;
    private final Map<String, Integer> characterization;
    private int usesLeft;
    private Player actualPlayer;

    public CharacterCard(Map<String, Integer> args, Character character, ActionFase actionFase) {
        super(actionFase);
        this.characterization = args;
        this.character = character;
        this.price = characterization.get("Price");
        this.usedOneTime = false;
        this.using = false;
        this.interestingColor = null;
    }

    protected void activator(Player player) {
        if (!usedOneTime) usedOneTime = true;
        usesLeft = characterization.get("Usages");
        if(usesLeft > 0) using = true;
        actualPlayer = player;
    }

    protected void activator(Player player, TeacherColor color) {
        activator(player);
        interestingColor = color;

    }

    protected void activator(Player player, Island island) {
        activator(player);
        interestingIsland = island;
    }

    protected void updateUse(){
        usesLeft--;
        if(usesLeft == 0){
            reset();
        }
    }

    private void reset() {
        interestingIsland = null;
        interestingColor = null;
        using = false;
        actualPlayer = null;
    }

    protected Island getInterestingIsland() {
        return interestingIsland;
    }

    protected TeacherColor getInterestingTeacherColor() {
        return interestingColor;
    }

    protected Map<String, Integer> getCharacterization() {
        return new HashMap<>(characterization);
    }

    Character getCharacter() {
        return character;
    }

    public int getPrice() {
        if (usedOneTime) return price + 1;
        else return price;
    }

    public boolean isInUse() {
        return using;
    }

    protected Player getActualPlayer() {
        return actualPlayer;
    }
}
