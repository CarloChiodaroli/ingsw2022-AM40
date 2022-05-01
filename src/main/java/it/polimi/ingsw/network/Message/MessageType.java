package it.polimi.ingsw.network.Message;

public enum MessageType {
    PING,    // Ping type message
    LOGIN,   // Log in request
    QUESTION,// in a Communication between Client and server this is the command or the question
    ANSWER,  // and this is the answer
    SUPPORT, // other type of messages which are not used in the game model
}