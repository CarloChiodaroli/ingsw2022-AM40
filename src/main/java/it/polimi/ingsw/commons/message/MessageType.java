package it.polimi.ingsw.commons.message;

public enum MessageType {

    // These are too much
    LOGIN_REQUEST,          // to be incorporated in LOGIN
    LOGIN_REPLY,            // to be incorporated in LOGIN

    PLAYER_NUMBER_REQUEST,  // to be incorporated in LOBBY
    PLAYER_NUMBER_REPLY,    // to be incorporated in LOBBY
    DISCONNECTION,          // to be incorporated in LOBBY
    PICK_FIRST_PLAYER,      // to be incorporated in LOBBY

    // These will remain
    PING,       // "Ping" if outgoing
                // "Pong" if ingoing
                // Practically no difference
    LOGIN,      // Log in management, Requests, Acceptations and Rejections
    LOBBY,      // Messages for the lobby management
                // Wizard choice,
    PLAY,       // Messages to be read by PlayMessageReader implementers
    ERROR,      // Various Errors
    GENERIC     // other type of messages which are not used in the game model
}