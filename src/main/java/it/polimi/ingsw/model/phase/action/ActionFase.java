package it.polimi.ingsw.model.phase.action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;
import it.polimi.ingsw.model.phase.action.states.*;
import it.polimi.ingsw.model.phase.action.states.CharacterCard;
import it.polimi.ingsw.model.phase.action.states.cards.CharacterCardFabric;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.model.table.MotherNature;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Manages the game's Action Phase in normal or in Expert Variant
 */
public class ActionFase {

    // Variables
    private final Game game;
    private final List<ActionFaseState> states;
    private boolean activated;

    // Non expert Status variables
    private int possibleStudentMovements;
    private boolean movedMotherNature;
    private boolean calculatedInfluence;
    private boolean mergedIslands;
    private boolean chosenCloud;

    // Expert variant attributes
    private final boolean expertVariant;
    private CharacterCard actualCard;
    private final List<CharacterCard> characterCards;

    /**
     * Constructor
     *
     * @param game the game in which the phase is used
     */
    public ActionFase(Game game) {
        this.game = game;
        this.expertVariant = game.isExpertVariant();
        this.activated = false;

        this.states = new ArrayList<>();
        this.states.add(new StudentMovement(this));
        this.states.add(new MotherNatureState(this));
        this.states.add(new Influence(this));
        this.states.add(new MergeIsland(this));
        this.states.add(new Finalize(this));
        if (expertVariant) {
            this.characterCards = CharacterCardFabric.getCards(this);
        } else {
            this.characterCards = null;
        }
    }

    /**
     * Starts a new Action phase.
     * Since, during a play, the action phase needs to keep memory of what happens,
     * this method is needed to reset the phase's state every time a player starts his phase in the round
     *
     * @param player the player who wants to start his action phase
     */
    public void startPhase(Player player) {
        if (!chosenCloud) return;
        if (!game.getPianificationFase().getActualPlayer().equals(player)) return;
        // reset of action phase state
        activated = true;
        possibleStudentMovements = 3;
        movedMotherNature = false;
        calculatedInfluence = false;
        mergedIslands = false;
        chosenCloud = false;

        if (expertVariant) {
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
        if (expertVariant && actualCard.isInUse()) {
            actualCard.handle(teacherColor, from, to);
        } else {
            states.get(0).handle(teacherColor, from, to);
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
        int maxHops = game.getPianificationFase().getMotherNatureHops(player);
        if (!expertVariant) {
            states.get(1).handle(player, motherNatureHops, maxHops);
        } else {
            if(actualCard.isInUse() &&
                CharacterCardFabric.getClassOfCard(actualCard.getCharacter()).equals("MotherNature")){
                actualCard.handle(player, motherNatureHops, maxHops);
            } else {
                states.get(1).handle(player, motherNatureHops, maxHops);
            }
        }
        movedMotherNature = true;
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
                    CharacterCardFabric.getClassOfCard(actualCard.getCharacter()).equals("Influence"))
                actualCard.handle(player, MotherNature.getMotherNature().getPosition().get());
            else states.get(2).handle(player, MotherNature.getMotherNature().getPosition().get());
            calculatedInfluence = true;
        } else {
            if (!calculatedInfluence || chosenCloud)
                throw new IllegalStateException("Cannot choose clouds now");
            states.get(4).handle(player, game.getTable().getCloudById(id)
                    .orElseThrow(() -> new NoSuchElementException("Cloud not found")));
            chosenCloud = true;
            activated = false;
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
        states.get(3).handle();
        if (game.getTable().getIslandList().size() == 3) {
            this.getGame().setEndgame(true);
            this.getGame().setendplayer(game.searchPlayerWithMostTower());
        }
        mergedIslands = true;
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
        if (!expertVariant) throw new IllegalStateException("Game is not in expert variant");
        if (actualCard == null) throw new IllegalStateException("No card has been activated");
        if (actualCard.isInUse()) {
            actualCard.handle(player, studentA, studentB);
        } else {
            throw new IllegalStateException("Card has been already used");
        }
        possibleStudentMovements--;
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
        actualCard = coreActivateCard(characters);
        actualCard.activator(player);
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
        actualCard = coreActivateCard(characters);
        actualCard.activator(player, color);
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
        actualCard = coreActivateCard(characters);
        actualCard.activator(player, island);
    }

    /**
     * Method which activates the wanted character card between the enabled cards
     * Activator which passes the needed tower color for the correct functioning of the card
     *
     * @param characters the character represented by the character card
     * @param player     the player who makes the request
     * @param color      the needed tower color
     * @throws NoSuchElementException    thrown when the requested card is not available for this game
     * @throws IllegalStateException     thrown when the actual context is not the right one to activate the card
     * @throws InvalidParameterException thrown when the player has not enough money to pay the card activation fee
     */
    public void activateCard(Characters characters, Player player, TowerColor color)
            throws NoSuchElementException, IllegalStateException, InvalidParameterException {
        actualCard = coreActivateCard(characters);
        actualCard.activator(player, color);
    }

    /**
     * Method which activates the wanted character card between the enabled cards
     *
     * @param characters the character represented by the character card
     * @throws NoSuchElementException    thrown when the requested card is not available for this game
     * @throws IllegalStateException     thrown when the actual context is not the right one to activate the card
     */
    private CharacterCard coreActivateCard(Characters characters)
            throws NoSuchElementException, IllegalStateException {
        isStateActivated();
        isCardPlayable(characters);
        return characterCards.stream()
                .filter(card -> card.getCharacter().equals(characters))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("Requested card is not playable"));

    }

    /**
     * Method which activates the wanted character card between the enabled cards
     *
     * @throws IllegalStateException thrown when the actual context is not the right one to activate the card
     */
    private void isCardPlayable(Characters character) throws IllegalStateException {
        if (!expertVariant)
            throw new IllegalStateException("This is not an expert variant game");
        if (actualCard != null)
            throw new IllegalStateException("Character Card already chosen");
        switch (CharacterCardFabric.getClassOfCard(character)) {
            case "StudentMovement" -> {
                if (possibleStudentMovements < 0 || movedMotherNature)
                    throw new IllegalStateException("The round has progressed too much to play this card");
            }
            case "MotherNature" -> {
                if (movedMotherNature)
                    throw new IllegalStateException("The round has progressed too much to play this card");
            }
            case "Influence" -> {
                if (calculatedInfluence)
                    throw new IllegalStateException("The round has progressed too much to play this card");
            }
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
     * @param character the intresting character
     * @return true if it is, else false
     */
    public boolean canBeActivated(Characters character) {
        return characterCards.stream()
                .anyMatch(card -> card.getCharacter().equals(character));
    }

    public Optional<Characters> getActualCharacter(){
        if(actualCard == null) return Optional.empty();
        return Optional.of(actualCard.getCharacter());
    }

    public Optional<StudentsManager> getCardMemory(Characters character){
        return characterCards.stream()
                .filter(card -> card.getCharacter().equals(character))
                .findAny()
                .orElseThrow(() -> new InvalidParameterException("Card not in game"))
                .getStudentContainer();
    }

    public boolean isActivated(){
        return activated;
    }

    private void isStateActivated() throws IllegalStateException{
        if(!isActivated()) throw new IllegalStateException("Action Phase is not Activated");
    }
}
