package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentsManagerTest {
    @Test
    public void studentsManagerTest(){
        StudentsManagerIstance manager = new StudentsManagerIstance(130, 26);
        int studentsYellow = manager.howManyStudents(TeacherColor.YELLOW);
        int studentsPink = manager.howManyStudents(TeacherColor.PINK);
        int studentsRed = manager.howManyStudents(TeacherColor.RED);
        int studentsGreen = manager.howManyStudents(TeacherColor.GREEN);
        int studentsBlue = manager.howManyStudents(TeacherColor.BLUE);

        for(TeacherColor color : TeacherColor.values()) {
            assertTrue(manager.addStudent(color));
        }

        assertEquals(manager.howManyTotStudents(), 5);

        assertEquals(manager.howManyStudents(TeacherColor.YELLOW), studentsYellow + 1);

        assertEquals(manager.howManyStudents(TeacherColor.PINK), studentsPink + 1);

        assertEquals(manager.howManyStudents(TeacherColor.RED), studentsRed + 1);

        assertEquals(manager.howManyStudents(TeacherColor.GREEN), studentsGreen + 1);

        assertEquals(manager.howManyStudents(TeacherColor.BLUE), studentsBlue + 1);

        for(TeacherColor color : TeacherColor.values()) {
            for (int i = 0; i < 25; i++) {
                manager.addStudent(color);
            }
            assertFalse(manager.addStudent(color));
            assertEquals(manager.howManyStudents(color), 26);
        }
        assertEquals(manager.howManyTotStudents(), 130);

        studentsYellow = 26;
        studentsPink = 26;
        studentsRed = 26;
        studentsGreen = 26;
        studentsBlue = 26;

        for(TeacherColor color : TeacherColor.values()) {
            assertTrue(manager.removeStudent(color));
        }

        assertEquals(manager.howManyTotStudents(), 125);

        assertEquals(manager.howManyStudents(TeacherColor.YELLOW), studentsYellow - 1);

        assertEquals(manager.howManyStudents(TeacherColor.PINK), studentsPink - 1);

        assertEquals(manager.howManyStudents(TeacherColor.RED), studentsRed - 1);

        assertEquals(manager.howManyStudents(TeacherColor.GREEN), studentsGreen - 1);

        assertEquals(manager.howManyStudents(TeacherColor.BLUE), studentsBlue - 1);

        for(TeacherColor color : TeacherColor.values()) {
            for (int i = 0; i < 25; i++) {
                manager.removeStudent(color);
            }
            assertFalse(manager.removeStudent(color));
            assertEquals(manager.howManyStudents(color), 0);
        }
        assertEquals(manager.howManyTotStudents(), 0);



    }

}



class StudentsManagerIstance extends StudentsManager{
    StudentsManagerIstance(int num, int numc){
        super(num, numc);
    }
}

class StudentsManagerIstance2 extends StudentsManager{
    StudentsManagerIstance2(int num){
        super(num);
    }
}