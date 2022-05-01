package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;
import it.polimi.ingsw.network.Message.AnswerMessage;
import it.polimi.ingsw.network.Message.Message;
import it.polimi.ingsw.network.Server.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameManager {

    private final Server server;
    private final GameController controller;
    private final static String serverName = "Server";

    public GameManager(Server server, GameController controller){
        this.server = server;
        this.controller = controller;
    }

    public void move(String player, TeacherColor color, String fromId, String toId){
        List<Message> messagesToSend;
        controller.moveStudent(player, color, fromId, toId);
        messagesToSend = getCompletePlaceUpdate(player, toId);
        server.sendBroadcast(messagesToSend);
        messagesToSend = getCompletePlaceUpdate(player, fromId);
        server.sendBroadcast(messagesToSend);
    }

    public void move(String player, TeacherColor fromColor, TeacherColor toColor, String placeId){
        List<Message> messagesToSend;
        controller.moveStudent(player, fromColor, toColor, placeId);
        messagesToSend = getCompletePlaceUpdate(player, placeId);
        // server.sendBroadcast(messagesToSend);
        messagesToSend = getCompletePlaceUpdate(player, "Entrance");
        server.sendBroadcast(messagesToSend);
    }

    public void move(String player, int hops){
        controller.moveMotherNature(player, hops);
        String MotherNaturePlace = controller.getMotherNaturePosition();
        AnswerMessage answer = new AnswerMessage(serverName);
        server.sendBroadcast(answer);
    }

    public void move(String player, String id){
        List<Message> messagesToSend;
        controller.chooseCloud(player, id);
        messagesToSend = getCompletePlaceUpdate(player, "Entrance");
        server.sendBroadcast(messagesToSend);
    }

    public void move(String player, Characters character){
        List<Message> messagesToSend;
        controller.playCharacterCard(player, character);
        //messagesToSend = getAllPlayerDashboards();
    }

    public void move(String player, Characters character, String islandId){
        List<Message> messagesToSend;
        controller.playCharacterCard(player, character, islandId);
    }

    public void move(String player, Characters character, TeacherColor color){
        List<Message> messagesToSend;
        controller.playCharacterCard(player, character, color);

    }

    private List<Message> getCompletePlaceUpdate(String player, String place){
        List<Message> messagesToSend = new ArrayList<>();
        Map<TeacherColor, Integer> studentsActual;
        Optional<TowerColor> towerActual;
        List<TeacherColor> teacherActual;
        studentsActual = controller.getStudentInPlace(player, place);
        towerActual = controller.getTowerInPlace(place);
        teacherActual = controller.getTeacherInPlace(player, place);
        try {
            AnswerMessage answerMessage = new AnswerMessage(serverName);
            answerMessage.message(studentsActual, place);
            messagesToSend.add(answerMessage);
            answerMessage = new AnswerMessage(serverName);
            towerActual.ifPresent(towerColor -> {
                AnswerMessage answer = new AnswerMessage(serverName);
                answer.message(towerColor, place);
                messagesToSend.add(answer);
            });
            if(!teacherActual.isEmpty()) {
                answerMessage = new AnswerMessage(serverName);
                answerMessage.message(teacherActual, place);
                messagesToSend.add(answerMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // it will be done better soon when answer will be better implemented
        }
        return messagesToSend;
    }

    /*private List<Message> sendAllPlayerDashboards(){
        List<String> names = controller.getPlayerNames();
        Map<TeacherColor, Integer> studentsActual;
        int towerActual;
        List<TeacherColor> teacherActual;
        for(String name: names){
            List<Message> messagesToSend = new ArrayList<>();
            studentsActual = controller.getStudentInPlace(name, "Entrance");
            towerActual = controller.getPlayerTowers(name);
            teacherActual = controller.getTeacherInPlace(name, "Room");
            AnswerMessage answerMessage = new AnswerMessage(serverName);
            answerMessage.message(studentsActual, place);
            messagesToSend.add(answerMessage);
            answerMessage = new AnswerMessage(serverName);
            towerActual.ifPresent(towerColor -> {
                AnswerMessage answer = new AnswerMessage(serverName);
                answer.message(towerColor);
                messagesToSend.add(answer);
            });
            if(!teacherActual.isEmpty()) {
                answerMessage = new AnswerMessage(serverName);
                answerMessage.message(teacherActual, place);
                messagesToSend.add(answerMessage);
            }
            server.sendBroadcast(messagesToSend);
        }
    }*/
}
