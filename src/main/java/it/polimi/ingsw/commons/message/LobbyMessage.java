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

    /**
     * Constructor, sends all player names
     *
     * @param sender sender
     * @param nicknameList a list of players name
     * @param numOfPlayers number of players
     */
    public LobbyMessage(String sender, List<String> nicknameList, int numOfPlayers) {
        super(sender, MessageType.LOBBY);
        this.command = "lobbyPlayers";
        this.numOfPlayers = numOfPlayers;
        this.nicknameList = nicknameList;
        super.message();
    }

    /**
     * Constructor, sends a player name, if is disconnected shows a disconnection, else shows the main player
     *
     * @param sender sender
     * @param command string with command
     * @param playerName player name
     */
    public LobbyMessage(String sender, String command, String playerName) {
        super(sender, MessageType.LOBBY);
        this.command = command;
        this.stringArg = playerName;
        super.message();
    }

    /**
     * Constructor, sends chosen wizard
     *
     * @param sender sender
     * @param wizard chosen wizard
     */
    public LobbyMessage(String sender, Wizard wizard) {
        super(sender, MessageType.LOBBY);
        this.command = "wizard";
        this.wizard = wizard;
        super.message();
    }

    /**
     * Constructor, sends an integer
     *
     * @param sender sender
     * @param command string with command
     * @param number number
     */
    public LobbyMessage(String sender, String command, int number) {
        super(sender, MessageType.LOBBY);
        this.command = command;
        this.numOfPlayers = number;
        super.message();
    }

    /**
     * Constructor
     *
     * @param sender sender
     * @param command string with command
     */
    public LobbyMessage(String sender, String command) {
        super(sender, MessageType.LOBBY);
        this.command = command;
        isStartGame = true;
        super.message();
    }

    /**
     * Constructor, sends a boolean
     *
     * @param sender sender
     * @param command string with command
     * @param boolArg chosen boolean
     */
    public LobbyMessage(String sender, String command, boolean boolArg) {
        super(sender, MessageType.LOBBY);
        this.command = command;
        isStartGame = true;
        this.boolArg = boolArg;
        super.message();
    }

    /**
     * Constructor, sends list of available wizards
     *
     * @param sender sender
     * @param command string with command
     * @param available a list of available wizards
     */
    public LobbyMessage(String sender, String command, List<Wizard> available) {
        super(sender, MessageType.LOBBY);
        this.command = command;
        isStartGame = true;
        this.available = available;
        super.message();
    }

    /**
     * Get command
     *
     * @return string with command
     */
    public String getCommand() {
        controlWritten();
        return command;
    }

    /**
     * Get players in lobby
     *
     * @return a list with players in lobby
     */
    public List<String> getLobbyPlayers() {
        controlWritten();
        return this.nicknameList;
    }

    /**
     * Get the main player
     *
     * @return name of the main player
     */
    public String getMainPlayerName() {
        controlWritten();
        return stringArg;
    }

    /**
     * Get disconnection
     *
     * @return name of disconnect player
     */
    public String getDisconnection() {
        controlWritten();
        return stringArg;
    }

    /**
     * Get number of students
     *
     * @return number of students
     */
    public int studentNumber() {
        controlWritten();
        return numOfPlayers;
    }

    /**
     * Get wizard
     *
     * @return chosen wizard
     */
    public Wizard getWizard() {
        controlWritten();
        return wizard;
    }

    /**
     * Get result
     *
     * @return true if is accepted
     */
    public boolean getAccepted() {
        controlWritten();
        return boolArg;
    }

    /**
     * Get if the game is started
     *
     * @return true if the game is started
     */
    public Boolean isStartGame() {
        controlWritten();
        return isStartGame;
    }

    /**
     * Get the game mode
     *
     * @return true if is expert
     */
    public boolean isExpert() {
        controlWritten();
        return boolArg;
    }

    /**
     * Get available wizards
     *
     * @return a list of available wizards
     */
    public List<Wizard> getAvailableWizards() {
        return available;
    }

    /**
     * Get max number of players
     *
     * @return max number of players
     */
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
