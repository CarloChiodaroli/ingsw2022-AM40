package it.polimi.ingsw.commons.message.play;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.message.MessageType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class which manages all Expert variant play messages.
 */
public class ExpertPlayMessage extends PlayMessage {

    private final static MessageType messageType = MessageType.EXPERT;

    /**
     * Constructor of message for switch of 2 students
     *
     * @param sender sender
     * @param move move
     * @param fromColor color of student in entrance
     * @param toColor color of student in destination
     * @param placeId destination place
     */
    public ExpertPlayMessage(String sender, String move, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), fromColor, toColor, placeId});
        Class<?>[] tmp = new Class<?>[]{String.class, TeacherColor.class, TeacherColor.class, String.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the character card
     *
     * @param sender sender
     * @param move move
     * @param character chosen character
     */
    public ExpertPlayMessage(String sender, String move, Characters character) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), character});
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the character card who needs an island
     *
     * @param sender sender
     * @param move move
     * @param character chosen character
     * @param id chosen island
     */
    public ExpertPlayMessage(String sender, String move, Characters character, String id) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), character, id});
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, String.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the character card who needs a color
     *
     * @param sender sender
     * @param move move
     * @param character chosen character
     * @param color chosen color
     */
    public ExpertPlayMessage(String sender, String move, Characters character, TeacherColor color) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), character, color});
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, TeacherColor.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the status of students in character card
     *
     * @param sender sender
     * @param move move
     * @param character chosen character
     * @param quantity a map with for each color the number of students
     */
    public ExpertPlayMessage(String sender, String move, Characters character, Map<TeacherColor, Integer> quantity) {
        super(sender, move, messageType);
        super.setTeacherColorIntegerMap(quantity);
        super.setParams(new Object[]{super.getSenderName(), character, "getTeacherColorIntegerMap"});
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, Map.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the status of coins of players and coins of character cards
     *
     * @param sender sender
     * @param move move
     * @param money a map with for each player the coins he has
     */
    public ExpertPlayMessage(String sender, String move, Map<String, Integer> money) {
        super(sender, move, messageType);
        super.setStringIntegerMap(money);
        super.setParams(new Object[]{super.getSenderName(), "getStringIntegerMap"});
        Class<?>[] tmp = new Class<?>[]{String.class, Map.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    /**
     * Constructor of the message for the status of prohibition cards
     *
     * @param sender sender
     * @param move move
     * @param ids a list of islands with prohibition cards
     */
    public ExpertPlayMessage(String sender, String move, List<String> ids) {
        super(sender, move, messageType);
        super.setStringList(ids);
        super.setParams(new Object[]{super.getSenderName(), "getStringList"});
        Class<?>[] tmp = new Class<?>[]{String.class, List.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }
}
