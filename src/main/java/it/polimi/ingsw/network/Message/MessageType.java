package it.polimi.ingsw.network.Message;

public enum MessageType {
    LOGIN_REQUEST,
    PLAYERNUMBER_REQUEST,
    PLAYERNUMBER_REPLY,
    LOGIN_REPLY,
    DISCONNECTION,
    MATCHINFOMESSAGE,
    PICK_FIRST_PLAYER,
    MATCH_INFO,
    ERROR,
    PING,    // Ping type message
    LOGIN,   // Log in request
    PLAY,    // Messages to be read by message readers (server model or client model)
    SUPPORT // other type of messages which are not used in the game model
}