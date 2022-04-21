package it.polimi.ingsw.model.phase.action.states.cards;

import it.polimi.ingsw.model.enums.CharacterCardType;
import it.polimi.ingsw.model.phase.action.ActionFase;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.phase.action.states.CharacterCard;

import java.util.*;

public class CharacterCardFabric {

    public static List<CharacterCard> getCards(ActionFase actionFase) {
        List<CharacterCard> enabledCharacterCards = new ArrayList<>();
        while(enabledCharacterCards.size() < 3) {
            Characters characters = getRandomCharacter();
            if (!alreadyCreated(characters, enabledCharacterCards))
                enabledCharacterCards.add(CharacterCardFabric.createCard(characters, actionFase));
        }
        return enabledCharacterCards;
    }

    private static boolean alreadyCreated(Characters characters, List<CharacterCard> existent){
        for(CharacterCard card: existent){
            if(card.getCharacter().equals(characters)) return true;
        }
        return false;
    }

    public static CharacterCard createCard(Characters type, ActionFase actionFase){
        CharacterCardType classOfCard = getClassOfCard(type);
        return switch (classOfCard) {
            case STUDENT-> new StudentMovementCard(type, actionFase, getCharacterization(type));
            case MOTHER -> new MotherNatureCard(type, actionFase, getCharacterization(type));
            case INFLUENCE -> new InfluenceCard(type, actionFase, getCharacterization(type));
        };
    }

    public static Characters getRandomCharacter(){
        Random random = new Random();
        int range = Characters.values().length;
        return Characters.values()[random.nextInt(range)];
    }

    private static Map<String, Integer> createBaseCharacterization(){
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
        result.put("Room",0);
        result.put("Tower",0);
        result.put("Student",0);
        result.put("Entrance",0);

        return result;
    }

    public static Map<String, Integer> getCharacterization(Characters characters){
        Map<String, Integer> result = createBaseCharacterization();
        switch(characters){
            case HOST -> {
                result.replace("Price", 2);
                result.replace("Usages", 3); // How many times is the card used
                result.replace("TeacherBehaviour", 1);
                return result;
            }
            case FRIAR -> {
                result.replace("Price", 1);
                result.replace("Memory", 4);
                result.replace("Usages", 1);
                result.replace("Island", 1);
                return result;
            }
            case QUEEN -> {
                result.replace("Price", 2);
                result.replace("Memory", 4);
                result.replace("Usages", 1);
                result.replace("Room", 1);
                result.replace("Player", 1);
                return result;
            }
            case THIEF -> {
                result.replace("Price", 3);
                result.replace("EffectAllPlayers", 1);
                return result;
            }
            case JESTER -> {
                result.replace("Price", 1);
                result.replace("Memory", 6);
                result.replace("Usages", 3);
                result.replace("Bidirectional", 1);
                result.replace("Entrance", 1);
                result.replace("Student", 2);
                result.replace("Player", 1);
                return result;
            }
            case MINSTREL -> {
                result.replace("Price", 1);
                result.replace("Usages", 2);
                result.replace("Bidirectional", 1);
                result.replace("Player", 1);
                result.replace("Room", 1);
                result.replace("Entrance", 1);
                result.replace("Student", 2);
                return result;
            }
            case MESSENGER -> {
                result.replace("Price", 1);
                return result;
            }
            case CRIER -> {
                result.replace("Price", 3);
                result.replace("Island", 1);
                return result;
            }
            case KNIGHT -> {
                result.replace("Price", 2);
                result.replace("Player", 2);
                return result;
            }
            case CENTAUR -> {
                result.replace("Price", 3);
                result.replace("Tower", 1);
                return result;
            }
            case SORCERER -> {
                result.replace("Price", 3);
                result.replace("Student", 1);
                return result;
            }
            case SORCERESS -> {
                result.replace("Price", 2);
                result.replace("Memory", 4);
                result.replace("Island", 1);
                result.replace("NoEntrySetter", 1);
                return result;
            }
            default -> {
                return result;
            }
        }
    }

    public static CharacterCardType getClassOfCard(Characters type){
        return Characters.getClassOfCard(type);
    }
}
