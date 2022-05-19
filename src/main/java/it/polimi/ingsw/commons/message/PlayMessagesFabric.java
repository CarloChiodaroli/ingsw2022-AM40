package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.enums.Characters;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayMessagesFabric {

    public static PlayMessage playAssistantCard(String player, int weight) {
        return new PlayMessage(player, "playAssistantCard", weight);
    }


    public static PlayMessage moveStudent(String player, TeacherColor color, String fromId, String toId) {
        return new PlayMessage(player, "moveStudent", color, fromId, toId);
    }


    public static PlayMessage moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        return new PlayMessage(player, "moveStudent", fromColor, toColor, placeId);
    }


    public static PlayMessage moveMotherNature(String player, Integer hops) {
        return new PlayMessage(player, "moveMotherNature", hops);
    }


    public static PlayMessage calcInfluence(String player) {
        return new PlayMessage(player, "calcInfluence");
    }


    public static PlayMessage chooseCloud(String player, String id) {
        return new PlayMessage(player, "chooseCloud", id);
    }


    public static PlayMessage playCharacterCard(String player, Characters character) {
        return new PlayMessage(player, "playCharacterCard", character);
    }


    public static PlayMessage playCharacterCard(String player, Characters character, String id) {
        return new PlayMessage(player, "playCharacterCard", character, id);
    }


    public static PlayMessage playCharacterCard(String player, Characters character, TeacherColor color) {
        return new PlayMessage(player, "playCharacterCard", character, color);
    }


    public static PlayMessage statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity) {
        return new PlayMessage(sender, "statusStudent", id, quantity);
    }


    public static PlayMessage statusTeacher(String sender, String id, List<TeacherColor> which) {
        return new PlayMessage(sender, "statusTeacher", id, which);
    }


    public static PlayMessage statusTower(String sender, Map<String, Optional<TowerColor>> conquests) {
        return new PlayMessage(sender,"statusTower", conquests);
    }


    public static PlayMessage statusIslandIds(String sender, List<String> ids) {
        return new PlayMessage(sender, "statusIslandIds", ids);
    }


    public static PlayMessage statusMotherNature(String sender, String islandId) {
        return new PlayMessage(sender, "statusMotherNature", islandId);
    }

    public static PlayMessage statusAction(String sender, String actualPlayer) {
        return new PlayMessage(sender, "statusAction", actualPlayer);
    }

    public static PlayMessage statusPlanning(String sender, String actualPlayer) {
        return new PlayMessage(sender, "statusPlanning", actualPlayer);
    }

    public static PlayMessage statusCharacterCard(String sender, Characters character){
        return new PlayMessage(sender, "statusCharacterCard", character);
    }

    public static PlayMessage statusAssistantCard(String sender, String playerName, int weight){
        return new PlayMessage(sender, "statusAssistantCard", playerName, weight);
    }
}
