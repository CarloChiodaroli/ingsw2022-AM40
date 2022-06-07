package it.polimi.ingsw.server.model.enums;

import it.polimi.ingsw.commons.enums.Characters;

import java.util.HashMap;
import java.util.Map;

public class CharactersLookup {

    private static final Map<Characters, ActionPhaseStateType> map = new HashMap<>() {{
        put(Characters.FRIAR, ActionPhaseStateType.STUDENT);
        put(Characters.HOST, ActionPhaseStateType.STUDENT);
        put(Characters.CRIER, ActionPhaseStateType.INFLUENCE);
        put(Characters.MESSENGER, ActionPhaseStateType.MOTHER);
        put(Characters.SORCERESS, ActionPhaseStateType.INFLUENCE);
        put(Characters.CENTAUR, ActionPhaseStateType.INFLUENCE);
        put(Characters.JESTER, ActionPhaseStateType.STUDENT);
        put(Characters.KNIGHT, ActionPhaseStateType.INFLUENCE);
        put(Characters.SORCERER, ActionPhaseStateType.INFLUENCE);
        put(Characters.MINSTREL, ActionPhaseStateType.STUDENT);
        put(Characters.QUEEN, ActionPhaseStateType.STUDENT);
        put(Characters.THIEF, ActionPhaseStateType.STUDENT);
    }};

    public static ActionPhaseStateType getType(Characters character) {
        return map.get(character);
    }
}
