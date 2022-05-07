package it.polimi.ingsw.network.Message;

import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MessageReader {

    public void move(String player, TeacherColor color, String fromId, String toId);

    public void move(String player, TeacherColor fromColor, TeacherColor toColor, String placeId);

    public void move(String player, int hops);

    public void move(String player, String id);

    public void move(String player, Characters character);

    public void move(String player, Characters character, String id);

    public void move(String player, Characters character, TeacherColor color);

    public void status(String sender, String id, Map<TeacherColor, Integer> quantity);

    public void status(String sender, String id, List<TeacherColor> which);

    public void status(String sender, Map<String, Optional<TowerColor>> conquests);

    public void status(String sender, List<String> ids);
}
