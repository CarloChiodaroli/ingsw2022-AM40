package it.polimi.ingsw.commons.message.play;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.MessageType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class which manages all Play related messages.
 */
public class NormalPlayMessage extends PlayMessage {

    private final static MessageType messageType = MessageType.PLAY;

    /**
     * Constructor of the message for move a student
     *
     * @param sender sender
     * @param move   move
     * @param color  color of student
     * @param fromId start of movement
     * @param toId   destination movement
     */
    public NormalPlayMessage(String sender, String move, TeacherColor color, String fromId, String toId) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), color, fromId, toId});
        Class<?>[] tmp = new Class<?>[]{String.class, TeacherColor.class, String.class, String.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for mother nature steps
     *
     * @param sender sender
     * @param move   move
     * @param hops   number of steps
     */
    public NormalPlayMessage(String sender, String move, Integer hops) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), hops});
        Class<?>[] tmp = new Class<?>[]{String.class, Integer.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }


    /**
     * Constructor of the message for the status of end game, pianification phase, action phase, position of mother nature
     * and chosen cloud
     *
     * @param sender sender
     * @param move   move
     * @param id     required id
     */
    public NormalPlayMessage(String sender, String move, String id) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), id});
        Class<?>[] tmp = new Class<?>[]{String.class, String.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the status of students
     *
     * @param sender   sender
     * @param move     move
     * @param id       player
     * @param quantity a map with for each color the number of students
     */
    public NormalPlayMessage(String sender, String move, String id, Map<TeacherColor, Integer> quantity) {
        super(sender, move, messageType);
        super.setTeacherColorIntegerMap(new HashMap<>(quantity));
        super.setParams(new Object[]{super.getSenderName(), id, "getTeacherColorIntegerMap"});
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, Map.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the status of teachers
     *
     * @param sender sender
     * @param move   move
     * @param id     player
     * @param which  a list with teachers color
     */
    public NormalPlayMessage(String sender, String move, String id, List<TeacherColor> which) {
        super(sender, move, messageType);
        super.setTeacherColorList(new ArrayList<>(which));
        super.setParams(new Object[]{super.getSenderName(), id, "getTeacherColorList"});
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, List.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the status of islands and clouds
     *
     * @param sender sender
     * @param move   move
     * @param which  id of islands or clouds
     */
    public NormalPlayMessage(String sender, String move, List<String> which) {
        super(sender, move, messageType);
        super.setStringList(new ArrayList<>(which));
        super.setParams(new Object[]{super.getSenderName(), "getStringList"});
        Class<?>[] tmp = new Class<?>[]{String.class, List.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the status of towers on islands
     *
     * @param sender    sender
     * @param move      move
     * @param conquests a map with for each island the color of the tower
     */
    public NormalPlayMessage(String sender, String move, Map<String, TowerColor> conquests) {
        super(sender, move, messageType);
        super.setStringTowerColorMap(conquests);
        super.setParams(new Object[]{super.getSenderName(), "getStringTowerColorMap"});
        Class<?>[] tmp = new Class<?>[]{String.class, Map.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Default constructor
     *
     * @param sender sender
     * @param move   move
     */
    public NormalPlayMessage(String sender, String move) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName()});
        Class<?>[] tmp = new Class<?>[]{String.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the status of towers in dashboard
     *
     * @param sender sender
     * @param move   move
     * @param player player name
     * @param color  color of towers
     */
    public NormalPlayMessage(String sender, String move, String player, TowerColor color) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), player, color});
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, TowerColor.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for play assistant card
     *
     * @param sender sender
     * @param move   move
     * @param player player
     * @param weight weight of chosen card
     */
    public NormalPlayMessage(String sender, String move, String player, Integer weight) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), player, weight});
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, Integer.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }
}
