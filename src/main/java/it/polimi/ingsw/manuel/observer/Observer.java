package it.polimi.ingsw.manuel.observer;

import it.polimi.ingsw.manuel.network.message.Message;

/**
        * Observer interface. It supports a generic method of update.
        */
public interface Observer {
    void update(Message message);
}