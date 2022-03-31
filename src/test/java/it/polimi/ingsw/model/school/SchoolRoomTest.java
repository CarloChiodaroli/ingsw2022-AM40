package it.polimi.ingsw.model.school;

import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SchoolRoomTest {
    @Test
    public void schoolRoomTest(){
        SchoolRoom room = new SchoolRoom();
        for(TeacherColor color: TeacherColor.values()) {
            assertEquals(room.getTable(color), new RoomTable(color));
        }
    }

}
