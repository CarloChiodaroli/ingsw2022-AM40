package it.polimi.ingsw.server.model.enums;


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
