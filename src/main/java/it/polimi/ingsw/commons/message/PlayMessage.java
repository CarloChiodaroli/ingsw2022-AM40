package it.polimi.ingsw.commons.message;

import it.polimi.ingsw.server.model.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class PlayMessage extends Message {

    private final Object[] params;
    private final List<String> paramsTypeNames;
    private Map<String, TowerColor> stringTowerColorMap = null;
    private Map<TeacherColor, Integer> teacherColorIntegerMap = null;
    private List<TeacherColor> teacherColorList;
    private final static MessageType messageType = MessageType.PLAY;
    private final String move;


    /**
     * Initializes the map which associates the Class of a parameter and its parser method
     * @return the associating map
     * @throws NoSuchMethodException if one of the methods is not found... it should not be thrown
     */
    private static Map<Class<?>, Method> getParserMap() throws NoSuchMethodException {
        Map<Class<?>, Method> result = new HashMap<>();
        result.put(Integer.class, PlayMessage.class.getDeclaredMethod("getInt", String.class));
        result.put(String.class, PlayMessage.class.getDeclaredMethod("getString", String.class));
        result.put(List.class, PlayMessage.class.getDeclaredMethod("getList", String.class));
        result.put(Map.class, PlayMessage.class.getDeclaredMethod("getMap", String.class));
        result.put(TeacherColor.class, PlayMessage.class.getDeclaredMethod("getTeacherColor", String.class));
        result.put(TowerColor.class, PlayMessage.class.getDeclaredMethod("getTowerColor", String.class));
        result.put(Characters.class, PlayMessage.class.getDeclaredMethod("getCharacters", String.class));
        return result;
    }

    /**
     * Runs the Message content, calling the appropriate method with
     * given name (Constructor's "move" parameter), and with given parameters (Other Constructor's parameter (wh\ sender))
     * from the manager
     * @param manager the manager class which needs to run the before mentioned method
     * @throws IllegalAccessException {@link java.lang.reflect.Method}'s invoke
     * @throws InvocationTargetException {@link java.lang.reflect.Method}'s invoke
     * @throws NoSuchMethodException if sent move parameter is not valid
     * @throws ClassNotFoundException if the parsing from class's name to class fails
     */
    public void executeMessage(MessageReader manager) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ClassNotFoundException{
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
    /**
     * Getter of saved Map which saves how many students are in a place (For testing purposes)
     * @return the map
     */
    public Map<TeacherColor, Integer> getTeacherColorIntegerMap() {
        return this.teacherColorIntegerMap;
    }

    // public only for testing
    /**
     * Getter of saved Map which saves which islands have which towers (For testing purposes)
     * @return the map
     */
    public Map<String, TowerColor> getStringTowerColorMap() {
        return this.stringTowerColorMap;
    }

    // public only for testing
    /**
     * Getter of saved List which saves which Teachers a player has (For testing purposes)
     * @return the map
     */
    public List<TeacherColor> getTeacherColorList() {
        return this.teacherColorList;
    }

    /**
     * "Parser" of the Map equivalent string saved in command parameters
     * @param data the string saved in the command parameters
     * @return the map
     */
    private Map<?, ?> getMap(String data) {
        Map<String, Map<?, ?>> baseMap = new HashMap<>();
        baseMap.put("getTeacherColorIntegerMap", teacherColorIntegerMap);
        baseMap.put("getStringTowerColorMap", stringTowerColorMap);
        return new HashMap<>(baseMap.get(data));
    }

    /**
     * "Parser" of the List equivalent string saved in command parameters
     * @param data the string saved in the command parameters
     * @return the list
     * @throws IllegalAccessException {@link java.lang.reflect.Method}'s invoke
     * @throws InvocationTargetException {@link java.lang.reflect.Method}'s invoke
     * @throws NoSuchMethodException if sent move parameter is not valid
     */
    private List<?> getList(String data) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (List<?>) PlayMessage.class.getDeclaredMethod(data).invoke(this);
    }

    /**
     * Parser of a string to a string
     * @param data the saved string
     * @return the parsed string
     */
    private String getString(String data) {
        return data;
    }

    /**
     * Parser of a string to an Integer
     * @param data the saved string
     * @return the parsed integer
     */
    private Integer getInt(String data) {
        Double tmp = Double.parseDouble(data); // Needs to be a Double... do not listen to intellij
        return  tmp.intValue();
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

    public PlayMessage(String sender, String move,  TeacherColor color, String fromId, String toId){
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), color, fromId, toId};
        Class<?>[] tmp = new Class<?>[]{String.class, TeacherColor.class, String.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move,  TeacherColor fromColor, TeacherColor toColor, String placeId) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), fromColor, toColor, placeId};
        Class<?>[] tmp = new Class<?>[]{String.class, TeacherColor.class, TeacherColor.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move,  Integer hops) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), hops};
        Class<?>[] tmp = new Class<?>[]{String.class, Integer.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move,  String id) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), id};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move,  Characters character) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), character};
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move,  Characters character, String id) {
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), character, id};
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, String.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move,  Characters character, TeacherColor color){
        super(sender, messageType);
        this.move = move;
        params = new Object[]{super.getSenderName(), character, color};
        Class<?>[] tmp = new Class<?>[]{String.class, Characters.class, TeacherColor.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move,  String id, Map<TeacherColor, Integer> quantity){
        super(sender, messageType);
        this.move = move;
        teacherColorIntegerMap = new HashMap<>(quantity);
        params = new Object[]{super.getSenderName(), id, "getTeacherColorIntegerMap"};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, Map.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move,  String id, List<TeacherColor> which){
        super(sender, messageType);
        this.move = move;
        teacherColorList = new ArrayList<>(which);
        params = new Object[]{super.getSenderName(), id, "getTeacherColorList"};
        Class<?>[] tmp = new Class<?>[]{String.class, String.class, List.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }

    public PlayMessage(String sender, String move,  Map<String, Optional<TowerColor>> conquests){
        super(sender, messageType);
        this.move = move;
        stringTowerColorMap = new HashMap<>();
        for (String place : conquests.keySet()) {
            if (conquests.get(place).isPresent()) {
                stringTowerColorMap.put(place, conquests.get(place).get());
            }
        }
        params = new Object[]{super.getSenderName(), "getStringTowerColorMap"};
        Class<?>[] tmp = new Class<?>[]{String.class, Map.class};
        paramsTypeNames = Arrays.stream(tmp).map(Class::getName).collect(Collectors.toList());
        super.message();
    }
}
