package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.enums.Wizard;

import java.util.List;

/**
 * This class manages all messages in the Lobby client server dialogue.
 */
public class LobbyMessage extends Message {

    private List<String> nicknameList;
    private String stringArg;
    private int numOfPlayers;
    private boolean isStartGame = false;
    private boolean boolArg;
    private Wizard wizard;
    private String command;
    private List<Wizard> available;

    // Sends all player names
    public LobbyMessage(String sender, List<String> nicknameList, int numOfPlayers) {
        super(sender, MessageType.LOBBY);
        this.command = "lobbyPlayers";
        this.numOfPlayers = numOfPlayers;
        this.nicknameList = nicknameList;
        super.message();
    }

    // sends a player name, if is disconnected shows a disconnection, else shows the main player
    public LobbyMessage(String sender, String command, String playerName) {
        super(sender, MessageType.LOBBY);
        this.command = command;
        this.stringArg = playerName;
        super.message();
    }

    // sends chosen wizard
    public LobbyMessage(String sender, Wizard wizard) {
        super(sender, MessageType.LOBBY);
        this.command = "wizard";
        this.wizard = wizard;
        super.message();
    }

    // sends an integer
    public LobbyMessage(String sender, String command, int number) {
        super(sender, MessageType.LOBBY);
        this.command = command;
        this.numOfPlayers = number;
        super.message();
    }

    public LobbyMessage(String sender, String command) {
        super(sender, MessageType.LOBBY);
        this.command = command;
        isStartGame = true;
        super.message();
    }

    public LobbyMessage(String sender, String command, boolean boolArg) {
        super(sender, MessageType.LOBBY);
        this.command = command;
        isStartGame = true;
        this.boolArg = boolArg;
        super.message();
    }

    public LobbyMessage(String sender, String command, List<Wizard> available) {
        super(sender, MessageType.LOBBY);
        this.command = command;
        isStartGame = true;
        this.available = available;
        super.message();
    }

    public String getCommand() {
        controlWritten();
        return command;
    }

    public List<String> getLobbyPlayers() {
        controlWritten();
        return this.nicknameList;
    }

    public String getMainPlayerName() {
        controlWritten();
        return stringArg;
    }

    public String getDisconnection() {
        controlWritten();
        return stringArg;
    }

    public int studentNumber() {
        controlWritten();
        return numOfPlayers;
    }

    public Wizard getWizard() {
        controlWritten();
        return wizard;
    }

    public boolean getAccepted() {
        controlWritten();
        return boolArg;
    }

    public Boolean isStartGame() {
        controlWritten();
        return isStartGame;
    }

    public boolean isExpert() {
        controlWritten();
        return boolArg;
    }

    public List<Wizard> getAvailableWizards() {
        return available;
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
