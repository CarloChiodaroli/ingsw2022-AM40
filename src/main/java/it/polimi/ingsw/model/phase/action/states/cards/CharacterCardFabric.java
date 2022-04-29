package it.polimi.ingsw.model.phase.action.states.cards;

import it.polimi.ingsw.model.enums.ActionPhaseStateType;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.phase.action.ActionPhase;
import it.polimi.ingsw.model.phase.action.states.CharacterCard;

import java.util.*;

public class CharacterCardFabric {

    private final static Map<Characters, Map<String, Integer>> particularities = allParticularities();

    public static List<CharacterCard> getCards(ActionPhase actionPhase) {
        List<CharacterCard> enabledCharacterCards = new ArrayList<>();
        while (enabledCharacterCards.size() < 3) {
            Characters characters = getRandomCharacter();
            if (!alreadyCreated(characters, enabledCharacterCards))
                enabledCharacterCards.add(CharacterCardFabric.createCard(characters, actionPhase));
        }
        return enabledCharacterCards;
    }

    private static boolean alreadyCreated(Characters characters, List<CharacterCard> existent) {
        for (CharacterCard card : existent) {
            if (card.getCharacter().equals(characters)) return true;
        }
        return false;
    }

    public static CharacterCard createCard(Characters type, ActionPhase actionPhase) {
        List<CharacterCard> possibleCards = new ArrayList<>();
        possibleCards.add(ActionPhaseStateType.STUDENT.getOrderPlace(), new StudentMovementCard(type, actionPhase, getCharacterization(type)));
        possibleCards.add(ActionPhaseStateType.MOTHER.getOrderPlace(), new MotherNatureCard(type, actionPhase, getCharacterization(type)));
        possibleCards.add(ActionPhaseStateType.INFLUENCE.getOrderPlace(), new InfluenceCard(type, actionPhase, getCharacterization(type)));
        return possibleCards.get(type.getType().getOrderPlace());
    }

    public static Characters getRandomCharacter() {
        Random random = new Random();
        int range = Characters.values().length;
        return Characters.values()[random.nextInt(range)];
    }

    private static Map<String, Integer> createBaseCharacterization() {
        Map<String, Integer> result = new HashMap<>();

        result.put("Price", 0); // Card Price
        result.put("Memory", 0); // Card memory size
        result.put("Usages", 1); // How many times the card is used
        result.put("Bidirectional", 0); // Has a bidirectional behaviour
        result.put("TeacherBehaviour", 0); // Changes behaviour of the movement of teachers
        result.put("EffectAllPlayers", 0); // Has effect to all players in this turn
        result.put("NoEntrySetter", 0); // Defines if it needs to set no entry tile
        // On how many things the card works on
        result.put("Island", 0);
        result.put("Player", 0);
        result.put("Room", 0);
        result.put("Tower", 0);
        result.put("Student", 0);
        result.put("Entrance", 0);

        return result;
    }

    private static Map<Characters, Map<String, Integer>> allParticularities(){
        Map<Characters, Map<String, Integer>> allCharacterizations = new HashMap<>();

        for (Characters value : Characters.values()) {
            allCharacterizations.put(value, new HashMap<>());
        }

        allCharacterizations.get(Characters.FRIAR).put("Price", 1);
        allCharacterizations.get(Characters.FRIAR).put("Memory", 4);
        allCharacterizations.get(Characters.FRIAR).put("Usages", 1);
        allCharacterizations.get(Characters.FRIAR).put("Island", 1);

        allCharacterizations.get(Characters.HOST).put("Price", 2);
        allCharacterizations.get(Characters.HOST).put("Usages", 3);
        allCharacterizations.get(Characters.HOST).put("TeacherBehaviour", 1);

        allCharacterizations.get(Characters.CRIER).put("Price", 3);
        allCharacterizations.get(Characters.CRIER).put("Island", 1);

        allCharacterizations.get(Characters.MESSENGER).put("Price", 1);

        allCharacterizations.get(Characters.SORCERESS).put("Price", 2);
        allCharacterizations.get(Characters.SORCERESS).put("Memory", 4);
        allCharacterizations.get(Characters.SORCERESS).put("Island", 1);
        allCharacterizations.get(Characters.SORCERESS).put("NoEntrySetter", 1);

        allCharacterizations.get(Characters.CENTAUR).put("Price", 3);
        allCharacterizations.get(Characters.CENTAUR).put("Tower", 1);

        allCharacterizations.get(Characters.JESTER).put("Price", 1);
        allCharacterizations.get(Characters.JESTER).put("Memory", 6);
        allCharacterizations.get(Characters.JESTER).put("Usages", 3);
        allCharacterizations.get(Characters.JESTER).put("Bidirectional", 1);
        allCharacterizations.get(Characters.JESTER).put("Entrance", 1);
        allCharacterizations.get(Characters.JESTER).put("Student", 2);
        allCharacterizations.get(Characters.JESTER).put("Player", 1);

        allCharacterizations.get(Characters.KNIGHT).put("Price", 2);
        allCharacterizations.get(Characters.KNIGHT).put("Player", 2);

        allCharacterizations.get(Characters.SORCERER).put("Price", 3);
        allCharacterizations.get(Characters.SORCERER).put("Student", 1);

        allCharacterizations.get(Characters.MINSTREL).put("Price", 1);
        allCharacterizations.get(Characters.MINSTREL).put("Usages", 2);
        allCharacterizations.get(Characters.MINSTREL).put("Bidirectional", 1);
        allCharacterizations.get(Characters.MINSTREL).put("Player", 1);
        allCharacterizations.get(Characters.MINSTREL).put("Room", 1);
        allCharacterizations.get(Characters.MINSTREL).put("Entrance", 1);
        allCharacterizations.get(Characters.MINSTREL).put("Student", 2);

        allCharacterizations.get(Characters.QUEEN).put("Price", 2);
        allCharacterizations.get(Characters.QUEEN).put("Memory", 4);
        allCharacterizations.get(Characters.QUEEN).put("Usages", 1);
        allCharacterizations.get(Characters.QUEEN).put("Room", 1);
        allCharacterizations.get(Characters.QUEEN).put("Player", 1);

        allCharacterizations.get(Characters.THIEF).put("Price", 3);
        allCharacterizations.get(Characters.THIEF).put("EffectAllPlayers", 1);

        return allCharacterizations;
    }

    public static Map<String, Integer> getCharacterization(Characters characters) {
        Map<String, Integer> result = createBaseCharacterization();
        particularities.get(characters).forEach(result::replace);
        return result;
    }
}
