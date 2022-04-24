package it.polimi.ingsw.model.phase.action.states;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.phase.action.ActionFaseState;
import it.polimi.ingsw.model.phase.action.ActionPhase;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class CharacterCard extends ActionFaseState {

    private final Map<String, Integer> characterization;
    private final Characters characters;
    private final int price;
    private boolean usedOneTime;
    private boolean using;
    private int usesLeft;
    private TeacherColor interestingColor;
    private Island interestingIsland;
    private Player actualPlayer;


    public CharacterCard(Map<String, Integer> args, Characters characters, ActionPhase actionPhase) {
        super(actionPhase);
        this.characterization = args;
        this.characters = characters;
        this.price = characterization.get("Price");
        this.usedOneTime = false;
        this.using = false;
        this.interestingColor = null;
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

    public void updateUse() {
        usesLeft--;
        if (usesLeft == 0) {
            reset();
        }
    }

    private void reset() {
        interestingIsland = null;
        interestingColor = null;
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

    public void playerPays(Player player) throws InvalidParameterException {
        if (!player.pay(getPrice())) throw new InvalidParameterException("Player cannot pay for the card");
    }

    public Optional<StudentsManager> getStudentContainer() {
        return Optional.empty();
    }

    public void activator(ActionFaseState decorated, Player player, TeacherColor color) {
    }

    public void activator(ActionFaseState decorated, Player player) {
    }

    public void activator(ActionFaseState decorated, Player player, Island island) {
    }
}
