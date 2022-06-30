package it.polimi.ingsw.server.model;


import it.polimi.ingsw.commons.enums.TeacherColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Lots of pieces and entities in the game keep memory of students, this class gets extended by all classes that manage students.
 */
public abstract class StudentsManager {
    private final int maxStudents;
    private final int maxStudentsColor;
    private final HashMap<TeacherColor, Integer> manager;

    /**
     * Class Constructor where, when completely filled, needs to have equal quantity of students for each color
     *
     * @param maxStudents      max number of total students
     * @param maxStudentsColor max number of students of one color
     */
    public StudentsManager(int maxStudents, int maxStudentsColor) {
        this.maxStudents = maxStudents;
        this.maxStudentsColor = maxStudentsColor;
        this.manager = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            manager.put(color, 0);
        }
    }

    /**
     * Class Constructor where students can be of different quantities
     *
     * @param maxStudents max number of total students
     */
    public StudentsManager(int maxStudents) {
        this.maxStudents = maxStudents;
        this.maxStudentsColor = maxStudents;
        this.manager = new HashMap<>();
        for (TeacherColor color : TeacherColor.values()) {
            manager.put(color, 0);
        }
    }

    /**
     * If it's allowed, add one student of the selected color
     *
     * @param color color of the student to add
     * @return true if the student has been added
     */
    public boolean addStudent(TeacherColor color) {
        boolean perm;
        perm = permissionAdd(manager.get(color));
        if (perm) {
            manager.replace(color, manager.get(color), manager.get(color) + 1);
            return true;
        }
        return false;
    }

    /**
     * If it's allowed, remove one student of the selected color
     *
     * @param color color of the student to remove
     * @return true if the student has been removed
     */
    public boolean removeStudent(TeacherColor color) {
        boolean perm;
        perm = permissionRemove(manager.get(color));
        if (perm) {
            manager.replace(color, manager.get(color), manager.get(color) - 1);
            return true;
        }
        return false;
    }

    /**
     * Verify that can be added the number of students chosen
     *
     * @param students number of students of the color want add
     */
    private boolean permissionAdd(int students) {
        return students < maxStudentsColor && howManyTotStudents() < maxStudents;
    }

    /**
     * Verify that can be removed the number of students chosen
     *
     * @param students number of students of the color want remove
     */
    private boolean permissionRemove(int students) {
        return students > 0 && howManyTotStudents() > 0;
    }

    /**
     * The number of student of the required color
     *
     * @return desired color
     */
    public int howManyStudents(TeacherColor color) {
        return manager.get(color);
    }

    /**
     * The number of total students
     *
     * @return how many students there are
     */
    public int howManyTotStudents() {
        int studentTot = 0;
        for (TeacherColor color : TeacherColor.values()) {
            studentTot += manager.get(color);
        }
        return studentTot;
    }

    /**
     * If is allowed, remove one student
     *
     * @return color of the removed student, if any
     */
    public Optional<TeacherColor> getStudent() {
        for (TeacherColor color : TeacherColor.values()) {
            if (removeStudent(color)) {
                return Optional.of(color);
            }
        }
        return Optional.empty();
    }

    /**
     * Get max students allowed
     *
     * @return number of max students
     */
    public int getMaxStudents() {
        return maxStudents;
    }

    /**
     * Get the number of students for each color
     *
     * @return a map showing the number of students for each color
     */
    public Map<TeacherColor, Integer> getMap() {
        return manager;
    }
}