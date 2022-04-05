package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentsManagerTest {
    @Test
    public void addStudentsTest(){
        StudentsManagerIstance manager = new StudentsManagerIstance(130, 26);
        int studentsYellow = manager.howManyStudents(TeacherColor.YELLOW);
        int studentsPink = manager.howManyStudents(TeacherColor.PINK);
        int studentsRed = manager.howManyStudents(TeacherColor.RED);
        int studentsGreen = manager.howManyStudents(TeacherColor.GREEN);
        int studentsBlue = manager.howManyStudents(TeacherColor.BLUE);

        for(TeacherColor color : TeacherColor.values()) {
            assertTrue(manager.addStudent(color));
        }

        assertEquals(5, manager.howManyTotStudents());
        assertEquals(studentsYellow + 1, manager.howManyStudents(TeacherColor.YELLOW));
        assertEquals(studentsPink + 1, manager.howManyStudents(TeacherColor.PINK));
        assertEquals(studentsRed + 1, manager.howManyStudents(TeacherColor.RED));
        assertEquals(studentsGreen + 1, manager.howManyStudents(TeacherColor.GREEN));
        assertEquals(studentsBlue + 1, manager.howManyStudents(TeacherColor.BLUE));

        for(TeacherColor color : TeacherColor.values()) {
            for (int i = 0; i < 25; i++) {
                manager.addStudent(color);
            }
            assertFalse(manager.addStudent(color));
            assertEquals(26, manager.howManyStudents(color));
        }
        assertEquals(130, manager.howManyTotStudents());

        studentsYellow = 26;
        studentsPink = 26;
        studentsRed = 26;
        studentsGreen = 26;
        studentsBlue = 26;

        for(TeacherColor color : TeacherColor.values()) {
            assertTrue(manager.removeStudent(color));
        }

        assertEquals(125, manager.howManyTotStudents());
        assertEquals(studentsYellow - 1, manager.howManyStudents(TeacherColor.YELLOW));
        assertEquals(studentsPink - 1, manager.howManyStudents(TeacherColor.PINK));
        assertEquals(studentsRed - 1, manager.howManyStudents(TeacherColor.RED));
        assertEquals(studentsGreen - 1, manager.howManyStudents(TeacherColor.GREEN));
        assertEquals(studentsBlue - 1, manager.howManyStudents(TeacherColor.BLUE));

        for(TeacherColor color : TeacherColor.values()) {
            for (int i = 0; i < 25; i++) {
                manager.removeStudent(color);
            }
            assertFalse(manager.removeStudent(color));
            assertEquals(0, manager.howManyStudents(color));
        }
        assertEquals(0, manager.howManyTotStudents());

    }

    @Test
    public void istanceManager2Test(){
        StudentsManagerIstance2 manager = new StudentsManagerIstance2(26);
        assertEquals(26, manager.getMaxStudents());
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