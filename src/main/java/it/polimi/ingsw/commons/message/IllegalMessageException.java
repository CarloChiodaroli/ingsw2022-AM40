package it.polimi.ingsw.commons.message;

public class IllegalMessageException extends RuntimeException{

    public IllegalMessageException(){
        super();
    }

    public IllegalMessageException(String message){
        super(message);
    }
}
