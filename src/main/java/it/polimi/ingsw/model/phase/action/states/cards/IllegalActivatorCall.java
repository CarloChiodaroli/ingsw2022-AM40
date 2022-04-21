package it.polimi.ingsw.model.phase.action.states.cards;

public class IllegalActivatorCall extends RuntimeException{

    public IllegalActivatorCall(String message){
        super(message);
    }
}
