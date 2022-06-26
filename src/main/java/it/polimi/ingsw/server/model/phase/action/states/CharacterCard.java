package it.polimi.ingsw.server.model.phase.action.states;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.server.model.phase.action.ActionFaseState;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Island;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract Action Phase State, this class manages all common traits of character cards, like characterizations, price, activation ecc...
 */
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

    /**
     * Constructor
     */
    public CharacterCard(Map<String, Integer> args, Characters characters, ActionPhase actionPhase) {
        super(actionPhase);
        this.characterization = args;
        this.characters = characters;
        this.price = characterization.get("Price");
        this.usedOneTime = false;
        this.using = false;
        this.interestingColor = null;
        this.interestingIsland = null;
    }

    /**
     * Set if the card was already used and activate it
     *
     * @param player player
     * @return true
     */
    protected boolean activator(Player player) {
        if (!usedOneTime) usedOneTime = true;
        usesLeft = characterization.get("Usages");
        if (usesLeft > 0) using = true;
        actualPlayer = player;
        return true;
    }

    /**
     * Set if the card was already used, the interested color and activate it
     *
     * @param player player
     * @param color chosen color
     * @return if is allowed to activate, true
     */
    protected boolean activator(Player player, TeacherColor color) {
        if (getCharacterization("Student") > 0) {
            activator(player);
            interestingColor = color;
            return true;
        }
        return false;
    }

    /**
     * Set if the card was already used, the interested island and activate it
     *
     * @param player player
     * @param island chosen island
     * @return if is allowed to activate, true
     */
    protected boolean activator(Player player, Island island) {
        if (getCharacterization("Island") > 0) {
            activator(player);
            interestingIsland = island;
            return true;
        }
        return false;
    }

    /**
     * Reduce the uses left
     */
    public void updateUse() {
        usesLeft--;
        if (usesLeft == 0) {
            reset();
        }
    }

    /**
     * Reset all
     */
    private void reset() {
        interestingIsland = null;
        interestingColor = null;
        using = false;
        actualPlayer = null;
    }

    /**
     * Get island
     *
     * @return interesting island
     */
    public Island getInterestingIsland() {
        return interestingIsland;
    }

    /**
     * Get color
     *
     * @return interesting color
     */
    public TeacherColor getInterestingTeacherColor() {
        return interestingColor;
    }

    /**
     *
     *
     * @param arg
     * @return
     */
    public Integer getCharacterization(String arg) {
        return new HashMap<>(characterization).get(arg);
    }

    /**
     * Get character
     *
     * @return character
     */
    public Characters getCharacter() {
        return characters;
    }

    /**
     * Price of a card
     *
     * @return price
     */
    public int getPrice() {
        if (usedOneTime) return price + 1;
        else return price;
    }

    /**
     * Get if the card is in use
     *
     * @return true if the card is using
     */
    public boolean isInUse() {
        return using;
    }

    /**
     * Get actual player
     *
     * @return actual player
     */
    public Player getActualPlayer() {
        return actualPlayer;
    }

    /**
     * Check if the payer can pay for the chosen card
     *
     * @param player player
     * @throws InvalidParameterException the parameter is not valid
     */
    public void playerPays(Player player) throws InvalidParameterException {
        if (!player.pay(getPrice())) throw new InvalidParameterException("Player cannot pay for the card");
    }

    /**
     * Get students container
     *
     * @return students container
     */
    public Optional<StudentsManager> getStudentContainer() {
        return Optional.empty();
    }

    /**
     * Activator for character cards who need a color
     *
     * @param decorated phase state
     * @param player player
     * @param color chosen color
     */
    public void activator(ActionFaseState decorated, Player player, TeacherColor color) {
        throw new IllegalStateException("Card cannot be activated with this activator");
    }

    /**
     * Activator for character cards
     *
     * @param decorated phase state
     * @param player player
     */
    public void activator(ActionFaseState decorated, Player player) {
        throw new IllegalStateException("Card cannot be activated with this activator");
    }

    /**
     * Activator for character cards who need an island
     *
     * @param decorated phase state
     * @param player player
     * @param island chosen island
     */
    public void activator(ActionFaseState decorated, Player player, Island island) {
        throw new IllegalStateException("Card cannot be activated with this activator");
    }
}
