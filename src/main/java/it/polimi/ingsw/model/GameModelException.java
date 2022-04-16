package it.polimi.ingsw.model;

public class GameModelException extends RuntimeException{

    public GameModelException(String message){
        super("Model - " + message);
    }
}
