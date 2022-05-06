package it.polimi.ingsw.network.Client;

import it.polimi.ingsw.Observer.Observable;
import it.polimi.ingsw.network.Message.Message;

public abstract class Client extends Observable {

    public abstract void sendMessage(Message message);

    public abstract void readMessage();

    public abstract void disconnect();

    public abstract void enablePinger(boolean enabled);
}