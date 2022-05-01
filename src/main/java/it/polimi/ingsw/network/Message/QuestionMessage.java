package it.polimi.ingsw.network.Message;

import it.polimi.ingsw.controller.GameManager;
import it.polimi.ingsw.model.enums.Characters;
import it.polimi.ingsw.model.enums.TeacherColor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class QuestionMessage extends Message{

    private Object[] params;
    private Method method;

    public QuestionMessage(String sender){
        super(sender, MessageType.QUESTION);
    }

    public void executeMessage(GameManager manager)
            throws IllegalAccessException, InvocationTargetException {
        super.controlWritten();
        method.invoke(manager, params);
    }

    public void message(TeacherColor color, String fromId, String toId) throws NoSuchMethodException{
        controlNotWritten();
        params = new Object[]{super.getPlayerName(), color, fromId, toId};
        method = GameManager.class.getMethod("move" , String.class, TeacherColor.class , String.class, String.class);
        super.message();
    }

    public void message(TeacherColor fromColor, TeacherColor toColor, String placeId) throws NoSuchMethodException{
        controlNotWritten();
        params = new Object[]{super.getPlayerName(), fromColor, toColor, placeId};
        method = GameManager.class.getMethod("move" , String.class, TeacherColor.class , TeacherColor.class, String.class);
        super.message();
    }

    public void message(int hops) throws NoSuchMethodException{
        controlNotWritten();
        params = new Object[]{super.getPlayerName(), hops};
        method = GameManager.class.getMethod("move", String.class, int.class);
        super.message();
    }

    public void message(String id) throws NoSuchMethodException{
        controlNotWritten();
        params = new Object[]{super.getPlayerName(), id};
        method = GameManager.class.getMethod("move", String.class, String.class);
        super.message();
    }

    public void message(Characters character) throws NoSuchMethodException{
        controlNotWritten();
        params = new Object[]{super.getPlayerName(), character};
        method = GameManager.class.getMethod("move", String.class, Characters.class);
        super.message();
    }

    public void message(Characters character, String id) throws NoSuchMethodException{
        controlNotWritten();
        params = new Object[]{super.getPlayerName(), character, id};
        method = GameManager.class.getMethod("move", String.class, Characters.class, String.class);
        super.message();
    }

    public void message(Characters character, TeacherColor color) throws NoSuchMethodException{
        controlNotWritten();
        params = new Object[]{super.getPlayerName(), character, color};
        method = GameManager.class.getMethod("move", String.class, Characters.class, TeacherColor.class);
        super.message();
    }
}
