package it.polimi.ingsw.server.enums;

import it.polimi.ingsw.commons.enums.Characters;

import java.util.HashMap;
import java.util.Map;

public class CardCharacterizations {

    public static Map<Characters, Map<String, Integer>> getMap(){
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

    public static Map<String, Integer> particular(Characters characters){
        return getMap().get(characters);
    }
}
