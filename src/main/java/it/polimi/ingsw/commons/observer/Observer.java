package it.polimi.ingsw.commons.observer;

import it.polimi.ingsw.commons.message.Message;

/**
        * Observer interface. It supports a generic method of update.
        */
public interface Observer {
    void update(Message message);
}