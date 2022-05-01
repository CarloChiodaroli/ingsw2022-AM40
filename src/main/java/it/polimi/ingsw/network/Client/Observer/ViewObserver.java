package it.polimi.ingsw.network.Client.Observer;

import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.player.AssistantCard;

import java.util.Map;

public interface ViewObserver {

    /**
     * Create a new connection to the server with the updated info.
     *
     * @param serverInfo a map of server address and server port.
     */
    void onUpdateServerInfo(Map<String, String> serverInfo);

    /**
     * Sends a message to the server with the updated nickname.
     *
     * @param nickname the nickname to be sent.
     */
    void onUpdateNickname(String nickname);

    /**
     * Sends a message to the server with the player number chosen by the user.
     *
     * @param playersNumber the number of players.
     */
    void onUpdatePlayersNumber(int playersNumber);

    /**
     * Sends a message to the server with the expert variant  chosen by the user.
     *
     * @param expertvariant the number of players.
     */
    void onUpdatePlayersNumber(boolean expertvariant);

    /**
     * Handles a disconnection wanted by the user.
     * (e.g. a click on the back button into the GUI).
     */
    void onDisconnection();

    /**
     * Sends a message to the server with the chosen assistant card  chosen by the user.
     *
     * @param assistantCard Assistant Card chosen
     */
    void onUpdatePlayersNumber(AssistantCard assistantCard);


    /**
     * Sends a message to the server with the chosen movement form entrance to  island chosen by the user.
     *
     * @param islandid Island Id chosen
     */
    void onUpdatemoveStudentToIsland (String islandid);

    /**
     * Sends a message to the server with the chosen movement form entrance to room table chosen by the user.
     *
     * @param teacherColor TeacherColor chosen
     */
    void onUpdatemoveStudentToRoomTable(TeacherColor teacherColor);

    /**
     * Sends a message to the server with the chosen Mother Nature movement chosen by the user.
     *
     * @param numstepsMotherNature number steps for Mother Nature chosen
     */
    void onUpdatemoveStudentToRoomTable(int numstepsMotherNature);

    /**
     * Sends a message to the server with the chosen movement form entrance to  cloud chosen by the user.
     *
     * @param cloudid cloud Id chosen
     */
    void onUpdatemoveStudentToCloud (String cloudid);

    /**
     * Sends a message to the server with the chosen movement character chosen by the user.
     *
     * @param characters the choice of the user about his god effect.
     */
    void onUpdateApplyEffect(Characters characters);
}
