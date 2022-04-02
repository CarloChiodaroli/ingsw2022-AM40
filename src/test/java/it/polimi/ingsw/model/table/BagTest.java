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
        boolean testYellow = false;
        boolean testPink = false;
        boolean testRed = false;
        boolean testGreen = false;
        boolean testBlue = false;
        while(!testYellow && !testPink && !testRed && !testGreen && !testBlue) {
            TeacherColor color = bag.getAStudent();

            if (color.equals(TeacherColor.YELLOW)) {
                assertEquals(bag.getStudentYellow(), studentsYellow + 1);
                assertEquals(bag.howManyTotStudents(), totStudents + 1);
                testYellow = true;
            }

            if (color.equals(TeacherColor.PINK)) {
                assertEquals(bag.getStudentPink(), studentsPink + 1);
                assertEquals(bag.howManyTotStudents(), totStudents + 1);
                testPink = true;
            }

            if (color.equals(TeacherColor.RED)) {
                assertEquals(bag.getStudentRed(), studentsRed + 1);
                assertEquals(bag.howManyTotStudents(), totStudents + 1);
                testRed = true;
            }

            if (color.equals(TeacherColor.GREEN)) {
                assertEquals(bag.getStudentGreen(), studentsGreen + 1);
                assertEquals(bag.howManyTotStudents(), totStudents + 1);
                testGreen = true;
            }

            if (color.equals(TeacherColor.BLUE)) {
                assertEquals(bag.getStudentBlue(), studentsBlue + 1);
                assertEquals(bag.howManyTotStudents(), totStudents + 1);
                testBlue = true;
            }
        }
    }

}
