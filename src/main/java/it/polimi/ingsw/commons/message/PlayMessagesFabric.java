package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.message.play.ExpertPlayMessage;
import it.polimi.ingsw.commons.message.play.NormalPlayMessage;

import java.util.List;
import java.util.Map;

public class PlayMessagesFabric {

    public static NormalPlayMessage playAssistantCard(String player, int weight) {
        return new NormalPlayMessage(player, "playAssistantCard", weight);
    }

    public static NormalPlayMessage moveStudent(String player, TeacherColor color, String fromId, String toId) {
        return new NormalPlayMessage(player, "moveStudent", color, fromId, toId);
    }

    public static NormalPlayMessage moveMotherNature(String player, Integer hops) {
        return new NormalPlayMessage(player, "moveMotherNature", hops);
    }

    public static NormalPlayMessage calcInfluence(String player) {
        return new NormalPlayMessage(player, "calcInfluence");
    }

    public static NormalPlayMessage chooseCloud(String player, String id) {
        return new NormalPlayMessage(player, "chooseCloud", id);
    }

    public static NormalPlayMessage statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity) {
        return new NormalPlayMessage(sender, "statusStudent", id, quantity);
    }

    public static NormalPlayMessage statusTeacher(String sender, String id, List<TeacherColor> which) {
        return new NormalPlayMessage(sender, "statusTeacher", id, which);
    }

    public static NormalPlayMessage statusTower(String sender, Map<String, TowerColor> conquests) {
        return new NormalPlayMessage(sender,"statusTower", conquests);
    }

    public static NormalPlayMessage statusTower(String sender, String playerName, TowerColor color){
        return new NormalPlayMessage(sender, "statusTower", playerName, color);
    }

    public static NormalPlayMessage statusIslandIds(String sender, List<String> ids) {
        return new NormalPlayMessage(sender, "statusIslandIds", ids);
    }

    public static NormalPlayMessage statusCloudIds(String sender, List<String> ids) {
        return new NormalPlayMessage(sender, "statusCloudIds", ids);
    }

    public static NormalPlayMessage statusMotherNature(String sender, String islandId) {
        return new NormalPlayMessage(sender, "statusMotherNature", islandId);
    }

    public static NormalPlayMessage statusAction(String sender, String actualPlayer) {
        return new NormalPlayMessage(sender, "statusAction", actualPlayer);
    }

    public static NormalPlayMessage statusPlanning(String sender, String actualPlayer) {
        return new NormalPlayMessage(sender, "statusPlanning", actualPlayer);
    }

    public static NormalPlayMessage statusAssistantCard(String sender, String playerName, int weight){
        return new NormalPlayMessage(sender, "statusAssistantCard", playerName, weight);
    }

    public static NormalPlayMessage statusEndGame(String sender, String winnerName){
        return new NormalPlayMessage(sender, "statusEndGame", winnerName);
    }

    // Expert part

    public static ExpertPlayMessage moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        return new ExpertPlayMessage(player, "moveStudent", fromColor, toColor, placeId);
    }

    public static ExpertPlayMessage statusStudent(String sender, Characters character, Map<TeacherColor, Integer> quantity){
        return new ExpertPlayMessage(sender, "statusStudent", character, quantity);
    }

    public static ExpertPlayMessage statusPlayerMoney(String sender, Map<String, Integer> money){
        return new ExpertPlayMessage(sender, "statusPlayerMoney", money);
    }

    public static ExpertPlayMessage playCharacterCard(String player, Characters character) {
        return new ExpertPlayMessage(player, "playCharacterCard", character);
    }

    public static ExpertPlayMessage playCharacterCard(String player, Characters character, String id) {
        return new ExpertPlayMessage(player, "playCharacterCard", character, id);
    }

    public static ExpertPlayMessage playCharacterCard(String player, Characters character, TeacherColor color) {
        return new ExpertPlayMessage(player, "playCharacterCard", character, color);
    }

    public static ExpertPlayMessage statusCharacterCard(String sender, List<Characters> characters){
        return new ExpertPlayMessage(sender, "statusCharacterCard", characters);
    }

    public static ExpertPlayMessage statusCharacterCard(String sender, Characters character){
        return new ExpertPlayMessage(sender, "statusCharacterCard", character);
    }
}
