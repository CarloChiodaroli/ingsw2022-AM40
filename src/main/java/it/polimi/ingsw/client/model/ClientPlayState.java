package it.polimi.ingsw.client.model;

import java.util.List;

public class ClientPlayState {

    private List<String> playerNames;
    private String mainPlayer;

    public ClientPlayState(){
        // needs to know the view
    }

    public void setMainPlayer(String mainPlayer) {
        this.mainPlayer = mainPlayer;
        // Update view of the change
    }

    public void setPlayerNames(List<String> playerNames) {
        this.playerNames = playerNames;
        // Update view of the change
    }

    public String getMainPlayer() {
        return mainPlayer;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }
}

