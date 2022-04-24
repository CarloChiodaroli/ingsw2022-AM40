package it.polimi.ingsw.model.enums;

public enum CharacterCardType {
    STUDENT, MOTHER, INFLUENCE;

    public static int getEquivalentInt(CharacterCardType ch) {
        return switch (ch) {
            case STUDENT -> 0;
            case MOTHER -> 1;
            case INFLUENCE -> 2;
        };
    }
}
