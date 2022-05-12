package it.polimi.ingsw.manuel.network.client;

import it.polimi.ingsw.manuel.network.message.Message;
import it.polimi.ingsw.manuel.observer.Observable;


import java.util.logging.Logger;

/**
 * Abstract class to communicate with the server. Every type of connection must implement this interface.
 */
public abstract class Client extends Observable {

    public static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    public abstract void sendMessage(Message message);

    public abstract void readMessage();

    public abstract void disconnect();

    public abstract void enablePinger(boolean enabled);
}
