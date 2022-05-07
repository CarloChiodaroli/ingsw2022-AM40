package it.polimi.ingsw.network.Message;

public enum MessageType {
    PING,    // Ping type message
    LOGIN,   // Log in request
    PLAY,    // Messages to be read by message readers (server model or client model)
    SUPPORT, // other type of messages which are not used in the game model
}