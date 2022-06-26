package it.polimi.ingsw.server.model.table;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;

/**
 * Models game's Cloud.
 */
public class Cloud extends StudentsManager {
    private final String id;

    /**
     * Constructor
     * @param id Cloud's Id
     * @param equalsStudents number of student that cloud can contains
     */
    public Cloud(String id, int equalsStudents) {
        super(equalsStudents);
        this.id = id;
    }

    /**
     * Get the is of the cloud
     * @return String Cloud's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Build and fill the clouds
     * @param bag from where students are caught
     * @throws IllegalStateException the bag is empty
     */
    public void buildCloud(Bag bag) throws IllegalStateException {
        int color;
        if (bag.howManyTotStudents() < this.getMaxStudents())
            throw new IllegalStateException("Not enough students to fill the cloud");
        for (int i = 0; i < this.getMaxStudents(); i++) {
            color = (int) (Math.random() * 5);
            while (!this.addStudent(TeacherColor.values()[color])) {
                color = (int) (Math.random() * 5);
            }
            bag.removeStudent(TeacherColor.values()[color]);
        }
    }
}
