package it.polimi.ingsw.model.enums;

public enum Characters {
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

    public static String getClassOfCard(Characters type){
        return switch (type) {
            case HOST, FRIAR, QUEEN, THIEF, JESTER, MINSTREL -> "StudentMovement";
            case MESSENGER -> "MotherNature";
            case SORCERESS, SORCERER, CENTAUR, KNIGHT, CRIER -> "Influence";
        };
    }
}

