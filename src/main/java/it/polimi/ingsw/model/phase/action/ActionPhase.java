package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.ActionPhaseStateType;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.phase.action.states.*;
import it.polimi.ingsw.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.model.phase.action.states.cards.InfluenceCard;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.model.table.MotherNature;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Manages the game's Action Phase in normal or in Expert Variant
 */
public class ActionPhase {

    // Variables
    private final Game game;
    private final Map<ActionPhaseStateType, ActionFaseState> states;
    private boolean activated;

    // Non expert Status variables
    private int possibleStudentMovements;
    private boolean movedMotherNature;
    private boolean calculatedInfluence;
    private boolean mergedIslands;
    private boolean chosenCloud;
    private int actualState;

    // Expert variant attributes
    private final boolean expertVariant;
    private CharacterCard actualCard;
    private final Map<Characters, CharacterCard> characterCards;

    /**
     * Constructor
     *
     * @param game the game in which the phase is used
     */
    public ActionPhase(Game game) {
        this.game = game;
        this.expertVariant = game.isExpertVariant();
        this.activated = false;
        this.states = new HashMap<>();
        this.states.put(ActionPhaseStateType.STUDENT, new StudentMovement(this));
        this.states.put(ActionPhaseStateType.MOTHER, new MotherNatureState(this));
        this.states.put(ActionPhaseStateType.INFLUENCE, new Influence(this));
        this.states.put(ActionPhaseStateType.MERGE, new MergeIsland(this));
        this.states.put(ActionPhaseStateType.CLOUD, new Finalize(this));
        if (isExpertVariant()) {
            this.characterCards = CharacterCardFabric.getCards(this);
        } else {
            this.characterCards = null;
        }
        actualState = -1;
    }

    public Map<Characters, CharacterCard> getCharacterCards() {
        return characterCards;
    }

    /**
     * Starts a new Action phase.
     * Since, during a play, the action phase needs to keep memory of what happens,
     * this method is needed to reset the phase's state every time a player starts his phase in the round
     *
     * @param player the player who wants to start his action phase
     */
    public void startPhase(Player player) {
        if (activated) return;
        if (!game.getPianificationFase().getActualPlayer().equals(player)) return;
        player.enable();
        // reset of action phase state
        activated = true;
        possibleStudentMovements = 3;
        movedMotherNature = false;
        calculatedInfluence = false;
        mergedIslands = false;
        chosenCloud = false;
        actualState = ActionPhaseStateType.STUDENT.getOrderPlace();
        if (isExpertVariant()) {
            actualCard = null;
        }
    }

    // Commands

    /**
     * Manages the call to the movement of Students between places
     *
     * @param teacherColor is the color of the student to move
     * @param from         is the place from which the student is moved
     * @param to           is the place where the student is moved to
     * @throws IllegalStateException is thrown when it's not the right moment to move students
     */
    public void request(TeacherColor teacherColor, Optional<StudentsManager> from, Optional<StudentsManager> to)
            throws IllegalStateException {
        isStateActivated();
        if (possibleStudentMovements <= 0 || calculatedInfluence)
            throw new IllegalStateException("Cannot move any students");
        if (isExpertVariant() &&
                getActualCard().isPresent() &&
                actualCard.getCharacter().getType().equals(ActionPhaseStateType.STUDENT) &&
                actualCard.isInUse()) {
            actualCard.handle(teacherColor, from, to);
        } else {
            states.get(ActionPhaseStateType.STUDENT).handle(teacherColor, from, to);
        }
        possibleStudentMovements--;
    }

    /**
     * It's a special student movement request needed by those cards who bidirectionally shift students between places.
     * Since those cards move students between three well-defined places (player's school entrance, p.'s s. room and the card),
     * at every call, thanks the context, is known if it's legal and what places are the ones to move students in and out.
     *
     * @param player   the player who makes the request
     * @param studentA the color of the student from the player's entrance
     * @param studentB the color of the student from the player's room or from the card
     * @throws IllegalStateException is thrown when it's not the right moment nor the right game mode to use this kind of student movement
     */
    public void request(Player player, TeacherColor studentA, TeacherColor studentB) throws IllegalStateException {
        isStateActivated();
        controlExpertVariant();
        controlActualCard();
        if (possibleStudentMovements <= 0 || calculatedInfluence)
            throw new IllegalStateException("Cannot move any students");
        if (actualCard.isInUse()) {
            actualCard.handle(player, studentA, studentB);
        } else {
            throw new IllegalStateException("Card has been already used");
        }
        possibleStudentMovements--;
    }

    /**
     * Manages the call to move mother nature
     *
     * @param player           the player who makes the request to move mother Nature
     * @param motherNatureHops the number of hops that the player wants her to make
     * @throws IllegalStateException is thrown when it's not the right moment to move mother nature
     */
    public void request(Player player, int motherNatureHops) throws IllegalStateException {
        isStateActivated();
        if (movedMotherNature)
            throw new IllegalStateException("Mother nature has been already moved once");
        actualState = ActionPhaseStateType.MOTHER.getOrderPlace();
        int maxHops = game.getPianificationFase().getMotherNatureHops(player);
        if (!isExpertVariant()) {
            states.get(ActionPhaseStateType.MOTHER).handle(player, motherNatureHops, maxHops);
        } else {
            if (getActualCard().isPresent() &&
                    actualCard.isInUse() &&
                    actualCard.getCharacter().getType().equals(ActionPhaseStateType.MOTHER)) {
                actualCard.handle(player, motherNatureHops, maxHops);
            } else {
                states.get(ActionPhaseStateType.MOTHER).handle(player, motherNatureHops, maxHops);
            }
        }
        movedMotherNature = true;
        actualState++;
    }

    /**
     * Manages the request to calc the influence or the request of a cloud according to the context
     *
     * @param player the player who makes the request
     * @param id     the id of the cloud, or "MotherNature" to calc the influence
     * @throws IllegalStateException is thrown when it's not the right moment to calc the influence nor to choose a cloud
     */
    public void request(Player player, String id) throws IllegalStateException, NoSuchElementException {
        isStateActivated();
        if (id.equals("MotherNature")) {
            if (!movedMotherNature || calculatedInfluence)
                throw new IllegalStateException("Cannot calculate Influence now");
            if (MotherNature.getMotherNature().getPosition().isEmpty())
                throw new RuntimeException("Mother Nature does not know where she is");
            if (actualCard != null &&
                    actualCard.isInUse() &&
                    actualCard.getCharacter().getType().equals(ActionPhaseStateType.INFLUENCE))
                actualCard.handle(player, MotherNature.getMotherNature().getPosition().get());
            else
                states.get(ActionPhaseStateType.INFLUENCE).handle(player, MotherNature.getMotherNature().getPosition().get());
            calculatedInfluence = true;
            actualState++;
            request();
        } else {
            if (!calculatedInfluence || chosenCloud)
                throw new IllegalStateException("Cannot choose clouds now");
            states.get(ActionPhaseStateType.CLOUD).handle(player, game.getTable().getCloudById(id)
                    .orElseThrow(() -> new NoSuchElementException("Cloud not found")));
            game.nextPlayer();
        }
    }

    /**
     * Called to merge islands
     *
     * @throws IllegalStateException is thrown when it's not the right moment to merge the islands
     */
    public void request() throws IllegalStateException {
        isStateActivated();
        if (mergedIslands || !calculatedInfluence)
            throw new IllegalStateException("Cannot merge Islands now");
        states.get(ActionPhaseStateType.MERGE).handle();
        if (game.getTable().getIslandList().size() == 3) {
            this.getGame().setEndgame(true);
            this.getGame().setendplayer(game.searchPlayerWithMostTower());
        }
        mergedIslands = true;
        actualState++;
    }

    // Activate Card methods

    /**
     * Method which activates the wanted character card between the enabled ones
     *
     * @param characters the character represented by the character card
     * @param player     the player who makes the request
     * @throws NoSuchElementException    thrown when the requested card is not available for this game
     * @throws IllegalStateException     thrown when the actual context is not the right one to activate the card
     * @throws InvalidParameterException thrown when the player has not enough money to pay the card activation fee
     */
    public void activateCard(Characters characters, Player player)
            throws NoSuchElementException, IllegalStateException, InvalidParameterException {
        CharacterCard tmp = coreActivateCard(characters);
        ActionFaseState decorated = states.get(tmp.getCharacter().getType());
        tmp.activator(decorated, player);
        actualCard = tmp;
    }

    /**
     * Method which activates the wanted character card between the enabled ones
     * Activator which passes the needed teacher color for the correct functioning of the card
     *
     * @param characters the character represented by the character card
     * @param player     the player who makes the request
     * @param color      the needed teacher color
     * @throws NoSuchElementException    thrown when the requested card is not available for this game
     * @throws IllegalStateException     thrown when the actual context is not the right one to activate the card
     * @throws InvalidParameterException thrown when the player has not enough money to pay the card activation fee
     */
    public void activateCard(Characters characters, Player player, TeacherColor color)
            throws NoSuchElementException, IllegalStateException, InvalidParameterException {
        CharacterCard tmp = coreActivateCard(characters);
        ActionFaseState decorated = states.get(tmp.getCharacter().getType());
        tmp.activator(decorated, player, color);
        actualCard = tmp;
    }

    /**
     * Method which activates the wanted character card between the enabled cards
     * Activator which passes the needed island for the correct functioning of the card
     *
     * @param characters the character represented by the character card
     * @param player     the player who makes the request
     * @param island     the needed island
     * @throws NoSuchElementException    thrown when the requested card is not available for this game
     * @throws IllegalStateException     thrown when the actual context is not the right one to activate the card
     * @throws InvalidParameterException thrown when the player has not enough money to pay the card activation fee
     */
    public void activateCard(Characters characters, Player player, Island island)
            throws NoSuchElementException, IllegalStateException, InvalidParameterException {
        CharacterCard tmp = coreActivateCard(characters);
        ActionFaseState decorated = states.get(tmp.getCharacter().getType());
        tmp.activator(decorated, player, island);
        actualCard = tmp;
    }

    /**
     * Method which activates the wanted character card between the enabled cards
     *
     * @param characters the character represented by the character card
     * @throws NoSuchElementException thrown when the requested card is not available for this game
     * @throws IllegalStateException  thrown when the actual context is not the right one to activate the card
     */
    private CharacterCard coreActivateCard(Characters characters)
            throws NoSuchElementException, IllegalStateException {
        isStateActivated();
        isCardPlayable(characters);
        return characterCards.get(characters);
    }

    /**
     * Method which activates the wanted character card between the enabled cards
     *
     * @throws IllegalStateException thrown when the actual context is not the right one to activate the card
     */
    private void isCardPlayable(Characters character) throws IllegalStateException {
        controlExpertVariant();
        if (getActualCard().isPresent())
            throw new IllegalStateException("Character Card already chosen");
        if (character.getType().getOrderPlace() > actualState) {
            throw new IllegalStateException("The round has progressed too much to play this card");
        }
        if (!characterCards.containsKey(character)) {
            throw new IllegalStateException("Requested card is not in this play");
        }
    }

    /**
     * Simple getter of the game
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Controls if, chosen a character, if it's card is playable
     *
     * @param character the interesting character
     * @return true if it is, else false
     */
    public boolean canBeActivated(Characters character) {
        return characterCards.containsKey(character);
    }

    public Optional<Characters> getActualCharacter() {
        return getActualCard().map(CharacterCard::getCharacter);
    }

    public Optional<StudentsManager> getCardMemory(Characters character) {
        return characterCards.get(character).getStudentContainer();
    }

    public void giveNoEntryTileBack() {
        controlExpertVariant();
        InfluenceCard noEntryCard = (InfluenceCard) characterCards.entrySet().stream()
                .filter(x -> x.getValue().getCharacterization("NoEntrySetter") > 0)
                .findAny()
                .orElseThrow(() -> new IllegalStateException("No in game card holds no entry tiles"))
                .getValue();
        noEntryCard.giveNoEntryBack();
    }

    public boolean isActivated() {
        return activated;
    }

    private void isStateActivated() throws IllegalStateException {
        if (!isActivated()) throw new IllegalStateException("Action Phase is not Activated");
    }

    public void reset() {
        activated = false;
    }

    public void setCalculatedInfluence(boolean calculatedInfluence) {
        this.calculatedInfluence = calculatedInfluence;
    }

    public void setChosenCloud(boolean chosenCloud) {
        this.chosenCloud = chosenCloud;
    }

    public void setPossibleStudentMovements(int possibleStudentMovements) {
        this.possibleStudentMovements = possibleStudentMovements;
    }

    public void setMovedMotherNature(boolean movedMotherNature) {
        this.movedMotherNature = movedMotherNature;
    }

    public void setMergedIslands(boolean mergedIslands) {
        this.mergedIslands = mergedIslands;
    }

    public void controlExpertVariant() throws IllegalStateException {
        if (!isExpertVariant())
            throw new IllegalStateException("Game is not in expert variant");
    }

    public boolean isExpertVariant() {
        return expertVariant;
    }

    public Optional<CharacterCard> getActualCard() {
        return Optional.ofNullable(actualCard);
    }

    public void controlActualCard() throws IllegalStateException {
        if (getActualCard().isEmpty()) {
            throw new IllegalStateException("There is no activated card");
        }
    }

    public void setActualState(int state) {
        this.actualState = state;
    }

}
