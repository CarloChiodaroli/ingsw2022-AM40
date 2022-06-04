package it.polimi.ingsw.client.observer;
import it.polimi.ingsw.commons.enums.Wizard;

import java.util.Map;

public interface ViewObserver {

    void onUpdateServerInfo(Map<String, String> serverInfo);

    void onUpdateNickname(String nickname);

    void onUpdatePlayersNumber(int playersNumber);

    void onDisconnection();

    void onUpdateWizard(Wizard wizard);

    void onUpdateStart();

    void onUpdateExpert(boolean choice);
}
