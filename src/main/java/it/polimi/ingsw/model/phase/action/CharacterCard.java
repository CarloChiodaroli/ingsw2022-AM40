package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.table.Island;

abstract class CharacterCard extends ActionFaseState{

    private final int price;
    private boolean usedOneTime;
    private final Character character;
    private boolean using;
    private TeacherColor interestingColor;
    private Island interestingIsland;

    public CharacterCard(int price, Character character, ActionFase actionFase){
        super(actionFase);
        this.character = character;
        this.price = price;
        this.usedOneTime = false;
        this.using = false;
        this.interestingColor = null;
    }

    protected void activator(){
        if(!usedOneTime) usedOneTime = true;
        using = true;
    }

    protected void activator(TeacherColor color){
        activator();
        interestingColor = color;

    }

    protected void activator(Island island){
        activator();
        interestingIsland = island;
    }

    private void reset(){
        interestingIsland = null;
        interestingColor = null;
        using = false;
    }

    protected Island getInterestingIsland(){
        return interestingIsland;
    }

    protected TeacherColor getInterestingTeacherColor(){
        return interestingColor;
    }

    Character getCharacter(){
        return character;
    }

    public int getPrice() {
        if(usedOneTime) return price + 1;
        else return price;
    }

    public boolean isInUse(){
        return using;
    }
}
