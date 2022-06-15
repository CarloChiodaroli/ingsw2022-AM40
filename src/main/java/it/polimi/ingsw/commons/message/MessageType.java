package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.message.play.ExpertPlayMessage;
import it.polimi.ingsw.commons.message.play.NormalPlayMessage;
import it.polimi.ingsw.commons.message.play.PlayMessage;

public enum MessageType {

    // These will remain
    PING(PingMessage.class),        // "Ping" if outgoing // "Pong" if ingoing // Practically no difference
    LOGIN(LoginMessage.class),      // Log in management, Requests, Acceptations and Rejections
    LOBBY(LobbyMessage.class),      // Messages for the lobby management
    PLAY(NormalPlayMessage.class),  // Messages to be read by PlayMessageReader implementers - Normal variant game
    EXPERT(ExpertPlayMessage.class),// Messages to be read by PlayMessageReader implementers - Expert variant game
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