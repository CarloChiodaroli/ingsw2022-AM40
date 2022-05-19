package it.polimi.ingsw.commons.observer;
import java.util.Map;

public interface ViewObserver {

    void onUpdateServerInfo(Map<String, String> serverInfo);

    void onUpdateNickname(String nickname);

    void onUpdatePlayersNumber(int playersNumber);

    void onDisconnection();
}
