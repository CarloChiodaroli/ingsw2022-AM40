package it.polimi.ingsw.model.table;

import it.polimi.ingsw.model.StudentsManager;
import it.polimi.ingsw.model.enums.TeacherColor;

/**
 * CLASS Cloud
 */
public class Cloud extends StudentsManager {
    private final String id;

    /**
     * @param id             Cloud's Id
     * @param equalsStudents number of student that cloud can contains
     */
    public Cloud(String id, int equalsStudents) {
        super(equalsStudents);
        this.id = id;
    }

    /**
     * @return String Cloud's ID
     */
    public String getId() {
        return id;
    }

    public void buildClouds(Bag bag) {
        int color;
        for (int i = 0; i < this.getMaxStudents(); i++) {
            color = (int) (Math.random() * 5);
            while (!this.addStudent(TeacherColor.values()[color])) {
                color = (int) (Math.random() * 5);
            }
            bag.removeStudent(TeacherColor.values()[color]);
        }
    }
}
