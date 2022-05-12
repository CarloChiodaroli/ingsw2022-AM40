package it.polimi.ingsw.manuel.model;


import it.polimi.ingsw.manuel.network.message.LobbyMessage;
import it.polimi.ingsw.manuel.observer.Observable;

import java.util.ArrayList;
import java.util.List;

public class Game extends Observable {
    private static Game instance;
    public static final int MAX_PLAYERS = 3;
    public static final String SERVER_NICKNAME = "server";
    private int chosenPlayersNumber;
    private List<Player> players;

    private Game() {
        this.players = new ArrayList<>();
    }

    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    public Player getPlayerByNickname(String nickname) {
        return players.stream()
                .filter(player -> nickname.equals(player.getNickname()))
                .findFirst()
                .orElse(null);
    }

    public void addPlayer(Player player) {
        players.add(player);
        if (chosenPlayersNumber != 0) {
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.chosenPlayersNumber));
        }
    }

    public boolean removePlayerByNickname(String nickname, boolean notifyEnabled) {
        boolean result = players.remove(getPlayerByNickname(nickname));
        if (notifyEnabled) {
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.chosenPlayersNumber));
        }

        return result;
    }

    public int getNumCurrentPlayers() {
        return players.size();
    }

    public boolean setChosenMaxPlayers(int chosenMaxPlayers) {
        if (chosenMaxPlayers > 0 && chosenMaxPlayers <= MAX_PLAYERS) {
            this.chosenPlayersNumber = chosenMaxPlayers;
            notifyObserver(new LobbyMessage(getPlayersNicknames(), this.chosenPlayersNumber));
            return true;
        }
        return false;
    }

    public int getChosenPlayersNumber() {
        return chosenPlayersNumber;
    }

    public boolean isNicknameTaken(String nickname) {
        return players.stream()
                .anyMatch(p -> nickname.equals(p.getNickname()));
    }

    public List<String> getPlayersNicknames() {
        List<String> nicknames = new ArrayList<>();
        for (Player p : players) {
            nicknames.add(p.getNickname());
        }
        return nicknames;
    }

    public List<Player> getPlayers() {
        return players;
    }

}
