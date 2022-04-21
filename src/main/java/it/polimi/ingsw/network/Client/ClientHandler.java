package it.polimi.ingsw.network.Client;

import it.polimi.ingsw.network.Message.Message;
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