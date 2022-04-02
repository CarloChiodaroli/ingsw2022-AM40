package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchoolRoomTest {
    @Test
    public void schoolRoomTest(){
        SchoolRoom room = new SchoolRoom();
        assertEquals(room.getTable(TeacherColor.YELLOW).getTeacherColor(), TeacherColor.YELLOW);
        assertEquals(room.getTable(TeacherColor.PINK).getTeacherColor(), TeacherColor.PINK);
        assertEquals(room.getTable(TeacherColor.RED).getTeacherColor(), TeacherColor.RED);
        assertEquals(room.getTable(TeacherColor.GREEN).getTeacherColor(), TeacherColor.GREEN);
        assertEquals(room.getTable(TeacherColor.BLUE).getTeacherColor(), TeacherColor.BLUE);
    }

}
