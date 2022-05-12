package it.polimi.ingsw.network.Server;

import it.polimi.ingsw.network.Message.Message;

/**
 * Interface to handle clients. Every type of connection must implement this interface.
 */
public interface ClientHandler {

    boolean isConnected();

    void disconnect();

    void sendMessage(Message message);
}
