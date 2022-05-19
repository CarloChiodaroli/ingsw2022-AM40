package it.polimi.ingsw.server.network;

import it.polimi.ingsw.commons.message.Message;

/**
 * Interface to handle clients. Every type of connection must implement this interface.
 */
public interface ClientHandler {

    boolean isConnected();

    void disconnect();

    void sendMessage(Message message);
}
