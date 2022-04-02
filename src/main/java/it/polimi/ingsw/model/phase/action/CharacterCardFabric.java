package it.polimi.ingsw.model.phase.action;

import java.util.ArrayList;
import java.util.List;

public class CharacterCardFabric {

    public static List<CharacterCard> getCards(ActionFase actionFase) {
        List<CharacterCard> enabledCharacterCards = new ArrayList<>();
        while(enabledCharacterCards.size() < 3) {
            Character character = Character.getRandomCharacter();
            if (!alreadyCreated(character, enabledCharacterCards))
                enabledCharacterCards.add(CharacterCardFabric.createCard(character, actionFase));
        }
        return enabledCharacterCards;
    }

    private static boolean alreadyCreated(Character character, List<CharacterCard> existent){
        for(CharacterCard card: existent){
            if(card.getCharacter().equals(character)) return true;
        }
        return false;
    }

    public static CharacterCard createCard(Character type, ActionFase actionFase){
        String classOfCard = Character.getClassOfCard(type);
        return switch (classOfCard) {
            case "StudentMovement"-> new StudentMovementCard(type, actionFase, Character.getCharacterization(type));
            case "MotherNature" -> new MotherNatureCard(type, actionFase, Character.getCharacterization(type));
            case "Influence" -> new InfluenceCard(type, actionFase, Character.getCharacterization(type));
            default -> null;
        };
    }
}
