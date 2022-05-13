package it.polimi.ingsw.server.model;

public class GameModelException extends RuntimeException{

    public GameModelException(String message){
        super("Model - " + message);
    }
}
