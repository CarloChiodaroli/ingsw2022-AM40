package it.polimi.ingsw.model;


import it.polimi.ingsw.model.phase.action.Characters;
import it.polimi.ingsw.model.player.AssistantCard;
import it.polimi.ingsw.model.player.Player;

import java.util.NoSuchElementException;

/**
 * This class manages all interactions between model and controller and handles model exceptions
 */
public class GameModel {

    private final Game game;

    public GameModel(){
        this.game = new Game();
    }

    /**
     * Adds a player to the game
     * @param name is the name of the player to add, the name need to be different from each other
     */
    public void addPlayer(String name){
        game.addPlayer(name);
    }

    /**
     * Starts the game once there are enough players
     */
    public void startGame(){
        game.gameStarter();
    }

    /**
     * Switches the variant of the game between Expert or not Expert variants
     */
    public void switchExpertVariant(){
        game.switchExpertVariant();
    }

    // Player moves
    /**
     * Gets a player from player name, used to get the right player to execute commands from
     * @param playerName the name of the player who sent the command
     * @return the player corresponding to the given name
     * @throws NoSuchElementException if no player with that name was found
     */
    private Player getPlayer(String playerName) throws NoSuchElementException {
        return game.getPlayers().stream()
                .filter(player -> player.getName().equals(playerName))
                .findAny()
                .orElseThrow();
    }

    /**
     * Builds the message to send when a player was not found
     * @param playerName the name pf the not existing player
     * @return the message
     */
    private static String playerNotFoundMessage(String playerName){
        return "No " + playerName + " player found";
    }

    /**
     * Makes a player play an assistant card
     * @param playerName the name of the player who plays the card
     * @param cardWeight the weight of the card the player wants to play
     */
    public void playAssistantCard(String playerName, int cardWeight){
        try {
            getPlayer(playerName).playAssistantCard(new AssistantCard(cardWeight));
        }
        catch(NoSuchElementException e){
            System.err.println(playerNotFoundMessage(playerName));
        }
    }

    /**
     * Makes a player move a student
     * @param playerName the name of the player who makes the move
     * @param color the color of the student the player wants to move
     * @param sourceId the id of the source of the movement of the student
     * @param destinationId the id og the destination of the movement of the student
     */
    public void moveStudent(String playerName, TeacherColor color, String sourceId, String destinationId){
        try {
            getPlayer(playerName).moveStudent(color, sourceId, destinationId);
        }
        catch(NoSuchElementException e){
            System.err.println(playerNotFoundMessage(playerName));
        }
    }

    /**
     * Makes a player shift two students from two different places
     * @param playerName the name of the player who makes the move
     * @param entranceStudent the color of the student ho is in the entrance
     * @param otherStudent the color of the student who is in the room or card
     */
    public void moveStudent(String playerName, TeacherColor entranceStudent, TeacherColor otherStudent){
        try {
            getPlayer(playerName).moveStudent(entranceStudent, otherStudent);
        }
        catch(NoSuchElementException e){
            System.err.println(playerNotFoundMessage(playerName));
        }
    }

    /**
     * Moves mother nature
     * @param playerName the name of the player who makes the move
     * @param steps the number of steps he wants to move her
     */
    public void moveMotherNature(String playerName, int steps){
        try {
            getPlayer(playerName).moveMotherNature(steps);
        }
        catch(NoSuchElementException e){
            System.err.println(playerNotFoundMessage(playerName));
        }
    }

    /**
     * Makes the calculation of the influence
     * @param playerName the name of the player who makes the move
     */
    public void calcInfluence(String playerName){
        try {
            getPlayer(playerName).calcInfluence();
        }
        catch(NoSuchElementException e){
            System.err.println(playerNotFoundMessage(playerName));
        }
    }

    /**
     * Executes the choice of the cloud by the player at the end of his action phase
     * @param playerName the name of the player who makes the move
     * @param cloudId the id of the chosen cloud
     */
    public void chooseCloud(String playerName, String cloudId){
        try {
            getPlayer(playerName).chooseCloud(cloudId);
        }
        catch(NoSuchElementException e){
            System.err.println(playerNotFoundMessage(playerName));
        }
    }

    /**
     * Makes the player play and pay a character card
     * @param playerName the name of the player who makes the move
     * @param character the character of the card the player wants to use
     */
    public void playCharacterCard(String playerName, Characters character){
        try {
            getPlayer(playerName).playCharacterCard(character);
        }
        catch(NoSuchElementException e){
            System.err.println(playerNotFoundMessage(playerName));
        }
    }
}
