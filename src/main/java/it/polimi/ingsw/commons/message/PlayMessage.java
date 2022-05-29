package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.enums.Characters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

// Removed Javadoc to make this class less overwhelming
public class PlayMessage extends Message {

    private final Object[] params;
    private final List<String> paramsTypeNames;
    private Map<String, TowerColor> stringTowerColorMap = null;
    private Map<TeacherColor, Integer> teacherColorIntegerMap = null;
    private List<TeacherColor> teacherColorList;
    private List<String> stringList;
    private final static MessageType messageType = MessageType.PLAY;
    private final String move;


    private static Map<Class<?>, Method> getParserMap() throws NoSuchMethodException {
        Map<Class<?>, Method> result = new HashMap<>();
        result.put(Integer.class, PlayMessage.class.getDeclaredMethod("getInt", String.class));
        result.put(String.class, PlayMessage.class.getDeclaredMethod("getString", String.class));
        result.put(List.class, PlayMessage.class.getDeclaredMethod("getList", String.class));
        result.put(Map.class, PlayMessage.class.getDeclaredMethod("getMap", String.class));
        result.put(TeacherColor.class, TeacherColor.class.getMethod("valueOf", String.class));
        result.put(TowerColor.class, TowerColor.class.getMethod("valueOf", String.class));
        result.put(Characters.class, Characters.class.getMethod("valueOf", String.class));
        return result;
    }

    public void executeMessage(MessageReader manager) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
        super.controlWritten();
        Map<Class<?>, Method> parser = getParserMap();
        List<Class<?>> paramClassList = new ArrayList<>();
        for (String name : paramsTypeNames) {
            paramClassList.add(Class.forName(name));
        }
        Class<?>[] paramClassArray = paramClassList.toArray(this::intFunction);
        for (int i = 0; i < paramClassArray.length; i++) {
            params[i] = parser.get(paramClassArray[i]).invoke(this, params[i].toString());
        }
        Method method = MessageReader.class.getMethod(move, paramClassArray);
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

    public List<String> getStringList(){
        return this.stringList;
    }

    private Map<?, ?> getMap(String data) {
        Map<String, Map<?, ?>> baseMap = new HashMap<>();
        baseMap.put("getTeacherColorIntegerMap", teacherColorIntegerMap);
        baseMap.put("getStringTowerColorMap", stringTowerColorMap);
        return new HashMap<>(baseMap.get(data));
    }

    private List<?> getList(String data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (List<?>) PlayMessage.class.getDeclaredMethod(data).invoke(this);
    }

    private String getString(String data) {
        return data;
    }

    private Integer getInt(String data) {
        Double tmp = Double.parseDouble(data); // Needs to be a Double... do not listen to intellij
        return tmp.intValue();
    }

    private TeacherColor getTeacherColor(String data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method valueOf = TeacherColor.class.getMethod("valueOf", String.class);
        System.out.println(valueOf);
        return (TeacherColor) valueOf.invoke(TeacherColor.class, data);
    }

    private TowerColor getTowerColor(String data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method valueOf = TowerColor.class.getMethod("valueOf", String.class);
        return (TowerColor) valueOf.invoke(TowerColor.class, data);
    }

    private Characters getCharacters(String data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method valueOf = Characters.class.getMethod("valueOf", String.class);
        return (Characters) valueOf.invoke(Characters.class, data);
    }

    private Class<?>[] intFunction(int size) {
        return new Class<?>[size];
    }

    public PlayMessage(String sender, String move, TeacherColor color, String fromId, String toId) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), color, fromId, toId};
        Class<?>[] tmp = new Class<?>[]{String.class, TeacherColor.class, String.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), fromColor, toColor, placeId};
        Class<?>[] tmp = new Class<?>[]{String.class, TeacherColor.class, TeacherColor.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, Integer hops) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), hops};
        Class<?>[] tmp = new Class<?>[]{String.class, Integer.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, String id) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), id};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, Characters character) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), character};
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, Characters character, String id) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), character, id};
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, Characters character, TeacherColor color) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), character, color};
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, TeacherColor.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, String id, Map<TeacherColor, Integer> quantity) {
        super(sender, messageType);
        this.move = move;
        teacherColorIntegerMap = new HashMap<>(quantity);
        params = new Object[]{super.getSenderName(), id, "getTeacherColorIntegerMap"};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, Map.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, String id, List<TeacherColor> which) {
        super(sender, messageType);
        this.move = move;
        teacherColorList = new ArrayList<>(which);
        params = new Object[]{super.getSenderName(), id, "getTeacherColorList"};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, List.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, List<String> which) {
        super(sender, messageType);
        this.move = move;
        stringList = new ArrayList<>(which);
        params = new Object[]{super.getSenderName(), "getStringList"};
        Class<?>[] tmp = new Class<?>[]{String.class, List.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, Map<String, TowerColor> conquests) {
        super(sender, messageType);
        this.move = move;
        stringTowerColorMap = conquests;
        params = new Object[]{super.getSenderName(), "getStringTowerColorMap"};
        Class<?>[] tmp = new Class<?>[]{String.class, Map.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move){
        super(sender, MessageType.PLAY);
        this.move = move;
        params = new Object[]{super.getSenderName()};
        Class<?>[] tmp = new Class<?>[]{String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, String player, TowerColor color){
        super(sender, MessageType.PLAY);
        this.move = move;
        params = new Object[]{super.getSenderName(), player, color};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, TowerColor.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move, String player, Integer weight){
        super(sender, MessageType.PLAY);
        this.move = move;
        params = new Object[]{super.getSenderName(), player, weight};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, Integer.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }
}
