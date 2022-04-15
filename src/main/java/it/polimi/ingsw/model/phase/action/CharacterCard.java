package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.TeacherColor;
import it.polimi.ingsw.model.TowerColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public abstract class CharacterCard extends ActionFaseState {

    private final int price;
    private boolean usedOneTime;
    private final Characters characters;
    private boolean using;
    private TeacherColor interestingColor;
    private Island interestingIsland;
    private final Map<String, Integer> characterization;
    private int usesLeft;
    private Player actualPlayer;
    private TowerColor interestingTower;

    public CharacterCard(Map<String, Integer> args, Characters characters, ActionFase actionFase) {
        super(actionFase);
        this.characterization = args;
        this.characters = characters;
        this.price = characterization.get("Price");
        this.usedOneTime = false;
        this.using = false;
        this.interestingColor = null;
        this.interestingTower = null;
    }

    public boolean activator(Player player) {
        if (!usedOneTime) usedOneTime = true;
        usesLeft = characterization.get("Usages");
        if (usesLeft > 0) using = true;
        actualPlayer = player;
        return true;
    }

    public boolean activator(Player player, TeacherColor color) {
        if (getCharacterization("Student") > 0) {
            activator(player);
            interestingColor = color;
            return true;
        }
        return false;
    }

    public boolean activator(Player player, Island island) {
        if (getCharacterization("Island") > 0) {
            activator(player);
            interestingIsland = island;
            return true;
        }
        return false;
    }

    public boolean activator(Player player, TowerColor color) {
        if (getCharacterization("Tower") > 0) {
            activator(player);
            interestingTower = color;
            return true;
        }
        return false;
    }

    public void updateUse() {
        usesLeft--;
        if (usesLeft == 0) {
            reset();
        }
    }

    private void reset() {
        interestingIsland = null;
        interestingColor = null;
        interestingTower = null;
        using = false;
        actualPlayer = null;
    }

    public Island getInterestingIsland() {
        return interestingIsland;
    }

    public TeacherColor getInterestingTeacherColor() {
        return interestingColor;
    }

    public Integer getCharacterization(String arg) {
        return new HashMap<>(characterization).get(arg);
    }

    public Characters getCharacter() {
        return characters;
    }

    public int getPrice() {
        if (usedOneTime) return price + 1;
        else return price;
    }

    public boolean isInUse() {
        return using;
    }

    public Player getActualPlayer() {
        return actualPlayer;
    }

    public boolean playerPays(Player player) throws InvalidParameterException {
        if(player.pay(getPrice())){
            return true;
        } else throw new InvalidParameterException("Player cannot pay for the card");
    }
}
