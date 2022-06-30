package it.polimi.ingsw.server.enums;

/**
 * Action Phase states are of five types, this enum sums up these types.
 * Useful out of the game model to keep track of what Character cards can be activated when.
 */
public enum ActionPhaseStateType {
    /**
     * For Student movement state types
     */
    STUDENT(0),
    /**
     * For Mother nature movement state types
     */
    MOTHER(1),
    /**
     * For Influence calc state types
     */
    INFLUENCE(2),
    /**
     * For Merge island state types
     */
    MERGE(3),
    /**
     * For Finalizing state types
     */
    CLOUD(4);

    private final int orderPlace;

    /**
     * Constructor
     *
     * @param orderPlace the place in the execution queue
     */
    ActionPhaseStateType(int orderPlace) {
        this.orderPlace = orderPlace;
    }

    /**
     * Getter
     *
     * @return the place in the execution queue
     */
    public int getOrderPlace() {
        return orderPlace;
    }


}
