package it.polimi.ingsw.model;

import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.commons.enums.TeacherColor;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class StudentsManagerTest {
    @Test
    public void addStudentsTest(){
        StudentsManagerIstance manager = new StudentsManagerIstance(130, 26);
        HashMap<TeacherColor, Integer> managerTest = new HashMap<>();
        for(TeacherColor color: TeacherColor.values()){
            managerTest.put(color, 0);
        }

        for(TeacherColor color : TeacherColor.values()) {
            assertFalse(manager.removeStudent(color));
            assertTrue(manager.addStudent(color));
        }

        assertEquals(5, manager.howManyTotStudents());
        for(TeacherColor color: TeacherColor.values()){
            assertEquals(managerTest.get(color) + 1, manager.howManyStudents(color));
            managerTest.replace(color, managerTest.get(color), managerTest.get(color) + 1);
        }

        for(TeacherColor color : TeacherColor.values()) {
            for (int i = 0; i < 25; i++) {
                manager.addStudent(color);
                managerTest.replace(color, managerTest.get(color), managerTest.get(color) + 1);
            }
            assertFalse(manager.addStudent(color));
            assertEquals(26, manager.howManyStudents(color));
        }
        assertEquals(130, manager.howManyTotStudents());

        for(TeacherColor color : TeacherColor.values()) {
            assertFalse(manager.addStudent(color));
            assertTrue(manager.removeStudent(color));
        }

        assertEquals(125, manager.howManyTotStudents());
        for(TeacherColor color: TeacherColor.values()){
            assertEquals(managerTest.get(color) - 1, manager.howManyStudents(color));
            managerTest.replace(color, managerTest.get(color), managerTest.get(color) - 1);
        }

        for(TeacherColor color : TeacherColor.values()) {
            for (int i = 0; i < 25; i++) {
                manager.removeStudent(color);
                managerTest.replace(color, managerTest.get(color), managerTest.get(color) - 1);
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



class StudentsManagerIstance extends StudentsManager {
    StudentsManagerIstance(int num, int numc){
        super(num, numc);
    }
}

class StudentsManagerIstance2 extends StudentsManager{
    StudentsManagerIstance2(int num){
        super(num);
    }
}