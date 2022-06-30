package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;

import java.util.Arrays;
import java.util.List;

/**
 * Models game's Student bag.
 */
public class Bag extends StudentsManager {

    private final static int defaultColorNumOfStudents = 26;
    private final static int defaultTotalNumOfStudents = defaultColorNumOfStudents * 5;


    /**
     * Constructor
     *
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

    /**
     * Get if there are any student in the bag
     *
     * @return true if the bag is empty
     */
    public boolean isEmpty() {
        return howManyTotStudents() == 0;
    }

    /**
     * If any, remove one student of a random color
     *
     * @return TeacherColor the color of the caught student
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
