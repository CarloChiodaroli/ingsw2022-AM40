package it.polimi.ingsw.server.network;

import it.polimi.ingsw.commons.message.Message;

/**
 * Interface to handle clients. Every type of connection must implement this interface.
 */
public interface ClientHandler {

    /**
     * Returns the connection status
     *
     * @return true if the client is connected
     */
    boolean isConnected();

    /**
     * Disconnect client
     */
    void disconnect();

    /**
     * Sends a message to the client
     *
     * @param message message
     */
    void sendMessage(Message message);
}
