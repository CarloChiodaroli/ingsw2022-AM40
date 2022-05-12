package it.polimi.ingsw.network.Message;

import it.polimi.ingsw.manuel.model.Game;

import java.util.List;

public class LobbyMessage extends Message {

    private final List<String> nicknameList;
    private final int maxPlayers;

    public LobbyMessage(List<String> nicknameList, int maxPlayers) {
        super(Game.SERVER_NICKNAME, MessageType.LOBBY);
        this.nicknameList = nicknameList;
        this.maxPlayers = maxPlayers;
    }

    public List<String> getNicknameList() {
        return nicknameList;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public String toString() {
        return "LobbyMessage{" +
                "senderName=" + getSenderName() +
                ", PlayerNameList=" + nicknameList +
                ", numPlayers=" + maxPlayers +
                '}';
    }

}
