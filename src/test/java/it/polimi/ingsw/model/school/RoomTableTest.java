package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTableTest {
    @Test
    public void creationTest() {
        RoomTable table = new RoomTable(TeacherColor.YELLOW);
        assertEquals(table.getTeacherColor(), TeacherColor.YELLOW);
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.PINK);
        assertEquals(table.getTeacherColor(), TeacherColor.PINK);
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.RED);
        assertEquals(table.getTeacherColor(), TeacherColor.RED);
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.GREEN);
        assertEquals(table.getTeacherColor(), TeacherColor.GREEN);
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.BLUE);
        assertEquals(table.getTeacherColor(), TeacherColor.BLUE);
        assertFalse(table.hasTeacher());

    }

    @Test
    public void howManyStudentsTest(){
        RoomTable table = new RoomTable(TeacherColor.YELLOW);
        assertEquals(table.howManyStudentsColor(), table.howManyStudents(TeacherColor.YELLOW));

        table = new RoomTable(TeacherColor.PINK);
        assertEquals(table.howManyStudentsColor(), table.howManyStudents(TeacherColor.PINK));

        table = new RoomTable(TeacherColor.RED);
        assertEquals(table.howManyStudentsColor(), table.howManyStudents(TeacherColor.RED));

        table = new RoomTable(TeacherColor.GREEN);
        assertEquals(table.howManyStudentsColor(), table.howManyStudents(TeacherColor.GREEN));

        table = new RoomTable(TeacherColor.BLUE);
        assertEquals(table.howManyStudentsColor(), table.howManyStudents(TeacherColor.BLUE));
    }

}
