package it.polimi.ingsw.model.phase.action;

import java.util.Random;

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
}
