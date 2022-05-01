package it.polimi.ingsw.network.Client.Observer;

import it.polimi.ingsw.network.Message.Message;

public interface Observer {
    void update(Message message);

}
