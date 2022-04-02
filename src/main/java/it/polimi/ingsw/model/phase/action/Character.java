package it.polimi.ingsw.model.phase.action;

import java.util.*;

public enum Character {
    FRIAR,
    HOST,
    CRIER,
    MESSENGER,
    SORCERESS,
    CENTAUR,
    JESTER,
    KNIGHT,
    SORCERER,
    MINSTREL,
    QUEEN,
    THIEF;

    public static Character getRandomCharacter(){
        Random random = new Random();
        int range = Character.values().length;
        return Character.values()[random.nextInt(range)];
    }

    private static Map<String, Integer> createBaseCharacterization(){
        Map<String, Integer> result = new HashMap<>();
        result.put("Price", 0); // Card Price
        result.put("Memory", 0); // Card memory size
        result.put("Usages", 0); // How many times the card is used
        result.put("Bidirectional", 0); // Has a bidirectional behaviour
        result.put("AddToMaxHops", 0); // How many hops to add to mother nature movement
        result.put("DecoratesNoExpert", 0); // Changes behaviour of the no expert art of the action phase
        result.put("TeacherBehaviour", 0); // Changes behaviour of the movement of teachers
        result.put("EffectAllPlayers", 0); // Has effect to all players in this tur

        // How many things the card works on
        result.put("Island", 0);
        result.put("Player", 0);
        result.put("Room",0);
        result.put("Tower",0);
        result.put("Student",0);
        result.put("Entrance",0);

        return result;
    }

    public static Map<String, Integer> getCharacterization(Character character){
        Map<String, Integer> result = createBaseCharacterization();
        switch(character){
            case HOST -> {
                result.replace("Price", 2);
                result.replace("Usages", 3); // How many times is the card used
                result.replace("DecorateNoExpert", 3);
                result.replace("TeacherBehaviour", 3);
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
                return result;
            }
            case MINSTREL -> {
                result.replace("Price", 1);
                result.replace("Usages", 2);
                result.replace("Bidirectional", 1);
                result.replace("Room", 1);
                result.replace("Entrance", 1);
                result.replace("Student", 1);
                return result;
            }
            case MESSENGER -> {
                result.replace("Price", 1);
                result.replace("AddToMaxHops", 2);
                return result;
            }
            case CRIER -> {
                result.replace("Price", 3);
                result.replace("Island", 1);
                return result;
            }
            case KNIGHT -> {
                result.replace("Price", 2);
                result.replace("Player", 1);
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
                return result;
            }
            default -> {
                return result;
            }
        }
    }

    public static String getClassOfCard(Character type){
        return switch (type) {
            case HOST, FRIAR, QUEEN, THIEF, JESTER, MINSTREL -> "StudentMovement";
            case MESSENGER -> "MotherNature";
            case SORCERESS, SORCERER, CENTAUR, KNIGHT, CRIER -> "Influence";
        };
    }
}
