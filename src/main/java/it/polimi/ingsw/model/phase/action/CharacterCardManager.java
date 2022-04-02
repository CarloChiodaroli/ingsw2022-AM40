package it.polimi.ingsw.model.phase.action;

import java.util.ArrayList;
import java.util.List;

public class CharacterCardManager {

    private final List<CharacterCard> enabledCharacterCards;

    public CharacterCardManager(ActionFase actionFase){
        this.enabledCharacterCards = new ArrayList<>();
        while(this.enabledCharacterCards.size() < 3){ // commented for making action phase testing
            Character character = Character.getRandomCharacter();
            if(!alreadyCreated(character))
                this.enabledCharacterCards.add(CharacterCardManager.createCard(character, actionFase));
        }
    }

    public List<CharacterCard> getCards() {
        return enabledCharacterCards;
    }

    private boolean alreadyCreated(Character character){
        for(CharacterCard card: enabledCharacterCards){
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
