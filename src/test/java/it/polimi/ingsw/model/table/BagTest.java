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
        for(int i = 0; i < 129; i++) {
            TeacherColor color = bag.getAStudent();

            if (color.equals(TeacherColor.YELLOW)) {
                assertEquals(bag.howManyStudents(TeacherColor.YELLOW), studentsYellow - 1);
                assertEquals(bag.howManyTotStudents(), totStudents - 1);
                studentsYellow--;
                totStudents--;
            }

            if (color.equals(TeacherColor.PINK)) {
                assertEquals(bag.howManyStudents(TeacherColor.PINK), studentsPink - 1);
                assertEquals(bag.howManyTotStudents(), totStudents - 1);
                studentsPink--;
                totStudents--;
            }

            if (color.equals(TeacherColor.RED)) {
                assertEquals(bag.howManyStudents(TeacherColor.RED), studentsRed - 1);
                assertEquals(bag.howManyTotStudents(), totStudents - 1);
                studentsRed--;
                totStudents--;
            }

            if (color.equals(TeacherColor.GREEN)) {
                assertEquals(bag.howManyStudents(TeacherColor.GREEN), studentsGreen - 1);
                assertEquals(bag.howManyTotStudents(), totStudents - 1);
                studentsGreen--;
                totStudents--;
            }

            if (color.equals(TeacherColor.BLUE)) {
                assertEquals(bag.howManyStudents(TeacherColor.BLUE), studentsBlue - 1);
                assertEquals(bag.howManyTotStudents(), totStudents - 1);
                studentsBlue--;
                totStudents--;
            }
        }

        assertEquals(bag.howManyTotStudents(), 1);
    }

}
