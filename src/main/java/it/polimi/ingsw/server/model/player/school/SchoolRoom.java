package it.polimi.ingsw.server.model.player.school;

import it.polimi.ingsw.commons.enums.TeacherColor;
import it.polimi.ingsw.server.model.StudentsManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Models School Room part of the Player's {@link SchoolDashboard Dashboard}, keeping also track of the teachers.
 */
public class SchoolRoom extends StudentsManager {

    private final static int lengthOfTables = 10;
    private final Map<TeacherColor, Boolean> teachers;
    private boolean moneyPlace;

    /**
     * Construction of 5 rooms, one for each color
     */
    public SchoolRoom() {
        super(lengthOfTables * 5, lengthOfTables);
        teachers = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            teachers.put(color, false);
        }
        resetMoneyPlace();
    }

    /**
     * Add the teacher of the required color
     *
     * @param color color of the teacher
     */
    public void addTeacher(TeacherColor color) {
        teachers.replace(color, true);
    }

    /**
     * Remove the teacher of the required color
     *
     * @param color color of the teacher
     */
    public void removeTeacher(TeacherColor color) {
        teachers.replace(color, false);
    }

    /**
     * Get the presence of the teacher of the required color
     *
     * @param color color required
     * @return true if there's the teacher
     */
    public boolean getTeacherPresence(TeacherColor color) {
        return teachers.get(color);
    }

    @Override
    public boolean addStudent(TeacherColor color) {
        int old = super.howManyStudents(color);
        boolean result = super.addStudent(color);
        int actual = super.howManyStudents(color);
        if(result){
            if(old < actual && actual!=0 && actual%3 == 0){
                this.moneyPlace = true;
            }
        }
        return result;
    }

    /**
     * Get the positions who give moneys
     *
     * @return true if the position gives moneys
     */
    public boolean getMoneyPlace() {
        return moneyPlace;
    }

    /**
     * Set false the give money
     */
    public void resetMoneyPlace() {
        moneyPlace = false;
    }
}
