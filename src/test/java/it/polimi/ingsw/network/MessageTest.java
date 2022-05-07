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
import it.polimi.ingsw.network.Message.PlayMessage;
import it.polimi.ingsw.network.Server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
    public void messageSendTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        PlayMessage question;

        aldo.getEntrance().addStudent(TeacherColor.BLUE);

        question = new PlayMessage(aldoName, TeacherColor.BLUE, "Entrance", "Room");

        // is sent --- travels --- is received

        question.executeMessage(manager);

        assertEquals(1, aldo.getRoomTable(TeacherColor.BLUE).howManyTotStudents());
    }

    @Test
    public void messageGsonCreationTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException {

        PlayMessage question = new PlayMessage(aldoName, TeacherColor.BLUE, "Entrance", "Room");
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        Gson guguson = builder.create();
        String gsonSerialization = gson.toJson(question);
        String gugusononon = guguson.toJson(question);
        // assertDoesNotThrow(() -> {Message massage = guguson.fromJson(gugusononon, Message.class);}); issue to solve
        PlayMessage message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());

        question = new PlayMessage(aldoName, TeacherColor.BLUE, TeacherColor.GREEN, "Room");
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());

        question = new PlayMessage(aldoName, 2);
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());

        question = new PlayMessage(aldoName, "c_1");
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());

        question = new PlayMessage(aldoName, Characters.CRIER);
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());

        question = new PlayMessage(aldoName, Characters.CRIER, "i_1");
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());

        question = new PlayMessage(aldoName, Characters.CRIER, TeacherColor.GREEN);
        gson = builder.create();
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());

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
    }
}

class FakeServer extends Server {

    public FakeServer(GameController controller) {
        super(controller);
    }
}
