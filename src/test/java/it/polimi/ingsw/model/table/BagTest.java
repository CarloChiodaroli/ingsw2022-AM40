package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.enums.TeacherColor;
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
                assertEquals(studentsYellow - 1, bag.howManyStudents(TeacherColor.YELLOW));
                assertEquals(totStudents - 1, bag.howManyTotStudents());
                studentsYellow--;
                totStudents--;
            }

            if (color.equals(TeacherColor.PINK)) {
                assertEquals(studentsPink - 1, bag.howManyStudents(TeacherColor.PINK));
                assertEquals(totStudents - 1, bag.howManyTotStudents());
                studentsPink--;
                totStudents--;
            }

            if (color.equals(TeacherColor.RED)) {
                assertEquals(studentsRed - 1, bag.howManyStudents(TeacherColor.RED));
                assertEquals(totStudents - 1, bag.howManyTotStudents());
                studentsRed--;
                totStudents--;
            }

            if (color.equals(TeacherColor.GREEN)) {
                assertEquals(studentsGreen - 1, bag.howManyStudents(TeacherColor.GREEN));
                assertEquals(totStudents - 1, bag.howManyTotStudents());
                studentsGreen--;
                totStudents--;
            }

            if (color.equals(TeacherColor.BLUE)) {
                assertEquals(studentsBlue - 1, bag.howManyStudents(TeacherColor.BLUE));
                assertEquals(totStudents - 1, bag.howManyTotStudents());
                studentsBlue--;
                totStudents--;
            }
        }

        assertEquals(1, bag.howManyTotStudents());
    }

}