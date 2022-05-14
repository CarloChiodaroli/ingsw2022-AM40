package it.polimi.ingsw.server.controller;

public class PlayMessagesReader {

    private final int numOfPlayers;
    private final String mainPlayer;
    private final GameController controller;

    public PlayMessagesReader(String mainPlayer, int numOfPlayers){
        if(numOfPlayers != 2 && numOfPlayers != 3) throw new IllegalArgumentException();
        this.numOfPlayers = numOfPlayers;
        this.mainPlayer = mainPlayer;
        this.controller = new GameController();
        this.controller.addPlayer(mainPlayer);
    }

    public void addPlayer(String playerName){
        controller.addPlayer(playerName);
    }

    public void deletePlayer(String playerName){
        controller.deletePlayer(playerName);
    }

    public int getNumCurrPlayers(){
        return controller.getPlayerNames().size();
    }

}
