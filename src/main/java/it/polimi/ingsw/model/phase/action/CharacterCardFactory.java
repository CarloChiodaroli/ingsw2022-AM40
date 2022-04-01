package it.polimi.ingsw.model.phase.action;

import java.util.ArrayList;
import java.util.List;

public class CharacterCardFactory {

    private final List<CharacterCard> enabledCharacterCards;

    public CharacterCardFactory(){
        this.enabledCharacterCards = new ArrayList<>();
        /*while(this.enabledCharacterCards.size() < 3){ // commented for making action phase testing
            Character character = Character.getRandomCharacter();
            if(!alreadyCreated(character))
                this.enabledCharacterCards.add(CharacterCardFactory.createCard(character));
        }*/
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

    /*public static CharacterCard createCard(Character type, ActionFase actionFase){
        switch(type){
            case HOST:
                return new StudentMovementCard(2, Character.HOST, actionFase);
            case CRIER:
                return new InfluenceCard(3, Character.CRIER, actionFase);
            case FRIAR:
                return new StudentMovementCard(1, Character.FRIAR, actionFase);
            case QUEEN:
                return new StudentMovementCard(2, Character.QUEEN, actionFase);
            case THIEF:
                return new StudentMovementCard(3, Character.THIEF, actionFase);
            case JESTER:
                return new StudentMovementCard(1, Character.JESTER, actionFase);
            case KNIGHT:
                return new InfluenceCard(2, Character.KNIGHT, actionFase);
            case CENTAUR:
                return new InfluenceCard(3, Character.CENTAUR, actionFase);
            case MINSTREL:
                return new StudentMovementCard(1, Character.MINSTREL, actionFase);
            case SORCERER:
                return new InfluenceCard(3, Character.SORCERER, actionFase);
            case MESSENGER:
                return new MotherNatureCard(1, Character.MESSENGER, actionFase);
            case SORCERESS:
                return new InfluenceCard(2, Character.SORCERESS, actionFase);
            default:
                return null;
        }
    }*/
}
