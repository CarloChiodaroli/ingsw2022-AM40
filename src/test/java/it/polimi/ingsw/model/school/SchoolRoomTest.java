package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchoolRoomTest {
    @Test
    public void schoolRoomTest(){
        SchoolRoom room = new SchoolRoom();
        assertEquals(TeacherColor.YELLOW, room.getTable(TeacherColor.YELLOW).getTeacherColor());
        assertEquals(TeacherColor.PINK, room.getTable(TeacherColor.PINK).getTeacherColor());
        assertEquals(TeacherColor.RED, room.getTable(TeacherColor.RED).getTeacherColor());
        assertEquals(TeacherColor.GREEN, room.getTable(TeacherColor.GREEN).getTeacherColor());
        assertEquals(TeacherColor.BLUE, room.getTable(TeacherColor.BLUE).getTeacherColor());
    }

}
