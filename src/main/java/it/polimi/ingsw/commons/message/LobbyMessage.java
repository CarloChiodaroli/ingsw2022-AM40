package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.enums.Wizard;

import java.util.ArrayList;
import java.util.List;

public class LobbyMessage extends Message {

    private List<String> nicknameList;
    private String stringArg;
    private int numOfPlayers;
    private boolean isDisconnection;
    private Wizard wizard;

    // Sends all player names
    public LobbyMessage(String sender, List<String> nicknameList) {
        super(sender, MessageType.LOBBY);
        this.nicknameList = nicknameList;
        super.message();
    }

    // sends a player name, if is disconnected shows a disconnection, else shows the main player
    public LobbyMessage(String sender, String playerName, boolean isDisconnection) {
        super(sender, MessageType.LOBBY);
        this.stringArg = playerName;
        this.isDisconnection = isDisconnection;
        super.message();
    }

    // sends chosen wizard
    public LobbyMessage(String sender, Wizard wizard) {
        super(sender, MessageType.LOBBY);
        this.numOfPlayers = 0;
        this.stringArg = null;
        this.nicknameList = new ArrayList<>();
        this.isDisconnection = false;
        this.wizard = wizard;
        super.message();
    }

    // sends an integer
    public LobbyMessage(String sender, int players) {
        super(sender, MessageType.LOBBY);
        this.numOfPlayers = players;
        super.message();
    }

    public List<String> getLobbyPlayers() throws IllegalMessageException {
        controlWritten();
        return this.nicknameList;
    }

    public String getMainPlayerName() throws IllegalMessageException {
        controlWritten();
        if (isDisconnection) throw new IllegalMessageException();
        return stringArg;
    }

    public String getDisconnection() throws IllegalMessageException {
        controlWritten();
        if (!isDisconnection) throw new IllegalMessageException();
        return stringArg;
    }

    public int chosenStudentNumber() throws IllegalMessageException {
        controlWritten();
        if (numOfPlayers == 0) throw new IllegalMessageException();
        return numOfPlayers;
    }

    public Wizard getWizard() throws IllegalMessageException {
        controlWritten();
        if (wizard == null) throw new IllegalMessageException();
        return wizard;
    }

    public List<String> getNicknameList() {
        return nicknameList;
    }

    public int getMaxPlayers() {
        return numOfPlayers;
    }

    @Override
    public String toString() {
        return "LobbyMessage{" +
                "senderName=" + getSenderName() +
                ", PlayerNameList=" + nicknameList +
                ", numPlayers=" + numOfPlayers +
                '}';
    }

}
