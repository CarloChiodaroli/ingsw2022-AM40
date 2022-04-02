package it.polimi.ingsw.model.table;
import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class IslandTest {
    Island to = new Island("C_2");
    @Test
    public void elaborationMergeIslandIdTest(){
        Island is1 = new Island("S_400");
        Island is2 = new Island("S_280");
        assertEquals("S_400",is1.getId());
        assertEquals("S_280",is2.getId());
        assertEquals("S_400_280",new Island(is1,is2).getId());
        assertEquals(2,new Island(is1,is2).howManyEquivalents());
    }


    @Test
    public void addStudentIslandTest() {
        Island island = new Island("I_1");
        for(TeacherColor tc : TeacherColor.values())
        {
            for(int i=0;i<10;i++)
                island.addStudent(tc);
        }

        assertEquals(50,island.howManyTotStudents());

        Island istoadd = new Island("I_2");
        istoadd.addStudentfromSmallIsland(island);

        assertEquals(50,istoadd.howManyTotStudents());
    }
}