package it.polimi.ingsw.network.Message;

public enum MessageType {
    PING,
    LOGIN_REQUEST,
    PLAYERNUMBER_REQUEST,
    PLAYERNUMBER_REPLY,
    LOGIN_REPLY,
    DISCONNECTION,
    MATCHINFOMESSAGE,
    PICK_FIRST_PLAYER,
    MATCH_INFO,
    ERROR
}