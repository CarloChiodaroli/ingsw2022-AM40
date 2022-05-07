package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.table.Island;
import it.polimi.ingsw.network.Message.Message;
import it.polimi.ingsw.network.Message.MessageReader;
import it.polimi.ingsw.network.Message.MessageType;
import it.polimi.ingsw.network.Message.PlayMessage;
import it.polimi.ingsw.network.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private GameController controller;
    private Game game;
    private Player aldo;
    private Player giovanni;
    private final static String aldoName = "Aldo";
    private final static String giovanniName = "Giovanni";
    private final static String serverName = "Server";
    private final int giovanniAssistantCardValue = 5;
    private final int aldoAssistantCardValue = 3;
    private GameManager manager;
    private FakeServer server;

    @BeforeEach
    public void initTest() {
        controller = new GameController();

        controller.addPlayer(aldoName);
        controller.addPlayer(giovanniName);
        controller.switchExpertVariant();
        controller.startGame();
        game = controller.getModel().getGame();

        aldo = game.getPlayers().get(0);
        giovanni = game.getPlayers().get(1);

        for (TeacherColor color : TeacherColor.values()) {
            for (int i = aldo.getEntrance().howManyStudents(color); i > 0; i--) {
                aldo.getEntrance().removeStudent(color);
            }
            for (int j = giovanni.getEntrance().howManyStudents(color); j > 0; j--) {
                giovanni.getEntrance().removeStudent(color);
            }
            for (Island island : game.getTable().getIslandList()) {
                for (int k = island.howManyStudents(color); k > 0; k--) {
                    island.removeStudent(color);
                }
            }
        }

        controller.playAssistantCard(aldoName, aldoAssistantCardValue);
        controller.playAssistantCard(giovanniName, giovanniAssistantCardValue);

        server = new FakeServer(controller);

        manager = new GameManager(server, controller);
    }

    @Test
    public void messageSendTest()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        PlayMessage question;
        aldo.getEntrance().addStudent(TeacherColor.BLUE);
        question = new PlayMessage(aldoName, TeacherColor.BLUE, "Entrance", "Room");

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        String gsonSerialization = gson.toJson(question);

        Gson ggson = builder.create();
        String gsonSeSerialization = ggson.toJson(question);
        Message missage = ggson.fromJson(gsonSeSerialization, Message.class);
        assertEquals(MessageType.PLAY, missage.getMessageType());

        PlayMessage message = gson.fromJson(gsonSerialization, PlayMessage.class);
        message.executeMessage(manager);
        assertEquals(1, aldo.getRoomTable(TeacherColor.BLUE).howManyTotStudents());


    }

    @Test
    public void message1Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        GsonBuilder builder;
        Gson gson;
        PlayMessage message;
        String gsonSerialization;
        question = new PlayMessage(aldoName, TeacherColor.BLUE, "Entrance", "Room");
        builder = new GsonBuilder().setPrettyPrinting();
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);
    }

    @Test
    public void message2Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson;
        PlayMessage message;
        String gsonSerialization;

        question = new PlayMessage(aldoName, TeacherColor.BLUE, TeacherColor.GREEN, "Room");
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);
    }

    @Test
    public void message3Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson;
        PlayMessage message;
        String gsonSerialization;

        question = new PlayMessage(aldoName, 2);
        gson = builder.create();
        gsonSerialization = gson.toJson(question);

        System.out.println(gsonSerialization);

        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);

    }

    @Test
    public void message4Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson;
        PlayMessage message;
        String gsonSerialization;

        question = new PlayMessage(aldoName, "c_1");
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);

    }

    @Test
    public void message5Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson;
        PlayMessage message;
        String gsonSerialization;


        question = new PlayMessage(aldoName, Characters.CRIER);
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);

    }

    @Test
    public void message6Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson;
        PlayMessage message;
        String gsonSerialization;

        question = new PlayMessage(aldoName, Characters.CRIER, "i_1");
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);

    }

    @Test
    public void message7Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson;
        PlayMessage message;
        String gsonSerialization;


        question = new PlayMessage(aldoName, Characters.CRIER, TeacherColor.GREEN);
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);

    }

    @Test
    public void message8Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson;
        PlayMessage message;
        String gsonSerialization;


        Map<TeacherColor, Integer> testMapA = new HashMap<>();
        int i = 1;
        for (TeacherColor color : TeacherColor.values()) {
            testMapA.put(color, i++);
        }
        question = new PlayMessage(serverName, "Entrance", testMapA);
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        System.out.println(gsonSerialization);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertEquals(testMapA, message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);

    }

    @Test
    public void message9Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson;
        PlayMessage message;
        String gsonSerialization;

        System.out.println("--------------------------------");

        Map<String, Optional<TowerColor>> testMapB = new HashMap<>();
        Map<String, TowerColor> testMapC = new HashMap<>();
        testMapB.put("i_1", Optional.empty());
        testMapB.put("i_2", Optional.of(TowerColor.WHITE));
        testMapB.put("i_3", Optional.of(TowerColor.BLACK));
        testMapB.put("i_4", Optional.of(TowerColor.WHITE));
        testMapB.put("i_5", Optional.of(TowerColor.BLACK));
        testMapB.put("i_6", Optional.of(TowerColor.BLACK));
        testMapB.put("i_7", Optional.of(TowerColor.BLACK));
        testMapB.put("i_8", Optional.of(TowerColor.WHITE));
        testMapB.put("i_9", Optional.empty());
        testMapB.put("i_10", Optional.of(TowerColor.BLACK));
        testMapB.put("i_11", Optional.of(TowerColor.WHITE));
        testMapB.put("i_12", Optional.empty());
        testMapC.put("i_2", TowerColor.WHITE);
        testMapC.put("i_3", TowerColor.BLACK);
        testMapC.put("i_4", TowerColor.WHITE);
        testMapC.put("i_5", TowerColor.BLACK);
        testMapC.put("i_6", TowerColor.BLACK);
        testMapC.put("i_7", TowerColor.BLACK);
        testMapC.put("i_8", TowerColor.WHITE);
        testMapC.put("i_10", TowerColor.BLACK);
        testMapC.put("i_11", TowerColor.WHITE);
        question = new PlayMessage(serverName, testMapB);
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        System.out.println(gsonSerialization);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertEquals(testMapC, message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);

    }

    @Test
    public void message10Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson;
        PlayMessage message;
        String gsonSerialization;


        System.out.println("--------------------------------");

        List<TeacherColor> testList = new ArrayList<>();
        testList.add(TeacherColor.BLUE);
        testList.add(TeacherColor.PINK);
        testList.add(TeacherColor.GREEN);
        question = new PlayMessage(serverName, aldoName, testList);
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        System.out.println(gsonSerialization);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertEquals(testList, message.getTeacherColorList());
        message.executeMessage(manager);
    }
}

class FakeServer extends Server {
    public FakeServer(GameController controller) {
        super(controller);
    }
}

class FakeGameManager implements MessageReader {

    @Override
    public void move(String player, TeacherColor color, String fromId, String toId) {
        assertEquals(String.class, player.getClass());
        assertEquals(TeacherColor.class, color.getClass());
        assertEquals(String.class, fromId.getClass());
        assertEquals(String.class, toId.getClass());
    }

    @Override
    public void move(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        assertEquals(String.class, player.getClass());
        assertEquals(TeacherColor.class, fromColor.getClass());
        assertEquals(TeacherColor.class, toColor.getClass());
        assertEquals(String.class, placeId.getClass());
    }

    @Override
    public void move(String player, Integer hops) {
        assertEquals(String.class, player.getClass());
        assertEquals(2, hops);
    }

    @Override
    public void move(String player, String id) {
        assertEquals(String.class, player.getClass());
        assertEquals(String.class, id.getClass());
    }

    @Override
    public void move(String player, Characters character) {
        assertEquals(String.class, player.getClass());
        assertEquals(Characters.class, character.getClass());
    }

    @Override
    public void move(String player, Characters character, String id) {
        assertEquals(String.class, player.getClass());
        assertEquals(Characters.class, character.getClass());
        assertEquals(String.class, id.getClass());
    }

    @Override
    public void move(String player, Characters character, TeacherColor color) {
        assertEquals(String.class, player.getClass());
        assertEquals(Characters.class, character.getClass());
        assertEquals(TeacherColor.class, color.getClass());
    }

    @Override
    public void move(String sender, String id, Map<TeacherColor, Integer> quantity) {
        assertEquals(String.class, sender.getClass());
        assertEquals(String.class, id.getClass());
        assertTrue(Arrays.stream(quantity.getClass().getInterfaces()).collect(Collectors.toList()).contains(Map.class));
    }

    @Override
    public void move(String sender, String id, List<TeacherColor> which) {
        assertEquals(String.class, sender.getClass());
        assertEquals(String.class, id.getClass());
        assertTrue(Arrays.stream(which.getClass().getInterfaces()).collect(Collectors.toList()).contains(List.class));
    }

    @Override
    public void move(String sender, Map<String, Optional<TowerColor>> conquests) {
        assertEquals(String.class, sender.getClass());
        assertTrue(Arrays.stream(conquests.getClass().getInterfaces()).collect(Collectors.toList()).contains(Map.class));
    }

    @Override
    public void move(String sender, List<String> ids) {
        assertEquals(String.class, sender.getClass());
        assertTrue(Arrays.stream(ids.getClass().getInterfaces()).collect(Collectors.toList()).contains(List.class));
    }
}
