package it.polimi.ingsw.server.model.phase.action.states.cards;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.server.enums.ActionPhaseStateType;
import it.polimi.ingsw.server.enums.CardCharacterizations;
import it.polimi.ingsw.server.enums.CharactersLookup;
import it.polimi.ingsw.server.model.phase.action.ActionPhase;
import it.polimi.ingsw.server.model.phase.action.states.CharacterCard;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Class used while creating the Action phase to draw the three character cards at the start of the play, and fabricates them.
 */
public abstract class CharacterCardFabric {

    private final static Map<Characters, Map<String, Integer>> particularities = allParticularities();

    /**
     * Get character cards of the game
     *
     * @param actionPhase action phase of the game
     * @return a map witch for each card the character
     */
    public static Map<Characters, CharacterCard> getCards(ActionPhase actionPhase) {
        Map<Characters, CharacterCard> enabledCharacterCards = new HashMap<>();
        while (enabledCharacterCards.size() < 3) {
            Characters characters = getRandomCharacter();
            if (!enabledCharacterCards.containsKey(characters)) {
                enabledCharacterCards.put(characters, createCard(characters, actionPhase));
            }
        }
        return enabledCharacterCards;
    }

    /**
     * Create the character cards
     *
     * @param type        characterization of the card
     * @param actionPhase action phase of the game
     * @return a card with the required character
     */
    public static CharacterCard createCard(Characters type, ActionPhase actionPhase) {
        Map<ActionPhaseStateType, CharacterCard> possibleCards = new HashMap<>();
        possibleCards.put(ActionPhaseStateType.STUDENT, new StudentMovementCard(type, actionPhase, getCharacterization(type)));
        possibleCards.put(ActionPhaseStateType.MOTHER, new MotherNatureCard(type, actionPhase, getCharacterization(type)));
        possibleCards.put(ActionPhaseStateType.INFLUENCE, new InfluenceCard(type, actionPhase, getCharacterization(type)));
        return possibleCards.get(CharactersLookup.getType(type));
    }

    /**
     * Get a random character
     *
     * @return the caught character
     */
    public static Characters getRandomCharacter() {
        Random random = new Random();
        int range = Characters.values().length;
        return Characters.values()[random.nextInt(range)];
    }

    /**
     * Create the characterizations
     *
     * @return a map witch the character and an util int
     */
    private static Map<String, Integer> createBaseCharacterization() {
        Map<String, Integer> result = new HashMap<>();

        result.put("Price", 0); // Card Price
        result.put("Memory", 0); // Card memory size
        result.put("Usages", 1); // How many times the card is used
        result.put("Bidirectional", 0); // Has a bidirectional behaviour
        result.put("TeacherBehaviour", 0); // Changes behaviour of the movement of teachers
        result.put("EffectAllPlayers", 0); // Has effect to all players in this turn
        result.put("NoEntrySetter", 0); // Defines if it needs to set no entry tile

        // things the card works with
        result.put("Island", 0);
        result.put("Player", 0);
        result.put("Room", 0);
        result.put("Tower", 0);
        result.put("Student", 0);
        result.put("Entrance", 0);

        return result;
    }

    /**
     * Getter of all particular characterizations of every single character
     */
    private static Map<Characters, Map<String, Integer>> allParticularities() {
        return CardCharacterizations.getMap();
    }

    /**
     * Getter of a particular characterization
     *
     * @param characters the character of which we want the characterization
     * @return the characterization map
     */
    public static Map<String, Integer> getCharacterization(Characters characters) {
        Map<String, Integer> result = createBaseCharacterization();
        particularities.get(characters).forEach(result::replace);
        return result;
    }
}
