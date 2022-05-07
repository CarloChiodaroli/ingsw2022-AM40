package it.polimi.ingsw.network.Message;

import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class PlayMessage extends Message {

    private Object[] params;
    private List<String> paramsTypeNames;
    private Map<String, TowerColor> stringTowerColorMap = null;
    private Map<TeacherColor, Integer> teacherColorIntegerMap = null;
    private List<TeacherColor> teacherColorList;
    private final static MessageType messageType = MessageType.PLAY;

    private static Map<Class<?>, Method> getParserMap() throws NoSuchMethodException {
        Map<Class<?>, Method> result = new HashMap<>();
        result.put(int.class, PlayMessage.class.getDeclaredMethod("getInt", String.class));
        result.put(String.class, PlayMessage.class.getDeclaredMethod("getString", String.class));
        result.put(List.class, PlayMessage.class.getDeclaredMethod("getList", String.class));
        result.put(Map.class, PlayMessage.class.getDeclaredMethod("getMap", String.class));
        result.put(Enum.class, PlayMessage.class.getDeclaredMethod("getEnum", Class.class, String.class));
        return result;
    }

    public PlayMessage(String sender) throws NoSuchMethodException {
        super(sender, MessageType.PLAY);
    }

    public void executeMessage(MessageReader manager) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException, InstantiationException {
        super.controlWritten();
        Map<Class<?>, Method> parser = getParserMap();
        List<Class<?>> paramClassList = new ArrayList<>();
        for (String name : paramsTypeNames) {
            paramClassList.add(Class.forName(name));
        }
        Class<?>[] paramClassArray = paramClassList.toArray(this::intFunction);
        Method method = MessageReader.class.getMethod("move", paramClassArray);
        paramClassArray[0].isEnum();
        for (int i = 0; i < paramClassArray.length; i++) {
            if (paramClassArray[i].isEnum()) {  // need to solve issue
                String data = params[i].toString();
                Method valueOf = paramClassArray[i].getMethod("valueOf", String.class);
                params[i] = valueOf.invoke(paramClassArray[i], data);
            } else {
                params[i] = parser.get(paramClassArray[i]).invoke(this, params[i]);
            }
        }
        method.invoke(manager, params);
    }

    // public only for testing
    public Map<TeacherColor, Integer> getTeacherColorIntegerMap() {
        return this.teacherColorIntegerMap;
    }

    // public only for testing
    public Map<String, TowerColor> getStringTowerColorMap() {
        return this.stringTowerColorMap;
    }

    // public only for testing
    public List<TeacherColor> getTeacherColorList() {
        return this.teacherColorList;
    }

    private Map<?, ?> getMap(String data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (Map<?, ?>) PlayMessage.class.getDeclaredMethod(data).invoke(this);
    }

    private List<?> getList(String data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (List<?>) PlayMessage.class.getDeclaredMethod(data).invoke(this);
    }

    private String getString(String data) {
        return data;
    }

    private int getInt(String data) {
        return Integer.parseInt(data);
    }

    private Object getEnum(Class<?> cla, String data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String puuu = data.toString(); // well naming is hard // issue to solve
        Method puuuj = cla.getMethod("valueOf", String.class);
        return puuuj.invoke(cla, puuu);
    }

    private Class<?>[] intFunction(int size) {
        return new Class<?>[size];
    }

    public PlayMessage(String sender, TeacherColor color, String fromId, String toId) throws NoSuchMethodException {
        super(sender, messageType);
        params = new Object[]{super.getPlayerName(), color, fromId, toId};
        Class<?>[] tmp = new Class<?>[]{String.class, TeacherColor.class, String.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, TeacherColor fromColor, TeacherColor toColor, String placeId) throws NoSuchMethodException {
        super(sender, messageType);
        params = new Object[]{super.getPlayerName(), fromColor, toColor, placeId};
        Class<?>[] tmp = new Class<?>[]{String.class, TeacherColor.class, TeacherColor.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, int hops) throws NoSuchMethodException {
        super(sender, messageType);
        params = new Object[]{super.getPlayerName(), hops};
        Class<?>[] tmp = new Class<?>[]{String.class, int.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String id) throws NoSuchMethodException {
        super(sender, messageType);
        params = new Object[]{super.getPlayerName(), id};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, Characters character) throws NoSuchMethodException {
        super(sender, messageType);
        params = new Object[]{super.getPlayerName(), character};
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, Characters character, String id) throws NoSuchMethodException {
        super(sender, messageType);
        params = new Object[]{super.getPlayerName(), character, id};
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, Characters character, TeacherColor color) throws NoSuchMethodException {
        super(sender, messageType);
        params = new Object[]{super.getPlayerName(), character, color};
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, TeacherColor.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String id, Map<TeacherColor, Integer> quantity) throws NoSuchMethodException {
        super(sender, messageType);
        teacherColorIntegerMap = new HashMap<>(quantity);
        params = new Object[]{super.getPlayerName(), id, "getTeacherColorIntegerMap"};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, Map.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String id, List<TeacherColor> which) throws NoSuchMethodException {
        super(sender, messageType);
        teacherColorList = new ArrayList<>(which);
        params = new Object[]{super.getPlayerName(), id, "getTeacherColorList"};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, List.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, Map<String, Optional<TowerColor>> conquests) throws NoSuchMethodException {
        super(sender, messageType);
        stringTowerColorMap = new HashMap<>();
        for (String place : conquests.keySet()) {
            if (conquests.get(place).isPresent()) {
                stringTowerColorMap.put(place, conquests.get(place).get());
            }
        }
        params = new Object[]{super.getPlayerName(), "getStringTowerColorMap"};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, Map.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }
}
