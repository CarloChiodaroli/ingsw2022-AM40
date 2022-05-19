package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.server.model.StudentsManager;
import it.polimi.ingsw.commons.enums.TeacherColor;

import java.util.Arrays;
import java.util.List;


public class Bag extends StudentsManager {

    private final static int defaultColorNumOfStudents = 26;
    private final static int defaultTotalNumOfStudents = defaultColorNumOfStudents * 5;


    /**
     * @param maxStudents      Max number of student
     * @param maxStudentsColor Max number of student for each color
     */
    public Bag(int maxStudents, int maxStudentsColor) {
        super(maxStudents, maxStudentsColor);
        for (TeacherColor color : TeacherColor.values()) {
            for (int i = 0; i < maxStudentsColor; i++) {
                this.addStudent(color);
            }
        }
    }

    public Bag() {
        super(defaultTotalNumOfStudents, defaultColorNumOfStudents);
        for (TeacherColor color : TeacherColor.values()) {
            for (int i = 0; i < defaultColorNumOfStudents; i++) {
                this.addStudent(color);
            }
        }
    }

    public boolean isEmpty() {
        return howManyTotStudents() == 0;
    }

    /**
     * @return TeacherColor It returns a casual color
     */
    public TeacherColor getAStudent() {
        if (isEmpty()) return null;
        int i;
        List<TeacherColor> colors = Arrays.asList(TeacherColor.values());
        do {
            i = (int) (Math.random() * 5);
            if (howManyStudents(colors.get(i)) > 0) {
                removeStudent(colors.get(i));
                return colors.get(i);
            }
            if (isEmpty()) return null;
        } while (true);
    }

}
