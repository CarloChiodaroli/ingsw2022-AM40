package it.polimi.ingsw.commons.message;

public enum MessageType {

    // These are too much // these lines will be deleted in a dedicated cleaning commit
    // LOGIN_REQUEST,                                       // incorporated in LOGIN
    // LOGIN_REPLY,                                         // incorporated in LOGIN
    // PLAYER_NUMBER_REQUEST(PlayerNumberRequest.class),    // incorporated in LOBBY
    // PLAYER_NUMBER_REPLY(PlayerNumberReply.class),        // incorporated in LOBBY
    // DISCONNECTION(DisconnectionMessage.class),           // incorporated in LOBBY
    // PICK_FIRST_PLAYER,                                   // incorporated in LOBBY

    // These will remain
    PING(PingMessage.class),        // "Ping" if outgoing // "Pong" if ingoing // Practically no difference
    LOGIN(LoginMessage.class),      // Log in management, Requests, Acceptations and Rejections
    LOBBY(LobbyMessage.class),      // Messages for the lobby management
    PLAY(PlayMessage.class),        // Messages to be read by PlayMessageReader implementers
    ERROR(ErrorMessage.class),      // Various Errors and Disconnection command sent by server
    GENERIC(GenericMessage.class);  // other type of messages which are not used in the game model

    private final Class<?> implementingClass;

    MessageType(Class<?> implementingClass) {
        this.implementingClass = implementingClass;
    }

    public Class<?> getImplementingClass() {
        return implementingClass;
    }
}