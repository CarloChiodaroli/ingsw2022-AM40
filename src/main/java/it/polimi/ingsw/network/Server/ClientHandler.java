package it.polimi.ingsw.network.Server;

import it.polimi.ingsw.network.Message.Message;

/**
 * Interface with method for communication from Server to Client
 */
public interface ClientHandler {

    /**
     * Returns the connection status.
     * @return true if the client is connected, false othwerwise .
     */
    boolean isConnected();

    /**
     * Disconnects from the client.
     */
    void disconnect();

    /**
     * Sends a message to the client.
     * @param message the message to be sent.
     */
    void sendMessage(Message message);
}