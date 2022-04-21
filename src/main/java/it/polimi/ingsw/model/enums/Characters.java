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

    public static CharacterCardType getClassOfCard(Characters type){
        return switch (type) {
            case HOST, FRIAR, QUEEN, THIEF, JESTER, MINSTREL -> CharacterCardType.STUDENT;
            case MESSENGER -> CharacterCardType.MOTHER;
            case SORCERESS, SORCERER, CENTAUR, KNIGHT, CRIER -> CharacterCardType.INFLUENCE;
        };
    }
}

