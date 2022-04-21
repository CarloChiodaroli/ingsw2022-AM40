package it.polimi.ingsw.network;


import it.polimi.ingsw.network.Message.Message;

/**
 * Abstract class to communicate with the server. Every type of connection must implement this interface.
 */
public abstract class Client  {

    /**
     * Sends a message to the server.
     *
     * @param message the message to be sent.
     */
    public abstract void sendMessage(Message message);

    /**
     * Asynchronously reads a message from the server and notifies the ClientController.
     */
    public abstract void readMessage();

    /**
     * Disconnects from the server.
     */
    public abstract void disconnect();

    /**
     * Enable a heartbeat (ping messages) to keep the connection alive.
     *
     * @param enabled set this argument to {@code true} to enable the heartbeat.
     *                set to {@code false} to kill the heartbeat.
     */
    public abstract void enablePinger(boolean enabled);
}