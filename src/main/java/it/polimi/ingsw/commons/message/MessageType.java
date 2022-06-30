package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.message.play.ExpertPlayMessage;
import it.polimi.ingsw.commons.message.play.NormalPlayMessage;

/**
 * Lists all particular types of message available.
 */
public enum MessageType {

    /**
     * Ping type odf messages needed to keep the connection alive
     */
    PING(PingMessage.class),        // "Ping" if outgoing // "Pong" if ingoing // Practically no difference
    /**
     * Login type of messages needed to manage connection requests
     */
    LOGIN(LoginMessage.class),      // Log in management, Requests, Acceptations and Rejections
    /**
     * Lobby type of messages to manage the lobby
     */
    LOBBY(LobbyMessage.class),      // Messages for the lobby management
    /**
     * Play type of messages needed to be read by Play message reader implementers, manages normal play messages
     */
    PLAY(NormalPlayMessage.class),  // Messages to be read by PlayMessageReader implementers - Normal variant game
    /**
     * Play type of messages needed to be read by Play message reader implementers, manages expert play messages
     */
    EXPERT(ExpertPlayMessage.class),// Messages to be read by PlayMessageReader implementers - Expert variant game
    /**
     * Error type of message needed to send errors to the client
     */
    ERROR(ErrorMessage.class),      // Various Errors and Disconnection command sent by server
    /**
     * Generic type of messages used to send simple strings of text
     */
    GENERIC(GenericMessage.class);  // other type of messages which are not used in the game model


    private final Class<?> implementingClass;

    /**
     * Constructor
     *
     * @param implementingClass the class which implements the specific types of messages
     */
    MessageType(Class<?> implementingClass) {
        this.implementingClass = implementingClass;
    }

    /**
     * Getter
     *
     * @return the implementing class
     */
    public Class<?> getImplementingClass() {
        return implementingClass;
    }
}