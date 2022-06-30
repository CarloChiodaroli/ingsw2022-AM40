package it.polimi.ingsw.client.network;

import it.polimi.ingsw.client.observer.Observable;
import it.polimi.ingsw.commons.message.Message;

import java.util.logging.Logger;

/**
 * Abstract class to communicate with the server. Every type of connection must implement this interface.
 */
public abstract class Client extends Observable {

    public static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    /**
     * Send a message to the server
     *
     * @param message message to be sent.
     */
    public abstract void sendMessage(Message message);

    /**
     * Read a message from the server and notifies the ClientController
     */
    public abstract void readMessage();

    /**
     * Disconnect from the server
     */
    public abstract void disconnect();

    /**
     * Enable a ping messages to keep the connection alive
     *
     * @param enabled true to enable the ping, else to kill the ping
     */
    public abstract void enablePinger(boolean enabled);
}
