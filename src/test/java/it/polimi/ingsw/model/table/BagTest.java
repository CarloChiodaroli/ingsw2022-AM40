package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.TeacherColor;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class BagTest {
    @Test
    public void bagTest(){
        Bag bag = new Bag(130, 26);
        int studentsYellow = bag.howManyStudents(TeacherColor.YELLOW);
        int studentsPink = bag.howManyStudents(TeacherColor.PINK);
        int studentsRed = bag.howManyStudents(TeacherColor.RED);
        int studentsGreen = bag.howManyStudents(TeacherColor.GREEN);
        int studentsBlue = bag.howManyStudents(TeacherColor.BLUE);
        int totStudents = bag.howManyTotStudents();
        boolean testYellow = false;
        boolean testPink = false;
        boolean testRed = false;
        boolean testGreen = false;
        boolean testBlue = false;
        while(!testYellow && !testPink && !testRed && !testGreen && !testBlue) {
            TeacherColor color = bag.getAStudent();

            if (color.equals(TeacherColor.YELLOW)) {
                assertEquals(bag.howManyStudents(TeacherColor.YELLOW), studentsYellow - 1);
                assertEquals(bag.howManyTotStudents(), totStudents - 1);
                testYellow = true;
            }

            if (color.equals(TeacherColor.PINK)) {
                assertEquals(bag.howManyStudents(TeacherColor.PINK), studentsPink - 1);
                assertEquals(bag.howManyTotStudents(), totStudents - 1);
                testPink = true;
            }

            if (color.equals(TeacherColor.RED)) {
                assertEquals(bag.howManyStudents(TeacherColor.RED), studentsRed - 1);
                assertEquals(bag.howManyTotStudents(), totStudents - 1);
                testRed = true;
            }

            if (color.equals(TeacherColor.GREEN)) {
                assertEquals(bag.howManyStudents(TeacherColor.GREEN), studentsGreen - 1);
                assertEquals(bag.howManyTotStudents(), totStudents - 1);
                testGreen = true;
            }

            if (color.equals(TeacherColor.BLUE)) {
                assertEquals(bag.howManyStudents(TeacherColor.BLUE), studentsBlue - 1);
                assertEquals(bag.howManyTotStudents(), totStudents - 1);
                testBlue = true;
            }
        }
    }

}
