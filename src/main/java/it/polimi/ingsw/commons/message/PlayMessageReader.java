package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;

import java.util.List;
import java.util.Map;

public interface PlayMessageReader {

    // Normal

    void playAssistantCard(String player, Integer weight);

    void moveStudent(String player, TeacherColor color, String fromId, String toId);

    void moveMotherNature(String player, Integer hops);

    void calcInfluence(String player);

    void chooseCloud(String player, String id);

    void statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity);

    void statusTeacher(String sender, String id, List<TeacherColor> which);

    void statusTower(String sender, Map<String, TowerColor> conquests);

    void statusTower(String sender, String player, TowerColor color);

    void statusIslandIds(String sender, List<String> ids);

    void statusCloudIds(String sender, List<String> ids);

    void statusMotherNature(String sender, String islandId);

    void statusAction(String sender, String actualPlayer);

    void statusPlanning(String sender, String actualPlayer);

    void statusAssistantCard(String sender, String player, Integer weight);

    void statusEndGame(String sender, String winner);

    // Expert

    void moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId);

    void playCharacterCard(String player, Characters character);

    void playCharacterCard(String player, Characters character, String id);

    void playCharacterCard(String player, Characters character, TeacherColor color);

    void statusCharacterCard(String sender, Characters character);

    void statusCharacterCard(String sender, Map<String, Integer> money);

    void statusPlayerMoney(String sender, Map<String, Integer> money);

    void statusStudent(String sender, Characters character, Map<TeacherColor, Integer> quantity);

    void statusNoEntry(String sender, List<String> islandIds);
}
