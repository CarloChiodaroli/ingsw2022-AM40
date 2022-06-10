package it.polimi.ingsw.commons.message.play;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.message.Message;
import it.polimi.ingsw.commons.message.MessageType;
import it.polimi.ingsw.commons.message.PlayMessageReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

// Removed Javadoc to make this class less overwhelming
public class NormalPlayMessage extends PlayMessage {

    private final static MessageType messageType = MessageType.PLAY;

    public NormalPlayMessage(String sender, String move, TeacherColor color, String fromId, String toId) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), color, fromId, toId});
        Class<?>[] tmp = new Class<?>[]{String.class, TeacherColor.class, String.class, String.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public NormalPlayMessage(String sender, String move, Integer hops) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), hops});
        Class<?>[] tmp = new Class<?>[]{String.class, Integer.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public NormalPlayMessage(String sender, String move, String id) {
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), id});
        Class<?>[] tmp = new Class<?>[]{String.class, String.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public NormalPlayMessage(String sender, String move, String id, Map<TeacherColor, Integer> quantity) {
        super(sender, move, messageType);
        super.setTeacherColorIntegerMap(new HashMap<>(quantity));
        super.setParams(new Object[]{super.getSenderName(), id, "getTeacherColorIntegerMap"});
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, Map.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public NormalPlayMessage(String sender, String move, String id, List<TeacherColor> which) {
        super(sender, move, messageType);
        super.setTeacherColorList(new ArrayList<>(which));
        super.setParams(new Object[]{super.getSenderName(), id, "getTeacherColorList"});
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, List.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public NormalPlayMessage(String sender, String move, List<String> which) {
        super(sender, move, messageType);
        super.setStringList(new ArrayList<>(which));
        super.setParams(new Object[]{super.getSenderName(), "getStringList"});
        Class<?>[] tmp = new Class<?>[]{String.class, List.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public NormalPlayMessage(String sender, String move, Map<String, TowerColor> conquests) {
        super(sender, move, messageType);
        super.setStringTowerColorMap(conquests);
        super.setParams(new Object[]{super.getSenderName(), "getStringTowerColorMap"});
        Class<?>[] tmp = new Class<?>[]{String.class, Map.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public NormalPlayMessage(String sender, String move){
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName()});
        Class<?>[] tmp = new Class<?>[]{String.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public NormalPlayMessage(String sender, String move, String player, TowerColor color){
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), player, color});
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, TowerColor.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public NormalPlayMessage(String sender, String move, String player, Integer weight){
        super(sender, move, messageType);
        super.setParams(new Object[]{super.getSenderName(), player, weight});
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, Integer.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }
}
