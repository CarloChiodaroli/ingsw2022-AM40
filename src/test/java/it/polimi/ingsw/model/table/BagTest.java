package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class BagTest {
    @Test
    public void bagTest(){
        Bag bag = new Bag(26, 130);
        int studentsYellow = bag.getStudentYellow();
        int studentsPink = bag.getStudentPink();
        int studentsRed = bag.getStudentRed();
        int studentsGreen = bag.getStudentGreen();
        int studentsBlue = bag.getStudentBlue();
        int totStudents = bag.howManyTotStudents();

        if(bag.getAStudent().equals(TeacherColor.YELLOW)){
            assertEquals(bag.getStudentYellow(), studentsYellow + 1);
        }

        if(bag.getAStudent().equals(TeacherColor.PINK)){
            assertEquals(bag.getStudentPink(), studentsPink + 1);
        }

        if(bag.getAStudent().equals(TeacherColor.RED)){
            assertEquals(bag.getStudentRed(), studentsRed + 1);
        }

        if(bag.getAStudent().equals(TeacherColor.GREEN)){
            assertEquals(bag.getStudentGreen(), studentsGreen + 1);
        }

        if(bag.getAStudent().equals(TeacherColor.BLUE)){
            assertEquals(bag.getStudentBlue(), studentsBlue + 1);
        }

        assertEquals(bag.howManyTotStudents(), totStudents + 1);

    }

}
