package it.polimi.ingsw.model.phases.action;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.phase.action.StudentsContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StudentsContainerTest {

    @Test
    public void test(){
        StudentsContainer container = new StudentsContainer(4);

        container.addStudent(TeacherColor.RED);
        container.addStudent(TeacherColor.RED);
        container.addStudent(TeacherColor.BLUE);
        container.addStudent(TeacherColor.PINK);

        assertEquals(2, container.howManyStudents(TeacherColor.RED));
        assertEquals(1, container.howManyStudents(TeacherColor.BLUE));
        assertEquals(1, container.howManyStudents(TeacherColor.PINK));
        assertEquals(4, container.howManyTotStudents());

    }
}
