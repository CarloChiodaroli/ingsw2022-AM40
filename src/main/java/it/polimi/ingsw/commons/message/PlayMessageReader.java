package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;

import java.util.List;
import java.util.Map;

/**
 * Every class which "reads" {@link it.polimi.ingsw.commons.message.play.PlayMessage play messages} needs to implement this interface.
 * This interface on what the {@link it.polimi.ingsw.commons.message.play.PlayMessage#executeMessage(PlayMessageReader) executeMessage}
 * method bases itself to call the correct method to run by the "reader".
 */
public interface PlayMessageReader {

    // Normal

    /**
     * Take the assistant card and send it to the server
     *
     * @param player player name
     * @param weight weight of chosen card
     */
    void playAssistantCard(String player, Integer weight);

    /**
     * Take the movement of student and send it to the server
     *
     * @param player player name
     * @param color  color of student
     * @param fromId start of movement
     * @param toId   destination of movement
     */
    void moveStudent(String player, TeacherColor color, String fromId, String toId);

    /**
     * Take mother nature steps and send it to the server
     *
     * @param player player name
     * @param hops   number of steps
     */
    void moveMotherNature(String player, Integer hops);

    /**
     * Call influence calculate and send it to the server
     *
     * @param player player name
     */
    void calcInfluence(String player);

    /**
     * Take chosen cloud and send it to the server
     *
     * @param player player name
     * @param id     cloud id
     */
    void chooseCloud(String player, String id);

    /**
     * Take the status of students and send it to the server
     *
     * @param sender   sender
     * @param id       player
     * @param quantity a map with for each color the number of students
     */
    void statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity);

    /**
     * Take the status of teachers and send it to the server
     *
     * @param sender sender
     * @param id     player
     * @param which  a list with teachers color
     */
    void statusTeacher(String sender, String id, List<TeacherColor> which);

    /**
     * Take the status of towers on islands and send it to the server
     *
     * @param sender    sender
     * @param conquests a map with for each island the color of the tower
     */
    void statusTower(String sender, Map<String, TowerColor> conquests);

    /**
     * Take the status of towers in dashboard and send it to the server
     *
     * @param sender sender
     * @param player player name
     * @param color  color of towers
     */
    void statusTower(String sender, String player, TowerColor color);

    /**
     * Take the status of islands and send it to the server
     *
     * @param sender sender
     * @param ids    id of islands
     */
    void statusIslandIds(String sender, List<String> ids);

    /**
     * Take the status of clouds and send it to the server
     *
     * @param sender sender
     * @param ids    id of clouds
     */
    void statusCloudIds(String sender, List<String> ids);

    /**
     * Take the status of mother nature and send it to the server
     *
     * @param sender   sender
     * @param islandId island with mother nature
     */
    void statusMotherNature(String sender, String islandId);

    /**
     * Take the status of action phase and send it to the server
     *
     * @param sender       sender
     * @param actualPlayer actual player in action
     */
    void statusAction(String sender, String actualPlayer);

    /**
     * Take the status of pianification phase and send it to the server
     *
     * @param sender       sender
     * @param actualPlayer actual player in pianification phase
     */
    void statusPlanning(String sender, String actualPlayer);

    /**
     * Take the status of assistant cards and send it to the server
     *
     * @param sender sender
     * @param player player name
     * @param weight weight of assistant card chosen
     */
    void statusAssistantCard(String sender, String player, Integer weight);

    /**
     * Take the status of end game and send it to the server
     *
     * @param sender sender
     * @param winner name of winner
     */
    void statusEndGame(String sender, String winner);

    /**
     * While reconnecting, takes the personal deck status of the reconnecting client
     *
     * @param sender     sender
     * @param assistants list which represents the personal deck
     */
    void statusRemainingAssistants(String sender, List<String> assistants);

    // Expert

    /**
     * Take the switch of 2 students and send it to the server
     *
     * @param player    player name
     * @param fromColor color of student in entrance
     * @param toColor   color of student in destination
     * @param placeId   destination place
     */
    void moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId);

    /**
     * Take the character card and send it to the server
     *
     * @param player    player name
     * @param character chosen character
     */
    void playCharacterCard(String player, Characters character);

    /**
     * Take the character card who needs an island and send it to the server
     *
     * @param player    player name
     * @param character chosen character
     * @param id        chosen island
     */
    void playCharacterCard(String player, Characters character, String id);

    /**
     * Take the character card who needs a color and send it to the server
     *
     * @param player    player name
     * @param character chosen character
     * @param color     chosen color
     */
    void playCharacterCard(String player, Characters character, TeacherColor color);

    /**
     * Take the status of a character card and send it to the server
     *
     * @param sender    sender
     * @param character chosen character
     */
    void statusCharacterCard(String sender, Characters character);

    /**
     * Take the status of coins of character cards and send it to the server
     *
     * @param sender sender
     * @param money  a map with for each character the number of coins
     */
    void statusCharacterCard(String sender, Map<String, Integer> money);

    /**
     * Take the status of coins of players and send it to the server
     *
     * @param sender sender
     * @param money  a map with for each player the coins he has
     */
    void statusPlayerMoney(String sender, Map<String, Integer> money);

    /**
     * Take the status of students in character card and send it to the server
     *
     * @param sender    sender
     * @param character chosen character
     * @param quantity  a map with for each color the number of students
     */
    void statusStudent(String sender, Characters character, Map<TeacherColor, Integer> quantity);

    /**
     * Take the status of prohibition cards and send it to the server
     *
     * @param sender    sender
     * @param islandIds a list of islands with prohibition cards
     */
    void statusNoEntry(String sender, List<String> islandIds);
}
