package it.polimi.ingsw.model.school;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.player.school.SchoolRoom;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchoolRoomTest {
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

}
