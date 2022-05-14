package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.enums.Characters;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MessageReader {

    void moveStudent(String player, TeacherColor color, String fromId, String toId);

    void moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId);

    void moveMotherNature(String player, Integer hops);

    void chooseCloud(String player, String id);

    void playCharacterCard(String player, Characters character);

    void playCharacterCard(String player, Characters character, String id);

    void playCharacterCard(String player, Characters character, TeacherColor color);

    void statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity);

    void statusTeacher(String sender, String id, List<TeacherColor> which);

    void statusTower(String sender, Map<String, Optional<TowerColor>> conquests);

    void statusIslandIds(String sender, List<String> ids);
}
