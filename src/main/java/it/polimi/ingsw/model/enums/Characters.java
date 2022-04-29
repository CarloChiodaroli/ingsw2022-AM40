package it.polimi.ingsw.model.enums;

public enum Characters {

    // The characters are listed as in the player's handbook
    FRIAR(ActionPhaseStateType.STUDENT),
    HOST(ActionPhaseStateType.STUDENT),
    CRIER(ActionPhaseStateType.INFLUENCE),
    MESSENGER(ActionPhaseStateType.MOTHER),
    SORCERESS(ActionPhaseStateType.INFLUENCE),
    CENTAUR(ActionPhaseStateType.INFLUENCE),
    JESTER(ActionPhaseStateType.STUDENT),
    KNIGHT(ActionPhaseStateType.INFLUENCE),
    SORCERER(ActionPhaseStateType.INFLUENCE),
    MINSTREL(ActionPhaseStateType.STUDENT),
    QUEEN(ActionPhaseStateType.STUDENT),
    THIEF(ActionPhaseStateType.STUDENT);

    private final ActionPhaseStateType type;


    Characters(ActionPhaseStateType type){
        this.type = type;
    }

    public ActionPhaseStateType getType(){
        return type;
    }
}

