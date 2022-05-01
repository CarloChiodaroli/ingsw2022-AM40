package it.polimi.ingsw.network.Message;

import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.enums.TowerColor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnswerMessage extends Message{

    private Object[] params;
    private Method method;

    public AnswerMessage(String sender){
        super(sender, MessageType.ANSWER);
    }

    public void message(Map<TeacherColor, Integer> content, String id){

    }

    public void message(TowerColor towerColor, String id){

    }

    public void message(List<TeacherColor> professorList, String id){

    }
}
