package it.polimi.ingsw.server.enums;

/**
 * Action Phase states are of five types, this enum sums up these types.
 * Useful out of the game model to keep track of what Character cards can be activated when.
 */
public enum ActionPhaseStateType {
    STUDENT(0),
    MOTHER(1),
    INFLUENCE(2),
    MERGE(3),
    CLOUD(4);

    private final int orderPlace;


    ActionPhaseStateType(int orderPlace) {
        this.orderPlace = orderPlace;
    }

    public int getOrderPlace() {
        return orderPlace;
    }


}
