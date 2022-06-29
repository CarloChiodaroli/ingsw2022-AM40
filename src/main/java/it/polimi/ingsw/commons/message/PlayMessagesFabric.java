package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.play.ExpertPlayMessage;
import it.polimi.ingsw.commons.message.play.NormalPlayMessage;

import java.util.List;
import java.util.Map;

/**
 * Fabric class needed to build {@link it.polimi.ingsw.commons.message.play.PlayMessage Play messages}.
 */
public abstract class PlayMessagesFabric {

    /**
     * Fabric the message for play assistant card
     *
     * @param player player
     * @param weight weight of chosen card
     * @return message in normal game mode
     */
    public static NormalPlayMessage playAssistantCard(String player, int weight) {
        return new NormalPlayMessage(player, "playAssistantCard", weight);
    }

    /**
     * Fabric the message for move a student
     *
     * @param player player
     * @param color color of student
     * @param fromId start of movement
     * @param toId destination movement
     * @return  message in normal game mode
     */
    public static NormalPlayMessage moveStudent(String player, TeacherColor color, String fromId, String toId) {
        return new NormalPlayMessage(player, "moveStudent", color, fromId, toId);
    }

    /**
     * Fabric the message for mother nature steps
     *
     * @param player player name
     * @param hops number of steps
     * @return  message in normal game mode
     */
    public static NormalPlayMessage moveMotherNature(String player, Integer hops) {
        return new NormalPlayMessage(player, "moveMotherNature", hops);
    }

    /**
     * Fabric the message for influence calculate
     *
     * @param player player name
     * @return  message in normal game mode
     */
    public static NormalPlayMessage calcInfluence(String player) {
        return new NormalPlayMessage(player, "calcInfluence");
    }

    /**
     * Fabric the message for chosen cloud
     *
     * @param player player name
     * @param id cloud id
     * @return  message in normal game mode
     */
    public static NormalPlayMessage chooseCloud(String player, String id) {
        return new NormalPlayMessage(player, "chooseCloud", id);
    }

    /**
     * Fabric the message for the status of students
     *
     * @param sender sender
     * @param id player
     * @param quantity a map with for each color the number of students
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity) {
        return new NormalPlayMessage(sender, "statusStudent", id, quantity);
    }

    /**
     * Fabric the message for the status of teachers
     *
     * @param sender sender
     * @param id player
     * @param which a list with teachers color
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusTeacher(String sender, String id, List<TeacherColor> which) {
        return new NormalPlayMessage(sender, "statusTeacher", id, which);
    }

    /**
     * Fabric the message for the status of towers on islands
     *
     * @param sender sender
     * @param conquests a map with for each island the color of the tower
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusTower(String sender, Map<String, TowerColor> conquests) {
        return new NormalPlayMessage(sender, "statusTower", conquests);
    }

    /**
     * Fabric the message for the status of towers in dashboard
     *
     * @param sender sender
     * @param playerName player name
     * @param color color of towers
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusTower(String sender, String playerName, TowerColor color) {
        return new NormalPlayMessage(sender, "statusTower", playerName, color);
    }

    /**
     * Fabric the message for the status of islands
     *
     * @param sender sender
     * @param ids id of islands
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusIslandIds(String sender, List<String> ids) {
        return new NormalPlayMessage(sender, "statusIslandIds", ids);
    }

    /**
     * Fabric the message for the status of clouds
     *
     * @param sender sender
     * @param ids id of clouds
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusCloudIds(String sender, List<String> ids) {
        return new NormalPlayMessage(sender, "statusCloudIds", ids);
    }

    /**
     * Take the status of mother nature and send it to the server
     *
     * @param sender sender
     * @param islandId island with mother nature
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusMotherNature(String sender, String islandId) {
        return new NormalPlayMessage(sender, "statusMotherNature", islandId);
    }

    /**
     * Fabric the message for the status of action phase
     *
     * @param sender sender
     * @param actualPlayer actual player in action
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusAction(String sender, String actualPlayer) {
        return new NormalPlayMessage(sender, "statusAction", actualPlayer);
    }

    /**
     * Fabric the message for the status of pianification phase
     *
     * @param sender sender
     * @param actualPlayer actual player in pianification phase
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusPlanning(String sender, String actualPlayer) {
        return new NormalPlayMessage(sender, "statusPlanning", actualPlayer);
    }

    /**
     * Fabric the message for the status of assistant cards
     *
     * @param sender sender
     * @param playerName player name
     * @param weight weight of assistant card chosen
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusAssistantCard(String sender, String playerName, int weight) {
        return new NormalPlayMessage(sender, "statusAssistantCard", playerName, weight);
    }

    /**
     * Fabric the message for the status of end game
     *
     * @param sender sender
     * @param winnerName name of winner
     * @return  message in normal game mode
     */
    public static NormalPlayMessage statusEndGame(String sender, String winnerName) {
        return new NormalPlayMessage(sender, "statusEndGame", winnerName);
    }

    public static NormalPlayMessage statusRemainingAssistants(String sender, List<String> assistants){
        return new NormalPlayMessage(sender, "statusRemainingAssistants", assistants);
    }

    // Expert part

    /**
     * Fabric the message for switch of 2 students
     *
     * @param player player name
     * @param fromColor color of student in entrance
     * @param toColor color of student in destination
     * @param placeId destination place
     * @return  message in expert game mode
     */
    public static ExpertPlayMessage moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        return new ExpertPlayMessage(player, "moveStudent", fromColor, toColor, placeId);
    }

    /**
     * Fabric the message for the status of students in character card
     *
     * @param sender sender
     * @param character chosen character
     * @param quantity a map with for each color the number of students
     * @return  message in expert game mode
     */
    public static ExpertPlayMessage statusStudent(String sender, Characters character, Map<TeacherColor, Integer> quantity) {
        return new ExpertPlayMessage(sender, "statusStudent", character, quantity);
    }

    /**
     * Fabric the message for the status of coins of players
     *
     * @param sender sender
     * @param money a map with for each player the coins he has
     * @return  message in expert game mode
     */
    public static ExpertPlayMessage statusPlayerMoney(String sender, Map<String, Integer> money) {
        return new ExpertPlayMessage(sender, "statusPlayerMoney", money);
    }

    /**
     * Fabric the message for the status of coins of character cards
     *
     * @param sender sender
     * @param money a map with for each character the number of coins
     * @return  message in expert game mode
     */
    public static ExpertPlayMessage statusCharacterCard(String sender, Map<String, Integer> money) {
        return new ExpertPlayMessage(sender, "statusCharacterCard", money);
    }

    /**
     * Fabric the message for the character card
     *
     * @param player player name
     * @param character chosen character
     * @return  message in expert game mode
     */
    public static ExpertPlayMessage playCharacterCard(String player, Characters character) {
        return new ExpertPlayMessage(player, "playCharacterCard", character);
    }

    /**
     * Fabric the message for the character card who needs an island
     *
     * @param player player name
     * @param character chosen character
     * @param id chosen island
     * @return  message in expert game mode
     */
    public static ExpertPlayMessage playCharacterCard(String player, Characters character, String id) {
        return new ExpertPlayMessage(player, "playCharacterCard", character, id);
    }

    /**
     * Fabric the message for the character card who needs a color
     *
     * @param player player name
     * @param character chosen character
     * @param color chosen color
     * @return  message in expert game mode
     */
    public static ExpertPlayMessage playCharacterCard(String player, Characters character, TeacherColor color) {
        return new ExpertPlayMessage(player, "playCharacterCard", character, color);
    }

    /**
     * Fabric the message for the status of a character card
     *
     * @param sender sender
     * @param character chosen character
     * @return  message in expert game mode
     */
    public static ExpertPlayMessage statusCharacterCard(String sender, Characters character) {
        return new ExpertPlayMessage(sender, "statusCharacterCard", character);
    }

    /**
     * Fabric the message for the status of prohibition cards
     *
     * @param sender sender
     * @param islandIds a list of islands with prohibition cards
     * @return  message in expert game mode
     */
    public static ExpertPlayMessage statusNoEntry(String sender, List<String> islandIds) {
        return new ExpertPlayMessage(sender, "statusNoEntry", islandIds);
    }
}
