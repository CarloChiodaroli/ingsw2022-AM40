package it.polimi.ingsw.network.Message;

import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MessageReader {

    void move(String player, TeacherColor color, String fromId, String toId);

    void move(String player, TeacherColor fromColor, TeacherColor toColor, String placeId);

    void move(String player, Integer hops);

    void move(String player, String id);

    void move(String player, Characters character);

    void move(String player, Characters character, String id);

    void move(String player, Characters character, TeacherColor color);

    void move(String sender, String id, Map<TeacherColor, Integer> quantity);

    void move(String sender, String id, List<TeacherColor> which);

    void move(String sender, Map<String, Optional<TowerColor>> conquests);

    void move(String sender, List<String> ids);
}
