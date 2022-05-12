package it.polimi.ingsw.manuel.network.server;

import it.polimi.ingsw.manuel.network.message.Message;

/**
 * Interface to handle clients. Every type of connection must implement this interface.
 */
public interface ClientHandler {

    boolean isConnected();

    void disconnect();

    void sendMessage(Message message);
}
