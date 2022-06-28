package it.polimi.ingsw.model.school;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.server.model.player.school.SchoolRoom;
import it.polimi.ingsw.server.model.table.Table;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link SchoolRoom} methods
 */
public class SchoolRoomTest {
    /**
     * Test creation rooms
     */
    @Test
    public void creationTest() {
        SchoolRoom room = new SchoolRoom();

        for(TeacherColor color: TeacherColor.values()){
            assertFalse(room.getTeacherPresence(color));
            room.addTeacher(color);
            assertTrue(room.getTeacherPresence(color));
        }

        for(TeacherColor color: TeacherColor.values()){
            assertTrue(room.getTeacherPresence(color));
            room.removeTeacher(color);
            assertFalse(room.getTeacherPresence(color));
        }
    }

    /**
     * Test money place in 3rd, 6th and 9th positions
     */
    @Test
    public void seeMoneyPlaceTest() {
        SchoolRoom room = new SchoolRoom();
        StudentsManager moor = (StudentsManager) room;

        moor.addStudent(TeacherColor.PINK); //1
        assertFalse(room.getMoneyPlace());

        moor.addStudent(TeacherColor.PINK); //2
        assertFalse(room.getMoneyPlace());

        moor.addStudent(TeacherColor.PINK); //3
        assertTrue(room.getMoneyPlace());

        room.resetMoneyPlace();

        moor.addStudent(TeacherColor.PINK); //4
        assertFalse(room.getMoneyPlace());

        moor.addStudent(TeacherColor.PINK); //5
        assertFalse(room.getMoneyPlace());

        moor.addStudent(TeacherColor.PINK); //6
        assertTrue(room.getMoneyPlace());

        room.resetMoneyPlace();

        moor.addStudent(TeacherColor.PINK); //7
        assertFalse(room.getMoneyPlace());

        moor.addStudent(TeacherColor.PINK); //8
        assertFalse(room.getMoneyPlace());

        moor.addStudent(TeacherColor.PINK); //9
        assertTrue(room.getMoneyPlace());

        room.resetMoneyPlace();

        moor.addStudent(TeacherColor.PINK); //10
        assertFalse(room.getMoneyPlace());

    }

}
