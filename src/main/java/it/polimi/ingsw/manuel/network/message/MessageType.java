package it.polimi.ingsw.manuel.network.message;

/**
 * This enum contains all the message type available and used by the server and clients.
 */
public enum MessageType {
    LOGIN_REQUEST, LOGIN_REPLY,
    PLAYERNUMBER_REQUEST, PLAYERNUMBER_REPLY,
    LOBBY,
    DISCONNECTION,
    GENERIC_MESSAGE,
    PING,
    ERROR,
}
