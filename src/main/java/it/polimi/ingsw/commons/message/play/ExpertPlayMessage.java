package it.polimi.ingsw.commons.message.play;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.message.MessageType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpertPlayMessage extends PlayMessage {

    private final static MessageType messageType = MessageType.EXPERT;

    public ExpertPlayMessage(String sender, String move, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        super(sender,move, messageType);
        super.setParams(new Object[]{super.getSenderName(), fromColor, toColor, placeId});
        Class<?>[] tmp = new Class<?>[]{String.class, TeacherColor.class, TeacherColor.class, String.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public ExpertPlayMessage(String sender, String move, Characters character) {
        super(sender,move, messageType);
        super.setParams(new Object[]{super.getSenderName(), character});
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public ExpertPlayMessage(String sender, String move, Characters character, String id) {
        super(sender,move, messageType);
        super.setParams(new Object[]{super.getSenderName(), character, id});
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, String.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    public ExpertPlayMessage(String sender, String move, Characters character, TeacherColor color) {
        super(sender,move, messageType);
        super.setParams(new Object[]{super.getSenderName(), character, color});
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, TeacherColor.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }
    
    public ExpertPlayMessage(String sender, String move, Characters character, Map<TeacherColor, Integer> quantity){
        super(sender,move, messageType);
        super.setTeacherColorIntegerMap(quantity);
        super.setParams(new Object[]{super.getSenderName(), character, "getTeacherColorIntegerMap"});
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, Map.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }

    //@Deprecated
    /*
    public ExpertPlayMessage(String sender, String move, Map<Characters, Integer> availableCharacters){
        super(sender,move, messageType);
        super.setCharactersIntegerMap(availableCharacters);
        super.setParams(new Object[]{super.getSenderName(), "getCharactersIntegerMap"});
        Class<?>[] tmp = new Class<?>[]{String.class, List.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }*/

    public ExpertPlayMessage(String sender, String move, Map<String, Integer> money){
        super(sender, move, messageType);
        super.setStringIntegerMap(money);
        super.setParams(new Object[]{super.getSenderName(), "getStringIntegerMap"});
        Class<?>[] tmp = new Class<?>[]{String.class, List.class};
        super.setParamsTypeNames(Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList()));
        super.message();
    }
}
