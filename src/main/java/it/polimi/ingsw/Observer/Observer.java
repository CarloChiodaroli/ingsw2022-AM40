package it.polimi.ingsw.Observer;

import it.polimi.ingsw.network.Message.Message;

/**
        * Observer interface. It supports a generic method of update.
        */
public interface Observer {
    void update(Message message);
}