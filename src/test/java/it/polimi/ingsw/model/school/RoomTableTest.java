package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.enums.TeacherColor;
import it.polimi.ingsw.model.player.school.RoomTable;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTableTest {
    @Test
    public void creationTest() {
        RoomTable table = new RoomTable(TeacherColor.YELLOW);
        assertEquals(TeacherColor.YELLOW, table.getTeacherColor());
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.PINK);
        assertEquals(TeacherColor.PINK, table.getTeacherColor());
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.RED);
        assertEquals(TeacherColor.RED, table.getTeacherColor());
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.GREEN);
        assertEquals(TeacherColor.GREEN, table.getTeacherColor());
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.BLUE);
        assertEquals(TeacherColor.BLUE, table.getTeacherColor());
        assertFalse(table.hasTeacher());

    }

    @Test
    public void removeTeacherTest(){
        RoomTable table = new RoomTable(TeacherColor.YELLOW);
        assertFalse(table.removeTeacher());
        table.setTeacherPresence(true);
        assertTrue(table.removeTeacher());
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.PINK);
        assertFalse(table.removeTeacher());
        table.setTeacherPresence(true);
        assertTrue(table.removeTeacher());
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.RED);
        assertFalse(table.removeTeacher());
        table.setTeacherPresence(true);
        assertTrue(table.removeTeacher());
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.GREEN);
        assertFalse(table.removeTeacher());
        table.setTeacherPresence(true);
        assertTrue(table.removeTeacher());
        assertFalse(table.hasTeacher());

        table = new RoomTable(TeacherColor.BLUE);
        assertFalse(table.removeTeacher());
        table.setTeacherPresence(true);
        assertTrue(table.removeTeacher());
        assertFalse(table.hasTeacher());

    }

    @Test
    public void howManyStudentsTest(){
        RoomTable table = new RoomTable(TeacherColor.YELLOW);
        assertEquals(table.howManyStudents(TeacherColor.YELLOW), table.howManyStudents());

        table = new RoomTable(TeacherColor.PINK);
        assertEquals(table.howManyStudents(TeacherColor.PINK), table.howManyStudents());

        table = new RoomTable(TeacherColor.RED);
        assertEquals(table.howManyStudents(TeacherColor.RED), table.howManyStudents());

        table = new RoomTable(TeacherColor.GREEN);
        assertEquals(table.howManyStudents(TeacherColor.GREEN), table.howManyStudents());

        table = new RoomTable(TeacherColor.BLUE);
        assertEquals(table.howManyStudents(TeacherColor.BLUE), table.howManyStudents());
    }

}
