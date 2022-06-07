package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.commons.message.*;
import it.polimi.ingsw.server.controller.inner.InboundController;
import it.polimi.ingsw.server.controller.inner.OutboundController;
import it.polimi.ingsw.server.controller.inner.TurnController;
import it.polimi.ingsw.server.controller.outer.GameManager;
import it.polimi.ingsw.server.controller.outer.PlayMessagesReader;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.GameModel;
import it.polimi.ingsw.commons.enums.Characters;
import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.commons.enums.TowerColor;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.table.Island;
import it.polimi.ingsw.server.network.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private Game game;
    private Player aldo;
    private Player giovanni;
    private final static String aldoName = "Aldo";
    private final static String giovanniName = "Giovanni";
    private final static String serverName = "Server";
    private final int giovanniAssistantCardValue = 5;
    private final int aldoAssistantCardValue = 3;
    private FakeServer server;
    private Gson gson;

    @Test
    public void minestroneTest() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        GameManager gameManager = new GameManager();
        PlayMessagesReader reader = new PlayMessagesReader(aldoName, gameManager);
        reader.setNumOfPlayers(2);

        reader.addPlayer(giovanniName);
        reader.startGame();

        InboundController inbound = reader.getInbound();
        OutboundController outbound = reader.getOutbound();
        GameModel model = inbound.getModel();
        game = model.getGame();
        TurnController turn = reader.getTurnController();

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
        reader.playAssistantCard(aldoName, aldoAssistantCardValue);
        reader.playAssistantCard(giovanniName, giovanniAssistantCardValue);

        PlayMessage question;
        aldo.getEntrance().addStudent(TeacherColor.BLUE);
        question = PlayMessagesFabric.moveStudent(aldoName, TeacherColor.BLUE, "Entrance", "Room");

        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        String gsonSerialization = gson.toJson(question);

        Gson ggson = builder.create();
        String gsonSeSerialization = ggson.toJson(question);
        Message missage = ggson.fromJson(gsonSeSerialization, Message.class);
        assertEquals(MessageType.PLAY, missage.getMessageType());

        PlayMessage message = gson.fromJson(gsonSerialization, PlayMessage.class);
        message.executeMessage(reader);
        assertEquals(1, outbound.getStudentInPlace(aldoName, "Room").get(TeacherColor.BLUE));
    }

    @BeforeEach
    public void initAll(){
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    @Test
    public void message1Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        PlayMessage message;
        String gsonSerialization;
        question = new PlayMessage(aldoName, "moveStudent", TeacherColor.BLUE, "Entrance", "Room");
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
        PlayMessage message;
        String gsonSerialization;

        question = new PlayMessage(aldoName, "moveStudent", TeacherColor.BLUE, TeacherColor.GREEN, "Room");
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
        PlayMessage message;
        String gsonSerialization;

        question = new PlayMessage(aldoName, "moveMotherNature", 2);
        gsonSerialization = gson.toJson(question);

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
        PlayMessage message;
        String gsonSerialization;

        question = new PlayMessage(aldoName,"chooseCloud",  "c_1");
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

        PlayMessage message;
        String gsonSerialization;


        question = new PlayMessage(aldoName, "playCharacterCard", Characters.CRIER);
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
        PlayMessage message;
        String gsonSerialization;

        question = new PlayMessage(aldoName, "playCharacterCard", Characters.CRIER, "i_1");
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
        PlayMessage message;
        String gsonSerialization;


        question = new PlayMessage(aldoName, "playCharacterCard", Characters.CRIER, TeacherColor.GREEN);
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);

    }

    @Test
    public void message11Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        PlayMessage message;
        String gsonSerialization;

        question = PlayMessagesFabric.playAssistantCard(aldoName, 7);
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertNull(message.getTeacherColorList());
        message.executeMessage(manager);
    }

    @Test
    public void message12Test() throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        FakeGameManager manager = new FakeGameManager();
        PlayMessage question;
        PlayMessage message;
        String gsonSerialization;

        question = PlayMessagesFabric.calcInfluence(aldoName);
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
        PlayMessage message;
        String gsonSerialization;


        Map<TeacherColor, Integer> testMapA = new HashMap<>();
        int i = 1;
        for (TeacherColor color : TeacherColor.values()) {
            testMapA.put(color, i++);
        }
        question = new PlayMessage(serverName, "statusStudent", "Entrance", testMapA);
        gsonSerialization = gson.toJson(question);
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
        PlayMessage message;
        String gsonSerialization;
        Map<String, TowerColor> testMapC = new HashMap<>();
        testMapC.put("i_2", TowerColor.WHITE);
        testMapC.put("i_3", TowerColor.BLACK);
        testMapC.put("i_4", TowerColor.WHITE);
        testMapC.put("i_5", TowerColor.BLACK);
        testMapC.put("i_6", TowerColor.BLACK);
        testMapC.put("i_7", TowerColor.BLACK);
        testMapC.put("i_8", TowerColor.WHITE);
        testMapC.put("i_10", TowerColor.BLACK);
        testMapC.put("i_11", TowerColor.WHITE);
        question = new PlayMessage(serverName, "statusTower", testMapC);
        gsonSerialization = gson.toJson(question);
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
        PlayMessage message;
        String gsonSerialization;

        List<TeacherColor> testList = new ArrayList<>();
        testList.add(TeacherColor.BLUE);
        testList.add(TeacherColor.PINK);
        testList.add(TeacherColor.GREEN);
        question = new PlayMessage(serverName, "statusTeacher", aldoName, testList);
        gsonSerialization = gson.toJson(question);
        message = gson.fromJson(gsonSerialization, PlayMessage.class);
        assertNull(message.getStringTowerColorMap());
        assertNull(message.getTeacherColorIntegerMap());
        assertEquals(testList, message.getTeacherColorList());
        message.executeMessage(manager);
    }
}

class FakeServer extends Server {
    public FakeServer(GameManager controller) {
        super(controller);
    }
}

class FakeGameManager implements PlayMessageReader {

    @Override
    public void playAssistantCard(String player, Integer weight) {

    }

    @Override
    public void moveStudent(String player, TeacherColor color, String fromId, String toId) {
        assertEquals(String.class, player.getClass());
        assertEquals(TeacherColor.class, color.getClass());
        assertEquals(String.class, fromId.getClass());
        assertEquals(String.class, toId.getClass());
    }

    @Override
    public void moveStudent(String player, TeacherColor fromColor, TeacherColor toColor, String placeId) {
        assertEquals(String.class, player.getClass());
        assertEquals(TeacherColor.class, fromColor.getClass());
        assertEquals(TeacherColor.class, toColor.getClass());
        assertEquals(String.class, placeId.getClass());
    }

    @Override
    public void moveMotherNature(String player, Integer hops) {
        assertEquals(String.class, player.getClass());
        assertEquals(2, hops);
    }

    @Override
    public void calcInfluence(String player) {

    }

    @Override
    public void chooseCloud(String player, String id) {
        assertEquals(String.class, player.getClass());
        assertEquals(String.class, id.getClass());
    }

    @Override
    public void playCharacterCard(String player, Characters character) {
        assertEquals(String.class, player.getClass());
        assertEquals(Characters.class, character.getClass());
    }

    @Override
    public void playCharacterCard(String player, Characters character, String id) {
        assertEquals(String.class, player.getClass());
        assertEquals(Characters.class, character.getClass());
        assertEquals(String.class, id.getClass());
    }

    @Override
    public void playCharacterCard(String player, Characters character, TeacherColor color) {
        assertEquals(String.class, player.getClass());
        assertEquals(Characters.class, character.getClass());
        assertEquals(TeacherColor.class, color.getClass());
    }

    @Override
    public void statusStudent(String sender, String id, Map<TeacherColor, Integer> quantity) {
        assertEquals(String.class, sender.getClass());
        assertEquals(String.class, id.getClass());
        assertTrue(Arrays.stream(quantity.getClass().getInterfaces()).collect(Collectors.toList()).contains(Map.class));
    }

    @Override
    public void statusTeacher(String sender, String id, List<TeacherColor> which) {
        assertEquals(String.class, sender.getClass());
        assertEquals(String.class, id.getClass());
        assertTrue(Arrays.stream(which.getClass().getInterfaces()).collect(Collectors.toList()).contains(List.class));
    }

    @Override
    public void statusTower(String sender, Map<String, TowerColor> conquests) {
        assertEquals(String.class, sender.getClass());
        assertTrue(Arrays.stream(conquests.getClass().getInterfaces()).collect(Collectors.toList()).contains(Map.class));
    }

    @Override
    public void statusTower(String sender, String player, TowerColor color) {

    }

    @Override
    public void statusIslandIds(String sender, List<String> ids) {
        assertEquals(String.class, sender.getClass());
        assertTrue(Arrays.stream(ids.getClass().getInterfaces()).collect(Collectors.toList()).contains(List.class));
    }

    @Override
    public void statusCloudIds(String sender, List<String> ids) {

    }

    @Override
    public void statusMotherNature(String sender, String islandId) {

    }

    @Override
    public void statusAction(String sender, String actualPlayer) {

    }

    @Override
    public void statusPlanning(String sender, String actualPlayer) {

    }

    @Override
    public void statusCharacterCard(String sender, Characters character) {

    }

    @Override
    public void statusAssistantCard(String sender, String player, Integer weight) {

    }

    @Override
    public void statusEndGame(String sender, String winner) {

    }
}
