package it.polimi.ingsw.model.phase.action;

import java.util.ArrayList;
import java.util.List;

public class CharacterCardFabric {

    public static List<CharacterCard> getCards(ActionFase actionFase) {
        List<CharacterCard> enabledCharacterCards = new ArrayList<>();
        while(enabledCharacterCards.size() < 3) {
            Characters characters = Characters.getRandomCharacter();
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
        String classOfCard = Characters.getClassOfCard(type);
        return switch (classOfCard) {
            case "StudentMovement"-> new StudentMovementCard(type, actionFase, Characters.getCharacterization(type));
            case "MotherNature" -> new MotherNatureCard(type, actionFase, Characters.getCharacterization(type));
            case "Influence" -> new InfluenceCard(type, actionFase, Characters.getCharacterization(type));
            default -> null;
        };
    }
}
