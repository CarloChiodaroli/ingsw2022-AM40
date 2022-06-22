package it.polimi.ingsw.commons.message.play;

import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.commons.message.Message;
import it.polimi.ingsw.commons.message.MessageType;
import it.polimi.ingsw.commons.message.PlayMessageReader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all common traits of {@link NormalPlayMessage} and {@link ExpertPlayMessage}, their memory and their execution
 */
public class PlayMessage extends Message {

    private final String move;
    private Object[] params;
    private List<String> paramsTypeNames;
    private Map<String, TowerColor> stringTowerColorMap = null;
    private Map<TeacherColor, Integer> teacherColorIntegerMap = null;
    private Map<String, Integer> stringIntegerMap;
    private List<TeacherColor> teacherColorList;
    private List<String> stringList;

    PlayMessage(String sender, String move, MessageType type) {
        super(sender, type);
        this.move = move;
    }

    void setParams(Object[] params) {
        this.params = params;
    }

    void setParamsTypeNames(List<String> paramsTypeNames) {
        this.paramsTypeNames = paramsTypeNames;
    }

    void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    void setStringTowerColorMap(Map<String, TowerColor> stringTowerColorMap) {
        this.stringTowerColorMap = stringTowerColorMap;
    }

    void setTeacherColorIntegerMap(Map<TeacherColor, Integer> teacherColorIntegerMap) {
        this.teacherColorIntegerMap = teacherColorIntegerMap;
    }

    void setTeacherColorList(List<TeacherColor> teacherColorList) {
        this.teacherColorList = teacherColorList;
    }

    void setStringIntegerMap(Map<String, Integer> stringIntegerMap) {
        this.stringIntegerMap = stringIntegerMap;
    }

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

    public void executeMessage(PlayMessageReader manager) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException {
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
        Method method = PlayMessageReader.class.getMethod(move, paramClassArray);
        method.invoke(manager, params);
    }

    private Map<TeacherColor, Integer> getTeacherColorIntegerMap() {
        return this.teacherColorIntegerMap;
    }

    private Map<String, TowerColor> getStringTowerColorMap() {
        return this.stringTowerColorMap;
    }

    private List<TeacherColor> getTeacherColorList() {
        return this.teacherColorList;
    }

    private Map<String, Integer> getStringIntegerMap() {
        return this.stringIntegerMap;
    }

    private List<String> getStringList() {
        return this.stringList;
    }

    private Map<?, ?> getMap(String data) {
        Map<String, Map<?, ?>> baseMap = new HashMap<>();
        baseMap.put("getTeacherColorIntegerMap", teacherColorIntegerMap);
        baseMap.put("getStringTowerColorMap", stringTowerColorMap);
        baseMap.put("getStringIntegerMap", stringIntegerMap);
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
}
