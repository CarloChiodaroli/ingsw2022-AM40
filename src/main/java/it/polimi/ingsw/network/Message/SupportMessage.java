package it.polimi.ingsw.network.Message;

public class SupportMessage extends Message{

    public SupportMessage(String player){
        super(player, MessageType.SUPPORT);
    }


}
