package it.polimi.ingsw.network.Message;

public class IllegalMessageException extends RuntimeException{

    public IllegalMessageException(){
        super();
    }

    public IllegalMessageException(String message){
        super(message);
    }
}
